package de.objectcode.canyon.bpe.engine.activities;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.BPERuntimeContext;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.ExtensibleElement;
import de.objectcode.canyon.bpe.engine.InvalidStateException;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.evaluator.IJoinCondition;
import de.objectcode.canyon.bpe.engine.handler.Fault;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.bpe.util.IStateHolder;
import de.objectcode.canyon.spi.RepositoryException;

/**
 * Abstract base class of all activities.
 * 
 * @author junglas
 * @created 7. Juni 2004
 */
public abstract class Activity extends ExtensibleElement implements
		IStateHolder {
	static final long serialVersionUID = -8224445822985940497L;

	private final static Log log = LogFactory.getLog(Activity.class);

	protected ActivityState m_state;

	protected IJoinCondition m_joinCondition;

	protected IActivityContainer m_parentActivity;

	protected Scope m_scope;

	protected String m_name;

	protected String m_id;

	protected Map<String, Link> m_incomingLinks;

	protected Map<String, Link> m_outgoingLinks;

	/**
	 * Constructor for the Activity object
	 * 
	 * @param name
	 *          Description of the Parameter
	 * @param scope
	 *          Description of the Parameter
	 */
	protected Activity(String name, Scope scope) {
		this(null, name, scope);
	}

	protected Activity(String id, String name, Scope scope) {
		m_id = id;
		m_name = name;
		m_scope = scope;

		m_state = ActivityState.OPEN;

		m_incomingLinks = new LinkedHashMap<String, Link>();
		m_outgoingLinks = new LinkedHashMap<String, Link>();

		if (m_scope != null) {
			m_scope.register(this);
		}
	}

	/**
	 * @param id
	 *          The id to set.
	 */
	void setId(String id) {
		m_id = id;
	}

	/**
	 * @param parentActivity
	 *          The parentActivity to set.
	 */
	public void setParentActivity(IActivityContainer parentActivity) {
		m_parentActivity = parentActivity;
		if (parentActivity.getScope() != null)
			setScope(parentActivity.getScope());
	}

	public void setScope(Scope scope) {
		m_scope = scope;
	}

	/**
	 * @param joinCondition
	 *          The joinCondition to set.
	 */
	public void setJoinCondition(IJoinCondition joinCondition) {
		m_joinCondition = joinCondition;
	}

	/**
	 * @param state
	 *          The state to set.
	 */
	public void setState(ActivityState state) {
		m_state = state;
	}

	/**
	 * @return Returns the scope.
	 */
	public Scope getScope() {
		return m_scope;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return m_id;
	}

	/**
	 * @return Returns the parentActivity.
	 */
	public IActivityContainer getParentActivity() {
		return m_parentActivity;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * @return Returns the joinCondition.
	 */
	public IJoinCondition getJoinCondition() {
		return m_joinCondition;
	}

	/**
	 * @return Returns the state.
	 */
	public ActivityState getState() {
		return m_state;
	}

	/**
	 * Gets the variables attribute of the Activity object
	 * 
	 * @return The variables value
	 */
	public IVariable[] getVariables() {
		return m_scope.getVariables();
	}

	/**
	 * Gets a process/scope variable.
	 * 
	 * @param name
	 *          The name of the variable
	 * @return The variable value
	 */
	public IVariable getVariable(String name) {
		return m_scope.getVariable(name);
	}

	/**
	 * Gets the engine attribute of the Activity object
	 * 
	 * @return The engine value
	 */
	public BPEEngine getEngine() {
		return m_scope.getProcess().getEngine();
	}

	/**
	 * Gets the process attribute of the Activity object
	 * 
	 * @return The process value
	 */
	public BPEProcess getProcess() {
		return m_scope.getProcess();
	}

	/**
	 * Get an incoming link to this activity by name.
	 * 
	 * @param name
	 * @return
	 */
	public Link getIncomingLink(String name) {
		return m_incomingLinks.get(name);
	}

	/**
	 * Gets the incomingLinks attribute of the Activity object
	 * 
	 * @return The incomingLinks value
	 */
	public Link[] getIncomingLinks() {
		Link[] ret = new Link[m_incomingLinks.size()];

		m_incomingLinks.values().toArray(ret);

		return ret;
	}

	private Link[] getIncomingLinksCascading() {
		List<Link> incomingLinks = new ArrayList<Link>(m_incomingLinks.values());
		IActivityContainer parent = getParentActivity();
		while (parent != null) {
			if (parent instanceof Activity) {
				incomingLinks.addAll(Arrays.asList(((Activity) parent)
						.getIncomingLinks()));
			}
			parent = parent.getParentActivity();
		}
		Link[] result = new Link[incomingLinks.size()];
		incomingLinks.toArray(result);
		return result;
	}

	/**
	 * @return Returns the outgoingLinks.
	 */
	public Link[] getOutgoingLinks() {
		Link[] ret = new Link[m_outgoingLinks.size()];

		m_outgoingLinks.values().toArray(ret);

		return ret;
	}

	/**
	 * Gets the outgoingLink attribute of the Activity object
	 * 
	 * @param name
	 *          Description of the Parameter
	 * @return The outgoingLink value
	 */
	public Link getOutgoingLink(String name) {
		return m_outgoingLinks.get(name);
	}

	/**
	 * Adds a feature to the IncomingLink attribute of the Activity object
	 * 
	 * @param linkName
	 *          The feature to be added to the Link attribute
	 * @param toActivity
	 *          The feature to be added to the Link attribute
	 * @return Description of the Return Value
	 */
	public Link addLink(String linkName, Activity toActivity) {
		Link link = new Link(linkName, this, toActivity);

		toActivity.m_incomingLinks.put(link.getName(), link);
		m_outgoingLinks.put(link.getName(), link);

		return link;
	}

	/**
	 * Check if the activity has incoming links.
	 * 
	 * @return Description of the Return Value
	 * @throws EngineException
	 */
	public boolean hasIncomingLinks() {
		return !m_incomingLinks.isEmpty();
	}

	/**
	 * Activate the activity.
	 * 
	 * @throws EngineException
	 */
	public void activate() throws EngineException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("Activating Activity " + m_id + " '" + m_name
						+ "' of process '" + m_scope.getProcess().getProcessInstanceId()
						+ "'");
			}

			if (m_state != ActivityState.OPEN) {
				throw new InvalidStateException("Trying to activate activity " + m_id
						+ " ('" + m_name + "') of process '"
						+ m_scope.getProcess().getProcessInstanceId()
						+ "' that is not open");
			}

			// Set all incoming links to false to ensure that the activity is not
			// activated by an old link

			for (Link l : m_incomingLinks.values()) {
				l.setState(LinkState.FALSE);
			}

			m_state = ActivityState.ACTIVATED;

			getEngine().getEventHub().fireActivityStateEvent(this, m_state);
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * Deactivate the activity.
	 * 
	 * @throws EngineException
	 */
	public void deactivate() throws EngineException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("Deactivating Activity " + m_id + " '" + m_name
						+ "' of process '" + m_scope.getProcess().getProcessInstanceId()
						+ "'");
			}

			if (m_state != ActivityState.OPEN) {
				throw new InvalidStateException("Trying to deactivate activity " + m_id
						+ " ('" + m_name + "') of process '"
						+ m_scope.getProcess().getProcessInstanceId()
						+ "' that is not open");
			}

			// Set all incoming links to false to ensure that the activity is not
			// activated by an old link

			for (Link l : m_incomingLinks.values()) {
				l.setState(LinkState.FALSE);
			}

			m_state = ActivityState.DEACTIVATED;

			getEngine().getEventHub().fireActivityStateEvent(this, m_state);
		} catch (Exception e) {
			handleException(e);
		}

	}

	/**
	 * Start the activity.
	 * 
	 * @exception EngineException
	 *              Description of the Exception
	 */
	public void start() throws EngineException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("Starting Activity " + m_id + " '" + m_name
						+ "' of process '" + m_scope.getProcess().getProcessInstanceId()
						+ "'");
			}

			if (m_state != ActivityState.ACTIVATED) {
				throw new InvalidStateException("Trying to start activity " + m_id
						+ " ('" + m_name + "') of process '"
						+ m_scope.getProcess().getProcessInstanceId()
						+ "' that is not activated");
			}

			m_state = ActivityState.RUNNING;

			getEngine().getEventHub().fireActivityStateEvent(this, m_state);
		} catch (Exception e) {
			handleException(e);
		}

	}

	/**
	 * Complete the activity.
	 * 
	 * @exception EngineException
	 *              Description of the Exception
	 */
	public void complete() throws EngineException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("Completing Activity " + m_id + " '" + m_name
						+ "' of process '" + m_scope.getProcess().getProcessInstanceId()
						+ "'");
			}

			if (m_state != ActivityState.RUNNING) {
				throw new InvalidStateException("Trying to complete activity " + m_id
						+ " ('" + m_name + "') of process '"
						+ m_scope.getProcess().getProcessInstanceId()
						+ "' that is not running");
			}

			m_state = ActivityState.COMPLETED;

			getEngine().getEventHub().fireActivityStateEvent(this, m_state);

			for (Link link : m_outgoingLinks.values()) {
				link.fire();
			}

			if (m_parentActivity != null) {
				m_parentActivity.childCompleted(this);
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @exception EngineException
	 *              Description of the Exception
	 */
	public void abort() throws EngineException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("Aborting Activity " + m_id + " '" + m_name
						+ "' of process '" + m_scope.getProcess().getProcessInstanceId()
						+ "'");
			}

			if (m_state != ActivityState.RUNNING) {
				throw new InvalidStateException("Trying to abort activity " + m_id
						+ " ('" + m_name + "') of process '"
						+ m_scope.getProcess().getProcessInstanceId()
						+ "' that is not running");
			}
			m_state = ActivityState.ABORT;

			getEngine().getEventHub().fireActivityStateEvent(this, m_state);

			for (Link link : m_outgoingLinks.values()) {
				link.fail();
			}

			if (m_parentActivity != null) {
				m_parentActivity.childAborted(this);
			}
		} catch (Exception e) {
			handleException(e);
		}

	}

	public void terminate() throws EngineException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("Terminating Activity " + m_id + " '" + m_name
						+ "' of process '" + m_scope.getProcess().getProcessInstanceId()
						+ "'");
			}

			m_state = ActivityState.ABORT;

			getEngine().getEventHub().fireActivityStateEvent(this, m_state);

		} catch (Exception e) {
			handleException(e);
		}

	}

	/**
	 * Description of the Method
	 * 
	 * @exception EngineException
	 *              Description of the Exception
	 */
	public void skip() throws EngineException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("Skiping Activity " + m_id + " '" + m_name + "' of process '"
						+ m_scope.getProcess().getProcessInstanceId() + "'");
			}

			if (m_state != ActivityState.DEACTIVATED) {
				throw new InvalidStateException("Trying to skip activity " + m_id
						+ " ('" + m_name + "') of process '"
						+ m_scope.getProcess().getProcessInstanceId()
						+ "' that is not deactivated");
			}

			m_state = ActivityState.SKIPED;

			getEngine().getEventHub().fireActivityStateEvent(this, m_state);

			for (Link link : m_outgoingLinks.values()) {
				link.fail();
			}

			if (m_parentActivity != null) {
				m_parentActivity.childSkiped(this);
			}
		} catch (Exception e) {
			handleException(e);
		}

	}

	/**
	 * Reset the activity. I.e. reopen the activity.
	 * 
	 * @throws EngineException
	 */
	public void reopen() throws EngineException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("Reopening Activity " + m_id + " '" + m_name
						+ "' of process '" + m_scope.getProcess().getProcessInstanceId()
						+ "'");
			}

			if (m_state != ActivityState.COMPLETED && m_state != ActivityState.ABORT
					&& m_state != ActivityState.SKIPED) {
				throw new InvalidStateException("Trying to reopen activity " + m_id
						+ " ('" + m_name + "') of process '"
						+ m_scope.getProcess().getProcessInstanceId()
						+ "' that is neither completed nor aborted");
			}
			m_state = ActivityState.OPEN;

			getEngine().getEventHub().fireActivityStateEvent(this, m_state);

			for (Link link : m_outgoingLinks.values()) {
				link.reset();
			}
		} catch (Exception e) {
			handleException(e);
		}

	}

	private void enforceActivation() throws EngineException {
		if (m_state == ActivityState.RUNNING) {
			abort();
		}
		if (m_state == ActivityState.COMPLETED || m_state == ActivityState.ABORT
				|| m_state == ActivityState.SKIPED) {
			reopen();
		}
		activate();
	}

	private void enforceDeactivation() throws EngineException {
	}

	/**
	 * Description of the Method
	 * 
	 * @param link
	 *          Description of the Parameter
	 * @exception EngineException
	 *              Description of the Exception
	 */
	protected void linkStateChanged(Link link) throws EngineException {
		if (log.isDebugEnabled()) {
			log.debug("linkStateChanged: " + link.getName() + " " + link.getState()
					+ " on Activity " + m_id + " '" + m_name + "' of process '"
					+ m_scope.getProcess().getProcessInstanceId() + "'");
		}

		Link[] incomingLinks = getIncomingLinksCascading();
		for (int i = 0; i < incomingLinks.length; i++) {
			Link l = incomingLinks[i];

			if (l.getState() == LinkState.UNKNOWN) {
				if (m_state == ActivityState.OPEN && m_parentActivity.isNonBlocked()) {
					if (!checkLoop(l, new HashSet())) {
						return;
					} else {
						l.setState(LinkState.FALSE);
					}
				} else {
					// Do nothing if there are incoming links with unknown state
					return;
				}
			}
		}

		// todo check: do we have to cascade here to?
		if (m_joinCondition == null) {
			boolean state = false;
			Iterator it = m_incomingLinks.values().iterator();

			// implicit XOR
			while (it.hasNext()) {
				Link l = (Link) it.next();

				if (l.getState() == LinkState.TRUE) {
					state = true;
					break;
				}
			}

			if (state) {
				enforceActivation();
			} else {
				if (m_state == ActivityState.OPEN) {
					deactivate();
				}
			}
		} else {
			if (m_joinCondition.eval(this)) {
				enforceActivation();
			} else {
				if (m_state == ActivityState.OPEN) {
					deactivate();
				}
			}
		}
	}

	protected boolean hasLooped(Activity activity) {
		if (activity == this) {
			return true;
		}

		// todo ugly casting but IActivityContainer ist not always a Activity
		if (activity.getParentActivity() != null)
			return hasLooped(activity.getParentActivity());

		return false;
	}

	protected boolean hasLooped(IActivityContainer activityContainer) {
		if (activityContainer == this) {
			return true;
		}

		// todo ugly casting but IActivityContainer ist not always a Activity
		if (activityContainer.getParentActivity() != null)
			return hasLooped(activityContainer.getParentActivity());

		return false;
	}

	/**
	 * Description of the Method
	 * 
	 * @param l
	 *          Description of the Parameter
	 * @param activities
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected boolean checkLoop(Link l, Set<Activity> activities) {
		Activity activity = l.getSource();

		if (hasLooped(activity)) {
			return true;
		}

		// todo do we have to check contains cascading?
		if (!activities.contains(activity)) {
			Link[] links = activity.getIncomingLinksCascading();
			int i;

			activities.add(activity);
			for (i = 0; i < links.length; i++) {
				if (links[i].getState() == LinkState.UNKNOWN
						&& checkLoop(links[i], activities)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Description of the Method
	 * 
	 * @param element
	 *          Description of the Parameter
	 */
	public void toDom(Element element) {
		element.addAttribute("id", m_id);
		element.addAttribute("name", m_name);
		element.addAttribute("state", m_state.getTag());

		if (!m_incomingLinks.isEmpty()) {
			Element incomingLinks = element.addElement("incoming-links");

			for (Link link : m_incomingLinks.values()) {
				link.toDom(incomingLinks.addElement(link.getElementName()));
			}
		}

		if (!m_outgoingLinks.isEmpty()) {
			Element outgoingLinks = element.addElement("outgoing-links");

			for (Link link : m_outgoingLinks.values()) {
				link.toDom(outgoingLinks.addElement(link.getElementName()));
			}
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param out
	 *          Description of the Parameter
	 * @exception IOException
	 *              Description of the Exception
	 */
	public void dehydrate(HydrationContext context, ObjectOutput out)
			throws IOException {
		if (context.getSchema() == HydrationContext.CLASSIC_SCHEMA) {
			out.writeUTF(m_id);
			out.writeInt(m_state.getValue());
		} else {
			out.writeByte(m_state.getValue());
		}

		for (Link link : m_outgoingLinks.values()) {
			link.dehydrate(context, out);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param in
	 *          Description of the Parameter
	 * @exception IOException
	 *              Description of the Exception
	 */
	public void hydrate(HydrationContext context, ObjectInput in)
			throws IOException {
		if (context.getSchema() == HydrationContext.CLASSIC_SCHEMA) {
			String id = in.readUTF();

			if (!m_id.equals(id)) {
				throw new IOException("Activity id does not match: " + id + " " + m_id);
			}

			m_state = ActivityState.fromInt(in.readInt());
		} else {
			m_state = ActivityState.fromInt(in.readByte());
		}

		for (Link link : m_outgoingLinks.values()) {
			link.hydrate(context, in);
		}
	}

	protected String getBaseDiagnosticInfo() {
		StringBuffer buffy = new StringBuffer();
		buffy.append("id=").append(getId());
		buffy.append(", name=").append(getName());
		buffy.append(", piid=").append(getProcess().getProcessInstanceId());
		return buffy.toString();
	}

	protected String getAdditionalDiagnosticInfo() {
		return "";
	}

	protected String getDiagnosticInfo() {
		StringBuffer buffy = new StringBuffer();
		buffy.append("[").append(getBaseDiagnosticInfo());
		String add = getAdditionalDiagnosticInfo();
		if (add.length() > 0)
			buffy.append(", ").append(add);
		buffy.append("]");
		return buffy.toString();
	}

	private String startExceptionProcessInstance(String clientId,
			String processId, String parentProcessInstanceIdPath, HashMap parameters)
			throws WMWorkflowException, RepositoryException, EngineException {
		String processInstanceId = null;

		ComplexType type = new ComplexType("flow-request");
		Message message = new Message(processId + "-init", type);
		Iterator parameterNames = parameters.keySet().iterator();
		while (parameterNames.hasNext()) {
			String parameterName = (String) parameterNames.next();
			Object parameterValue = parameters.get(parameterName);
			message.getContent().set(parameterName, parameterValue);
		}
		if (parentProcessInstanceIdPath != null)
			message.getContent().set("parentProcessIdPath",
					parentProcessInstanceIdPath);

		processInstanceId = getEngine().handleMessage(
				new BPERuntimeContext(null, clientId), message);

		return processInstanceId;
	}

	protected void handleException(Exception e) throws EngineException {
		StringBuffer message = new StringBuffer();
		message.append("Exception in activity [");
		message.append("id='").append(getId());
		message.append("', name='").append(getName());
		message.append("', state=").append(getState());
		message.append(" of process [");
		message.append("id='").append(getProcess().getId());
		message.append("', piid=").append(getProcess().getProcessInstanceId());
		message.append(", ppiidp=").append(
				getProcess().getParentProcessInstanceIdPath());
		message.append("]");
		log.error(message.toString(), e);

		// TODO somehow this leeds to Stackoverflows ... 

    // jdiller / 06.12.2007 10:47:54
    //
    // This block in commented, to fix a special problem from Oerlinghausen.
    // The JBoss will not start because of permanent rethrowen exception

    // try {
		// 	
		// 	String[] path = null;
		// 	if (getProcess().getParentProcessInstanceIdPath()!=null)
		// 		path= getProcess().getParentProcessInstanceIdPath().split("_");
		// 	
		// 	// try to avoid an infinite loop.
		// 	// TODO Find a smarter loop detection
		// 	if (path == null || path.length < 20) {
		// 		Fault fault = new Fault(e, this);
		// 		if (m_scope != null) {
		// 			m_scope.throwFault(fault);
		// 			return;
		// 		}
		// 	} else {
		// 		log.error("Infinite loop while handling fault:", e);
		// 	}
		// } catch (Exception ee) {
		// 	log.error("ERROR while handling fault:", e);
		// 	throw new EngineException("Cannot throw fault", ee);
		// }

		if (e instanceof EngineException)
			throw (EngineException) e;
		else
			throw new EngineException(e);
	}

}