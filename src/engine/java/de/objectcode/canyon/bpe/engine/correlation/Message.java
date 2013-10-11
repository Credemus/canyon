package de.objectcode.canyon.bpe.engine.correlation;

import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.engine.variable.ComplexValue;

/**
 * @author    junglas
 * @created   16. Juni 2004
 */
public class Message
{
  private  String        m_operation;
  private  ComplexValue  m_content;

  public Message ( String operation, ComplexType type )
  {
    m_operation = operation;
    m_content = new ComplexValue(type);
  }

  /**
   *Constructor for the Message object
   *
   * @param operation  Description of the Parameter
   * @param content    Description of the Parameter
   */
  public Message( String operation, ComplexValue content )
  {
    m_operation = operation;
    m_content = content;
  }


  /**
   * @return   Returns the content.
   */
  public ComplexValue getContent()
  {
    return m_content;
  }


  /**
   * @return   Returns the operation.
   */
  public String getOperation()
  {
    return m_operation;
  }
  
  public String toString()
  {
    return "Message(" + m_operation + "," + m_content + ")";
  }
}
