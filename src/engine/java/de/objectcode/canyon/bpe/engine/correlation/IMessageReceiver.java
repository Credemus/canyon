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
  public String getMessageOperation ();
  
  public ComplexType getMessageContentType ();

  public boolean isActive ( );
  
  public boolean isCreateInstance ( );
  
  public CorrelationSet[] getCorrelationSets();
  
  /**
   * Called by the message broker upon matching message.
   * 
   * @param message
   * @return <tt>true</tt> if the message receiver consumes the message, <tt>false</tt> if the message should stay pertinent
   */
  public boolean onMessage ( Message message ) throws EngineException;
}
