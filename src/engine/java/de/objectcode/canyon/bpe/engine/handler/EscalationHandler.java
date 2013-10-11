package de.objectcode.canyon.bpe.engine.handler;

import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.ActivityState;
import de.objectcode.canyon.bpe.engine.activities.Scope;
import de.objectcode.canyon.bpe.engine.activities.xpdl.XPDLWorklistActivity;
import de.objectcode.canyon.bpe.engine.variable.BasicVariable;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.data.BasicType;

/**
 * An EscalationHandler is a specifc OnAlarmHandler which takes cares of
 * Escalation Alarms
 * 
 * @author xylander
 * 
 */
public class EscalationHandler extends OnAlarmHandler {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = Logger.getLogger(EscalationHandler.class);
	
	private Activity m_activity;
	private String m_activityId;
	private String m_activityName;
	private String m_processDefinitionId;
	private String m_processDefinitionName;
	private String m_processDefinitionVersion;
	private String m_activityPerformer;
	private XPDLWorklistActivity m_xpdlWorklistActivity;
	
	public EscalationHandler(Activity activity, XPDLWorklistActivity xpdlWorklistActivity) {
		super(activity.getLimit(),activity.getWorkflowProcess().getProcessHeader().getDurationUnit());
		m_xpdlWorklistActivity = xpdlWorklistActivity;
		m_activityId = activity.getId();
		m_activityName = activity.getName();
		m_processDefinitionId = activity.getWorkflowProcess().getId();
		m_processDefinitionVersion = activity.getWorkflowProcess().findWorkflowVersion();
		m_processDefinitionName = activity.getWorkflowProcess().getName();
		m_activityPerformer = activity.getPerformer();
	}
	
	
	private static String[] ESCALATION_PROPERTY_NAMES = new String[] {
			"activityId",
			"processDefinitionVersion", "activityName",
			"processDefinitionId", "processDefinitionName", "activityPerformer" };

	public final static String STATE_VARIABLE_NAME = "state";
	public final static String RETRY_PERFORMER_VARIABLE_NAME = "retryPerformer";
	public final static String RETRY_DUE_DATE_VARIABLE_NAME = "retryDueDate";
	
	private String capitalize(String s) {
		if (s.length() > 0) {
			StringBuffer sb = new StringBuffer(s.substring(0, 1).toUpperCase());
			if (s.length() > 1)
				sb.append(s.substring(1));
			return sb.toString();
		} else
			return s;
	}

	private String getVariableName(String propertyName) {
		return "escalation" + capitalize(propertyName);
	}


	
	public String[] getEscalationVariableParameterNames() {
		String[] result = new String[ESCALATION_PROPERTY_NAMES.length];
		for (int i = 0; i < result.length; i++) {
			String propertyName = ESCALATION_PROPERTY_NAMES[i];
			result[i] = getVariableName(propertyName);
		}
		return result;
	}

	public String[] getEscalationProcessParameterNames() {
		String[] result = new String[ESCALATION_PROPERTY_NAMES.length];
		for (int i = 0; i < result.length; i++) {
			String propertyName = ESCALATION_PROPERTY_NAMES[i];
			result[i] = getVariableName(propertyName);
		}
		return result;
	}

	public void registerVariables(Scope scope) {
		// TODO use complex type ....
		for (int i = 0; i < ESCALATION_PROPERTY_NAMES.length; i++) {
			String propertyName = ESCALATION_PROPERTY_NAMES[i];
			scope.addVariable(new BasicVariable(getVariableName(propertyName),
					BasicType.STRING, null));
		}
	}

	public void setScope(Scope scope) {
		super.setScope(scope);
		registerVariables(scope);
	}

	private void setVariable(String propertyName) {
		try {
			String value = (String) PropertyUtils.getSimpleProperty(this,
					propertyName);
			m_scope.getVariable(getVariableName(propertyName)).setValue(value);
		} catch (Exception e) {
			LOGGER.warn("IGNORE", e);
		}
	}

	public void onAlarm() throws EngineException {
		for (int i = 0; i < ESCALATION_PROPERTY_NAMES.length; i++) {
			String propertyName = ESCALATION_PROPERTY_NAMES[i];
			setVariable(propertyName);
		}
		super.onAlarm();
	}

	public String getActivityId() {
		return m_activityId;
	}

	public String getActivityName() {
		return m_activityName;
	}

	public String getActivityPerformer() {
		return m_activityPerformer;
	}

	public String getProcessDefinitionId() {
		return m_processDefinitionId;
	}

	public String getProcessDefinitionName() {
		return m_processDefinitionName;
	}

	public String getProcessDefinitionVersion() {
		return m_processDefinitionVersion;
	}

	public void childSkiped(
			de.objectcode.canyon.bpe.engine.activities.Activity childActivity)
			throws EngineException {
		if (m_scope.getState() == ActivityState.RUNNING)
			m_scope.complete();
	}
	
	public void setAlarmTime(long dueDate) {
		m_currentUntil = dueDate;
	}

	protected void computeAlarmTime() throws EngineException {
    // assume m_currentUntil is set before via setDueDate 	
  }
	
	public String getRetryPerformer() {
		return (String) m_scope.getVariable(RETRY_PERFORMER_VARIABLE_NAME).getValue();
	}
	
	public Date getRetryDueDate() {
		return (Date) m_scope.getVariable(RETRY_DUE_DATE_VARIABLE_NAME).getValue();
	}

	public boolean isRetrying() {
		return "RETRY".equals(m_scope.getVariable(STATE_VARIABLE_NAME).getValue());
	}

	public void setRetryDueDate(Date date) {
		m_scope.getVariable(RETRY_DUE_DATE_VARIABLE_NAME).setValue(date);
	}

	public void activate() throws EngineException {
			if (isRetrying()) {
				if (getRetryDueDate()==null) {
						setRetryDueDate(new Date(getAlarmTime()));
						setAlarmTime(Long.MAX_VALUE);
				} else {
					// check retry time to prevent loops
					long retryDueTime = getRetryDueDate().getTime();
					if (retryDueTime>System.currentTimeMillis())
						setAlarmTime(retryDueTime);
					else
						setAlarmTime(Long.MAX_VALUE);
				}
			} else {
				setAlarmTime(m_xpdlWorklistActivity.computeDueDate());				
			}
		super.activate();
	}
}
