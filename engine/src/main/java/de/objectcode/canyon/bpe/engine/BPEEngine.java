package de.objectcode.canyon.bpe.engine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.security.x509.ReasonFlags;

import de.objectcode.canyon.bpe.connector.WorklistConnectorListener;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.activities.ActivityState;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.activities.xpdl.XPDLToolActivity;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.correlation.MessageType;
import de.objectcode.canyon.bpe.engine.event.EventHub;
import de.objectcode.canyon.bpe.engine.handler.IAlarmReceiver;
import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.bpe.repository.IProcessInstanceRepository;
import de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor;
import de.objectcode.canyon.bpe.repository.IProcessRepository;
import de.objectcode.canyon.bpe.repository.IProcessVisitor;
import de.objectcode.canyon.bpe.repository.ProcessInstance;
import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.async.IAsyncManager;
import de.objectcode.canyon.spi.calendar.IBusinessCalendar;
import de.objectcode.canyon.spi.event.IBPEEventBroker;
import de.objectcode.canyon.spi.lock.ILockManager;
import de.objectcode.canyon.spi.tool.IToolRepository;
import de.objectcode.canyon.spi.tool.MessageEvent;
import de.objectcode.canyon.worklist.IWorklistEngine;
import de.objectcode.canyon.worklist.spi.participant.IParticipantRepository;

/**
 * @author    junglas
 * @created   15. Juni 2004
 */
public class BPEEngine
{
  private final static  Log                         log                            = LogFactory.getLog( BPEEngine.class );

  public final static   String                      ENGINE_ID                      = "BPE_EGINE";

  private               ILockManager                m_lockManager;
  private               IParticipantRepository      m_participantRepository;
  private               IProcessRepository          m_processRepository;
  private               IProcessInstanceRepository  m_processInstanceRepository;
  private               IWorklistEngine             m_worklistEngine;
  private               IAsyncManager               m_asyncManager;
  private               IToolRepository             m_toolRepository;
  private               EventHub                    m_eventHub;
  private				IBPEEventBroker				m_bpeEventBroker;
  
  private								IBusinessCalendar						m_businessCalendar;
  
  private               Map                         m_processIdByMessageOperation;

  private               SortedMap                   m_alarms;


  /**
   *Constructor for the BPEEngine object
   */
  public BPEEngine()
  {
    m_eventHub = new EventHub(this);
    m_processIdByMessageOperation = new HashMap();
    m_alarms = new TreeMap();
  }


  public void setParticipantRepository( IParticipantRepository participantRepository )
  {
    m_participantRepository = participantRepository;
  }

  /**
   * @param lockManager  The lockManager to set.
   */
  public void setLockManager( ILockManager lockManager )
  {
    m_lockManager = lockManager;
  }


  /**
   * @param asyncManager  The asyncManager to set.
   */
  public void setAsyncManager( IAsyncManager asyncManager )
  {
    m_asyncManager = asyncManager;
  }


  /**
   * @param toolRepository  The toolRepository to set.
   */
  public void setToolRepository( IToolRepository toolRepository )
  {
    m_toolRepository = toolRepository;
  }


  /**
   * @param processInstanceRepository  The processInstanceRepository to set.
   */
  public void setProcessInstanceRepository(
      IProcessInstanceRepository processInstanceRepository )
  {
    m_processInstanceRepository = processInstanceRepository;
  }


  /**
   * Sets the processRepository attribute of the BPEEngine object
   *
   * @param processRepository  The new processRepository value
   */
  public void setProcessRepository( IProcessRepository processRepository )
  {
    m_processRepository = processRepository;
  }


  /**
   * Sets the worklistEngine attribute of the BPEEngine object
   *
   * @param worklistEngine  The new worklistEngine value
   */
  public void setWorklistEngine( IWorklistEngine worklistEngine )
  {
    m_worklistEngine = worklistEngine;
    m_worklistEngine.addWorklistListener( new WorklistConnectorListener( this ) );
  }


  /**
   * @return   Returns the lockManager.
   */
  public ILockManager getLockManager()
  {
    return m_lockManager;
  }


  /**
   * @return   Returns the toolRepository.
   */
  public IToolRepository getToolRepository()
  {
    return m_toolRepository;
  }


