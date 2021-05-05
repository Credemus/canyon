package de.objectcode.canyon.bpe.engine.correlation;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;

/**
 * @author junglas
 */
public interface IMessageReceiver
{
  /**
   * Get the type of messages the receiver is interested in.
   * 
   * @return
   */
  String getMessageOperation();
  
  ComplexType getMessageContentType();

  boolean isActive();
  
  boolean isCreateInstance();
  
  CorrelationSet[] getCorrelationSets();
  
  /**
   * Called by the message broker upon matching message.
   * 
   * @param message
   * @return <tt>true</tt> if the message receiver consumes the message, <tt>false</tt> if the message should stay pertinent
   */
  boolean onMessage(Message message) throws EngineException;
}
