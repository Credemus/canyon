package de.objectcode.canyon.ejb.async;

import java.util.HashMap;
import java.util.Iterator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.Status;

import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.BPERuntimeContext;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.engine.variable.ComplexValue;
import de.objectcode.canyon.ejb.BaseMessageBean;
import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.tool.IMessageHandler;
import de.objectcode.canyon.spi.tool.MessageEvent;
import de.objectcode.canyon.worklist.IWorklistEngine;

/**
 * @ejb:bean
 *      name="WorklistEventBean"
 *      display-name="Canyon Async worklist event Dispatcher"
 *      transaction-type="Bean"
 *      destination-type="javax.jms.Queue"
 *      acknowledge-mode="Auto-acknowledge"
 *      subscription-durability="Durable"
 * @jboss:destination-jndi-name
 *      name="queue/WSApplicationEvent"
 * @weblogic:message-driven
 *      destination-jndi-name="queue/WSApplicationEvent"
 *      jms-polling-interval-seconds="10"
 *
 * @ejb.resource-ref res-ref-name="ServiceManager" res-type="de.objectcode.canyon.spi.ServiceManager"
 *   res-auth="Application"
 * @jboss.resource-ref res-ref-name="ServiceManager" jndi-name="java:/canyon/ServiceManager"
 * @ejb.resource-ref res-ref-name="jms/QCF" res-type="javax.jms.QueueConnectionFactory"
 *   res-auth="Container"
 * @jboss.resource-ref res-ref-name="jms/QCF" jndi-name="ConnectionFactory"
 *
 *
 * @author    junglas
 * @created   22. Juli 2004
 */
public class WorklistEventBean extends BaseMessageBean implements MessageListener
{
	static final long serialVersionUID = 3573126815689237103L;
	
	private final static  String           SERVICE_MANAGER_COMP_NAME  = "java:comp/env/ServiceManager";
  protected transient   ServiceManager   m_serviceManager;
  protected transient   IWorklistEngine  m_worklistEngine;
  private               QueueConnection  m_connection;
  private               QueueSession     m_session;


  /**
   * Description of the Method
   *
   * @exception JMSException     Description of the Exception
   * @exception NamingException  Description of the Exception
   */
  private void setupQueueSession()
    throws JMSException, NamingException
  {
    InitialContext          iniCtx  = new InitialContext();
    Object                  tmp     = iniCtx.lookup( "java:comp/env/jms/QCF" );
    QueueConnectionFactory  qcf     = ( QueueConnectionFactory ) tmp;
    m_connection = qcf.createQueueConnection();
    m_session = m_connection.createQueueSession( false, QueueSession.AUTO_ACKNOWLEDGE );
    m_connection.start();
  }


  /**
   * Create a new ZReihe SessionBean.
   */
  public void ejbCreate()
  {
    m_log.debug( "ejbCreate" );

    try {
      obtainServiceManager();

      setupQueueSession();
    }
    catch ( Exception e ) {
      m_log.fatal( "Exception", e );
    }
  }


  /**
   * Description of the Method
   */
  public void ejbRemove()
  {
    super.ejbRemove();

    m_log.debug( "ejbRemove" );
    m_ctx = null;
    try {
      if ( m_session != null ) {
        m_session.close();
      }
      if ( m_connection != null ) {
        m_connection.close();
      }
    }
    catch ( JMSException e ) {
      e.printStackTrace();
    }
  }


  /**
   * Description of the Method
   *
   * @exception NamingException  Description of the Exception
   */
  protected void obtainServiceManager()
    throws NamingException
  {
    m_log.debug( "obtainServiceManager" );

    InitialContext  ctx     = new InitialContext();

    ServiceManager  svcMgr  = ( ServiceManager ) ctx.lookup( SERVICE_MANAGER_COMP_NAME );

    m_serviceManager = svcMgr;
    m_worklistEngine = svcMgr.getWorklistEngine();
  }


