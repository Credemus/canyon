package de.objectcode.canyon.bpe.engine.handler;

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.activities.ActivityState;
import de.objectcode.canyon.bpe.engine.activities.Scope;
import de.objectcode.canyon.bpe.engine.variable.BasicVariable;
import de.objectcode.canyon.model.data.BasicType;

/**
 * Handles faults
 * 
 * The name is used as type
 * 
 * @author junglas
 * @created 14. Juni 2004
 */
public class FaultHandler extends BaseHandler {
	static final long serialVersionUID = -7303550676124833490L;

	private static Logger LOGGER = Logger.getLogger(FaultHandler.class);

	protected String m_faultName;

	protected String m_code = "defaultException";

	protected transient boolean m_doNotReactOnDeactivate = false;

	/**
	 * Constructor for the FaultHandler object
	 */
	public FaultHandler() {
		m_faultName = null;
	}

	public FaultHandler(String code) {
		m_code = code;
	}

	/**
	 * Constructor for the FaultHandler object
	 * 
	 * @param faultName
	 *          Description of the Parameter
	 */
	public FaultHandler(String faultName, String code) {
		m_faultName = faultName;
		m_code = code;
	}

	/**
	 * @return Returns the faultName.
	 */
	public String getFaultName() {
		return m_faultName;
	}

	public String getMessageVariableName() {
		return m_code + "_message";
	}

	public String getStackTraceVariableName() {
		return m_code + "_stacktrace";
	}

	public String getTypeVariableName() {
		return m_code + "_type";
	}

	/**
	 * @see de.objectcode.canyon.bpe.engine.activities.IActivityContainer#isNonBlocked()
	 */
	public boolean isNonBlocked() {
		return false;
	}

	/**
	 * @return The elementName value
	 * @see de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
	 */
	public String getElementName() {
		return m_faultName == null ? "catchAll" : "catch";
	}

	private String getVariableName(String propertyName) {
		return m_code + "_" + propertyName;
	}

	private void setVariable(Fault fault, String propertyName) {
		try {
			String value = (String) PropertyUtils.getSimpleProperty(fault,
					propertyName);
			m_scope.getVariable(getVariableName(propertyName)).setValue(value);
		} catch (Exception e) {
			LOGGER.warn("IGNORE", e);
		}
	}

	public void fire(Fault fault) throws EngineException {
		for (int i = 0; i < FAULT_PROPERTY_NAMES.length; i++) {
			String propertyName = FAULT_PROPERTY_NAMES[i];
			setVariable(fault, propertyName);
		}
//		m_scope.getVariable(getMessageVariableName()).setValue(fault.getMessage());
//		m_scope.getVariable(getStackTraceVariableName()).setValue(
//				fault.getStackTrace());
//		m_scope.getVariable(getTypeVariableName()).setValue(fault.getName());
		m_activity.activate();

		m_doNotReactOnDeactivate = true;
		m_scope.handlerFired();
		m_doNotReactOnDeactivate = false;
	}

	public void deactivate() throws EngineException {
		if (!m_doNotReactOnDeactivate) {
			if (m_activity.getState() == ActivityState.OPEN) {
				m_activity.deactivate();
			}
		}
	}

	public void activate() throws EngineException {
		if (m_activity.getState() == ActivityState.COMPLETED
				|| m_activity.getState() == ActivityState.ABORT
				|| m_activity.getState() == ActivityState.SKIPED)
			m_activity.reopen();
	}

	/**
	 * @param element
	 *          Description of the Parameter
	 * @see de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
	 */
	public void toDom(Element element) {
		super.toDom(element);

		if (m_faultName != null) {
			element.addAttribute("faultName", m_faultName);
		}
	}

	public void childSkiped(Activity childActivity) throws EngineException {
		// This does not work for OnAlarmHandler so we do it here
		// TODO yx REFACTOR
		if (m_scope.getState() == ActivityState.RUNNING) // FaultHandler ob
			// BPEProcess is
			// different. PROCESS is
			// already completeted
			// see Tool3Test
			m_scope.complete();
	}

	private static String[] FAULT_PROPERTY_NAMES = new String[] { "stackTrace",
			"activityId", "processInstanceIdPath", "processDefinitionVersion",
			"message", "activityName", "processDefinitionId",
			"processDefinitionName", "name" };


	private String capitalize(String s) {
		if (s.length()>0) {
			StringBuffer sb = new StringBuffer(s.substring(0,1).toUpperCase());
			if (s.length()>1)
				sb.append(s.substring(1));
			return sb.toString();
		}	else
			return s;
	}
	
	public String[] getFaultVariableParameterNames() {
		String[] result = new String[FAULT_PROPERTY_NAMES.length];
		for (int i = 0; i < result.length; i++) {
			String propertyName = FAULT_PROPERTY_NAMES[i];
			result[i] = getVariableName(propertyName);
		}
		return result;		
	}
	
	public String[] getFaultProcessParameterNames() {
		String[] result = new String[FAULT_PROPERTY_NAMES.length];
		for (int i = 0; i < result.length; i++) {
			String propertyName = FAULT_PROPERTY_NAMES[i];
			result[i] = "fault" + capitalize(propertyName);
		}
		return result;
	}
	
	public void registerVariables(Scope scope) {
		// TODO use complex type ....
		for (int i = 0; i < FAULT_PROPERTY_NAMES.length; i++) {
			String propertyName = FAULT_PROPERTY_NAMES[i];
			scope.addVariable(new BasicVariable(getVariableName(propertyName),
					BasicType.STRING, null));
		}
	}

	public String getCode() {
		return m_code;
	}

	public void setScope(Scope scope) {
		super.setScope(scope);
    registerVariables(scope);        		
	}
	
	public static void main(String[] args) {
		try {
			PropertyDescriptor[] pd = PropertyUtils
					.getPropertyDescriptors(Fault.class);
			for (int i = 0; i < pd.length; i++) {
				PropertyDescriptor descriptor = pd[i];
				if (descriptor.getReadMethod() != null) {
					System.out.println(descriptor.getName());
					System.out.println(descriptor.getDisplayName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
