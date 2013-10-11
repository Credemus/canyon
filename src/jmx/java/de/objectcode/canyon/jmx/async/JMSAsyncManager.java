package de.objectcode.canyon.jmx.async;

import javax.jms.DeliveryMode;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.async.IAsyncManager;

/**
 * @author    junglas
 * @created   29. Oktober 2003
 */
public class JMSAsyncManager implements IAsyncManager
{
  private final static  Log                     log                       = LogFactory.getLog( JMSAsyncManager.class );
  private               Queue                   m_asyncQueue;
  private               QueueConnectionFactory  m_queueConnectionFactory;


  /**
   *Constructor for the JMSAsyncManager object
   *
   * @param jndiName             Description of the Parameter
   * @exception NamingException  Description of the Exception
   */
  public JMSAsyncManager( String jndiName )
    throws NamingException
  {
    InitialContext  ctx  = new InitialContext();

    m_asyncQueue = ( Queue ) ctx.lookup( jndiName );
    m_queueConnectionFactory = ( QueueConnectionFactory ) ctx.lookup( "ConnectionFactory" );
  }

	public void beginTransaction() throws RepositoryException
	{
	}
	
	public void endTransaction(boolean flush) throws RepositoryException
	{
	}
  

  /**
   * Description of the Method
   *
   * @param request  Description of the Parameter
   */
  private void asyncRequest( AsyncRequest request )
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "Enqueuing request " + request );
    }

    QueueConnection  connection  = null;
    QueueSession     session     = null;

    try {
      connection = m_queueConnectionFactory.createQueueConnection();
      session = connection.createQueueSession( false, QueueSession.AUTO_ACKNOWLEDGE );
      connection.start();

      Message      msg     = session.createObjectMessage( request );

      QueueSender  sender  = session.createSender( m_asyncQueue );

      sender.send( msg, DeliveryMode.PERSISTENT, 4, 0L );
      
      sender.close();
    }
    catch ( Exception e ) {
      log.error( "Error enqueueing request", e );
    }
    finally {
      if ( session != null ) {
        try {
          connection.stop();
          session.close();
        }
        catch ( Exception e ) {
        }
      }
      if ( connection != null ) {
        try {
          connection.close();
        }
        catch ( Exception e ) {
        }
      }
    }
  }
  
  public void asyncCompleteBPEActivity ( String processInstanceId, String activityId )
  {
  	if ( log.isDebugEnabled() ){
  		log.debug("asyncCompleteBPEActivity: " + processInstanceId + " " + activityId );
  	}
  	asyncRequest( new AsyncCompleteBPEActivity(processInstanceId, activityId));
  }
}