package de.objectcode.canyon.bpe.connector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.activities.xpdl.XPDLWorklistActivity;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.variable.ComplexValue;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.worklist.IWorklistListener;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

/**
 * @author junglas
 * @created 15. Juli 2004
 */
public class WorklistConnectorListener implements IWorklistListener {
  private final static Log log = LogFactory.getLog(WorklistConnectorListener.class);

  private BPEEngine m_bpeEngine;


  /**
   * Constructor for the WorklistConnectorListener object
   *
   * @param engine Description of the Parameter
   */
  public WorklistConnectorListener(BPEEngine engine) {
    m_bpeEngine = engine;
  }


  /**
   * @param workItem Description of the Parameter
   * @throws WMWorkflowException Description of the Exception
   * @see de.objectcode.canyon.worklist.IWorklistListener#workItemAborted(de.objectcode.canyon.worklist.spi.worklist.IWorkItem)
   */
  public void workItemAborted(IWorkItem workItem)
          throws WMWorkflowException {
    if (!BPEEngine.ENGINE_ID.equals(workItem.getEngineId()))
      return;

    try {
      BPEProcess process = m_bpeEngine.getProcessInstance(workItem.getProcessInstanceId());

      Activity activity = process.getActivity(workItem.getActivityInstanceId());

      if (activity != null && activity instanceof XPDLWorklistActivity) {
        ((XPDLWorklistActivity) activity).workItemAborted(workItem);

        process.startActivatedActivities();

        m_bpeEngine.updateProcessInstance(process);
      } else {
        Message message = new Message("worklist-aborted", process.getType("worklist-response"));

        message.getContent().set("processId", workItem.getProcessInstanceId());
        message.getContent().set("activityId", workItem.getActivityInstanceId());

        m_bpeEngine.handleMessage(workItem.getProcessInstanceId(), message);
      }
    } catch (Exception e) {
      log.error("Exception", e);
    }
  }


  /**
   * @param workItem Description of the Parameter
   * @throws WMWorkflowException Description of the Exception
   * @see de.objectcode.canyon.worklist.IWorklistListener#workItemCompleted(de.objectcode.canyon.worklist.spi.worklist.IWorkItem)
   */
  public void workItemCompleted(IWorkItem workItem)
          throws WMWorkflowException {
    if (!BPEEngine.ENGINE_ID.equals(workItem.getEngineId()))
      return;

    try {
      BPEProcess process = m_bpeEngine.getProcessInstance(workItem.getProcessInstanceId());

      Activity activity = process.getActivity(workItem.getActivityInstanceId());

      if (activity != null && activity instanceof XPDLWorklistActivity) {
        ((XPDLWorklistActivity) activity).workItemCompleted(workItem);

        process.startActivatedActivities();

        m_bpeEngine.updateProcessInstance(process);
      } else {
        log.error("Unexpected activity: Should not happen");
      }
    } catch (Exception e) {
      log.error("Exception", e);
    }
  }


  /**
   * @param workItem Description of the Parameter
   * @throws WMWorkflowException Description of the Exception
   * @see de.objectcode.canyon.worklist.IWorklistListener#workItemStarted(de.objectcode.canyon.worklist.spi.worklist.IWorkItem)
   */
  public void workItemStarted(IWorkItem workItem)
          throws WMWorkflowException {
  }


  /**
   * @param workItem Description of the Parameter
   * @throws WMWorkflowException Description of the Exception
   * @see de.objectcode.canyon.worklist.IWorklistListener#workItemTerminated(de.objectcode.canyon.worklist.spi.worklist.IWorkItem)
   */
  public void workItemTerminated(IWorkItem workItem)
          throws WMWorkflowException {
  }
}
