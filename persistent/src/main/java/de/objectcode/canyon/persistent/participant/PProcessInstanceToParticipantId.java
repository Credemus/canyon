package de.objectcode.canyon.persistent.participant;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   14.07.2003
 * @version   $Id: PProcessInstanceToParticipantId.java,v 1.1 2003/11/03 14:45:04 junglas Exp $
 */
public class PProcessInstanceToParticipantId implements Serializable
{
	static final long serialVersionUID = -5046660118981972752L;
	
	private  String  m_processInstanceId;
  private  String  m_participantId;


  /**
   *Constructor for the HibProcInstToUserId object
   */
  public PProcessInstanceToParticipantId() { }


  /**
   *Constructor for the HibProcInstToUserId object
   *
   * @param processInstanceId  Description of the Parameter
   * @param participantId      Description of the Parameter
   */
  public PProcessInstanceToParticipantId( String processInstanceId, String participantId )
  {
    m_processInstanceId = processInstanceId;
    m_participantId = participantId;
  }


  /**
   * @hibernate.property column="PROCESSINSTANCEID" type="string" length="256"
   *
   *
   * @param string
   */
  public void setProcessInstanceId( String string )
  {
    m_processInstanceId = string;
  }


  /**
   * @param string
   */
  public void setParticipantId( String string )
  {
    m_participantId = string;
  }


  /**
   * @return
   */
  public String getProcessInstanceId()
  {
    return m_processInstanceId;
  }


  /**
   * @hibernate.property column="PARTICIPANTID" type="string" length="64"
   *
   *
   * @return
   */
  public String getParticipantId()
  {
    return m_participantId;
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public int hashCode()
  {
    int  code  = m_processInstanceId.hashCode();

    code ^= m_participantId.hashCode();

    return code;
  }


  /**
   * Description of the Method
   *
   * @param obj  Description of the Parameter
   * @return     Description of the Return Value
   */
  public boolean equals( Object obj )
  {
    if ( obj == null || !( obj instanceof PProcessInstanceToParticipantId ) ) {
      return false;
    }

    PProcessInstanceToParticipantId  castObj  = ( PProcessInstanceToParticipantId ) obj;

    return m_processInstanceId.equals( castObj.m_processInstanceId ) && m_participantId.equals( castObj.m_participantId );
  }
}