  /**
   * Description of the Method
   *
   * @param message  Description of the Parameter
   */
  private void resend( Message message )
  {
    m_log.debug( "resend" );

    if ( message instanceof ObjectMessage ) {
      try {
        Queue          dest         = ( Queue ) message.getJMSDestination();
        QueueSender    sender       = m_session.createSender( dest );
        ObjectMessage  newMessage   = m_session.createObjectMessage( ( ( ObjectMessage ) message ).getObject() );

        int            resendCount  = 0;

        try {
          resendCount = message.getIntProperty( "resendCount" );
        }
        catch ( Exception e ) {
        }

        newMessage.setIntProperty( "resendCount", resendCount + 1 );

        sender.send( newMessage );
        sender.close();
      }
      catch ( Exception e ) {
        m_log.fatal( "Exception", e );
      }
    }
  }


  /**
   * Description of the Method
   *
   * @param message  Description of the Parameter
   */
  public void onMessage( Message message )
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug( "onMessage(" + message + ")" );
    }

    int  resendCount  = 0;

    try {
      resendCount = message.getIntProperty( "resendCount" );
    }
    catch ( Exception e ) {
    }

    if ( m_log.isDebugEnabled() ) {
      m_log.debug( "Resend: " + resendCount );
    }

    try {
      beginTransaction();
      if ( message instanceof ObjectMessage ) {
        ObjectMessage  objMsg   = ( ObjectMessage ) message;
        Object         payload  = objMsg.getObject();
        if ( payload instanceof HashMap ) {
          HashMap  eventParams     = ( HashMap ) payload;

          String   startProcessId  = null;
          String   terminateProcessId = null;
          String   engineId        = objMsg.getStringProperty( "engineId" );
          String   eventType       = objMsg.getStringProperty( "eventType" );
          String   processId = objMsg.getStringProperty( "processId" );
          String   action = objMsg.getStringProperty( "action" );
          if (action==null)
          	action = MessageEvent.ACTION_NOTIFY;
          if (MessageEvent.ACTION_START.equals(action))
          	startProcessId = processId;
          else if (MessageEvent.ACTION_TERMINATE.equals(action))
          	terminateProcessId = processId;
          String   multiplicity    = objMsg.getStringProperty( "multiplicity" );
          if (multiplicity==null)
          	multiplicity = MessageEvent.MULTIPLICITY_ONE;
          String   userId          = objMsg.getStringProperty( "userId" );
          String   clientId        = objMsg.getStringProperty( "clientId" );
          String   parentProcessInstanceIdPath = objMsg.getStringProperty( "parentProcessInstanceIdPath" );

          MessageEvent event = new MessageEvent(eventParams, engineId, eventType, processId, action, userId, clientId, parentProcessInstanceIdPath);
          
          if (terminateProcessId!=null) {
            int numberOfTerminations = m_serviceManager.getBpeEngine().terminateProcessInstances(event);
            if (numberOfTerminations==0) {
              m_log.warn( "No processInstances terminated by event: " + eventType + " " + eventParams );
            }
            return;
          }
          
          if ( resendCount <= 3 && eventType != null ) {
          	String[] workItemIds = m_worklistEngine.findWorkItemsForEvent( event );
            rollbackTransaction();
            beginTransaction();
            if (workItemIds.length>0) { 
            	for (int i = 0; i < workItemIds.length; i++) {
								String workItemId = workItemIds[i];
								if (!m_worklistEngine.handleEvent(workItemId, event)) {
									resend(message);
								}
								if (MessageEvent.MULTIPLICITY_ONE.equals(multiplicity) || MessageEvent.MULTIPLICITY_EXACTLY_ONE.equals(multiplicity))
									break;
							}
						} else {
              if ( startProcessId == null ) {
                m_log.error( "No process received event: " + eventType + " " + eventParams );
                return;
              }

              WorkflowProcess  process  = ( WorkflowProcess ) m_serviceManager.getBpeEngine().getProcessRepository().getProcessSource( startProcessId );
              if (process == null) {
              	m_log.error("Cannot find process definition for id '" + startProcessId + "'");
              	return;
              }
              	
              ExtendedAttribute messageHandlerAttr = process.getExtendedAttribute("canyon:messageHandlerClass");
              if (messageHandlerAttr != null) {
                String className = messageHandlerAttr.getValue();
                try {
                  Class clazz = Class.forName(className);
                  IMessageHandler messageHandler = (IMessageHandler) clazz.newInstance();
                  messageHandler.handle(event);
                } catch (Exception e) {
                  m_log.error( "Error during message handling:",e );
                  return;
                }
              }

              if ( BPEEngine.ENGINE_ID.equals(engineId)) {
                if ( m_log.isInfoEnabled() ) {
                  m_log.info("Starting clientId='" + clientId + "', process id='" + startProcessId + "' by event: " + eventType + " " + eventParams );
                }

                ComplexValue content = new ComplexValue(new ComplexType(startProcessId + "-subflow-request"));
                de.objectcode.canyon.bpe.engine.correlation.Message msg = new de.objectcode.canyon.bpe.engine.correlation.Message(startProcessId + "-init", content);
                
                Iterator  it                 = eventParams.keySet().iterator();

                while ( it.hasNext() ) {
                  String  name   = ( String ) it.next();
                  Object  value  = eventParams.get( name );
                
                  content.set(name,value);
                }
                content.set("parentProcessIdPath", parentProcessInstanceIdPath);
                String piid = m_serviceManager.getBpeEngine().handleMessage(new BPERuntimeContext(userId,clientId),msg);
                if ( m_log.isInfoEnabled() ) {
                  m_log.info("Started clientId='" + clientId + "', process id='"+startProcessId + "',piid='"+piid+"' by event: eventType='" + eventType + "', eventParams=" + eventParams);
                }

              }
            }
          }

          return;
        }
      }
      m_log.warn( "Non-runnable message discarded: " + message );
    }
    catch ( Throwable e ) {
      try {
        rollbackTransaction();
      }
      catch ( Throwable ex ) {
      }
      m_log.error( "Exception", e );
      resend( message );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Throwable e ) {
        resend( message );
      }
    }
  }


  /**
   * (non-Javadoc)
   *
   * @exception RepositoryException  Description of the Exception
   * @see                            javax.ejb.SessionSynchronization#afterBegin()
   */
  protected void beginTransaction()
    throws RepositoryException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug( "beginTransaction:" );
    }
    try {
      m_ctx.getUserTransaction().begin();
    }
    catch ( Exception e ) {
      m_log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    m_serviceManager.beginTransaction();
  }


  /**
   * (non-Javadoc)
   *
   * @see   javax.ejb.SessionSynchronization#beforeCompletion()
   */
  protected void rollbackTransaction()
  {
    try {
      int  status  = m_ctx.getUserTransaction().getStatus();

      if ( m_log.isDebugEnabled() ) {
        m_log.debug( "rollbackTransaction:" + status );
      }

      try {
        if ( status == Status.STATUS_ACTIVE || status == Status.STATUS_MARKED_ROLLBACK ) {
          m_serviceManager.beforeEndTransaction( false );
        }
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }

      try {
        if ( status == Status.STATUS_ACTIVE || status == Status.STATUS_MARKED_ROLLBACK ) {
          m_ctx.getUserTransaction().rollback();
        }
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }

      try {
        if ( status == Status.STATUS_ACTIVE || status == Status.STATUS_MARKED_ROLLBACK ) {
          m_serviceManager.afterEndTransaction(false);
        }
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
    catch ( Exception e ) {
      m_log.error( "Exception", e );
    }
  }


  /**
   * Description of the Method
   */
  protected void commitTransaction()
  {
    try {
      int  status  = m_ctx.getUserTransaction().getStatus();

      if ( m_log.isDebugEnabled() ) {
        m_log.debug( "endTransaction:" + status );
      }

      try {
        if ( status == Status.STATUS_ACTIVE ) {
          m_serviceManager.beforeEndTransaction( true );
        }
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }

      try {
        if ( status == Status.STATUS_ACTIVE ) {
          m_ctx.getUserTransaction().commit();
        }
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }

      try {
        if ( status == Status.STATUS_ACTIVE ) {
          m_serviceManager.afterEndTransaction(true);
        }
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
    catch ( Exception e ) {
      m_log.error( "Exception", e );
    }
  }
}
