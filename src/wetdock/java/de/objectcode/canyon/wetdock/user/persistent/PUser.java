package de.objectcode.canyon.wetdock.user.persistent;

import de.objectcode.canyon.wetdock.api.user.UserData;


/**
 * @hibernate.class table="PUSERS"
 * 
 * @author junglas
 */
public class PUser
{
  private String m_userId;
  private String m_password;

  public PUser()
  {
  }
  
  public PUser ( UserData userData )
  {
    m_userId = userData.getUserId();
    m_password = userData.getPassword();
  }
  
  /**
   * @hibernate.id generator-class="assigned" column="USERID" type="string" length="64"
   * 
   * @return Returns the userId.
   */
  public String getUserId()
  {
    return m_userId;
  }

  /**
   * @param userId
   *          The userId to set.
   */
  public void setUserId( String userId )
  {
    m_userId = userId;
  }
  
  
  /**
   * @hibernate.property type="string" length="64" column="PASSWD" not-null="true"
   * 
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
  
  public UserData getUserData ( )
  {
    return new UserData ( m_userId, m_password );
  }
}
