package de.objectcode.canyon.persistent.participant;

/**
 * @hibernate.class table="PPROCINSTTOPART"
 *
 * @author    junglas
 * @created   14.07.2003
 * @version   $Id: PProcessInstanceToParticipant.java,v 1.1 2003/11/03 14:45:04 junglas Exp $
 */
public class PProcessInstanceToParticipant
{
  private  PProcessInstanceToParticipantId  m_id;
  private  String                           m_resolvedParticipantId;


  /**
   *Constructor for the PProcessInstanceToParticipant object
   */
  public PProcessInstanceToParticipant() { }


  /**
   *Constructor for the PProcessInstanceToParticipant object
   *
   * @param id      Description of the Parameter
   * @param userId  Description of the Parameter
   */
  public PProcessInstanceToParticipant( PProcessInstanceToParticipantId id, String userId )
  {
    m_id = id;
    m_resolvedParticipantId = userId;
  }


  /**
   * @param id
   */
  public void setId( PProcessInstanceToParticipantId id )
  {
    m_id = id;
  }


  /**
   * @param string
   */
  public void setResolvedParticipantId( String string )
  {
    m_resolvedParticipantId = string;
  }


  /**
   * @hibernate.id generator-class="assigned"
   *
   *
   * @return
   */
  public PProcessInstanceToParticipantId getId()
  {
    return m_id;
  }


  /**
   * @hibernate.property column="RESOLVEDID" type="string" length="30"
   *
   *
   * @return
   */
  public String getResolvedParticipantId()
  {
    return m_resolvedParticipantId;
  }

}
