package de.objectcode.canyon.wetdock.user.persistent;

import de.objectcode.canyon.wetdock.api.user.ClientData;

/**
 * @hibernate.class table="PCLIENTS"
 * 
 * @author junglas
 */
public class PClient
{
  private String m_clientId;
  
  public PClient()
  {
  }
  
  public PClient ( ClientData clientData )
  {
    m_clientId = clientData.getClientId();
  }
  
  /**
   * @hibernate.id generator-class="assigned" column="CLIENTID" type="string" length="64"
   * 
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
  
  public ClientData getClientData()
  {
    return new ClientData(m_clientId);
  }
}
