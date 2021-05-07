package de.objectcode.canyon.spiImpl.tool.event;

import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.tool.IToolConnector;
import de.objectcode.canyon.spi.tool.BPEContext;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.spi.tool.ReturnValue;
import de.objectcode.canyon.spiImpl.tool.event.BaseEventConnector.MessageDescriptor;

import java.util.HashMap;

import javax.jms.DeliveryMode;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author    junglas
 * @created   6. April 2004
 */
public class InternalEventConnector extends BaseEventConnector implements IToolConnector
{
  private final static  Log                     log                       = LogFactory.getLog( InternalEventConnector.class );
  private final static  String           SERVICE_MANAGER_COMP_NAME  = "java:comp/env/ServiceManager";


  /**
   *Constructor for the EventConnector object
   *
   * @param eventType                Description of the Parameter
   * @param startProcessId           Description of the Parameter
   * @param engineId                 Description of the Parameter
   * @param userId                   Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   */
  public InternalEventConnector( String engineId )
    throws RepositoryException
  {
    super(engineId );
  }

  protected void initQueue(MessageDescriptor messageDescriptor) throws RepositoryException {
    try {
      InitialContext  ctx  = new InitialContext();

      m_eventQueue = ( Queue ) ctx.lookup( messageDescriptor.fJndiQueueName );
      m_queueConnectionFactory = ( QueueConnectionFactory ) ctx.lookup( messageDescriptor.fJndiConnectionFactoryName );
      m_serviceManager  = ( ServiceManager ) ctx.lookup( SERVICE_MANAGER_COMP_NAME );
      m_worklistEngine = m_serviceManager.getWorklistEngine();
      
    }
    catch ( Exception e ) {
      throw new RepositoryException( e );
    }    
  }

  
  protected MessageDescriptor extractMessageDescriptor(BPEContext context, Parameter[] parameters) {
    MessageDescriptor md = new MessageDescriptor();
    int              i;
    md.fEventParams  = new HashMap<String, Object>();
    md.fClientId = context.getClientId();
    
    for ( i = 0; i < parameters.length; i++ ) {
      if ( parameters[i].mode != ParameterMode.OUT ) {
        md.fEventParams.put( parameters[i].formalName, parameters[i].value );
      }
    }
    return md;
  }

  
}
