package de.objectcode.canyon.worklist;

import java.util.Date;

import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.spi.process.IProcessDefinitionID;

/**
 * @author junglas
 */
public interface IActivityInfo
{
  public String getProcessInstanceId ();
  
  public String getProcessInstanceIdPath ();

  public IProcessDefinitionID getProcessDefinitionId ();
  
  public int getCompletionStrategy();
  
  public int getAssignStrategy();
  
  public String getActivityDefinitionId ();
  
  public String getActivityInstanceId ();
  
  public String getName();
  
  public int getPriority();
  
  public Date getDueDate();
  
  public Participant[] getPerformers();

  public String getProcessStarter ();
  
  public Object getVariableValue(String variableName);
  
  public String getExtendedAttributeValue(String attributeName);
  
  public String getProcessName();
  
  public boolean isEscalationRetry();
}
