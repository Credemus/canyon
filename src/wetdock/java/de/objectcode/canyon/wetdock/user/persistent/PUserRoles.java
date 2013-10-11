package de.objectcode.canyon.wetdock.user.persistent;

/**
 * @hibernate.class table="PUSER_ROLES"
 * 
 * @author junglas
 */
public class PUserRoles
{
  private long    m_entityOid;
  private PUser   m_user;
  private PRole   m_role;
  private PClient m_client;

  public PUserRoles ()
  {
  }
  
  public PUserRoles ( PUser user, PRole role, PClient client )
  {
    m_user = user;
    m_role = role;
    m_client = client;
  }
  
  /**
   * @hibernate.many-to-one not-null="true"
   *                        class="de.objectcode.canyon.wetdock.user.persistent.PClient"
   *                        column="CLIENTID" cascade="none"
   * 
   * @return Returns the client.
   */
  public PClient getClient()
  {
    return m_client;
  }

  /**
   * @param client
   *          The client to set.
   */
  public void setClient( PClient client )
  {
    m_client = client;
  }

  /**
   * @hibernate.id generator-class="native" column="ENTITYID" type="long"
   *               unsaved-value="0"
   * 
   * @return Returns the entityOid.
   */
  public long getEntityOid()
  {
    return m_entityOid;
  }

  /**
   * @param entityOid
   *          The entityOid to set.
   */
  public void setEntityOid( long entityOid )
  {
    m_entityOid = entityOid;
  }

  /**
   * @hibernate.many-to-one not-null="true"
   *                        class="de.objectcode.canyon.wetdock.user.persistent.PRole"
   *                        column="ROLEID" cascade="none"
   * 
   * @return Returns the role.
   */
  public PRole getRole()
  {
    return m_role;
  }

  /**
   * @param role
   *          The role to set.
   */
  public void setRole( PRole role )
  {
    m_role = role;
  }

  /**
   * @hibernate.many-to-one not-null="true"
   *                        class="de.objectcode.canyon.wetdock.user.persistent.PUser"
   *                        column="USERID" cascade="none"
   * 
   * @return Returns the user.
   */
  public PUser getUser()
  {
    return m_user;
  }

  /**
   * @param user
   *          The user to set.
   */
  public void setUser( PUser user )
  {
    m_user = user;
  }
}
