package de.objectcode.canyon.spi.instance;

import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.filter.IFilter;

/**
 * @author    junglas
 * @created   17. Oktober 2003
 */
public interface IInstanceRepository
{
	public void beginTransaction() throws RepositoryException;
	
	public void endTransaction(boolean flush) throws RepositoryException;
	
  /**
   * Creates a new process instance for the given process definition.
   *
   * @param processDefinitionId       The process definition ID.
   * @param parentActivityInstanceId  The ID of the parent activity instance,
   * if any.
   * @param processInstanceName       The name of the process instance.
   * @param priority                  Process instance priority.
   * @param state                     The instance state, one of the integer values defined in
   * {@link org.wfmc.wapi.WMProcessInstanceState}.
   * @param participants              The list of process participants.
   * @param processDefinitionVersion  Description of the Parameter
   * @return                          The new process instance.
   * @throws RepositoryException      Workflow client exception.
   */
  public IProcessInstance createProcessInstance( String processDefinitionId, String processDefinitionVersion,
      String parentActivityInstanceId, String processInstanceName,
      int priority, int state, String[] participants )
    throws RepositoryException;

  /**
   * Get the specified process instance.
   *
   * @param processInstanceId     The process instance id
   * @return                      The process instance
   * @throws RepositoryException  Workflow client exception
   */
  public IProcessInstance findProcessInstance( String processInstanceId )
    throws RepositoryException;

  /**
   * Description of the Method
   *
   * @param acitivityInstanceId      Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public IProcessInstance[] findChildProcessInstances( String activityInstanceId )
    throws RepositoryException;


  /**
   * Description of the Method
   *
   * @param processDefinitionId      Description of the Parameter
   * @param filter                   Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public int countProcessInstances( String processDefinitionId, IFilter filter )
    throws RepositoryException;



  /**
   * Retrieve a list of process instances.
   *
   * @param processDefinitionId      The process definition Id, can be
   * <code>null>/code> to select from instances of any process definition.
   * @param filter                   A Filter specification.
   * @return                         An array of matching process instances.
   * @exception RepositoryException  Description of the Exception
   */
  public IProcessInstance[] findProcessInstances( String processDefinitionId,
      IFilter filter )
    throws RepositoryException;


  /**
   * Permanently deletes the specified process instance.  Implementations
   * must cascade this delete to include all related ActivityInstance,
   * WorkItem and AttributeInstance entities.
   *
   * @param processInstanceId     The ID of the process instance to delete.
   * @throws RepositoryException
   */
  public void deleteProcessInstance( String processInstanceId )
    throws RepositoryException;

  /**
   * Returns the specified activity instance.
   *
   * @param processDefinitionId      The process definition id.
   * @param processInstanceId        The process instance id.
   * @param activityDefinitionId     The activity definition id.
   * @param activityName             The name of the activity.
   * @param blockActivityInstanceId  The block activity instance ID, or
   *  <code>null</code> if the activity is not defined within an activity set.
   * @param priority                 Activity priority.
   * @param state                    Activity state, one of the integer values defined in
   * {@link org.wfmc.wapi.WMActivityInstanceState}.
   * @param participants             The list of participants for this activity.
   * @return                         The new activity instance.
   * @throws RepositoryException     Workflow client exception.
   */
  public IActivityInstance createActivityInstance( String processDefinitionId,
      String processInstanceId, String activityDefinitionId,
      String activityName, String blockActivityInstanceId,
      int joinType, String[] transitionIds,
      int priority, int state,
      String[] participants, int completionStrategy )
    throws RepositoryException;

  /**
   * Reads the specified activity instance.
   *
   * @param activityInstanceId    The activity instance id.
   * @return                      The activity instance.
   * @throws RepositoryException  Workflow client exception.
   */
  public IActivityInstance findActivityInstance( String activityInstanceId )
    throws RepositoryException;


  /**
   * Reads the specified activity instance.  This method takes either the
   * activity definition ID or the activity instance ID.
   *
   * @param processInstanceId        The process instance id.
   * @param activityDefinitionId     The activity definition id.
   * @param blockActivityInstanceId  The ID of the block activity instance to
   * which the acitivty instance belongs.
   * @return                         The activity instance.
   * @throws RepositoryException     Workflow client exception.
   */
  public IActivityInstance findActivityInstance( String processInstanceId,
      String activityDefinitionId, String blockActivityInstanceId )
    throws RepositoryException;


  /**
   * Description of the Method
   *
   * @param processInstanceId        Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public IActivityInstance[] findActivityInstances( String processInstanceId )
    throws RepositoryException;


  /**
   * Description of the Method
   *
   * @param processDefinitionId      Description of the Parameter
   * @param activityDefinitionId     Description of the Parameter
   * @param filter                   Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public int countActivityInstances( String processDefinitionId,
      String activityDefinitionId, IFilter filter )
    throws RepositoryException;


  /**
   * Reads a list of activity instances.
   *
   * @param processDefinitionId      The process definition ID, can be
   * <code>null</code>.
   * @param filter                   A Filter specification.
   * @param activityDefinitionId     Description of the Parameter
   * @return                         An array of matching activity instances.
   * @exception RepositoryException  Description of the Exception
   */
  public IActivityInstance[] findActivityInstances( String processDefinitionId,
      String activityDefinitionId, IFilter filter )
    throws RepositoryException;


}
