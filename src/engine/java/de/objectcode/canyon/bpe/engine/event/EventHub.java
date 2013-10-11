package de.objectcode.canyon.bpe.engine.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.api.worklist.BPEEvent;
import de.objectcode.canyon.api.worklist.ProcessInstanceData;
import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.activities.ActivityState;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.activities.xpdl.BaseXPDLActivity;
import de.objectcode.canyon.bpe.engine.activities.xpdl.XPDLToolActivity;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spiImpl.tool.bpe.BPEFacade;
import de.objectcode.canyon.worklist.IActivityInfo;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

/**
 * This class dispatches BPE Events 
 * @author junglas
 */
public class EventHub {
	private final static Log log = LogFactory.getLog(EventHub.class);

	private BPEEngine m_bpeEngine;

	private BPEFacade m_bpeFacade;

	public EventHub(BPEEngine engine) {
		m_bpeEngine = engine;
		m_bpeFacade = new BPEFacade(engine);
	}

	public void fireActivityStateEvent(Activity activity, ActivityState newState) {
		if (activity instanceof XPDLToolActivity) {
			BaseXPDLActivity xpdlActivity = (BaseXPDLActivity) activity;
			switch (newState.getValue()) {
			case ActivityState.COMPLETED_INT:
				m_bpeEngine.getBpeEventBroker().fireBPEEvent(
						buildBPEEvent(xpdlActivity,
								BPEEvent.Type.AUTOMATIC_ACTIVITY_COMPLETED));
				break;
			case ActivityState.ABORT_INT:
				m_bpeEngine.getBpeEventBroker().fireBPEEvent(
						buildBPEEvent(xpdlActivity,
								BPEEvent.Type.AUTOMATIC_ACTIVITY_ABORTED));
				break;
			}
		} else if (activity instanceof BPEProcess) {
			switch (newState.getValue()) {
			case ActivityState.COMPLETED_INT:
				m_bpeEngine.getBpeEventBroker().fireBPEEvent(
						buildBPEEvent(activity, BPEEvent.Type.PROCESS_COMPLETED));
				break;
			case ActivityState.ABORT_INT:
				m_bpeEngine.getBpeEventBroker().fireBPEEvent(
						buildBPEEvent(activity, BPEEvent.Type.PROCESS_TERMINATED));
				break;
			}
		}
	}

	public void fireProcessMessageEvent(BPEProcess process, Message message) {
		if (message.getOperation() != null && message.getOperation().indexOf("-init")!=-1) {
			m_bpeEngine.getBpeEventBroker().fireBPEEvent(
					buildBPEEvent(process, BPEEvent.Type.PROCESS_STARTED));
		} else {
			if (log.isDebugEnabled()) {
				log.debug("IGNORING MESSAGE " + message);
			}
		}
	}

	private BPEEvent buildBPEEvent(Activity activity, BPEEvent.Type type) {

		try {
			long start = 0L;
			if (log.isInfoEnabled()) {
				start = System.currentTimeMillis();
			}

			ProcessInstanceData pid = m_bpeFacade.getProcessInstanceReadOnly(activity
					.getProcess().getProcessInstanceId());
			WorkflowProcess wp = m_bpeFacade.getProcess(
					activity.getProcess().getId(), activity.getProcess().getVersion());
			BPEEvent result = new BPEEvent(activity.getProcess().getClientId(), wp
					.getPackage().getId(), wp.getPackage().findPackageVersion(), activity
					.getProcess().getId(), activity.getProcess().getVersion(), pid
					.getName(), activity.getId(), activity.getName(), activity
					.getProcess().getProcessInstanceIdPath(), pid, System
					.currentTimeMillis(), type, null // Performers
			);
			if (log.isInfoEnabled()) {
				log.info("Split buildBPEEventActivity:"
						+ (System.currentTimeMillis() - start));
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private BPEEvent buildBPEEvent(String clientId, IActivityInfo activity, String[] performers) {

		try {
			long start = 0L;
			if (log.isInfoEnabled()) {
				start = System.currentTimeMillis();
			}

			ProcessInstanceData pid = m_bpeFacade.getProcessInstanceReadOnly(activity
					.getProcessInstanceId());
			WorkflowProcess wp = m_bpeFacade.getProcess(pid.getProcessDefinitionId(),
					pid.getProcessDefinitionVersion());

			StringBuilder pidp = new StringBuilder();
			if (pid.getParentProcessInstanceIdPath()!=null)
				pidp.append(pid.getParentProcessInstanceIdPath()).append("_");
			pidp.append(pid.getId());
			BPEEvent result = new BPEEvent(
					clientId,
					wp.getPackage().getId(), wp.getPackage().findPackageVersion(), pid
							.getProcessDefinitionId(), pid.getProcessDefinitionVersion(), pid
							.getName(), activity.getActivityDefinitionId(), activity
							.getName(),
					pidp.toString(), 
					pid, System.currentTimeMillis(),
					BPEEvent.Type.MANUAL_ACTIVITY_STARTED, performers);
			if (log.isInfoEnabled()) {
				log.info("Split buildBPEEventActivityInfo:"
						+ (System.currentTimeMillis() - start));
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private BPEEvent buildBPEEvent(IWorkItem workItem, BPEEvent.Type type) {

		try {
			long start = 0L;
			if (log.isInfoEnabled()) {
				start = System.currentTimeMillis();
			}

			ProcessInstanceData pid = m_bpeFacade.getProcessInstanceReadOnly(workItem.getProcessInstanceId());
			WorkflowProcess wp = m_bpeFacade.getProcess(pid.getProcessDefinitionId(),
					pid.getProcessDefinitionVersion());

			StringBuilder pidp = new StringBuilder();
			if (pid.getParentProcessInstanceIdPath()!=null)
				pidp.append(pid.getParentProcessInstanceIdPath()).append("_");
			pidp.append(pid.getId());
			
			BPEEvent result = new BPEEvent(
					workItem.getClientId(),
					wp.getPackage().getId(), wp.getPackage().findPackageVersion(), pid
							.getProcessDefinitionId(), pid.getProcessDefinitionVersion(), pid
							.getName(), workItem.getActivityDefinitionId(), workItem.getName(),
					pidp.toString(),
					pid, System.currentTimeMillis(),
					type, new String[] { workItem.getParticipant() });
			if (log.isInfoEnabled()) {
				log.info("Split buildBPEEventActivityInfo:"
						+ (System.currentTimeMillis() - start));
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void fireManualActivityStarted(String clientId, IActivityInfo activityInfo, String[] participants) {
		m_bpeEngine.getBpeEventBroker().fireBPEEvent(buildBPEEvent(clientId,activityInfo,participants));
	}
	
	public void fireManualActivityStateChanged(IWorkItem workItem, BPEEvent.Type type) {
		m_bpeEngine.getBpeEventBroker().fireBPEEvent(buildBPEEvent(workItem, type));
	}
	
}
