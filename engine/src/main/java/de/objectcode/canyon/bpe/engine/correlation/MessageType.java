package de.objectcode.canyon.bpe.engine.correlation;

import de.objectcode.canyon.bpe.engine.variable.ComplexType;

import java.io.Serializable;

/**
 * @author junglas
 * @created 16. Juli 2004
 */
public class MessageType implements Serializable {
  static final long serialVersionUID = 1645447820122616022L;

  private String m_messageOperation;
  private ComplexType m_contentType;


  /**
   * Constructor for the MessageType object
   *
   * @param messageOperation Description of the Parameter
   * @param contentType      Description of the Parameter
   */
  public MessageType(String messageOperation, ComplexType contentType) {
    m_messageOperation = messageOperation;
    m_contentType = contentType;
  }


  /**
   * @return Returns the contentType.
   */
  public ComplexType getContentType() {
    return m_contentType;
  }


  /**
   * @return Returns the messageOperation.
   */
  public String getMessageOperation() {
    return m_messageOperation;
  }
}
