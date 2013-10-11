package de.objectcode.canyon.persistent.audit;

import java.sql.Timestamp;

/**
 * Audit prefix corresponding to WfMC CWADPrefix.
 *
 *
 * @author    junglas
 * @created   20. November 2003
 */
public class PPrefix
{
  private  String     m_initialProcessInstanceId;
  private  String     m_currentProcessInstanceId;
  private  String     m_activityInstanceId;
  private  String     m_processState;
  private  int        m_eventCode;
  private  String     m_domainId;
  private  String     m_nodeId;
  private  String     m_userId;
  private  String     roleId;
  private  Timestamp  m_timestamp;
  private  String     m_informationId;


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
  public void setCurrentProcessInstanceId( String string )
  {
    m_currentProcessInstanceId = string;
  }


  /**
   * @param string
   */
  public void setDomainId( String string )
  {
    m_domainId = string;
  }


  /**
   * @param i
   */
  public void setEventCode( int i )
  {
    m_eventCode = i;
  }


  /**
   * @param string
   */
  public void setInformationId( String string )
  {
    m_informationId = string;
  }


  /**
   * @param string
   */
  public void setInitialProcessInstanceId( String string )
  {
    m_initialProcessInstanceId = string;
  }


  /**
   * @param string
   */
  public void setNodeId( String string )
  {
    m_nodeId = string;
  }


  /**
   * @param string
   */
  public void setProcessState( String string )
  {
    m_processState = string;
  }


  /**
   * @param timestamp
   */
  public void setTimestamp( Timestamp timestamp )
  {
    m_timestamp = timestamp;
  }


  /**
   * @param string
   */
  public void setUserId( String string )
  {
    m_userId = string;
  }


  /**
   * @param string
   */
  public void setRoleId( String string )
  {
    roleId = string;
  }


  /**
   * @hibernate.property column="ACTIVITYINSTANCEID" type="string" length="64"
   *
   *
   * @return
   */
  public String getActivityInstanceId()
  {
    return m_activityInstanceId;
  }


  /**
   * @hibernate.property column="CURRENTPROCESSID" type="string" length="64"
   *
   *
   * @return
   */
  public String getCurrentProcessInstanceId()
  {
    return m_currentProcessInstanceId;
  }


  /**
   * @hibernate.property column="DOMAINID" type="string" length="64"
   *
   *
   * @return
   */
  public String getDomainId()
  {
    return m_domainId;
  }


  /**
   * @hibernate.property column="EVENTCODE" type="integer" not-null="true"
   *
   *
   * @return
   */
  public int getEventCode()
  {
    return m_eventCode;
  }


  /**
   * @hibernate.property column="INFORMATIONID" type="string" length="64"
   *
   *
   * @return
   */
  public String getInformationId()
  {
    return m_informationId;
  }


  /**
   * @hibernate.property column="INITIALPROCESSID" type="string" length="64"
   *
   *
   * @return
   */
  public String getInitialProcessInstanceId()
  {
    return m_initialProcessInstanceId;
  }


  /**
   * @hibernate.property column="NODEID" type="string" length="64"
   *
   *
   * @return
   */
  public String getNodeId()
  {
    return m_nodeId;
  }


  /**
   * @hibernate.property column="PROCESSSTATE" type="string" length="64"
   *
   *
   * @return
   */
  public String getProcessState()
  {
    return m_processState;
  }


  /**
   * @hibernate.property column="TSTAMP" type="timestamp" not-null="true"
   *
   *
   * @return
   */
  public Timestamp getTimestamp()
  {
    return m_timestamp;
  }


  /**
   * @hibernate.property column="USERID" type="string" length="64"
   *
   *
   * @return
   */
  public String getUserId()
  {
    return m_userId;
  }


  /**
   * @hibernate.property column="ROLEID" type="string" length="64"
   *
   *
   * @return
   */
  public String getRoleId()
  {
    return roleId;
  }

}
