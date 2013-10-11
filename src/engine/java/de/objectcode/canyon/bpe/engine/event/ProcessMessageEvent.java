package de.objectcode.canyon.bpe.engine.event;

import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.correlation.Message;

import java.util.EventObject;

/**
 * @author    junglas
 * @created   21. Juni 2004
 */
public class ProcessMessageEvent extends EventObject
{
	static final long serialVersionUID = 8010852450729495813L;
	
	private  Message  m_message;


  /**
   *Constructor for the ProcessMessageEvent object
   *
   * @param process  Description of the Parameter
   * @param message  Description of the Parameter
   */
  public ProcessMessageEvent( BPEProcess process, Message message )
  {
    super( process );

    m_message = message;
  }


  /**
   * Gets the process attribute of the ProcessMessageEvent object
   *
   * @return   The process value
   */
  public BPEProcess getProcess()
  {
    return ( BPEProcess ) source;
  }


  /**
   * @return   Returns the message.
   */
  public Message getMessage()
  {
    return m_message;
  }
}