  /**
   * Gets the alarms attribute of the BPEEngine object
   *
   * @return   The alarms value
   */
  public SortedMap getAlarms()
  {
    return m_alarms;
  }



  /**
   * @return   Returns the processIdByMessageOperation.
   */
  public Map getProcessIdByMessageOperation()
  {
    return m_processIdByMessageOperation;
  }


  /**
   * @return   Returns the asyncManager.
   */
  public IAsyncManager getAsyncManager()
  {
    return m_asyncManager;
  }


  /**
   * @return   Returns the processRepository.
   */
  public IProcessRepository getProcessRepository()
  {
    return m_processRepository;
  }


  /**
   * @return   Returns the processInstanceRepository.
   */
  public IProcessInstanceRepository getProcessInstanceRepository()
  {
    return m_processInstanceRepository;
  }


  /**
   * @return   Returns the eventHub.
   */
  public EventHub getEventHub()
  {
    return m_eventHub;
  }


  /**
   * @return   Returns the worklistEngine.
   */
  public IWorklistEngine getWorklistEngine()
  {
    return m_worklistEngine;
  }

  /**
   * Gets the process attribute of the BPEEngine object
   *
   * @param processInstanceId        Description of the Parameter
   * @return                         The process value
   * @exception RepositoryException  Description of the Exception
   */
  public BPEProcess getProcessInstance( String processInstanceId )
    throws RepositoryException
  {
	  return getProcessInstance(processInstanceId, true);
  }
  

