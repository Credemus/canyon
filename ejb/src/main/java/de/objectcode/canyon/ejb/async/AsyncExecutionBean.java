/*
 *  Copyright (c) 2003 Adrian Price.  All rights reserved.
 */
package de.objectcode.canyon.ejb.async;


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

import de.objectcode.canyon.ejb.BaseMessageBean;
import de.objectcode.canyon.jmx.async.AsyncRequest;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;

/**
 * Provides a JMS-based asynchronous execution service.  The caller simply
 * places a <code>AsyncRequest</code> object onto the JMS queue; this MDB
 * dequeues it and invokes the runnable object's run() method. Obviously, the
 * security on this JMS queue must be properly configured to prevent
 * unauthorized code from being injected.
 *
 * @ejb:bean
 *      name="AsyncExecution"
 *      display-name="Canyon Async Request Dispatcher"
 *      transaction-type="Bean"
 *      destination-type="javax.jms.Queue"
 *      acknowledge-mode="Auto-acknowledge"
 *      subscription-durability="Durable"
 * @jboss:destination-jndi-name
 *      name="queue/WSAsyncRequest"
 * @weblogic:message-driven
 *      destination-jndi-name="queue/WSAsyncRequest"
 *      jms-polling-interval-seconds="10"
 *
 * @ejb.resource-ref res-ref-name="ServiceManager" res-type="de.objectcode.canyon.spi.ServiceManager"
 *   res-auth="Application"
 * @jboss.resource-ref res-ref-name="ServiceManager" jndi-name="java:/canyon/ServiceManager"
 * @ejb.resource-ref res-ref-name="jms/QCF" res-type="javax.jms.QueueConnectionFactory"
 *   res-auth="Container"
 * @jboss.resource-ref res-ref-name="jms/QCF" jndi-name="ConnectionFactory"
 *
 * @author    Adrian Price
 * @created   23. Juni 2003
 */
public class AsyncExecutionBean extends BaseMessageBean implements MessageListener
{
	static final long serialVersionUID = -7587776666762184618L;
	
	private final static  String           SERVICE_MANAGER_COMP_NAME  = "java:comp/env/ServiceManager";
  protected transient   ServiceManager   m_serviceManager;
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
  }


  /**
   * @param message  Description of the Parameter
   * @see            javax.jms.MessageListener#onMessage(javax.jms.Message)
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
        if ( payload instanceof AsyncRequest ) {
          AsyncRequest  request  = ( AsyncRequest ) payload;

          if ( resendCount > 3 ) {
            request.fail( m_serviceManager );
          } else {
            request.execute( m_serviceManager );
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
   * Description of the Method
   *
   * @param message  Description of the Parameter
   */
  private void resend( Message message )
  {
    m_log.debug( "resend" );
    
    if ( message instanceof ObjectMessage ) {
      try {
        Queue        dest         = ( Queue ) message.getJMSDestination();
        QueueSender  sender       = m_session.createSender( dest );
        ObjectMessage newMessage = m_session.createObjectMessage(((ObjectMessage)message).getObject());
        
        int          resendCount  = 0;

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
          m_serviceManager.beforeEndTransaction(false);
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
	} else {
	  m_log.warn("Transaction not active or marked rollback in rollback:" + status);
	  try {
	    m_serviceManager.getLockManager().releaseAllLocks();
	  } catch (Throwable t) {
	    m_log.error("Exception", t);
	    throw new RepositoryException(t);
	  }
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
          m_serviceManager.beforeEndTransaction(true);
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
	if (status == Status.STATUS_ACTIVE) {
	  m_serviceManager.afterEndTransaction(true);
	} else {
	  m_log.warn("Transaction not active in commit:" + status);
	  try {
	    m_serviceManager.getLockManager().releaseAllLocks();
	  } catch (Throwable t) {
	    m_log.error("Exception", t);
	    throw new RepositoryException(t);
	  }
	}
      } catch (Exception e) {
	m_log.error("Exception", e);
      }
    }
    catch ( Exception e ) {
      m_log.error( "Exception", e );
    }
  }
}
