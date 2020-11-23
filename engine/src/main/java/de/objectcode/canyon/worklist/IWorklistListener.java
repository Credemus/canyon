package de.objectcode.canyon.worklist;

import java.util.EventListener;

import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

/**
 * Event listener on the worklist engine.
 *
 *
 * @author    junglas
 * @created   2. Dezember 2003
 */
public interface IWorklistListener extends EventListener
{
  /**
   * Description of the Method
   *
   * @param workItem                 Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public void workItemStarted( IWorkItem workItem )
    throws WMWorkflowException;


  /**
   * Description of the Method
   *
   * @param workItem                 Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public void workItemAborted( IWorkItem workItem )
    throws WMWorkflowException;


  /**
   * Description of the Method
   *
   * @param workItem                 Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public void workItemCompleted( IWorkItem workItem )
    throws WMWorkflowException;


  /**
   * Description of the Method
   *
   * @param workItem                 Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public void workItemTerminated( IWorkItem workItem )
    throws WMWorkflowException;
}