  /**
   * Gets the process attribute of the BPEEngine object
   *
   * @param processInstanceId        Description of the Parameter
   * @return                         The process value
   * @exception RepositoryException  Description of the Exception
   */
  public BPEProcess getProcessInstanceReadOnly( String processInstanceId )
    throws RepositoryException
  {
	  return getProcessInstance(processInstanceId, false);
  }
  
  
  /**
   * Gets the process attribute of the BPEEngine object
   *
   * @param processInstanceId        Description of the Parameter
   * @return                         The process value
   * @exception RepositoryException  Description of the Exception
   */
  private BPEProcess getProcessInstance( String processInstanceId, boolean doLock )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "getProcessInstance: " + processInstanceId );
    }
		long start = 0;
		if (log.isInfoEnabled())
			start = System.currentTimeMillis();

	if (doLock)
		m_lockManager.lock(processInstanceId);

    ProcessInstance  processInstance  = m_processInstanceRepository.getProcessInstance( processInstanceId );

    if ( processInstance == null ) {
      return null;
    }

    BPEProcess       process          = m_processRepository.getProcess( processInstance.getProcessEntityOid() );

    process.setProcessInstanceId( processInstanceId );
    process.setBPEEngine( this );

    try {
      process.hydrate( new HydrationContext(), new ObjectInputStream( new ByteArrayInputStream( processInstance.getProcessState() ) ) );
    }
    catch ( IOException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
		if (log.isInfoEnabled())
			log.info("BPEEngine.getProcessInstance:" + (System.currentTimeMillis()-start));
    
    return process;
  }


  /**
   * Description of the Method
   *
   * @param processInstanceId        Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @exception EngineException      Description of the Exception
   */
  public void terminateProcessInstance( String processInstanceId )
    throws RepositoryException, EngineException
  {
    if ( log.isDebugEnabled() ) {
      log.debug("terminateProcessInstance: " + processInstanceId );
    }
    
    if ( processInstanceId != null && processInstanceId.length() > 0 ) {
      BPEProcess  process  = getProcessInstance( processInstanceId );

      process.terminateProcess();

      updateProcessInstance( process );
      terminateSubProcessInstances(new HashMap(), processInstanceId);
    }
  }


  /**
   * Description of the Method
   *
   * @param process                  Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   */
  public void updateProcessInstance( BPEProcess process )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "updateProcessInstance: " + process.getProcessInstanceId() );
    }

    Iterator               it               = process.getAlarmReceivers().iterator();

    while ( it.hasNext() ) {
      IAlarmReceiver  alarmReceiver  = ( IAlarmReceiver ) it.next();

      if ( alarmReceiver.isActive() ) {
        addAlarm( alarmReceiver.getAlarmTime(), process.getProcessInstanceId() );
      }
    }

    ByteArrayOutputStream  bos              = new ByteArrayOutputStream();

    try {
      ObjectOutputStream  oos  = new ObjectOutputStream( bos );

      process.dehydrate( new HydrationContext(), oos );
      oos.close();
    }
    catch ( IOException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }

    ProcessInstance        processInstance  = new ProcessInstance( process.getProcessEntityOid(), process.getId(), process.getProcessInstanceId(),
        process.getParentProcessInstanceId(), process.getState(), bos.toByteArray() );

    m_processInstanceRepository.updateProcessInstance( processInstance );
    if ( log.isInfoEnabled() ) {
      log.info( "updated process id='" + process.getId() + "', piid='" + processInstance.getProcessInstanceId() + "', size=" + processInstance.getProcessState().length);
    }
    
  }

	public long createPackageRevision(String id, String version) throws RepositoryException {
    return m_processRepository.createPackageRevision( id, version );
	}
  

  /**
   * Description of the Method
   *
   * @param process                  Description of the Parameter
   * @param processSource            Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   */
  public void createProcess( long packageRevisionOid, BPEProcess process, Serializable processSource )
    throws RepositoryException
  {
    process.setBPEEngine( this );
    m_processRepository.createProcess( packageRevisionOid, process, processSource );

    Iterator  it  = process.getCreateInstanceOperations().iterator();

    while ( it.hasNext() ) {
      MessageType  messageType  = ( MessageType ) it.next();

      m_processIdByMessageOperation.put( messageType.getMessageOperation(), process.getId() );
    }
  }


  /**
   * Description of the Method
   *
   * @param process                  Description of the Parameter
   * @param processSource            Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @deprecated PBPEProcess is now immutable
   */
  public void createOrReplaceProcess( long packageRevisionOid, BPEProcess process, Serializable processSource )
    throws RepositoryException
  {
  	createProcess(packageRevisionOid,process,processSource);
//    process.setBPEEngine( this );
//
//    try {
//      BPEProcess oldProcess = m_processRepository.getProcess( process.getId() );
//      if ( oldProcess != null ) {
//        process.setProcessEntityOid(oldProcess.getProcessEntityOid());
//        m_processRepository.updateProcess( process, processSource );
//      } else {
//        m_processRepository.saveProcess( process, processSource );
//      }
//    }
//    catch ( ObjectNotFoundException e ) {
//      m_processRepository.saveProcess( process, processSource );
//    }
//
//    Iterator  it  = process.getCreateInstanceOperations().iterator();
//
//    while ( it.hasNext() ) {
//      MessageType  messageType  = ( MessageType ) it.next();
//
//      m_processIdByMessageOperation.put( messageType.getMessageOperation(), process.getId() );
//    }
  }

  public int terminateProcessInstances(MessageEvent event) throws RepositoryException {
  	TerminateProcessInstanceVisitor visitor = new TerminateProcessInstanceVisitor(m_processRepository, event, false); 
    m_processInstanceRepository.iterateProcessInstances( event.getProcessId(), true, visitor); 
    int result = visitor.getNumberOfTerminatableProcessInstances();
    Iterator terminatedProcessInstanceIds = visitor.getTerminatedProcessInstanceIds().iterator();
    while (terminatedProcessInstanceIds.hasNext()) {
    	String terminatedProcessInstanceId = (String) terminatedProcessInstanceIds.next();
    	terminateSubProcessInstances(visitor.getProcessCache(),terminatedProcessInstanceId);
    }
    return result;
  }

  public List<String> reanimateProcessInstances(boolean readOnly) throws RepositoryException {
  	ReanimateProcessInstanceVisitor visitor = new ReanimateProcessInstanceVisitor(m_processRepository, readOnly); 
    m_processInstanceRepository.iterateProcessInstances( true, visitor); 
    List result = visitor.getMessages();
    return result;
  }

  
  public int countTerminateProcessInstances(MessageEvent event) throws RepositoryException {
  	TerminateProcessInstanceVisitor visitor = new TerminateProcessInstanceVisitor(m_processRepository, event, true); 
    m_processInstanceRepository.iterateProcessInstances( event.getProcessId(), true, visitor); 
    int result = visitor.getNumberOfTerminatableProcessInstances();
    return result;
  }
  
  public void terminateSubProcessInstances(Map processCache, String parentProcessInstanceId) throws RepositoryException {
  	TerminateSubProcessInstanceVisitor subVisitor = new TerminateSubProcessInstanceVisitor(m_processRepository, processCache);
    m_processInstanceRepository.iterateSubProcessInstances( parentProcessInstanceId, subVisitor);  	
    Iterator subProcessInstanceIds = subVisitor.getSubProcessInstanceIds().iterator();
    while (subProcessInstanceIds.hasNext()) {
    	String subProcessInstanceId = (String) subProcessInstanceIds.next();
    	terminateSubProcessInstances(processCache,subProcessInstanceId);
    }
  }
  
  /**
   * Description of the Method
   *
   * @param processId                Description of the Parameter
   * @param context                  Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  String createProcessInstance( BPERuntimeContext context, String processId, String parentProcessInstanceIdPath )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug("createProcessInstance: " + context + " " + processId);
    }
    
    Long packageRevisionOid = getPackageRevisionOid(parentProcessInstanceIdPath);
    BPEProcess             process          =  m_processRepository.getProcess( processId , packageRevisionOid);

    process.setStartedDate( new Date());
    process.setStartedBy( context.getUserId() );
    process.setClientId( context.getClientId() );
    process.setParentProcessInstanceIdPath( parentProcessInstanceIdPath );

    ByteArrayOutputStream  bos              = new ByteArrayOutputStream();

    try {
      ObjectOutputStream  oos  = new ObjectOutputStream( bos );

      process.dehydrate( new HydrationContext(), oos );
      oos.close();
    }
    catch ( IOException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }


    ProcessInstance        processInstance  = new ProcessInstance( process.getProcessEntityOid(), process.getId(), process.getProcessInstanceId(),
        process.getParentProcessInstanceId(), process.getState(), bos.toByteArray() );

    String piid = m_processInstanceRepository.saveProcessInstance( processInstance );
    if ( log.isInfoEnabled() ) {
      log.info( "started process id='" + process.getId() + "', piid='"+piid+"', parentPiidPath='"+parentProcessInstanceIdPath+"', userId='" + context.getUserId() + "', clientId='" + context.getClientId() +", size=" + processInstance.getProcessState().length);
    }
    
    return piid;
  }


  public Long getPackageRevisionOid(String parentProcessInstanceIdPath)
  throws RepositoryException {
		Long packageRevisionOid = null;
		long start = 0;
		if (log.isInfoEnabled())
			start = System.currentTimeMillis();
		try {
			String parentProcessInstanceProcessVersion = null;
			String parentProcessInstanceId = null;
			String parentProcessInstanceProcessID = null;
			if (parentProcessInstanceIdPath != null) {
				int index = parentProcessInstanceIdPath.lastIndexOf("_");
				if (index < parentProcessInstanceIdPath.length()) {
					parentProcessInstanceId = parentProcessInstanceIdPath
							.substring(index + 1);
					ProcessInstance parentProcessInstance = m_processInstanceRepository
							.getProcessInstance(parentProcessInstanceId);
					packageRevisionOid = m_processRepository
							.getPackageRevisionOid(parentProcessInstance
									.getProcessEntityOid());
				}
			}
			if (log.isInfoEnabled()) {
				if (packageRevisionOid != null)
					log.debug("DETERMINED PACKAGEREVISIONOID='" + packageRevisionOid 
							+ "' FROM PATH "
							+ parentProcessInstanceIdPath);
				else {
					if (parentProcessInstanceIdPath!=null) {
						throw new RuntimeException("CANNOT DETERMINE PACKAGEREVISIONOID FROM PATH " + parentProcessInstanceIdPath);
					}
				}
			}
		} catch (Exception e) {
			log.error("ERROR DURING DETERMINATION OF PACKAGEREVISION FROM PATH " + parentProcessInstanceIdPath, e);
			throw new RepositoryException("ERROR DURING DETERMINATION OF PACKAGEREVISION FROM PATH " + parentProcessInstanceIdPath, e);
		}
		if (log.isInfoEnabled()) {
	    	long split =  System.currentTimeMillis() - start;
	      log.info( "Split BPEEngine.getVersion:" + split + " ms" );  	
	    }
		return packageRevisionOid;
  }
  
	private String getVersion(String processId, String parentProcessInstanceIdPath)
			throws RepositoryException {
		String processVersion = null;
		long start = 0;
		if (log.isInfoEnabled())
			start = System.currentTimeMillis();
		try {
			String parentProcessInstanceProcessVersion = null;
			String parentProcessInstanceId = null;
			String parentProcessInstanceProcessID = null;
			if (parentProcessInstanceIdPath != null) {
				int index = parentProcessInstanceIdPath.lastIndexOf("_");
				if (index < parentProcessInstanceIdPath.length())
					parentProcessInstanceId = parentProcessInstanceIdPath
							.substring(index + 1);
				ProcessInstance parentProcessInstance = m_processInstanceRepository
						.getProcessInstance(parentProcessInstanceId);
				BPEProcess parentProcess = m_processRepository
						.getProcess(parentProcessInstance.getProcessEntityOid());
				parentProcessInstanceId = parentProcess.getId();
				parentProcessInstanceProcessVersion = parentProcess.getVersion();
				WorkflowProcess parentWorkflowProcess = (WorkflowProcess) m_processRepository
						.getProcessSource(parentProcessInstanceId,
								parentProcessInstanceProcessVersion);
				WorkflowPackage parentWorkflowPackage = parentWorkflowProcess
						.getPackage();
				WorkflowProcess workflowProcess = parentWorkflowPackage
						.getWorklowProcess(processId);
				processVersion = workflowProcess.findWorkflowVersion();
			}
			if (log.isInfoEnabled()) {
				if (processVersion != null)
					log.info("DETERMINED PROCESSVERSION '" + processVersion
							+ "' FOR PROCESSID='" + processId + "' FROM PATH "
							+ parentProcessInstanceIdPath);
				else {
					if (parentProcessInstanceIdPath!=null)
						log.warn("CANNOT DETERMINE PROCESS VERSION FOR PROCESSID='"
								+ processId + "' FROM PATH " + parentProcessInstanceIdPath);
					else
						log.info("USING ACTIVE PROCESS VERSION FOR PROCESSID '" + processId + "'");
				}
			}
		} catch (Exception e) {
			log.error("ERROR DURING DETERMINATION OF PROCESS VERSION FOR PROCESSID='" + processId
					+ "' FROM PATH " + parentProcessInstanceIdPath, e);
		}
		if (log.isInfoEnabled()) {
	    	long split =  System.currentTimeMillis() - start;
	      log.info( "Split BPEEngine.getVersion:" + split + " ms" );  	
	    }
			
		return processVersion;
	}


  /**
   * Description of the Method
   *
   * @exception RepositoryException  Description of the Exception
   * @exception EngineException      Description of the Exception
   */
  public void initialize()
    throws RepositoryException, EngineException
  {
    m_processRepository.iterateProcesses( new ProcessVisitor(), false );
    m_processInstanceRepository.iterateProcessInstances( true, new ProcessInstanceVisitor() );
  }


  /**
   * Description of the Method
   *
   * @param message                  Description of the Parameter
   * @param context                  Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   * @exception EngineException      Description of the Exception
   */
  public String handleMessage( BPERuntimeContext context, Message message )
    throws RepositoryException, EngineException
  {
    if ( log.isDebugEnabled() ) {
      log.debug("handleMessage: " + context + " " + message);
    }
    
    if ( m_processIdByMessageOperation.containsKey( message.getOperation() ) ) {
      String      processId          = ( String ) m_processIdByMessageOperation.get( message.getOperation() );

      String      processInstanceIdPath = ( String ) message.getContent().get( "parentProcessIdPath" );
      String      processInstanceId  = createProcessInstance( context, processId, processInstanceIdPath );

      m_lockManager.lock(processInstanceId);
      
      BPEProcess  process            = getProcessInstance( processInstanceId );

      process.activate();
      process.startActivatedActivities();

      process.handleMessage( message );

      updateProcessInstance( process );

      return processInstanceId;
    }

    return null;
  }


  /**
   * Adds a feature to the Alarm attribute of the BPEEngine object
   *
   * @param alarmTime          The feature to be added to the Alarm attribute
   * @param processInstanceId  The feature to be added to the Alarm attribute
   */
  synchronized void addAlarm( long alarmTime, String processInstanceId )
  {
    List  alarms  = ( List ) m_alarms.get( new Long( alarmTime ) );

    if ( alarms == null ) {
      alarms = new ArrayList();

      m_alarms.put( new Long( alarmTime ), alarms );
    }

    alarms.add( processInstanceId );
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public boolean checkAlarms() {
    String processInstanceId;
    synchronized (m_alarms) {
      if (m_alarms.isEmpty()) {
	return false;
      }

      long now = System.currentTimeMillis();
      Long first = (Long) m_alarms.firstKey();

      if (first == null || first.longValue() >= now)
	return false;

      List alarms = (List) m_alarms.get(first);
      processInstanceId = (String) alarms.get(0);

      alarms.remove(0);

      if (alarms.size() == 0) {
	m_alarms.remove(first);
      }
    }

    if (processInstanceId != null) {
      try {
	BPEProcess process = getProcessInstance(processInstanceId);

	process.handleAlarm();

	updateProcessInstance(process);
      } catch (Exception e) {
	// TODO Sollte hier nicht ein Rollback gemacht werden?
	log.error("Exception", e);
      }
      return true;
    } else
      return false;
  }


  /**
         * Description of the Method
         * 
         * @param message
         *                Description of the Parameter
         * @param processInstanceId
         *                Description of the Parameter
         * @exception RepositoryException
         *                    Description of the Exception
         * @exception EngineException
         *                    Description of the Exception
         */
  public void handleMessage( String processInstanceId, Message message )
    throws RepositoryException, EngineException
  {
    if ( log.isDebugEnabled() ) {
      log.debug("handleMessage: " + processInstanceId + " " + message);
    }
    
    if ( processInstanceId != null && processInstanceId.length() > 0 ) {
      BPEProcess  process  = getProcessInstance( processInstanceId );

      process.handleMessage( message );

      updateProcessInstance( process );
    }
  }

  private class ReanimateProcessInstanceVisitor implements IProcessInstanceVisitor {
  	
  	public ReanimateProcessInstanceVisitor(IProcessRepository repository, boolean readOnly) {
  		m_readOnly = readOnly;
  		m_processRepository = repository;
		}

    private  IProcessRepository  m_processRepository;
		ArrayList<String> m_messages = new ArrayList<String>();
  	boolean m_readOnly = true;

		public void visit(ProcessInstance processInstance) throws RepositoryException {
	    BPEProcess process = m_processRepository.getProcess( processInstance.getProcessEntityOid() );

      try {
        process.setBPEEngine( BPEEngine.this );      	
        // ???
        process.setProcessInstanceId( processInstance.getProcessInstanceId() );
        process.hydrate( new HydrationContext(), new ObjectInputStream( new ByteArrayInputStream( processInstance.getProcessState() ) ) );

        Iterator activities = process.getActitivities().iterator();
        while (activities.hasNext()) {
        	Activity activity = (Activity) activities.next();
        	if (activity instanceof XPDLToolActivity) {
        		XPDLToolActivity xpdlToolActivity = (XPDLToolActivity) activity;
      			StringBuilder buffy = new StringBuilder();
      			buffy.append(processInstance.getProcessId()).append("/");
      			buffy.append(processInstance.getProcessInstanceId()).append("/");
      			buffy.append(xpdlToolActivity.getId()).append(":");
        		if (xpdlToolActivity.getState() == ActivityState.RUNNING) {
        			if (m_readOnly)
        				buffy.append("STILL RUNNING -> MIGHT NEED REANIMATION");
        			else {
        					buffy.append("STILL RUNNING -> REANIMATING");
        					try {
        						 getAsyncManager().asyncCompleteBPEActivity(processInstance.getProcessInstanceId(),
        								xpdlToolActivity.getId());
	        					buffy.append("-> SUCCESS");
									} catch (Exception e) {
	        					buffy.append("-> ERROR");
									}
        			}
        		} else {
        			buffy.append("OK(").append("State=").append(xpdlToolActivity.getState()).append(")");
        		}
      			m_messages.add(buffy.toString());
        	}
        }
      }
      catch ( IOException e ) {
        log.error( "Exception", e );
        throw new RepositoryException( e );
      }
		}

		public List<String> getMessages() {
			return m_messages;
		}  	
  }
  
  
  private class TerminateProcessInstanceVisitor implements IProcessInstanceVisitor
  {
    private  MessageEvent		 m_messageEvent;
    private  IProcessRepository  m_processRepository;
    private Map m_processCache = new HashMap();
    private  boolean m_countOnly;
    
    private List m_terminatedProcessInstanceIds = new ArrayList();
    private int m_numberOfTerminatableProcessInstances=0;

    /**
     *Constructor for the ProcessInstanceClientIdVisitor object
     *
     * @param processRepository  Description of the Parameter
     * @param clientId           Description of the Parameter
     */
    TerminateProcessInstanceVisitor( IProcessRepository processRepository, MessageEvent event, boolean countOnly )
    {
      m_processRepository = processRepository;
      m_messageEvent = event;
      m_countOnly = countOnly;
    }


    /**
     * @param processInstance          Description of the Parameter
     * @exception RepositoryException  Description of the Exception
     * @throws 
     * @see                            de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor#visit(de.objectcode.canyon.bpe.repository.ProcessInstance)
     */
    public void visit( ProcessInstance processInstance )
      throws RepositoryException
    {
      BPEProcess  process  = ( BPEProcess ) m_processCache.get( new Long( processInstance.getProcessEntityOid() ) );

      if ( process == null ) {
        process = m_processRepository.getProcess( processInstance.getProcessEntityOid() );

        m_processCache.put( new Long( processInstance.getProcessEntityOid() ), process );
      }


      try {
        process.setBPEEngine( BPEEngine.this );      	
        // ???
        process.setProcessInstanceId( processInstance.getProcessInstanceId() );
        process.hydrate( new HydrationContext(), new ObjectInputStream( new ByteArrayInputStream( processInstance.getProcessState() ) ) );
        
        String clientId = m_messageEvent.getClientId();

        if ( (clientId == null ||
        	clientId.equals( process.getClientId())
        	&& (process.getState() == ActivityState.RUNNING || process.getState() == ActivityState.COMPLETED )) ) {
          Map params = m_messageEvent.getEventParams();
          Iterator parameterNames = params.keySet().iterator();
          if (matchesParameters(process, params, parameterNames)) {
          	m_numberOfTerminatableProcessInstances++;
          	if (!m_countOnly && process.getState() == ActivityState.RUNNING ) {
          		m_lockManager.lock(process.getProcessInstanceId());
          		process.terminateProcess();
          		updateProcessInstance( process );
          	}
            m_terminatedProcessInstanceIds.add(process.getProcessInstanceId());
          }
        }
      }
      catch ( IOException e ) {
        log.error( "Exception", e );
        throw new RepositoryException( e );
      } catch (EngineException e) {
        log.error( "Exception", e );
        throw new RepositoryException( e );
      }
    }


		private boolean matchesParameters(BPEProcess process, Map params, Iterator parameterNames) {
			boolean matches = true;
			while (parameterNames.hasNext()) {
			  String parameterName = (String) parameterNames.next();
			  Object value = params.get(parameterName);
			  IVariable variable = process.getVariable(parameterName);
			  if (variable==null) {
			    matches = false;
			    break;
			  } else {
			    Object variableValue = variable.getValue();
			    if (value!=null && variableValue!=null && !value.equals(variableValue)) {
			      matches = false;
			      break;
			    } 
			  }
			}
			return matches;
		}
		public List getTerminatedProcessInstanceIds() {
			return m_terminatedProcessInstanceIds;
		}
		public Map getProcessCache() {
			return m_processCache;
		}


		public int getNumberOfTerminatableProcessInstances() {
			return m_numberOfTerminatableProcessInstances;
		}
  }
  

  private class TerminateSubProcessInstanceVisitor implements
			IProcessInstanceVisitor {
		private String m_parentProcessInstanceId;
    private Map m_processCache = new HashMap();
    private List m_subProcessInstanceIds = new ArrayList();

		private IProcessRepository m_processRepository;

		/**
		 * Constructor for the ProcessInstanceClientIdVisitor object
		 * 
		 * @param processRepository
		 *          Description of the Parameter
		 * @param clientId
		 *          Description of the Parameter
		 */
		TerminateSubProcessInstanceVisitor(IProcessRepository processRepository, Map processCache ) {
			m_processRepository = processRepository;
			m_processCache = processCache;
		}

		
		public List getSubProcessInstanceIds() {
			return m_subProcessInstanceIds;
		}
		
		/**
		 * @param processInstance
		 *          Description of the Parameter
		 * @exception RepositoryException
		 *              Description of the Exception
		 * @throws
		 * @see de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor#visit(de.objectcode.canyon.bpe.repository.ProcessInstance)
		 */
		public void visit(ProcessInstance processInstance)
				throws RepositoryException {
      BPEProcess  process  = ( BPEProcess ) m_processCache.get( new Long( processInstance.getProcessEntityOid() ) );

      if ( process == null ) {
        process = m_processRepository.getProcess( processInstance.getProcessEntityOid() );

        m_processCache.put( new Long( processInstance.getProcessEntityOid() ), process );
      }

			try {
				// ???
				process.setBPEEngine(BPEEngine.this);
				process.setProcessInstanceId(processInstance.getProcessInstanceId());
				process.hydrate(new HydrationContext(), new ObjectInputStream(
						new ByteArrayInputStream(processInstance.getProcessState())));
				if (process.getState()==ActivityState.RUNNING) {
					m_lockManager.lock(process.getProcessInstanceId());
					process.terminateProcess();
					updateProcessInstance(process);
				}
				m_subProcessInstanceIds.add(processInstance.getProcessInstanceId());
			} catch (IOException e) {
				log.error("Exception", e);
				throw new RepositoryException(e);
			} catch (EngineException e) {
				log.error("Exception", e);
				throw new RepositoryException(e);
			}
		}
	}
  
  /**
	 * Description of the Class
	 * 
	 * @author junglas
	 * @created 24. Juni 2004
	 */
  private class ProcessVisitor implements IProcessVisitor
  {
    /**
		 * @param process
		 *          Description of the Parameter
		 * @param processSource
		 *          Description of the Parameter
		 * @see de.objectcode.canyon.bpe.repository.IProcessVisitor#visit(de.objectcode.canyon.bpe.engine.activities.BPEProcess)
		 */
    public void visit( BPEProcess process, Serializable processSource )
    {
      Iterator  it  = process.getCreateInstanceOperations().iterator();

      while ( it.hasNext() ) {
        MessageType  messageType  = ( MessageType ) it.next();

        m_processIdByMessageOperation.put( messageType.getMessageOperation(), process.getId() );
      }
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   24. Juni 2004
   */
  private class ProcessInstanceVisitor implements IProcessInstanceVisitor
  {
    private  Map  m_processCache  = new HashMap();


    /**
     * @param processInstance          Description of the Parameter
     * @exception RepositoryException  Description of the Exception
     * @see                            de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor#visit(de.objectcode.canyon.bpe.repository.ProcessInstance)
     */
    public void visit( ProcessInstance processInstance )
      throws RepositoryException
    {
      BPEProcess  process  = ( BPEProcess ) m_processCache.get( new Long( processInstance.getProcessEntityOid() ) );

      if ( process == null ) {
        process = m_processRepository.getProcess( processInstance.getProcessEntityOid() );

        m_processCache.put( new Long( processInstance.getProcessEntityOid() ), process );
      }

      try {
        process.setProcessInstanceId( processInstance.getProcessInstanceId() );
        process.setBPEEngine( BPEEngine.this );

        process.hydrate( new HydrationContext(), new ObjectInputStream( new ByteArrayInputStream( processInstance.getProcessState() ) ) );

        Iterator  it  = process.getAlarmReceivers().iterator();

        while ( it.hasNext() ) {
          IAlarmReceiver  alarmReceiver  = ( IAlarmReceiver ) it.next();

          if ( alarmReceiver.isActive() ) {
            addAlarm( alarmReceiver.getAlarmTime(), process.getProcessInstanceId() );
          }
        }
      }
      catch ( IOException e ) {
        log.error( "Exception", e );
        throw new RepositoryException( e );
      }
    }
  }


	public IBusinessCalendar getBusinessCalendar() {
		return m_businessCalendar;
	}


	public void setBusinessCalendar(IBusinessCalendar businessCalendar) {
		m_businessCalendar = businessCalendar;
	}


	public IBPEEventBroker getBpeEventBroker() {
		return m_bpeEventBroker;
	}


	public void setBpeEventBroker(IBPEEventBroker bpeEventBroker) {
		m_bpeEventBroker = bpeEventBroker;
	}


	public IParticipantRepository getParticipantRepository() {
		return m_participantRepository;
	}


}
