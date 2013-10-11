package de.objectcode.canyon.worklist.spi.worklist;

import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.filter.AndFilter;
import de.objectcode.canyon.spi.filter.IFilter;
import de.objectcode.canyon.worklist.IActivityInfo;

/**
 * @author    junglas
 * @created   1. Dezember 2003
 */
public interface IWorklistRepository
{
	public void beginTransaction() throws RepositoryException;
	
	public void endTransaction(boolean flush) throws RepositoryException;
	
	/**
   * Creates a work item.
   *
   * @param activityInstanceId    The activity instance id.
   * @param state                 The work item state, one of the integer values defined in
   * {@link org.wfmc.wapi.WMWorkItemState}.
   * @param participant           The name of the participant to whom the work item is
   * assigned.
   * @param performer             Description of the Parameter
   * @return                      The new work item.
   * @throws RepositoryException
   */
  public IWorkItem createWorkItem( String engineId, String clientId, IActivityInfo activityInfo, int state,
      String performer, String participant, IApplicationData[] applicationData )
    throws RepositoryException;


  /**
   * Description of the Method
   *
   * @param workItemId               Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public IWorkItem findWorkItem( String workItemId )
    throws RepositoryException;


  /**
   * Reads a set of work items.
   *
   * @param filter                   A Filter specification.
   * @return                         An array of matching work items.
   * @exception RepositoryException  Description of the Exception
   */
  public IWorkItem[] findWorkItems( IFilter filter )
    throws RepositoryException;


  /**
   * Reads a set of work items.
   *
   * @param filter                   A Filter specification.
   * @param maxResultSize            Maximum number of WorkItems
   * @return                         An array of matching work items.
   * @exception RepositoryException  Description of the Exception
   */
  public IWorkItem[] findWorkItems( IFilter filter, int maxResultSize )
    throws RepositoryException;

  /**
   * Description of the Method
   *
   * @param activityInstanceId       Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public IWorkItem[] findWorkItems( String processInstanceId, String activityInstanceId )
    throws RepositoryException;

  /**
   * Description of the Method
   *
   * @param filter                   Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public int countWorkItems( IFilter filter )
    throws RepositoryException;

	public IWorkItem[] findWorkItems(IFilter filter, int offset, int length, String[] sortAttrs, boolean[] sortAscending) throws RepositoryException;
	public int indexOf(WorkItemData workItemData, IFilter filter, String[] sortAttrs, boolean[] sortAscending ) throws RepositoryException;
}
