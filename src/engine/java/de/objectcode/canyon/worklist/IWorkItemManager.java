package de.objectcode.canyon.worklist;

import org.wfmc.wapi.WMAttribute;
import org.wfmc.wapi.WMFilter;
import org.wfmc.wapi.WMWorkItem;
import org.wfmc.wapi.WMWorkItemState;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.spi.filter.IFilter;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

/**
 * @author    junglas
 * @created   30. Oktober 2003
 */
public interface IWorkItemManager
{
  /**
   * Description of the Method
   *
   * @param workItemId               Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public WMWorkItem findWorkItem( String workItemId )
    throws WMWorkflowException;


  /**
   * Gets the workItem attribute of the IWorkItemManager object
   *
   * @param workItemId               Description of the Parameter
   * @return                         The workItem value
   * @exception WMWorkflowException  Description of the Exception
   */
  public IWorkItem getWorkItem( String workItemId )
    throws WMWorkflowException;


  /**
   * Description of the Method
   *
   * @param filter                   Description of the Parameter
   * @param countFlag                Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public WMWorkItem[] findWorkItems( WMFilter filter, boolean countFlag )
    throws WMWorkflowException;


  /**
   * Description of the Method
   *
   * @param workItemId               Description of the Parameter
   * @param filter                   Description of the Parameter
   * @param countFlag                Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public WMWorkItemState[] findWorkItemStates(
      String workItemId, WMFilter filter, boolean countFlag )
    throws WMWorkflowException;


  /**
   * Description of the Method
   *
   * @param workItemId               Description of the Parameter
   * @param attributeName            Description of the Parameter
   * @param attributeType            Description of the Parameter
   * @param attributeValue           Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public void assignWorkItemAttribute(
      String workItemId, String attributeName, int attributeType,
      Object attributeValue )
    throws WMWorkflowException;

  public void assignApplicationData (
    String workItemId, IApplicationData[] applicationData )
    throws WMWorkflowException;

  /**
   * Description of the Method
   *
   * @param workItemId               Description of the Parameter
   * @param attributeName            Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public WMAttribute findWorkItemAttributeValue(
      String workItemId, String attributeName )
    throws WMWorkflowException;


  /**
   * Description of the Method
   *
   * @param workItemId               Description of the Parameter
   * @param filter                   Description of the Parameter
   * @param countFlag                Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public WMAttribute[] findWorkItemAttributes( String workItemId,
      WMFilter filter, boolean countFlag )
    throws WMWorkflowException;


  /**
   * Description of the Method
   *
   * @param participant              Description of the Parameter
   * @param onlyOpen                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public int countWorkItems( String participant, boolean onlyOpen )
    throws WMWorkflowException;


  /**
   * Description of the Method
   *
   * @param participant              Description of the Parameter
   * @param onlyOpen                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public IWorkItem[] listWorkItems( String participant, boolean onlyOpen )
    throws WMWorkflowException;

  public IWorkItem[] listWorkItems( String participant, String clientId, boolean onlyOpen )
  throws WMWorkflowException;


  public IWorkItem[] listWorkItemsForProcessInstance( String processInstanceId, boolean onlyOpen )
	   throws WMWorkflowException;

  /**
   * Description of the Method
   *
   * @param sourceUser               Description of the Parameter
   * @param targetUser               Description of the Parameter
   * @param workItemId               Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public void reassignWorkItem( String sourceUser, String targetUser,
      String workItemId )
    throws WMWorkflowException;


  /**
   * Description of the Method
   *
   * @param workItemId               Description of the Parameter
   * @param newState                 Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public void changeWorkItemState( String workItemId,
      WMWorkItemState newState )
    throws WMWorkflowException;


  /**
   * Description of the Method
   *
   * @param workItemId               Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public void completeWorkItem( String workItemId )
    throws WMWorkflowException;

	/**
	 * 
	 * @param workItemId
	 * @param processInstanceId
	 * @return
	 * @throws WMWorkflowException
	 */
	public String[] findParticipants(String workItemId)
		throws WMWorkflowException;
			
			
	
	/**
	 * 
	 * @param processDefinitionId
	 * @param activityDefinitionId
	 * @param overdue
	 * @param performer
	 * @param participants
	 * @param states
   * @param activityName
	 * @return
	 * @throws WMWorkflowException
	 */
  public IWorkItem[] listWorkItems(
  		String processDefinitionId, 
			String activityDefinitionId, 
			Boolean overdue, 
			String performer, 
			String[] participants, 
			int[] states,
		  String activityName,
		  String processName,
		  String processInstanceIdPath) 
  throws WMWorkflowException; 
				
  public IWorkItem[] listWorkItems(String processDefinitionId,
      String activityDefinitionId, Boolean overdue, String performer,
      String[] participants, int[] states, String activityName,
      String processName, String processInstanceIdPath, int maxResultSize)
      throws WMWorkflowException;


	public IWorkItem[] listWorkItems(String userName, String clientId, boolean onlyOpen, int offset, int length, IFilter filter, String[] sortAttrs, boolean[] sortAscending)  throws WMWorkflowException;
	public int indexOf(WorkItemData workItemData, String userName, String clientId, boolean onlyOpen, IFilter addFilter, String[] sortAttrs, boolean[] sortAscending) throws WMWorkflowException;

	public int countWorkItems(String userName, String clientId, boolean onlyOpen, IFilter filter) throws WMWorkflowException;
}

