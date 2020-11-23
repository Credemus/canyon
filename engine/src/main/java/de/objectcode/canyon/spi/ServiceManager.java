package de.objectcode.canyon.spi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.spi.async.IAsyncManager;
import de.objectcode.canyon.spi.calendar.IBusinessCalendar;
import de.objectcode.canyon.spi.evaluator.IEvaluatorRepository;
import de.objectcode.canyon.spi.event.IBPEEventBroker;
import de.objectcode.canyon.spi.event.IWorkflowEventBroker;
import de.objectcode.canyon.spi.instance.IInstanceRepository;
import de.objectcode.canyon.spi.lock.ILockManager;
import de.objectcode.canyon.spi.parser.IParserFactory;
import de.objectcode.canyon.spi.process.IProcessRepository;
import de.objectcode.canyon.spi.tool.IToolRepository;
import de.objectcode.canyon.spiImpl.calendar.DefaultBusinessCalendar;
import de.objectcode.canyon.spiImpl.evaluator.DefaultEvaluatorRepository;
import de.objectcode.canyon.spiImpl.event.LoggingBPEEventListener;
import de.objectcode.canyon.spiImpl.event.async.AsyncBPEEventBroker;
import de.objectcode.canyon.spiImpl.event.async.AsyncWorkflowEventBroker;
import de.objectcode.canyon.spiImpl.lock.LockManager;
import de.objectcode.canyon.spiImpl.parser.DefaultParserFactory;
import de.objectcode.canyon.spiImpl.participant.DefaultParticipantRepository;
import de.objectcode.canyon.spiImpl.tool.DefaultToolRepository;
import de.objectcode.canyon.worklist.IWorklistEngine;
import de.objectcode.canyon.worklist.spi.participant.IParticipantRepository;
import de.objectcode.canyon.worklist.spi.participant.IResolverRepository;
import de.objectcode.canyon.worklist.spi.worklist.IWorklistRepository;

/**
 * @author    junglas
 * @created   15. Oktober 2003
 */
public class ServiceManager
{
  private final static  Log                     log                      = LogFactory.getLog( ServiceManager.class );

  private               IProcessRepository      m_processRepository;
  private               IInstanceRepository     m_instanceRepository;
  private               IToolRepository         m_toolRepository;
  private               IParserFactory          m_parserFactory;
  private               IBusinessCalendar       m_businessCalendar;
  private               IWorkflowEventBroker    m_workflowEventBroker;
  private               IBPEEventBroker    		m_bpeEventBroker;
  private               IEvaluatorRepository    m_evaluatorRepository;
  private               IAsyncManager           m_asyncManager;

  private               IWorklistEngine         m_worklistEngine;

  private               IWorklistRepository     m_worklistRepository;
  private               IParticipantRepository  m_participantRepository;
  private               IResolverRepository     m_resolverRepository;

  private               ILockManager            m_lockManager;

  private               BPEEngine               m_bpeEngine;


  /**
   *Constructor for the ServiceManager object
   */
  public ServiceManager()
  {
    m_parserFactory = new DefaultParserFactory();
    m_businessCalendar = new DefaultBusinessCalendar();
//    m_workflowEventBroker = new DefaultWorkflowEventBroker( this );
    m_workflowEventBroker = new AsyncWorkflowEventBroker( this );
    m_bpeEventBroker = new AsyncBPEEventBroker( this );
    m_participantRepository = new DefaultParticipantRepository();
    m_toolRepository = new DefaultToolRepository( this );
    m_evaluatorRepository = new DefaultEvaluatorRepository();
    m_lockManager = new LockManager();
  }


  /**
   * @param bpeEngine  The bpeEngine to set.
   */
  public void setBpeEngine( BPEEngine bpeEngine )
  {
    m_bpeEngine = bpeEngine;
    m_bpeEngine.setWorklistEngine( getWorklistEngine() );
    m_bpeEngine.setAsyncManager( getAsyncManager() );
    m_bpeEngine.setToolRepository( getToolRepository() );
    m_bpeEngine.setLockManager(getLockManager());
    m_bpeEngine.setBusinessCalendar(getBusinessCalendar());
    m_bpeEngine.setBpeEventBroker(getBpeEventBroker());
    m_bpeEngine.setParticipantRepository(getParticipantRepository());
  }


  /**
   * @param repository
   */
  public void setProcessRepository( IProcessRepository repository )
  {
    m_processRepository = repository;
  }


  /**
   * @param factory
   */
  public void setParserFactory( IParserFactory factory )
  {
    m_parserFactory = factory;
  }


  /**
   * @param repository
   */
  public void setInstanceRepository( IInstanceRepository repository )
  {
    m_instanceRepository = repository;
  }


  /**
   * @param calendar
   */
  public void setBusinessCalendar( IBusinessCalendar calendar )
  {
    m_businessCalendar = calendar;
    if ( m_bpeEngine != null )
      m_bpeEngine.setBusinessCalendar(calendar);
    
  }


  /**
   * @param broker
   */
  public void setWorkflowEventBroker( IWorkflowEventBroker broker )
  {
    m_workflowEventBroker = broker;
  }


