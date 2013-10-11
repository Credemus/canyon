package de.objectcode.canyon.user.participant;

import java.util.HashSet;
import java.util.Set;

/**
 * @hibernate.class table="PROLE"
 *
 * @author    junglas
 * @created   9. Dezember 2003
 */
public class PRole
{
  private  String  m_roleId;
  private  String  m_name;
  private  Set     m_users;


  /**
   *Constructor for the PRole object
   */
  public PRole() { }


  /**
   *Constructor for the PRole object
   *
   * @param roleId  Description of the Parameter
   * @param name    Description of the Parameter
   */
  public PRole( String roleId, String name )
  {
    m_roleId = roleId;
    m_name = name;
    m_users = new HashSet();
  }


  /**
   * @param string
   */
  public void setName( String string )
  {
    m_name = string;
  }


  /**
   * @param string
   */
  public void setRoleId( String string )
  {
    m_roleId = string;
  }


  /**
   * @param set
   */
  public void setUsers( Set set )
  {
    m_users = set;
  }


  /**
   * @hibernate.property column="NAME" type="string" length="64"
   *
   * @return
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * @hibernate.id generator-class="assigned"
   *
   * @return
   */
  public String getRoleId()
  {
    return m_roleId;
  }


  /**
   * @hibernate.set lazy="true" table="PROLE_PUSER"
   *     cascade="none"
   * @hibernate.collection-key column="ROLEID"
   * @hibernate.collection-many-to-many class="de.objectcode.canyon.user.participant.PUser" column="USERID"
   *
   *
   * @return
   */
  public Set getUsers()
  {
    return m_users;
  }

}
