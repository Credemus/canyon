package de.objectcode.canyon.persistent.audit;

/**
 * Conforms to WfMC AuditObject.
 *
 * @hibernate.class table="PAUDITOBJECT"
 * @hibernate.discriminator column="AUDITTYPE"
 *
 * @author    junglas
 * @created   20. November 2003
 */
public class PAuditObject
{
  protected  long     m_entityOid;
  protected  PPrefix  m_prefix;


  /**
   * @param prefix
   */
  public void setPrefix( PPrefix prefix )
  {
    m_prefix = prefix;
  }


  /**
   * @param l
   */
  public void setEntityOid( long l )
  {
    m_entityOid = l;
  }


  /**
   * @hibernate.component
   *
   * @return
   */
  public PPrefix getPrefix()
  {
    return m_prefix;
  }


  /**
   * @hibernate.id generator-class="native" column="ENTITYID" type="long" unsaved-value="0"
   *
   *
   * @return
   */
  public long getEntityOid()
  {
    return m_entityOid;
  }

}
