package de.objectcode.canyon.persistent.instance;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.objectcode.canyon.spi.instance.IActivityInstance;
import de.objectcode.canyon.spi.instance.IProcessInstance;
import de.objectcode.canyon.spi.process.IProcessDefinitionID;

/**
 * @hibernate.joined-subclass table="PPROCESSINSTANCES"
 * @hibernate.joined-subclass-key column="PROCESSINSTANCEID"
 *
 * @author    junglas
 * @created   20. Oktober 2003
 */
public class PProcessInstance extends PAttributedEntity implements IProcessInstance
{
  private  int                m_state;
  private  String             m_name;
  private  int                m_priority;
  private  Date               m_createdDate;
  private  Date               m_startedDate;
  private  Date               m_completedDate;
  private  Date               m_dueDate;
  private  Set                m_activitySet;
  private  IActivityInstance  m_parentActivityInstance;
  private  String[]           m_participants;


  /**
   *Constructor for the PProcessInstance object
   */
  public PProcessInstance()
  {
    super( IProcessInstance.PROPERTYDESCRIPTORS );
  }


  /**
   *Constructor for the PProcessInstance object
   *
   * @param processDefinitionId       Description of the Parameter
   * @param processDefinitionVersion  Description of the Parameter
   * @param instanceName              Description of the Parameter
   * @param priority                  Description of the Parameter
   * @param state                     Description of the Parameter
   * @param createdDate               Description of the Parameter
   * @param startedDate               Description of the Parameter
   * @param participants              Description of the Parameter
   */
  public PProcessInstance( IProcessDefinitionID processDefinitionId, String instanceName,
      int priority, int state, Date createdDate, Date startedDate,
      String[] participants )
  {
    super( IProcessInstance.PROPERTYDESCRIPTORS, processDefinitionId );

    m_name = instanceName;
    m_priority = priority;
    m_state = state;
    m_createdDate = createdDate;
    m_startedDate = startedDate;
    m_participants = participants;
    m_activitySet = new HashSet();
  }


  /**
   * @param date
   */
  public void setCompletedDate( Date date )
  {
    m_completedDate = date;
  }


  /**
   * @param date
   */
  public void setDueDate( Date date )
  {
    m_dueDate = date;
  }


  /**
   * @param string
   */
  public void setName( String string )
  {
    m_name = string;
  }


  /**
   * @param date
   */
  public void setStartedDate( Date date )
  {
    m_startedDate = date;
  }


  /**
   * @param date
   */
  public void setCreatedDate( Date date )
  {
    m_createdDate = date;
  }


  /**
   * @param i
   */
  public void setState( int i )
  {
    m_state = i;
  }


  /**
   * @param set
   */
  public void setActivitySet( Set set )
  {
    m_activitySet = set;
  }


  /**
   * @param i
   */
  public void setPriority( int i )
  {
    m_priority = i;
  }


  /**
   * @param instance
   */
  public void setParentActivityInstance( IActivityInstance instance )
  {
    m_parentActivityInstance = instance;
  }


  /**
   * @param strings
   */
  public void setParticipants( String[] strings )
  {
    m_participants = strings;
  }


  /**
   * @return   The processInstanceId value
   * @see      org.obe.spi.model.AttributedEntity#getProcessInstanceId()
   */
  public String getProcessInstanceId()
  {
    return getEntityId();
  }


  /**
   * @hibernate.property column="COMPLETEDDATE" type="timestamp" not-null="false"
   *
   * @return
   */
  public Date getCompletedDate()
  {
    return m_completedDate;
  }


  /**
   * @hibernate.property column="DUEDATE" type="timestamp" not-null="false"
   *
   * @return
   */
  public Date getDueDate()
  {
    return m_dueDate;
  }


  /**
   * @hibernate.property column="NAME" type="string" length="64" not-null="false"
   *
   * @return
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * @hibernate.property column="STARTEDDATE" type="timestamp" not-null="false"
   *
   * @return
   */
  public Date getStartedDate()
  {
    return m_startedDate;
  }


  /**
   * @hibernate.property column="CREATEDDATE" type="timestamp" not-null="true"
   *
   * @return
   */
  public Date getCreatedDate()
  {
    return m_createdDate;
  }


  /**
   * @hibernate.property column="STATE" type="integer" length="2" not-null="true"
   *
   * @return
   */
  public int getState()
  {
    return m_state;
  }


  /**
   * @hibernate.set cascade="all" lazy="true" inverse="true"
   * @hibernate.collection-key column="PROCESSINSTANCEID"
   * @hibernate.collection-one-to-many class="de.objectcode.canyon.persistent.instance.PActivityInstance"
   *
   * @return
   */
  public Set getActivitySet()
  {
    return m_activitySet;
  }


  /**
   * Gets the activities attribute of the HibProcessInstance object
   *
   * @return   The activities value
   */
  public Map getActivityInstances()
  {
    Map       ret  = new HashMap();
    Iterator  it   = m_activitySet.iterator();

    while ( it.hasNext() ) {
      PActivityInstance  activityInstance  = ( PActivityInstance ) it.next();

      ret.put( activityInstance.getActivityInstanceId(), activityInstance );
    }

    return ret;
  }


  /**
   * @hibernate.property column="PRIORITY" type="integer" length="2" not-null="true"
   *
   * @return
   */
  public int getPriority()
  {
    return m_priority;
  }


  /**
   * @hibernate.many-to-one column="PARENTACTIVITYINSTANCEID" class="de.objectcode.canyon.persistent.instance.PActivityInstance"
   *
   * @return
   */
  public IActivityInstance getParentActivityInstance()
  {
    return m_parentActivityInstance;
  }


  /**
   * @hibernate.array table="PPROCESSINSTANCE_PARTS" cascade="all"
   * @hibernate.collection-key column="PROCESSINSTANCEID"
   * @hibernate.collection-index column="IDX"
   * @hibernate.collection-element column="PARTICIPANT" type="string" length="64"
   *
   * @return
   */
  public String[] getParticipants()
  {
    return m_participants;
  }
  
  public String toString()
	{
  	StringBuffer buffer = new StringBuffer("PProcessInstance[");
  	
  	buffer.append("super=").append(super.toString());
  	buffer.append(", name=").append(m_name);
  	buffer.append(", priority=").append(m_priority);
  	buffer.append(", state=").append(m_state);
  	buffer.append("]");
  	
  	return buffer.toString();
  }
}
