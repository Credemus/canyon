package de.objectcode.canyon.wetdock.api.user;

import java.io.Serializable;

/**
 * @author junglas
 */
public class RoleData implements Serializable
{
  private String m_roleId;
  
  public RoleData()
  {
  }
  
  public RoleData ( String roleId ) 
  {
    m_roleId = roleId;
  }
  
  /**
   * @return Returns the roleId.
   */
  public String getRoleId()
  {
    return m_roleId;
  }
  /**
   * @param roleId The roleId to set.
   */
  public void setRoleId( String roleId )
  {
    m_roleId = roleId;
  }
}
