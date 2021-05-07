package de.objectcode.canyon.spiImpl.tool.event;

import java.util.HashMap;
import java.util.Properties;

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

import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.tool.BPEContext;
import de.objectcode.canyon.spi.tool.IToolConnector;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.spi.tool.ReturnValue;

/**
 * @author    junglas
 * @created   6. April 2004
 */
public class ExternalEventConnector extends BaseEventConnector implements IToolConnector
{
  private final static  Log                     log                       = LogFactory.getLog( ExternalEventConnector.class );



  /**
   *Constructor for the EventConnector object
   *
   * @param eventType                Description of the Parameter
   * @param startProcessId           Description of the Parameter
   * @param engineId                 Description of the Parameter
   * @param userId                   Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   */
  public ExternalEventConnector( String engineId  )
    throws RepositoryException
  {
    super(engineId );
  }

  protected void initQueue(MessageDescriptor messageDescriptor) throws RepositoryException {
    try {
      Properties environment = new Properties();
      environment.put("java.naming.provider.url",messageDescriptor.fJndiProviderUrl);
      environment.put("java.naming.factory.initial", System.getProperty(
          "java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory"));
      environment.put("java.naming.factory.url.pkgs", System.getProperty(
          "java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces"));

      InitialContext  ctx  = new InitialContext(environment);

      m_eventQueue = ( Queue ) ctx.lookup( messageDescriptor.fJndiQueueName );
      m_queueConnectionFactory = ( QueueConnectionFactory ) ctx.lookup( messageDescriptor.fJndiConnectionFactoryName );
    }
    catch ( Exception e ) {
      throw new RepositoryException( e );
    }    
  }

  
  protected MessageDescriptor extractMessageDescriptor(BPEContext context, Parameter[] parameters) {
    MessageDescriptor md = new MessageDescriptor();
    int              i;
    md.fEventParams  = new HashMap<String, Object>();

    for ( i = 0; i < parameters.length; i++ ) {
      if (parameters[i].formalName.equals("_canyon_externalEngineIdentifier")) {
        md.setEngineLocator(parameters[i].getValue().toString());
      } else if ( parameters[i].mode != ParameterMode.OUT ) {
        md.fEventParams.put( parameters[i].formalName, parameters[i].value );
      }
    }
    return md;
  }

  


}
