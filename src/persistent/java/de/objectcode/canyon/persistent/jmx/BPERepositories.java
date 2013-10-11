package de.objectcode.canyon.persistent.jmx;

import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.persistent.bpe.repository.ProcessInstanceRepository;
import de.objectcode.canyon.persistent.bpe.repository.ProcessRepository;
import de.objectcode.canyon.spi.ServiceManager;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.sf.hibernate.SessionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author    junglas
 * @created   13. Juli 2004
 */
public class BPERepositories implements BPERepositoriesMBean
{
  private final static  Log                        log                          = LogFactory.getLog( BPERepositories.class );
  private               String                     m_serviceManagerJndiName     = "java:/canyon/ServiceManager";
  private               String                     m_hibernateJndiName          = "java:/canyon/HibernateFactory";
  private               String                     m_bpeEngineJndiName          = "java:/canyon/BPEEngine";
  private               ProcessRepository          m_processRepository;
  private               ProcessInstanceRepository  m_processInstanceRepository;
  private               ObjectName                 m_timerService;


  /**
   * @param timerService  The timerService to set.
   */
  public void setTimerService( ObjectName timerService )
  {
    m_timerService = timerService;
  }


  /**
   * @param bpeEngineJndiName  The bpeEngineJndiName to set.
   */
  public void setBpeEngineJndiName( String bpeEngineJndiName )
  {
    m_bpeEngineJndiName = bpeEngineJndiName;
  }


  /**
   * @param hibernateJndiName  The hibernateJndiName to set.
   */
  public void setHibernateJndiName( String hibernateJndiName )
  {
    m_hibernateJndiName = hibernateJndiName;
  }


  /**
   * @param serviceManagerJndiName  The serviceManagerJndiName to set.
   */
  public void setServiceManagerJndiName( String serviceManagerJndiName )
  {
    m_serviceManagerJndiName = serviceManagerJndiName;
  }


  /**
   * @return   Returns the timerService.
   */
  public ObjectName getTimerService()
  {
    return m_timerService;
  }


  /**
   * @return   Returns the bpeEngineJndiName.
   */
  public String getBpeEngineJndiName()
  {
    return m_bpeEngineJndiName;
  }


  /**
   * @return   Returns the hibernateJndiName.
   */
  public String getHibernateJndiName()
  {
    return m_hibernateJndiName;
  }


  /**
   * @return   Returns the serviceManagerJndiName.
   */
  public String getServiceManagerJndiName()
  {
    return m_serviceManagerJndiName;
  }


  /**
   * Description of the Method
   *
   * @exception Exception  Description of the Exception
   */
  public void start()
    throws Exception
  {
    log.info( "Starting Repositories" );
    Context         ctx             = new InitialContext();
    SessionFactory  factory         = ( SessionFactory ) ctx.lookup( m_hibernateJndiName );
    ServiceManager  serviceManager  = ( ServiceManager ) ctx.lookup( m_serviceManagerJndiName );
    BPEEngine       bpeEngine       = ( BPEEngine ) ctx.lookup( m_bpeEngineJndiName );

    m_processRepository = new ProcessRepository( factory );
    m_processInstanceRepository = new ProcessInstanceRepository( factory );

    bpeEngine.setProcessRepository( m_processRepository );
    bpeEngine.setProcessInstanceRepository( m_processInstanceRepository );

    try {
  		serviceManager.beginTransaction();
    	if (System.getProperty("de.objectcode.canyon.jmx.bpe.BPEEngineService.migrationMode")==null) {
    		bpeEngine.initialize();
    	}
    }
    finally {
			boolean flush = true;
			serviceManager.beforeEndTransaction(flush);
			serviceManager.afterEndTransaction(true);
    }
  }


  /**
   * Description of the Method
   *
   * @exception Exception  Description of the Exception
   */
  public void stop()
    throws Exception { }

}
