package de.objectcode.canyon.persistent.async;

import java.util.Date;

/**
 * @hibernate.class table="PTIMEDNOTIFICATIONS"
 *
 * @author    junglas
 * @created   3. Dezember 2003
 */
public class PTimedNotification
{
  private  long    m_id;
  private  Date    m_when;
  private  Date    m_notified;
  private  String  m_processInstanceId;
  private  String  m_activityInstanceId;
  private  String  m_transitionId;


  /**
   *Constructor for the PTimedNotification object
   */
  public PTimedNotification() { }


  /**
   *Constructor for the PTimedNotification object
   *
   * @param when                Description of the Parameter
   * @param processInstanceId   Description of the Parameter
   * @param activityInstanceId  Description of the Parameter
   * @param transitionId        Description of the Parameter
   */
  public PTimedNotification( Date when, String processInstanceId, String activityInstanceId, String transitionId )
  {
    m_when = when;
    m_processInstanceId = processInstanceId;
    m_activityInstanceId = activityInstanceId;
    m_transitionId = transitionId;
  }


  /**
   * @param l
   */
  public void setId( long l )
  {
    m_id = l;
  }


  /**
   * @param string
   */
  public void setActivityInstanceId( String string )
  {
    m_activityInstanceId = string;
  }


  /**
   * @param string
   */
  public void setProcessInstanceId( String string )
  {
    m_processInstanceId = string;
  }


  /**
   * @param string
   */
  public void setTransitionId( String string )
  {
    m_transitionId = string;
  }


  /**
   * @param date
   */
  public void setWhen( Date date )
  {
    m_when = date;
  }


  /**
   * @param date
   */
  public void setNotified( Date date )
  {
    m_notified = date;
  }


  /**
   * @hibernate.id generator-class="native" column="ENTITYID" type="long" unsaved-value="0"
   *
   * @return
   */
  public long getId()
  {
    return m_id;
  }


  /**
   * @hibernate.property column="ACTIVITYINSTANCEID" type="string" length="64" not-null="true"
   *
   * @return
   */
  public String getActivityInstanceId()
  {
    return m_activityInstanceId;
  }


  /**
   * @hibernate.property column="PROCESSINSTANCEID" type="string" length="64" not-null="true"
   *
   * @return
   */
  public String getProcessInstanceId()
  {
    return m_processInstanceId;
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
   * @hibernate.property column="NOTIFYWHEN" type="timestamp" not-null="true"
   *
   * @return
   */
  public Date getWhen()
  {
    return m_when;
  }


  /**
   * @hibernate.property column="NOTIFIED" type="timestamp" not-null="false"
   *
   *
   * @return
   */
  public Date getNotified()
  {
    return m_notified;
  }

}
