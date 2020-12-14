package de.objectcode.canyon.persistent.instance;

import de.objectcode.canyon.spi.instance.IJoinedTransition;

/**
 * @hibernate.class table="PJOINEDTRANSITIONS"
 *
 * @author    junglas
 * @created   22. Oktober 2003
 */
public class PJoinedTransition implements IJoinedTransition
{
  private  long     m_entityOid;

  private  String   m_transitionId;
  private  boolean  m_fired;


  /**
   *Constructor for the PJoinedTransition object
   */
  public PJoinedTransition() { }


  /**
   *Constructor for the PJoinedTransition object
   *
   * @param transitionId  Description of the Parameter
   */
  public PJoinedTransition( String transitionId )
  {
    m_transitionId = transitionId;
    m_fired = false;
  }


  /**
   * @param string
   */
  public void setTransitionId( String string )
  {
    m_transitionId = string;
  }


  /**
   * @param b
   */
  public void setFired( boolean b )
  {
    m_fired = b;
  }


  /**
   * @param l
   */
  public void setEntityOid( long l )
  {
    m_entityOid = l;
  }


  /**
   * @hibernate.id generator-class="native" column="ENTITYID" type="long" unsaved-value="0"
   *
   * @return
   */
  public long getEntityOid()
  {
    return m_entityOid;
  }



  /**
   * @hibernate.property column="TRANSITIONID" type="string" length="64" not-null="true"
   *
   * @return
   */
  public String getTransitionId()
  {
    return m_transitionId;
  }


  /**
   * @hibernate.property column="FIRED" type="boolean"
   *
   * @return
   */
  public boolean isFired()
  {
    return m_fired;
  }


  /**
   * @see   de.objectcode.canyon.spi.instance.IJoinedTransition#fire()
   */
  public void fire()
  {
    m_fired = true;
  }


  /**
   * @see   de.objectcode.canyon.spi.instance.IJoinedTransition#reset()
   */
  public void reset()
  {
    m_fired = false;
  }

}
