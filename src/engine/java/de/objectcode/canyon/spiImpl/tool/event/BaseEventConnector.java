package de.objectcode.canyon.spiImpl.tool.event;

import java.lang.reflect.Method;
import java.util.HashMap;

import javax.jms.DeliveryMode;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.bpe.util.ExtendedAttributeHelper;
import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.tool.BPEContext;
import de.objectcode.canyon.spi.tool.IToolConnector;
import de.objectcode.canyon.spi.tool.MessageEvent;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.spi.tool.ReturnValue;
import de.objectcode.canyon.worklist.IWorklistEngine;

/**
 * @author junglas
 * @created 6. April 2004
 */
public abstract class BaseEventConnector implements IToolConnector {
	private final static Log log = LogFactory.getLog(BaseEventConnector.class);

	public final static String QUEUE_JNDI_NAME = "queue/WSApplicationEvent";

	public final static String CONNECTION_FACTORY_JNDI_NAME = "ConnectionFactory";

	protected String m_engineId;

	protected String m_userId;

	protected String m_eventType;

	protected Queue m_eventQueue;

	protected QueueConnectionFactory m_queueConnectionFactory;

	protected ServiceManager   m_serviceManager;
	
	protected IWorklistEngine  m_worklistEngine;
	
	protected String m_processId;

	protected String m_action;

	protected String m_multiplicity;

	abstract protected void initQueue(MessageDescriptor messageDescriptor)
			throws RepositoryException;

	public static BaseEventConnector getEventConnector(String engineId,
			Application application) throws RepositoryException {
		ExtendedAttributeHelper eah = new ExtendedAttributeHelper(application
				.getExtendedAttributes());
		if (eah.getOptionalValue("canyon:eventType") != null) {
			boolean isExternal = eah.getOptionalValue("canyon:eventType:external") != null;
			BaseEventConnector connector = null;
			if (isExternal)
				connector = new ExternalEventConnector(engineId);
			else
				connector = new InternalEventConnector(engineId);
			try {
				connector.init(application);
			} catch (Exception e) {
				throw new RepositoryException("Cannot init event connector", e);
			}
			return connector;
		}
		return null;
	}