  /**
   * @param repository
   */
  public void setEvaluatorRepository( IEvaluatorRepository repository )
  {
    m_evaluatorRepository = repository;
  }


  /**
   * @param repository
   */
  public void setParticipantRepository( IParticipantRepository repository )
  {
    m_participantRepository = repository;
  }


  /**
   * @param repository
   */
  public void setToolRepository( IToolRepository repository )
  {
    m_toolRepository = repository;
    if (m_bpeEngine!=null)
    	m_bpeEngine.setToolRepository( getToolRepository() );
    
  }


  /**
   * @param manager
   */
  public void setAsyncManager( IAsyncManager manager )
  {
    m_asyncManager = manager;
    if (m_bpeEngine!=null)
    	m_bpeEngine.setAsyncManager( getAsyncManager() );
  }


  /**
   * @param repository
   */
  public void setResolverRepository( IResolverRepository repository )
  {
    m_resolverRepository = repository;
  }


  /**
   * @param repository
   */
  public void setWorklistRepository( IWorklistRepository repository )
  {
    m_worklistRepository = repository;
  }


  /**
   * @param engine
   */
  public void setWorklistEngine( IWorklistEngine engine )
  {
    m_worklistEngine = engine;
    if (m_bpeEngine!=null)
    	m_bpeEngine.setWorklistEngine( getWorklistEngine() );
    
  }

  /**
   * @param lockManager  The lockManager to set.
   */
  public void setLockManager( ILockManager lockManager )
  {
    m_lockManager = lockManager;
    if ( m_bpeEngine != null )
      m_bpeEngine.setLockManager(lockManager);
  }


  /**
   * @return   Returns the bpeEngine.
   */
  public BPEEngine getBpeEngine()
  {
    return m_bpeEngine;
  }


  /**
   * @return
   */
  public IProcessRepository getProcessRepository()
  {
    return m_processRepository;
  }

  /**
   * @return
   */
  public IParserFactory getParserFactory()
  {
    return m_parserFactory;
  }


  /**
   * @return
   */
  public IInstanceRepository getInstanceRepository()
  {
    return m_instanceRepository;
  }


  /**
   * @return
   */
  public IBusinessCalendar getBusinessCalendar()
  {
    return m_businessCalendar;
  }


  /**
   * @return
   */
  public IWorkflowEventBroker getWorkflowEventBroker()
  {
    return m_workflowEventBroker;
  }


  /**
   * @return
   */
  public IEvaluatorRepository getEvaluatorRepository()
  {
    return m_evaluatorRepository;
  }


  /**
   * @return
   */
  public IParticipantRepository getParticipantRepository()
  {
    return m_participantRepository;
  }


  /**
   * @return
   */
  public IToolRepository getToolRepository()
  {
    return m_toolRepository;
  }


  /**
   * @return
   */
  public IAsyncManager getAsyncManager()
  {
    return m_asyncManager;
  }


  /**
   * @return
   */
  public IResolverRepository getResolverRepository()
  {
    return m_resolverRepository;
  }


  /**
   * @return
   */
  public IWorklistRepository getWorklistRepository()
  {
    return m_worklistRepository;
  }


  /**
   * @return
   */
  public IWorklistEngine getWorklistEngine()
  {
    return m_worklistEngine;
  }


  /**
   * @return   Returns the lockManager.
   */
  public ILockManager getLockManager()
  {
    return m_lockManager;
  }


  /**
   * Description of the Method
   *
   * @exception RepositoryException  Description of the Exception
   */
  public void beginTransaction()
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "beginTransaction" );
    }
    getInstanceRepository().beginTransaction();
    getParticipantRepository().beginTransaction();
    getProcessRepository().beginTransaction();
    getResolverRepository().beginTransaction();
    getWorklistRepository().beginTransaction();
    getAsyncManager().beginTransaction();
    getWorkflowEventBroker().beginTransaction();
    getBpeEventBroker().beginTransaction();
  }


  /**
   * Description of the Method
   *
   * @param flush                    Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   */
  public void beforeEndTransaction( boolean flush )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "beforeEndTransaction" );
    }
    getInstanceRepository().endTransaction( flush );
    getParticipantRepository().endTransaction( flush );
    getProcessRepository().endTransaction( flush );
    getResolverRepository().endTransaction( flush );
    getWorklistRepository().endTransaction( flush );
    getAsyncManager().endTransaction( flush );
  }


  /**
   * Description of the Method
   *
   * @exception RepositoryException  Description of the Exception
   */
  public void afterEndTransaction(boolean isCommit)
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "afterEndTransaction" );
    }

    try {
      if (isCommit) {
        getWorkflowEventBroker().commitTransaction();      
        getBpeEventBroker().commitTransaction();
      }
      getLockManager().releaseAllLocks();
    }
    catch ( Throwable t ) {
      log.error( "Exception", t);
      throw new RepositoryException( t );
    }
  }


public IBPEEventBroker getBpeEventBroker() {
	return m_bpeEventBroker;
}


public void setBpeEventBroker(IBPEEventBroker bpeEventBroker) {
	m_bpeEventBroker = bpeEventBroker;
}

}
