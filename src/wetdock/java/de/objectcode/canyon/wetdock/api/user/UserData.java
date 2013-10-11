package de.objectcode.canyon.wetdock.api.user;

import java.io.Serializable;

/**
 * @author junglas
 */
public class UserData implements Serializable
{
  private String m_userId;
  private String m_password;
  private String[] m_roleIds;

  public UserData()
  {
  }
  
  public UserData ( String userId, String password )
  {
    m_userId = userId;
    m_password = password;
  }

  /**
   * @return Returns the password.
   */
  public String getPassword()
  {
    return m_password;
  }
  /**
   * @param password The password to set.
   */
  public void setPassword( String password )
  {
    m_password = password;
  }
  /**
   * @return Returns the userId.
   */
  public String getUserId()
  {
    return m_userId;
  }
  /**
   * @param userId The userId to set.
   */
  public void setUserId( String userId )
  {
    m_userId = userId;
  }
  
  public String[] getRoleIds()
  {
    return m_roleIds;
  }
}
