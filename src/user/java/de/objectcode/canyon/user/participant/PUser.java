package de.objectcode.canyon.user.participant;

import java.util.HashSet;
import java.util.Set;

/**
 * @hibernate.class table="PUSER"
 *
 * @author    junglas
 * @created   9. Dezember 2003
 */
public class PUser
{
  private  String  m_userId;
  private  String  m_hashedPassword;
  private  String  m_name;
  private  String  m_email;
  private  Set     m_roles;


  /**
   *Constructor for the PUser object
   */
  public PUser() { }


  /**
   *Constructor for the PUser object
   *
   * @param userId          Description of the Parameter
   * @param hashedPassword  Description of the Parameter
   * @param name            Description of the Parameter
   * @param email           Description of the Parameter
   */
  public PUser( String userId, String hashedPassword, String name, String email )
  {
    m_userId = userId;
    m_hashedPassword = hashedPassword;
    m_name = name;
    m_email = email;
    m_roles = new HashSet();
  }


  /**
   * @param string
   */
  public void setEmail( String string )
  {
    m_email = string;
  }


  /**
   * @param string
   */
  public void setHashedPassword( String string )
  {
    m_hashedPassword = string;
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
  public void setUserId( String string )
  {
    m_userId = string;
  }


  /**
   * @param set
   */
  public void setRoles( Set set )
  {
    m_roles = set;
  }


  /**
   * @hibernate.property column="EMAIL" type="string" length="64"
   *
   * @return
   */
  public String getEmail()
  {
    return m_email;
  }


  /**
   * @hibernate.property column="HASHEDPASSWORD" type="string" length="64"
   *
   * @return
   */
  public String getHashedPassword()
  {
    return m_hashedPassword;
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
  public String getUserId()
  {
    return m_userId;
  }


  /**
   * @hibernate.set lazy="true" table="PROLE_PUSER"
   *     cascade="none"
   * @hibernate.collection-key column="USERID"
   * @hibernate.collection-many-to-many class="de.objectcode.canyon.user.participant.PRole" column="ROLEID"
   *
   * @return
   */
  public Set getRoles()
  {
    return m_roles;
  }

}
