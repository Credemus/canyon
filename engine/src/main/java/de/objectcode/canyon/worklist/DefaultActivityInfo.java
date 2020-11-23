package de.objectcode.canyon.worklist;

import java.util.Date;

import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.spi.instance.IActivityInstance;
import de.objectcode.canyon.spi.process.IProcessDefinitionID;

/**
 * @author    junglas
 * @created   20. Juli 2004
 */
public class DefaultActivityInfo implements IActivityInfo
{
  private  IActivityInstance  m_activityInstance;
  private  Activity m_activity;
  

  /**
   *Constructor for the DefaultActivityInfo object
   *
   * @param activityInstance  Description of the Parameter
   */
  public DefaultActivityInfo( Activity activity, IActivityInstance activityInstance )
  {
    m_activityInstance = activityInstance;
    m_activity = activity;
  }


  /**
   * @return   The activityDefinitionId value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getActivityDefinitionId()
   */
  public String getActivityDefinitionId()
  {
    return m_activityInstance.getActivityDefinitionId();
  }


  /**
   * @return   The activityInstanceId value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getActivityInstanceId()
   */
  public String getActivityInstanceId()
  {
    return m_activityInstance.getActivityInstanceId();
  }


  /**
   * @return   The completionStrategy value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getCompletionStrategy()
   */
  public int getCompletionStrategy()
  {
    return m_activity.getCompletionStrategy().getValue();
  }

  public int getAssignStrategy()
  {
    return m_activity.getAssignStrategy().getValue();
  }

  /**
   * @return   The dueDate value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getDueDate()
   */
  public Date getDueDate()
  {
    return m_activityInstance.getDueDate();
  }


  /**
   * @return   The name value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getName()
   */
  public String getName()
  {
    return m_activityInstance.getName();
  }


  /**
   * @return   The performers value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getPerformers()
   */
  public Participant[] getPerformers()
  {
    return m_activity.getPerformerParticipants();
  }


  /**
   * @return   The priority value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getPriority()
   */
  public int getPriority()
  {
    return m_activityInstance.getPriority();
  }


  /**
   * @return   The processDefinitionId value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getProcessDefinitionId()
   */
  public IProcessDefinitionID getProcessDefinitionId()
  {
    return m_activityInstance.getProcessDefinitionId();
  }


  /**
   * @return   The processInstanceId value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getProcessInstanceId()
   */
  public String getProcessInstanceId()
  {
    return m_activityInstance.getProcessInstance().getProcessInstanceId();
  }


  /**
   * @return   The processStarter value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getProcessStarter()
   */
  public String getProcessStarter()
  {
    // TODO Auto-generated method stub
    return null;
  }


  /* (non-Javadoc)
   * @see de.objectcode.canyon.worklist.IActivityInfo#getVariableValue(java.lang.String)
   */
  public Object getVariableValue(String variableName) {
    // TODO Auto-generated method stub
    return null;
  }


  /* (non-Javadoc)
   * @see de.objectcode.canyon.worklist.IActivityInfo#getProcessInstanceIdPath()
   */
  public String getProcessInstanceIdPath() {
    // TODO Auto-generated method stub 
    return null;
  }


public String getExtendedAttributeValue(String attributeName) {
	// TODO Auto-generated method stub
	return null;
}


public String getProcessName() {
	// TODO Auto-generated method stub
	return null;
}


public boolean isEscalationRetry() {
	return false;
}
}
