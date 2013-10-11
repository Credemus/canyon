package de.objectcode.canyon.bpe.engine;

/**
 * @author    junglas
 * @created   5. August 2004
 */
public class BPERuntimeContext
{
  private  String  m_userId;
  private  String  m_clientId;


  /**
   *Constructor for the BPERuntimeContext object
   *
   * @param userId    Description of the Parameter
   * @param clientId  Description of the Parameter
   */
  public BPERuntimeContext( String userId, String clientId )
  {
    m_userId = userId;
    m_clientId = clientId;
  }


  /**
   * @return   Returns the clientId.
   */
  public String getClientId()
  {
    return m_clientId;
  }


  /**
   * @return   Returns the userId.
   */
  public String getUserId()
  {
    return m_userId;
  }
  
  public String toString()
  {
    return "BPERuntimeContext ( userId=" + m_userId + " clientId=" + m_clientId + ")";
  }
}
