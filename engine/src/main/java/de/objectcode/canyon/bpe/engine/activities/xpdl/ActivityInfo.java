package de.objectcode.canyon.bpe.engine.activities.xpdl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.bpe.util.ExtendedAttributeHelper;
import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.spi.process.IProcessDefinitionID;
import de.objectcode.canyon.worklist.IActivityInfo;

/**
 * @author    junglas
 * @created   20. Juli 2004
 */
public class ActivityInfo implements IActivityInfo
{
  private  String         m_name;
  private  String         m_activityDefinitionId;
  private  String         m_activityInstanceId;
  private  String         m_processDefinitionId;
  private  String         m_processDefinitionVersion;
  private  String         m_processInstanceId;
  private  String         m_processInstanceIdPath;
  private  Date           m_dueDate;
  private  int            m_priority;
  private  int            m_completionStrategy;
  private  int            m_assignStrategy;
  private  Participant[]  m_performers;
  private  String         m_processStarter;
  private  Map			  m_variableMap;
  private  ExtendedAttributeHelper m_extendedAttributeHelper;
  private	 String				  m_processName;
  private  boolean				m_escalationRetry= false;  


  /**
   *Constructor for the ActivityInfo object
   *
   * @param name                  Description of the Parameter
   * @param activityDefinitionId  Description of the Parameter
   * @param activityInstanceId    Description of the Parameter
   * @param processDefinitionId   Description of the Parameter
   * @param processInstanceId     Description of the Parameter
   * @param dueDate               Description of the Parameter
   * @param priority              Description of the Parameter
   * @param performers            Description of the Parameter
   * @param processStarter        Description of the Parameter
   * @param completionStrategy    Description of the Parameter
   */
  public ActivityInfo( String name, String activityDefinitionId, String activityInstanceId, String processDefinitionId, String processDefinitionVersion, String processInstanceId, String processInstanceIdPath, Date dueDate,
      int priority, int completionStrategy, int assignStrategy, Participant[] performers, String processStarter, IVariable[] variables, ExtendedAttribute[] extendedAttributes, String processName, boolean escalationRetry )
  {
    m_name = name;
    m_activityDefinitionId = activityDefinitionId;
    m_activityInstanceId = activityInstanceId;
    m_processDefinitionId = processDefinitionId;
    m_processDefinitionVersion = processDefinitionVersion;
    m_processInstanceId = processInstanceId;
    m_processInstanceIdPath = processInstanceIdPath;
    m_dueDate = dueDate;
    m_priority = priority;
    m_completionStrategy = completionStrategy;
    m_assignStrategy = assignStrategy;
    m_performers = performers;
    m_processStarter = processStarter;
    m_variableMap = new HashMap();
    m_processName = processName; 
    m_escalationRetry = escalationRetry;
    for (int i = 0; i < variables.length; i++) {
    	IVariable var = variables[i];
    	Object value = var.getValue();
    	if (value != null)
    		m_variableMap.put(var.getName(), value);
    }
    m_extendedAttributeHelper = new ExtendedAttributeHelper(extendedAttributes);
  }


  /**
   * @return   Returns the completionStrategy.
   */
  public int getCompletionStrategy()
  {
    return m_completionStrategy;
  }

  public int getAssignStrategy()
  {
    return m_assignStrategy;
  }
  

  /**
   * @return   Returns the activityDefinitionId.
   */
  public String getActivityDefinitionId()
  {
    return m_activityDefinitionId;
  }


  /**
   * @return   Returns the processDefinitionId.
   */
  public IProcessDefinitionID getProcessDefinitionId()
  {
    return new ProcessDefinitionId();
  }


  /**
   * @return   The processStarter value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getProcessStarter()
   */
  public String getProcessStarter()
  {
    return m_processStarter;
  }


  /**
   * @return   The activityInstanceId value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getActivityInstanceId()
   */
  public String getActivityInstanceId()
  {
    return m_activityInstanceId;
  }


  /**
   * @return   The dueDate value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getDueDate()
   */
  public Date getDueDate()
  {
    return m_dueDate;
  }


  /**
   * @return   The name value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getName()
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * @return   The performers value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getPerformers()
   */
  public Participant[] getPerformers()
  {
    return m_performers;
  }


  /**
   * @return   The priority value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getPriority()
   */
  public int getPriority()
  {
    return m_priority;
  }


  /**
   * @return   The processInstanceId value
   * @see      de.objectcode.canyon.worklist.IActivityInfo#getProcessInstanceId()
   */
  public String getProcessInstanceId()
  {
    return m_processInstanceId;
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   20. Juli 2004
   */
  public class ProcessDefinitionId implements IProcessDefinitionID
  {

    /**
     * @return   The id value
     * @see      de.objectcode.canyon.spi.process.IProcessDefinitionID#getId()
     */
    public String getId()
    {
      return m_processDefinitionId;
    }


    /**
     * @return   The version value
     * @see      de.objectcode.canyon.spi.process.IProcessDefinitionID#getVersion()
     */
    public String getVersion()
    {
      return m_processDefinitionVersion;
    }
  }


/* (non-Javadoc)
 * @see de.objectcode.canyon.worklist.IActivityInfo#getVariableValue(java.lang.String)
 */
public Object getVariableValue(String variableName) {
	return m_variableMap.get(variableName);
}


  public String getExtendedAttributeValue(String attributeName) {
	  return m_extendedAttributeHelper.getValue(attributeName);
  }
  
  public String getProcessInstanceIdPath() {
    return m_processInstanceIdPath;
  }


	public String getProcessName() {
		return m_processName;
	}


	public boolean isEscalationRetry() {
		return m_escalationRetry;
	}
}
