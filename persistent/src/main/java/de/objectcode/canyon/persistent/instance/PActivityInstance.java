package de.objectcode.canyon.persistent.instance;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.objectcode.canyon.spi.instance.IActivityInstance;
import de.objectcode.canyon.spi.instance.IJoinedTransition;
import de.objectcode.canyon.spi.instance.IPersistentIterator;
import de.objectcode.canyon.spi.instance.IProcessInstance;

/**
 * @hibernate.joined-subclass table="PACTIVITYINSTANCES"
 * @hibernate.joined-subclass-key column="ACTIVITYINSTANCEID"
 *
 * @author    junglas
 * @created   20. Oktober 2003
 */
public class PActivityInstance extends PAttributedEntity implements IActivityInstance
{
  private  int                  m_state;
  private  String               m_name;
  private  int                  m_priority;
  private  String               m_blockActivityInstanceId;
  private  String               m_activityDefinitionId;
  private  Date                 m_startedDate;
  private  Date                 m_completedDate;
  private  Date                 m_dueDate;
  private  IProcessInstance     m_processInstance;
  private  String[]             m_participantsDB;
  private  Set                  m_childProcessInstanceSet;
  private  int                  m_joinType;
  private  IJoinedTransition[]  m_joinedTransitions;
  private  IPersistentIterator  m_blockActivityIterator;
  private  int                  m_completionStrategy;


  /**
   *Constructor for the PActivityInstance object
   */
  public PActivityInstance()
  {
    super( IActivityInstance.PROPERTYDESCRIPTORS );
  }


