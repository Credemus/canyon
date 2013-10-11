package de.objectcode.canyon.wetdock.api.user;

import java.io.Serializable;

/**
 * @author junglas
 */
public class ClientData implements Serializable
{
  private String m_clientId;
  
  public ClientData ( )
  {
  }
  
  public ClientData( String clientId )
  {
    m_clientId = clientId;
  }
  
  /**
   * @return Returns the clientId.
   */
  public String getClientId()
  {
    return m_clientId;
  }
  /**
   * @param clientId The clientId to set.
   */
  public void setClientId( String clientId )
  {
    m_clientId = clientId;
  }
}