	protected BaseEventConnector(String engineId) {
		m_engineId = engineId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.objectcode.canyon.spi.tool.IToolConnector#init(de.objectcode.canyon.model.application.Application)
	 */
	public void init(Application application) throws Exception {
		ExtendedAttributeHelper eah = new ExtendedAttributeHelper(application
				.getExtendedAttributes());
		m_userId = eah.getOptionalValue("canyon:userName");
		m_eventType = eah.getOptionalValue("canyon:eventType");
		m_multiplicity = eah.getOptionalValue("canyon:multiplicity");
		if (m_multiplicity==null)
			m_multiplicity = MessageEvent.MULTIPLICITY_ONE;
		// backward compatability
		String startProcessId = eah.getOptionalValue("canyon:startProcessId");
		String terminateProcessId = eah.getOptionalValue("canyon:terminateProcessId");
		if (startProcessId != null) {
			m_processId = startProcessId;
			m_action = MessageEvent.ACTION_START;
		} else if (terminateProcessId != null) {
			m_processId = terminateProcessId;
			m_action = MessageEvent.ACTION_TERMINATE;
		} else {
			m_processId = eah.getOptionalValue("canyon:processId");
			m_action = eah.getOptionalValue("canyon:action");
			if (m_action == null)
				m_action = MessageEvent.ACTION_NOTIFY;
		}
	}

	public static class MessageDescriptor {
		public String fEngineLocator;

		public String fClientId;

		public String fJndiProviderUrl;

		public String fJndiQueueName = QUEUE_JNDI_NAME;

		public String fJndiConnectionFactoryName = CONNECTION_FACTORY_JNDI_NAME;

		public HashMap fEventParams;

		public void setEngineLocator(String engineLocator) {
			fEngineLocator = engineLocator;
			String[] s = engineLocator.split(";");
			fJndiProviderUrl = s[0];
			fClientId = s[1];
			if (s.length > 2 && s[2] != null)
				fJndiQueueName = s[2];
			if (s.length > 3 && s[3] != null)
				fJndiConnectionFactoryName = s[3];
		}
	}

	abstract protected MessageDescriptor extractMessageDescriptor(
			BPEContext context, Parameter[] parameters);

	/**
	 * (non-Javadoc)
	 * 
	 * @param parameters
	 *          Description of the Parameter
	 * @param context
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *              Description of the Exception
	 * @see de.objectcode.canyon.spi.tool.IToolConnector#invoke(de.objectcode.canyon.spi.tool.Parameter[])
	 */
	public ReturnValue[] invoke(BPEContext context, Parameter[] parameters)
			throws Exception {

		MessageDescriptor messageDescriptor = extractMessageDescriptor(context,
				parameters);

		initQueue(messageDescriptor);

		HashMap eventParams = messageDescriptor.fEventParams;

		QueueConnection connection = null;
		QueueSession session = null;

		try {
			connection = m_queueConnectionFactory.createQueueConnection();
			session = connection.createQueueSession(false,
					QueueSession.AUTO_ACKNOWLEDGE);
			connection.start();

			Message msg = session.createObjectMessage(eventParams);

			msg.setStringProperty("engineId", m_engineId);
			msg.setStringProperty("eventType", m_eventType);
			msg.setStringProperty("userId", m_userId);
			msg.setStringProperty("clientId", messageDescriptor.fClientId);
			if (m_processId != null) {
				msg.setStringProperty("processId", m_processId);
			}
			msg.setStringProperty("action", m_action);
			msg.setStringProperty("multiplicity", m_multiplicity);
			msg.setStringProperty("parentProcessInstanceIdPath", context
					.getParentProcessInstanceIdPath());

			if (log.isInfoEnabled()) {
				log.info("Sending from context=" + context + " to engineLocator='"
						+ messageDescriptor.fEngineLocator + "' eventType=" + m_eventType
						+ " processId=" + m_processId + " action="
						+ m_action + " multiplicity=" + m_multiplicity + " " + eventParams);
			}

			buildMessage(context, parameters, msg);
			
			if (MessageEvent.ACTION_NOTIFY.equals(m_action) &&  MessageEvent.MULTIPLICITY_EXACTLY_ONE.equals(m_multiplicity) && m_worklistEngine!=null) {
				MessageEvent event = new MessageEvent(eventParams, m_engineId, m_eventType, m_processId, m_action, m_userId, messageDescriptor.fClientId, context
						.getParentProcessInstanceIdPath());				
				String[] workItemIds = m_worklistEngine.findWorkItemsForEvent(event);
				if (workItemIds == null || workItemIds.length!=1) {
					throw new NoMatchingEventHandlerFoundException(context);
				}
			}

			if (MessageEvent.ACTION_TERMINATE.equals(m_action) &&  (MessageEvent.MULTIPLICITY_EXACTLY_ONE.equals(m_multiplicity) ||  MessageEvent.MULTIPLICITY_NOT_ZERO.equals(m_multiplicity)) && m_serviceManager!=null) {
				MessageEvent event = new MessageEvent(eventParams, m_engineId, m_eventType, m_processId, m_action, m_userId, messageDescriptor.fClientId, context
						.getParentProcessInstanceIdPath());	
				int no = m_serviceManager.getBpeEngine().countTerminateProcessInstances(event);
				if (no == 0) {
					throw new NoMatchingProcessInstanceFoundException(context);
				}
				if (MessageEvent.MULTIPLICITY_EXACTLY_ONE.equals(m_multiplicity) && no >1) {
					throw new TooManyMatchingProcessInstancesFoundException(context,no);
				}
			}
			
			QueueSender sender = session.createSender(m_eventQueue);

			sender.send(msg, DeliveryMode.PERSISTENT, 4, 0L);

			sender.close();
		} finally {
			if (session != null) {
				try {
					connection.stop();
					session.close();
				} catch (Exception e) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
				}
			}
		}

		return new ReturnValue[0];
	}

	/**
	 * @param context
	 * @param parameters
	 * @param msg
	 */
	private void buildMessage(BPEContext context, Parameter[] parameters,
			Message msg) throws Exception {
		ExtendedAttribute messageBuilderAttr = context
				.getExtendedAttribute("canyon:messageBuilderClass");
		if (messageBuilderAttr == null || messageBuilderAttr.getValue() == null)
			return;
		String className = messageBuilderAttr.getValue();
		Class clazz = Class.forName(className);
		Object messageBuilder = clazz.newInstance();
		Method buildMethod = clazz.getMethod("build", new Class[] {
				BPEContext.class, Parameter[].class, Message.class });
		buildMethod.invoke(messageBuilder,
				new Object[] { context, parameters, msg });
	}

	public static class NoMatchingEventHandlerFoundException extends RuntimeException {
		static final long serialVersionUID = -4197161977104401608L;
		
		NoMatchingEventHandlerFoundException(BPEContext context) {
			super("No matching EventListener found:" + context);
		}
	}

	public static class NoMatchingProcessInstanceFoundException extends RuntimeException {
		static final long serialVersionUID = 4042991961769711919L;
		
		NoMatchingProcessInstanceFoundException(BPEContext context) {
			super("No matching ProcessInstance found:" + context);
		}
	}
	
	public static class TooManyMatchingProcessInstancesFoundException extends RuntimeException {
		static final long serialVersionUID = 647955311844249395L;
		
		TooManyMatchingProcessInstancesFoundException(BPEContext context, int no) {
			super("Too many (" + no +") matching ProcessInstances found:" + context);
		}
	}
	
}