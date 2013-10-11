package de.objectcode.canyon.wetdock.user.persistent;

import de.objectcode.canyon.wetdock.api.user.RoleData;

/**
 * @hibernate.class table="PROLES"
 * 
 * @author junglas
 */
public class PRole
{
  private String m_roleId;
  
  public PRole()
  {
  }
  
  public PRole ( RoleData roleData )
  {
    m_roleId = roleData.getRoleId();
  }

  /**
   * @hibernate.id generator-class="assigned" column="ROLEID" type="string" length="64"
   * 
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
  
  public RoleData getRoleData()
  {
    return new RoleData(m_roleId);
  }
}
