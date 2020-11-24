package de.objectcode.canyon.api.worklist;

import java.io.Serializable;

/**
 * This class is used to signal relevant activities of the process engine
 * @author xylander
 *
 */

public class BPEEvent implements Serializable {

	public enum Type {
		PROCESS_STARTED,
		PROCESS_COMPLETED,
		PROCESS_TERMINATED,
		AUTOMATIC_ACTIVITY_COMPLETED,
		AUTOMATIC_ACTIVITY_ABORTED,
		MANUAL_ACTIVITY_STARTED,
		MANUAL_ACTIVITY_ACCEPTED,
		MANUAL_ACTIVITY_REJECTED,
		MANUAL_ACTIVITY_DELEGATED,
		MANUAL_ACTIVITY_COMPLETED,
		MANUAL_ACTIVITY_ABORTED,
		ERROR};

	private String   m_clientId;
	private String   m_packageId;
	private String   m_packageVersion;
	private String   m_processId;
	private String   m_processVersion;
	private String   m_processName;
	private String   m_processInstanceIdPath;
	private String   m_activityId;
	private String   m_activityName;
	private ProcessInstanceData   m_processInstanceData;
	private long     m_timeStamp;
	private Type     m_type;
	private String[] m_performers;
	
	
	public BPEEvent(
			String clientId,
			String packageId, 
			String packageVersion, 
			String processId, 
			String processVersion, 
			String processName, 
			String activityId, 
			String activityName, 
			String processInstanceIdPath, 
			ProcessInstanceData processInstanceData, 
			long timeStamp, 
			Type type, 
			String[] performers) {
		super();
		m_clientId=clientId;
		m_packageId = packageId;
		m_packageVersion = packageVersion;
		m_processId = processId;
		m_processVersion = processVersion;
		m_processName = processName;
		m_processInstanceIdPath = processInstanceIdPath;
		m_activityId = activityId;
		m_activityName = activityName;
		m_processInstanceData = processInstanceData;
		m_timeStamp = timeStamp;
		m_type = type;
		m_performers = performers;
	}
	
	
	public String getActivityId() {
		return m_activityId;
	}
	public String getActivityName() {
		return m_activityName;
	}
	public String[] getPerformers() {
		return m_performers;
	}
	public String getProcessId() {
		return m_processId;
	}
	public ProcessInstanceData getProcessInstanceData() {
		return m_processInstanceData;
	}
	public String getProcessInstanceIdPath() {
		return m_processInstanceIdPath;
	}
	public String getProcessName() {
		return m_processName;
	}
	public String getProcessVersion() {
		return m_processVersion;
	}
	public long getTimeStamp() {
		return m_timeStamp;
	}
	public Type getType() {
		return m_type;
	}


	public String getClientId() {
		return m_clientId;
	}


	public String getPackageId() {
		return m_packageId;
	}
	
	public String getPackageVersion() {
		return m_packageVersion;
	}
	
	
	
	
}
