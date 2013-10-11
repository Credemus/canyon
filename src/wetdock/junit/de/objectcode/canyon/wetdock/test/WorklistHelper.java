package de.objectcode.canyon.wetdock.test;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.wfmc.wapi.WMWorkItemState;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.api.worklist.Worklist;
import de.objectcode.canyon.api.worklist.WorklistHome;
import de.objectcode.canyon.spi.filter.IFilter;

/**
 * @author junglas
 */
public class WorklistHelper
{
  private Worklist m_worklist;

  public WorklistHelper()
      throws NamingException, RemoteException, CreateException
  {
    InitialContext ctx = new InitialContext();

    WorklistHome worklistHome = (WorklistHome) PortableRemoteObject.narrow( ctx
        .lookup( WorklistHome.JNDI_NAME ), WorklistHome.class );

    m_worklist = worklistHome.create();
  }

  /**
   * @return Returns the worklist.
   */
  public Worklist getWorklist()
  {
    return m_worklist;
  }

  public WorkItemData[] listWorkItems( String userId, String clientId, boolean onlyOpen )
      throws RemoteException, WMWorkflowException
  {
    return m_worklist.listWorkItems( userId, clientId, 0, 1000, onlyOpen );
  }

  public WorkItemData findWorkItem( String userId, String clientId, String processInstanceId,
      String activityId )
      throws RemoteException, WMWorkflowException
  {
    WorkItemData[] workItems = listWorkItems( userId, clientId, true );
    int i;

    for ( i = 0; i < workItems.length; i++ ) {
      if (processInstanceId != null) {
      	if (processInstanceId.equals( workItems[i].getProcessInstanceId())
      			&& activityId.equals( workItems[i].getActivityId() ) )
      		return workItems[i];
      } else  {
      	if (activityId.equals( workItems[i].getActivityId() ) )
      		return workItems[i];      	
      }
    }

    return null;
  }

  public WorkItemData findWorkItemByProcessIdAndActivityId( String userId, String clientId, String processId,
      String activityId )
      throws RemoteException, WMWorkflowException
  {
    WorkItemData[] workItems = listWorkItems( userId, clientId, true );
    int i;

    for ( i = 0; i < workItems.length; i++ ) {
      	if (processId.equals( workItems[i].getProcessId())
      			&& activityId.equals( workItems[i].getActivityId() ) )
      		return workItems[i];      	
    }

    return null;
  }
  
  public WorkItemData findWorkItem( String userId, String clientId, 
      String activityId )
      throws RemoteException, WMWorkflowException
  {
    WorkItemData[] workItems = listWorkItems( userId, clientId, true );
    int i;

    for ( i = 0; i < workItems.length; i++ ) {
      if ( activityId.equals( workItems[i].getActivityId() ) )
        return workItems[i];
    }

    return null;
  }

  public WorkItemData[] findWorkItems( String userId, String clientId, IFilter filter, int offset, int length, String[] sortAttrs, boolean[] asc)
      throws RemoteException, WMWorkflowException
  {
    WorkItemData[] workItems = m_worklist.listWorkItems( userId, clientId, offset, length, true, filter, sortAttrs, asc );
    return workItems;
  }

  public int indexOfWorkItem( WorkItemData wd, String userId, String clientId, IFilter filter, String[] sortAttrs, boolean[] asc)
  throws RemoteException, WMWorkflowException
  {
  	return m_worklist.indexOf(wd, userId, clientId, false, filter, sortAttrs, asc);
  }
  
  public int countWorkItems( String userId, String clientId, boolean onlyOpen, IFilter filter)
  throws RemoteException, WMWorkflowException
{
  	return m_worklist.countWorkItems( userId, clientId, onlyOpen, filter);
}

  public void completeWorkItem( WorkItemData workItem )
      throws RemoteException, WMWorkflowException
  {
    workItem.setState( WMWorkItemState.CLOSED_COMPLETED );
    m_worklist.updateWorkItem( workItem );
  }

  public void acceptWorkItem(WorkItemData workItem) throws RemoteException,
			WMWorkflowException {
		workItem.setState(WMWorkItemState.OPEN_RUNNING);
		m_worklist.updateWorkItem(workItem);
	}

}