  /**
   *Constructor for the PActivityInstance object
   *
   * @param processInstance          Description of the Parameter
   * @param activityDefinitionId     Description of the Parameter
   * @param activityName             Description of the Parameter
   * @param blockActivityInstanceId  Description of the Parameter
   * @param priority                 Description of the Parameter
   * @param state                    Description of the Parameter
   * @param participants             Description of the Parameter
   * @param joinType                 Description of the Parameter
   * @param transitionIds            Description of the Parameter
   */
  public PActivityInstance( PProcessInstance processInstance, String activityDefinitionId,
      String activityName, String blockActivityInstanceId,
      int joinType, String[] transitionIds,
      int priority, int state,
      String[] participants, int completionStrategy )
  {
    super( IActivityInstance.PROPERTYDESCRIPTORS, processInstance.getProcessDefinitionId() );

    m_processInstance = processInstance;
    m_activityDefinitionId = activityDefinitionId;
    m_name = activityName;
    m_blockActivityInstanceId = blockActivityInstanceId;
    m_priority = priority;
    m_state = state;
    if ( participants != null ) {
      m_participantsDB = new String[participants.length];
      System.arraycopy(participants,0,m_participantsDB,0,participants.length);
    }
    m_blockActivityIterator = new PPersistentIterator();
    m_joinType = joinType;
    m_joinedTransitions = new IJoinedTransition[transitionIds.length];
    m_completionStrategy = completionStrategy;

    int  i;

    for ( i = 0; i < transitionIds.length; i++ ) {
      m_joinedTransitions[i] = new PJoinedTransition( transitionIds[i] );
    }

    m_childProcessInstanceSet = new HashSet();
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
   * @param i
   */
  public void setState( int i )
  {
    m_state = i;
  }


  /**
   * @param i
   */
  public void setPriority( int i )
  {
    m_priority = i;
  }


  /**
   * @param string
   */
  public void setBlockActivityInstanceId( String string )
  {
    m_blockActivityInstanceId = string;
  }



  /**
   * Sets the participants attribute of the PActivityInstance object
   *
   * @param participants  The new participants value
   */
  public void setParticipantsDB( String[] participantsDB )
  {
    m_participantsDB = participantsDB;
  }
  
  public void setParticipants ( String[] participants ) 
  {
    m_participantsDB = new String[participants.length];
    
    System.arraycopy(participants, 0, m_participantsDB, 0, m_participantsDB.length);
  }


  /**
   * @param set
   */
  public void setChildProcessInstanceSet( Set set )
  {
    m_childProcessInstanceSet = set;
  }


  /**
   * @param instance
   */
  public void setProcessInstance( IProcessInstance instance )
  {
    m_processInstance = instance;
  }


  /**
   * @param string
   */
  public void setActivityDefinitionId( String string )
  {
    m_activityDefinitionId = string;
  }


  /**
   * @param transitions
   */
  public void setJoinedTransitions( IJoinedTransition[] transitions )
  {
    m_joinedTransitions = transitions;
  }


  /**
   * @param iterator
   */
  public void setBlockActivityIterator( IPersistentIterator iterator )
  {
    m_blockActivityIterator = iterator;
  }


  /**
   * @param i
   */
  public void setJoinType( int i )
  {
    m_joinType = i;
  }


  /**
   * @param i
   */
  public void setCompletionStrategy( int i )
  {
    m_completionStrategy = i;
  }


  /**
   * @hibernate.property column="ACTIVTYDEFINITIONID" type="string" length="64" not-null="true"
   *
   * @return
   */
  public String getActivityDefinitionId()
  {
    return m_activityDefinitionId;
  }


  /**
   * @hibernate.set lazy="true"
   * @hibernate.collection-key column="PARENTACTIVITYINSTANCEID"
   * @hibernate.collection-one-to-many class="de.objectcode.canyon.persistent.instance.PProcessInstance"
   *
   * @return
   */
  public Set getChildProcessInstanceSet()
  {
    return m_childProcessInstanceSet;
  }



  /**
   * @hibernate.property column="BLOCKACTIVITYINSTANCEID" type="string" length="64" not-null="false"
   *
   * @return
   */
  public String getBlockActivityInstanceId()
  {
    return m_blockActivityInstanceId;
  }


  /**
   * @hibernate.array table="PACTIVITYINSTANCE_PARTS" cascade="all"
   * @hibernate.collection-key column="ACTIVITYINSTANCEID"
   * @hibernate.collection-index column="IDX"
   * @hibernate.collection-element column="PARTICIPANT" type="string" length="64"
   *
   * @return
   */
  public String[] getParticipantsDB()
  {
    return m_participantsDB;
  }
  
  public String[] getParticipants()
  {
  	String[] ret = new String[m_participantsDB.length];
  	
  	System.arraycopy(m_participantsDB,0,ret,0,m_participantsDB.length);
  	
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
   * @hibernate.many-to-one column="PROCESSINSTANCEID" class="de.objectcode.canyon.persistent.instance.PProcessInstance" not-null="true"
   *
   * @return
   */
  public IProcessInstance getProcessInstance()
  {
    return m_processInstance;
  }


  /**
   * Gets the activityInstanceId attribute of the HibActivityInstance object
   *
   * @return   The activityInstanceId value
   */
  public String getActivityInstanceId()
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
   * @hibernate.property column="STATE" type="integer" length="2" not-null="true"
   *
   * @return
   */
  public int getState()
  {
    return m_state;
  }


  /**
   * @hibernate.array cascade="all"
   * @hibernate.collection-key column="ACTIVITYINSTANCEID"
   * @hibernate.collection-index column="IDX"
   * @hibernate.collection-one-to-many class="de.objectcode.canyon.persistent.instance.PJoinedTransition"
   *
   * @return
   */
  public IJoinedTransition[] getJoinedTransitions()
  {
    return m_joinedTransitions;
  }


  /**
   * @hibernate.component class="de.objectcode.canyon.persistent.instance.PPersistentIterator"
   *
   * @return
   */
  public IPersistentIterator getBlockActivityIterator()
  {
    return m_blockActivityIterator;
  }


  /**
   * @hibernate.property column="JOINTYPE" type="integer" length="2" not-null="true"
   *
   * @return
   */
  public int getJoinType()
  {
    return m_joinType;
  }


  /**
   * @hibernate.property column="COMPLETION" type="integer" length="2" not-null="true"
   *
   * @return
   */
  public int getCompletionStrategy()
  {
    return m_completionStrategy;
  }


  /**
   * Gets the processInstanceId attribute of the PActivityInstance object
   *
   * @return   The processInstanceId value
   */
  public String getProcessInstanceId()
  {
    return m_processInstance.getProcessInstanceId();
  }
}
