package de.objectcode.canyon.drydock.worklist;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMWorkItemState;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.instance.IActivityInstance;
import de.objectcode.canyon.spi.instance.IProcessInstance;
import de.objectcode.canyon.spi.tool.MessageEvent;
import de.objectcode.canyon.worklist.AbstractWorklistEngine;
import de.objectcode.canyon.worklist.IActivityInfo;
import de.objectcode.canyon.worklist.IWorkItemManager;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;

/**
 * @author    junglas
 * @created   15. Juni 2004
 */
public class SimpleWorklistEngine extends AbstractWorklistEngine implements IWorklistChangeListener
{
  private final static Log log = LogFactory.getLog(SimpleWorklistEngine.class);
  
  private Worklist m_worklist;
  
  public SimpleWorklistEngine(Worklist worklist)
  {
    super(null);
    
    m_worklist = worklist;
    m_worklist.addWorklistChangeListener(this);
  }
  
  /**
   * @return   The workItemManager value
   * @see      de.objectcode.canyon.worklist.IWorklistEngine#getWorkItemManager()
   */
  public IWorkItemManager getWorkItemManager()
  {
    return null;
  }


  /**
   * @param eventType                Description of the Parameter
   * @param eventParameters          Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   * @see                            de.objectcode.canyon.worklist.IWorklistEngine#findWorkItemForEvent(java.lang.String, java.util.Map)
   */
  public String[] findWorkItemForEvent( MessageEvent event )
    throws WMWorkflowException
  {
    return new String[0];
  }


  /**
   * @param workItemId               Description of the Parameter
   * @param eventType                Description of the Parameter
   * @param eventParameters          Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   * @see                            de.objectcode.canyon.worklist.IWorklistEngine#handleEvent(java.lang.String, java.lang.String, java.util.Map)
   */
  public boolean handleEvent( String workItemId, MessageEvent event)
    throws WMWorkflowException
  {
    return false;
  }


  /**
   * @param activityInfo             Description of the Parameter
   * @param applicationData          Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   * @see                            de.objectcode.canyon.worklist.IWorklistEngine#startWorkItems(de.objectcode.canyon.worklist.IActivityInfo, de.objectcode.canyon.worklist.spi.worklist.IApplicationData)
   */
  public synchronized String[] startWorkItems( String engineId, String clientId, IActivityInfo activityInfo,
      IApplicationData[] applicationData, Set ignorePartcipants )
    throws WMWorkflowException
  {
    int index = m_worklist.getWorkItemCount();
    
    WorkItem workItem = new WorkItem ( engineId, clientId, activityInfo.getName(), activityInfo.getActivityInstanceId(), 
        activityInfo.getProcessInstanceId(), activityInfo.getProcessInstanceIdPath(), activityInfo.getPerformers(),
        applicationData);
    
    m_worklist.addWorkItem(workItem);
    
    return new String[] { String.valueOf(index) };
  }


  /**
   * @param processInstance          Description of the Parameter
   * @param activityInstance         Description of the Parameter
   * @param workflowProcess          Description of the Parameter
   * @param activity                 Description of the Parameter
   * @param applicationData          Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   * @see                            de.objectcode.canyon.worklist.IWorklistEngine#startWorkItems(de.objectcode.canyon.spi.instance.IProcessInstance, de.objectcode.canyon.spi.instance.IActivityInstance, de.objectcode.canyon.model.process.WorkflowProcess, de.objectcode.canyon.model.activity.Activity, de.objectcode.canyon.worklist.spi.worklist.IApplicationData)
   */
  public String[] startWorkItems( String engineId, String clientId, IProcessInstance processInstance,
      IActivityInstance activityInstance,
      WorkflowProcess workflowProcess,
      Activity activity,
      IApplicationData applicationData )
    throws WMWorkflowException
  {
    return null;
  }


  /**
   * @param processInstanceId        Description of the Parameter
   * @param activityInstanceId       Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   * @see                            de.objectcode.canyon.worklist.IWorklistEngine#terminateWorkItems(java.lang.String, java.lang.String)
   */
  public void terminateWorkItems( String processInstanceId,
      String activityInstanceId )
    throws WMWorkflowException
  {
    WorkItem workItem = m_worklist.findWorkItem ( processInstanceId, activityInstanceId );
    
    if ( workItem != null )
      workItem.setStatus(WMWorkItemState.CLOSED_TERMINATED_INT);
  }
  /**
   * @see de.objectcode.canyon.drydock.worklist.IWorklistChangeListener#worklistChanged()
   */
  public void worklistChanged ( )
  {
  }
  /**
   * @see de.objectcode.canyon.drydock.worklist.IWorklistChangeListener#workItemCompleted(de.objectcode.canyon.drydock.worklist.WorkItem)
   */
  public void workItemCompleted (WorkItem workItem)
  {
    try {
      super.fireWorkItemCompleted(workItem);
    }
    catch ( Exception e ) {
      log.error("Exception", e);
    }
  }

	public String[] findWorkItemsForEvent(MessageEvent event) throws WMWorkflowException {
		// TODO Auto-generated method stub
		return null;
	}
}
