package de.objectcode.canyon.worklist;

import java.util.Set;

import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.spi.tool.MessageEvent;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;

/**
 * @author    junglas
 * @created   1. Dezember 2003
 */
public interface IWorklistEngine
{
  public String[] startWorkItems ( String engineId, String clientId, IActivityInfo activityInfo, IApplicationData[] applicationData, Set ignoreParticipants )
  	throws WMWorkflowException;
  
  /**
   * Change the state of the workitems of an activity instance.
   */
  public void terminateWorkItems ( String processInstanceId, String activityInstanceId ) throws WMWorkflowException;

  public void addWorklistListener ( IWorklistListener listener );
  
  public void removeWorklistListener ( IWorklistListener listener );

  public IWorkItemManager getWorkItemManager();

  public String[] findWorkItemsForEvent ( MessageEvent event ) throws WMWorkflowException;
  
  public boolean handleEvent ( String workItemId, MessageEvent event ) throws WMWorkflowException;
}
