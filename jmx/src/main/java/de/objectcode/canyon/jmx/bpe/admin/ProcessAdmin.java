package de.objectcode.canyon.jmx.bpe.admin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.jms.DeliveryMode;
//import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession; 
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import de.objectcode.canyon.api.bpe.BPEProcessHome;
import de.objectcode.canyon.api.worklist.ProcessInstanceData;
import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.BPERuntimeContext;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.engine.variable.ComplexValue;
import de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor;
import de.objectcode.canyon.bpe.repository.IProcessRepository;
import de.objectcode.canyon.bpe.repository.IProcessVisitor;
import de.objectcode.canyon.bpe.repository.ProcessInstance;
import de.objectcode.canyon.bpe.util.DomSerializer;
import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.jmx.admin.BaseAdmin;
import de.objectcode.canyon.persistent.bpe.repository.PBPEProcessInstance;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spiImpl.event.LoggingBPEEventListener;

import de.objectcode.canyon.api.worklist.ProcessInstanceData.Attribute;
/**
 * @jmx.mbean name="canyon.bpe:service=ProcessAdmin" description="Process
 *            administration bean"
 * @jboss.service servicefile="jboss"
 * @jboss.depends object-name="canyon:service=ServiceManager"
 * @jboss.depends object-name="canyon.bpe:service=BPEEngineService"
 * @jboss.xmbean
 * 
 * @author junglas
 * @created 14. Juli 2004
 */
public class ProcessAdmin extends BaseAdmin implements ProcessAdminMBean {
	private final static Log log = LogFactory.getLog(ProcessAdmin.class);

	private String m_bpeJndiName = "java:/canyon/BPEEngine";

	private boolean m_started;

	private BPEEngine m_bpeEngine;

	/**
	 * @jmx.managed-attribute access="read-write" description="JNDI name of the
	 *                        ServiceManager"
	 * 
	 * @param jndiName
	 *          The new jndiName value
	 * @exception Exception
	 *              Description of the Exception
	 * @see de.neutrasoft.saints.core.obe.mbean.OBEServiceManagerMBean#setJndiName(java.lang.String)
	 */
	public void setBPEJndiName(String jndiName) throws Exception {
		m_bpeJndiName = jndiName;
	}

	/**
	 * @jmx.managed-attribute access="read-write" description="JNDI name of the
	 *                        async request queue"
	 * 
	 * @return The jndiName value
	 * @see de.neutrasoft.saints.core.obe.mbean.OBEServiceManagerMBean#getJndiName()
	 */
	public String getBPEJndiName() {
		return m_bpeJndiName;
	}

	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation
	 * 
	 * @exception Exception
	 *              Description of the Exception
	 */
	public void start() throws Exception {
		log.info("Starting ProcessAdmin");

		InitialContext ctx = new InitialContext();

		m_svcMgr = (ServiceManager) ctx.lookup(m_jndiName);
		m_bpeEngine = (BPEEngine) ctx.lookup(m_bpeJndiName);

		m_started = true;
	}

	/**
	 * @jmx.managed-operation
	 * 
	 * @exception Exception
	 *              Description of the Exception
	 * @see de.neutrasoft.saints.core.obe.mbean.OBEServiceManagerMBean#stop()
	 */
	public void stop() throws Exception {
		log.info("Stopping ProcessAdmin");
		m_svcMgr = null;
		m_bpeEngine = null;
		m_started = false;
	}

	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation description="Show the status of the bpe engine"
	 * 
	 * @return Description of the Return Value
	 * @exception Exception
	 *              Description of the Exception
	 */
	public String engineStatus() throws Exception {
		try {
			StringBuffer buffer = new StringBuffer();

			buffer.append("<h1>Message Operations</h1>");

			buffer.append("<table>");
			buffer.append("<tr><th>Message Operation</th><th>Process Id</th></tr>");

			Map processIdByMessageOperation = m_bpeEngine
					.getProcessIdByMessageOperation();
			Iterator it = processIdByMessageOperation.keySet().iterator();

			while (it.hasNext()) {
				String messageOperation = (String) it.next();
				String processId = (String) processIdByMessageOperation
						.get(messageOperation);

				buffer.append("<tr><td>").append(messageOperation).append("</td><td>")
						.append(processId).append("</td></tr>");
			}

			buffer.append("</table>");
			buffer.append("<h1>Alarms</h1>");
			buffer.append("<table>");
			buffer.append("<tr><th>Alarm</th><th>Process Id </th></tr>");

			SortedMap alarms = m_bpeEngine.getAlarms();
			it = alarms.keySet().iterator();

			while (it.hasNext()) {
				Long time = (Long) it.next();
				List alarmList = (List) alarms.get(time);

				Iterator alarmIt = alarmList.iterator();

				while (alarmIt.hasNext()) {
					String processId = (String) alarmIt.next();

					buffer.append("<tr><td>").append(new Date(time.longValue())).append(
							"</td><td>").append(processId).append("</td></tr>");
				}
			}

			buffer.append("</table>");

			return buffer.toString();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation description="List the process definitions"
	 * 
	 * @return Description of the Return Value
	 * @exception Exception
	 *              Description of the Exception
	 */
	public String listProcesses() throws Exception {
		try {
			beginTransaction();

			StringBuffer buffer = new StringBuffer();

			buffer.append("<h1>Process definitions</h1>");
			buffer.append("<table>");
			buffer.append("<tr><th>Id</th><th>Name</th></tr>");

			ProcessVisitor visitor = new ProcessVisitor(buffer);

			m_bpeEngine.getProcessRepository().iterateProcesses(visitor, false);

			buffer.append("</table>");

			return buffer.toString();
		} finally {
			endTransaction();
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation description="Show details of a process"
	 * @jmx.managed-parameter name="processId" type="java.lang.String"
	 *                        description="Process id"
	 * 
	 * @param processId
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *              Description of the Exception
	 */
	public String showProcessXML(String processId) throws Exception {
		try {
			beginTransaction();
			BPEProcess process = m_bpeEngine.getProcessRepository().getProcess(
					processId);

			StringWriter writer = new StringWriter();
			XMLWriter xmlWriter = new XMLWriter(writer, new OutputFormat("  ", true));

			xmlWriter.write(DomSerializer.toString(process));

			xmlWriter.close();

			return writer.toString();
		} catch (Exception e) {
			log.error("Exception", e);
			throw e;
		} finally {
			endTransaction();
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation description="List the process instances"
	 * @jmx.managed-parameter name="onlyOpen" type="boolean" description="Only
	 *                        open running instances"
	 * 
	 * @param onlyOpen
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *              Description of the Exception
	 */
	public String listProcessInstances(boolean onlyOpen) throws Exception {
		try {
			beginTransaction();
			StringBuffer buffer = new StringBuffer();

			buffer.append("<h1>Process Instances</h1>");
			buffer.append("<table>");
			buffer
					.append("<tr><th>Process Instance Id</th><th>Process Id</th><th>State</th></tr>");

			ProcessInstanceVisitor visitor = new ProcessInstanceVisitor(buffer);

			m_bpeEngine.getProcessInstanceRepository().iterateProcessInstances(
					onlyOpen, visitor);

			buffer.append("</table>");

			return buffer.toString();
		} finally {
			endTransaction();
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation description="Migrate the hydration schema of process
	 *                        instances"
	 * 
	 * @return Description of the Return Value
	 * @exception Exception
	 *              Description of the Exception
	 */
	public String migrateProcessInstances() throws Exception {
		StringBuffer buffer = new StringBuffer();
		Context ctx = new InitialContext();
		SessionFactory factory = (SessionFactory) ctx
				.lookup("java:/canyon/HibernateFactory");

		ArrayList idList = new ArrayList();
		Session session = null;
		beginTransaction();
		try {
			session = factory.openSession();

			Query query = session.createQuery("from o in class "
					+ PBPEProcessInstance.class);

			Iterator it = query.iterate();

			while (it.hasNext()) {
				PBPEProcessInstance processInstance = (PBPEProcessInstance) it.next();

				ProcessInstance pi = processInstance.getProcessInstance();
				idList.add(new Long(processInstance.getEntityOid()));
				session.evict(processInstance);
			}
		} catch (HibernateException e) {
			log.error("Exception", e);
			throw new RepositoryException(e);
		} finally {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
		endTransaction();
		int counter = 0;
		buffer.append("<h1>Migrate Process Instances</h1>");
		buffer.append("<table>");
		Iterator iterator = idList.iterator();
		ProcessInstanceMigrationVisitor visitor = new ProcessInstanceMigrationVisitor(
				m_bpeEngine.getProcessRepository(), buffer,
				HydrationContext.COMPRESSED_SCHEMA, HydrationContext.CLASSIC_SCHEMA);

		while (iterator.hasNext()) {
			long start = System.currentTimeMillis();
			long oid = ((Long) iterator.next()).longValue();
			beginTransaction();
			try {
				try {
					session = factory.openSession();
					PBPEProcessInstance processInstance = (PBPEProcessInstance) session
							.load(PBPEProcessInstance.class, new Long(oid));

					ProcessInstance pi = processInstance.getProcessInstance();
					visitor.visit(pi);
					processInstance.migrate(pi);

					session.update(processInstance);
					session.flush();
				} catch (HibernateException e) {
					log.error("Exception", e);
				} finally {
					try {
						session.close();
					} catch (Exception e) {
					}
				}
			} finally {
				endTransaction();
			}
			long stop = System.currentTimeMillis();
			log.info("Migratied #" + (++counter) + "/" + idList.size() + ": "
					+ (stop - start) + " ms");
		}


		buffer.append("</table>");
		buffer.append("OldTotalSize=").append(visitor.getOldTotalSize()).append(
				"<BR/>");
		buffer.append("NewTotalSize=").append(visitor.getNewTotalSize()).append(
				"<BR/>");
		buffer.append("Shrink=").append(
				visitor.getOldTotalSize() / (visitor.getNewTotalSize()!=0?visitor.getNewTotalSize():1) * 100.0).append(
				"<BR/>");

		return buffer.toString();
	}

	// url.openStream()


	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation description="Show details of a process instance"
	 * @jmx.managed-parameter name="processInstanceId" type="java.lang.String"
	 *                        description="Process instance id"
	 * 
	 * @param processInstanceId
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *              Description of the Exception
	 */
	public String showProcessInstance(String processInstanceId) throws Exception {
		try {
			return showProcessInstance(processInstanceId, getClass().getResource(
					"process-status.xsl"));
		} catch (Exception e) {
			log.error("Exception", e);
			throw e;
		} finally {
			endTransaction();
		}
	}


	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation description="Show details of a process instance"
	 * @jmx.managed-parameter name="processInstanceId" type="java.lang.String"
	 *                        description="Process instance id"
	 * 
	 * @param processInstanceId
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *              Description of the Exception
	 */
	public String showProcessInstanceXML(String processInstanceId) throws Exception {
		try {
			beginTransaction();

			BPEProcess process = m_bpeEngine.getProcessInstance(processInstanceId);

			StringWriter writer = new StringWriter();
			XMLWriter xmlWriter = new XMLWriter(writer, new OutputFormat("  ", true));
			xmlWriter.write(DomSerializer.toString(process));
			xmlWriter.close();
			return writer.toString();
		} catch (Exception e) {
			log.error("Exception", e);
			throw e;
		} finally {
			endTransaction();
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation description="Send Message"
	 * @jmx.managed-parameter name="userId" type="java.lang.String"
	 *                        description="User id"
	 * @jmx.managed-parameter name="clientId" type="java.lang.String"
	 *                        description="Client id"
	 * @jmx.managed-parameter name="messageOperation" type="java.lang.String"
	 *                        description="Message Operation"
	 * 
	 * @param messageOperation
	 *          Description of the Parameter
	 * @param userId
	 *          Description of the Parameter
	 * @param clientId
	 *          Description of the Parameter
	 * @exception Exception
	 *              Description of the Exception
	 */
	public void sendMessage(String userId, String clientId,
			String messageOperation) throws Exception {
		try {
			beginTransaction();

			Message message = new Message(messageOperation, new ComplexType("empty"));

			m_bpeEngine.handleMessage(new BPERuntimeContext(userId, clientId),
					message);
		} finally {
			endTransaction();
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation description="Send Message"
	 * @jmx.managed-parameter name="messageXML" type="java.lang.String"
	 *                        description="XML data of the message"
	 * 
	 * @param messageXML
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *              Description of the Exception
	 */
	public String sendMessageXML(String messageXML) throws Exception {
		SAXReader reader = new SAXReader();

		Document doc = reader.read(new StringReader(messageXML));

		Element messageElement = doc.getRootElement();
		String userId = messageElement.attributeValue("user");
		String clientId = messageElement.attributeValue("client");
		String messageOperation = messageElement.attributeValue("operation");
		String contentType = messageElement.attributeValue("type");
		ComplexValue value = new ComplexValue(new ComplexType(contentType));

		Iterator it = messageElement.elementIterator();

		while (it.hasNext()) {
			Element paramElement = (Element) it.next();

			value.set(paramElement.attributeValue("name"), paramElement
					.attributeValue("value"));
		}

		Message message = new Message(messageOperation, value);

		try {
			beginTransaction();

			m_bpeEngine.handleMessage(new BPERuntimeContext(userId, clientId),
					message);
		} catch (Exception e) {
			log.error("Exception", e);
		} finally {
			endTransaction();
		}

		return message.toString();
	}

	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation description="Restart Activity"
	 * @jmx.managed-parameter name="processInstanceId" type="java.lang.String"
	 *                        description="Process Instance Id"
	 * @jmx.managed-parameter name="activityId" type="java.lang.String"
	 *                        description="Activity Id"
	 * 
	 * @param messageXML
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *              Description of the Exception
	 */
	public String restartActivity(String processInstanceId, String activityId)
			throws Exception {
		SAXReader reader = new SAXReader();

		try {
			beginTransaction();

			m_bpeEngine.getAsyncManager().asyncCompleteBPEActivity(processInstanceId,
					activityId);
		} catch (Exception e) {
			log.error("Exception", e);
			return "ERROR:" + e.toString();
		} finally {
			endTransaction();
		}
		return "OK";
	}

    /**
     * Description of the Method
     *
     * @jmx.managed-operation description="Terminate a process instance"
     * @jmx.managed-parameter name="processInstanceId" type="java.lang.String"
     *                        description="Process instance id"
     *
     * @param processInstanceId
     *          Description of the Parameter
     * @return Description of the Return Value
     * @exception Exception
     *              Description of the Exception
     */
    public String terminateProcessInstance(String processInstanceId) throws Exception {
          try {
              InitialContext ctx = new InitialContext();
              
              BPEProcessHome home = (BPEProcessHome) PortableRemoteObject.narrow(ctx.lookup(BPEProcessHome.JNDI_NAME), BPEProcessHome.class);
              de.objectcode.canyon.api.bpe.BPEProcess process = home.create();
              process.terminateProcessInstance(processInstanceId);

               return "OK";
          } catch (Exception e) {
                log.error("Exception", e);
                throw e;
          } finally {
                endTransaction();
          }

    }
    
    /**
     * Description of the Method
     *
     * @jmx.managed-operation description="Update a process instance"
     * @jmx.managed-parameter name="processInstanceId" type="java.lang.String"
     *                        description="Process instance id"
     * @jmx.managed-parameter name="variableName" type="java.lang.String"
     *                        description="Process variable name"
     * @jmx.managed-parameter name="value" type="java.lang.String"
     *                        description="Process variable value"
     *
     * @param processInstanceId
     *          Description of the Parameter
     * @param variableName
     * @param value
     * @return Description of the Return Value
     * @exception Exception
     *              Description of the Exception
     */
    public String updateProcessInstance(String processInstanceId, String variableName, String value) throws Exception {
        try {
            InitialContext ctx = new InitialContext();

            BPEProcessHome home = (BPEProcessHome) PortableRemoteObject.narrow(ctx.lookup(BPEProcessHome.JNDI_NAME), BPEProcessHome.class);
            de.objectcode.canyon.api.bpe.BPEProcess process = home.create();
            ProcessInstanceData processInstanceData = process.getProcessInstance(processInstanceId);

            Attribute[] attrs = processInstanceData.getAttributes();
            HashMap variableMap = new HashMap();
            boolean found = false;

            for (int i = 0; i < attrs.length; i++) {
                Attribute attribute = attrs[i];

                if (attribute.getName().equals(variableName)) {
                    attribute = new Attribute(attribute.getName(), value);
                    found = true;
                }

                variableMap.put(attribute.getName(), attribute);
            }

            if (!found) {
                throw new RuntimeException("No variable '" + variableName + "' in process instance'" + processInstanceId + "' found");
            }

            /*
             * Übernommen aus 
             * com.objectcode.bep.businessEngine.client.ProcessClient.updateProcessInstance(ProcessInstanceData processInstanceData, Map workflowRelevantData)
             * 
             * processInstanceData  = processInstanceData
             * workflowRelevantData = variableMap
             * 
             */
            if (variableMap.size() > 0) {
                if (log.isDebugEnabled()) {
                    log.debug("update process instance for stickyness");
                }
                ProcessInstanceData newProcessInstanceData = new ProcessInstanceData(processInstanceData.getId(), processInstanceData
                        .getParentProcessInstanceIdPath(), processInstanceData.getProcessDefinitionId(), processInstanceData
                        .getProcessDefinitionVersion(), processInstanceData.getName(), processInstanceData.getState(), variableMap,
                        processInstanceData.getStartedDate(), processInstanceData.getStartedBy());

                process.updateProcessInstance(newProcessInstanceData);

            } else {
                if (log.isDebugEnabled()) {
                    log.debug("update process instance for stickyness");
                }
            }
            /*
             * Ende updateProcessInstance
             */
            return "OK";

        } catch (Exception e) {
            return e.toString();
        }
    }
    
    /**
     * Description of the Method
     *
     * @jmx.managed-operation description="sent a event"
     * @jmx.managed-parameter name="clientId" type="java.lang.String"
     *                        description="Client id"
     * @jmx.managed-parameter name="engineId" type="java.lang.String"
     *                        description="Engine name"
     * @jmx.managed-parameter name="userId" type="java.lang.String"
     *                        description="User id"
     * @jmx.managed-parameter name="eventType" type="java.lang.String"
     *                        description="event type"
     * @jmx.managed-parameter name="action" type="java.lang.String"
     *                        description="action"
     * @jmx.managed-parameter name="processId" type="java.lang.String"
     *                        description="Process id"
     * @jmx.managed-parameter name="param1Name" type="java.lang.String"
     *                        description="Parameter name 1"
     * @jmx.managed-parameter name="param1Value" type="java.lang.String"
     *                        description="Parameter value 1"
     * @jmx.managed-parameter name="param2Name" type="java.lang.String"
     *                        description="Parameter name 2"
     * @jmx.managed-parameter name="param2Value" type="java.lang.String"
     *                        description="Parameter value 2"
     * @jmx.managed-parameter name="param3Name" type="java.lang.String"
     *                        description="Parameter name 3"
     * @jmx.managed-parameter name="param3Value" type="java.lang.String"
     *                        description="Parameter value 3"
     * @jmx.managed-parameter name="param4Name" type="java.lang.String"
     *                        description="Parameter name 4"
     * @jmx.managed-parameter name="param4Value" type="java.lang.String"
     *                        description="Parameter value 4"
     * @param processInstanceId
     *          Description of the Parameter
     * @param variableName
     * @param value
     * @return Description of the Return Value
     * @exception Exception
     *              Description of the Exception
     */
    public String sendEvent(String clientId, String engineId, String userId, String eventType, String action, String processId,
            String param1Name, String param1Value, String param2Name, String param2Value, String param3Name, String param3Value,
            String param4Name, String param4Value) {
        QueueConnection connection = null;
        QueueSession session = null;

        try {
            InitialContext ctx = new InitialContext();
            Queue eventQueue = (Queue) ctx.lookup("queue/WSApplicationEvent");
            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ctx.lookup("ConnectionFactory");
            connection = queueConnectionFactory.createQueueConnection();
            session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            connection.start();
            HashMap eventParams = new HashMap();
            if (param1Name != null && param1Name.length() > 0)
                eventParams.put(param1Name, param1Value);
            if (param2Name != null && param2Name.length() > 0)
                eventParams.put(param2Name, param2Value);
            if (param3Name != null && param3Name.length() > 0)
                eventParams.put(param3Name, param3Value);
            if (param4Name != null && param4Name.length() > 0)
                eventParams.put(param4Name, param4Value);

            javax.jms.Message msg = session.createObjectMessage(eventParams);

            msg.setStringProperty("engineId", engineId);
            msg.setStringProperty("eventType", eventType);
            msg.setStringProperty("userId", userId);
            msg.setStringProperty("clientId", clientId);
            msg.setStringProperty("action", action);
            msg.setStringProperty("processId", processId);

            QueueSender sender = session.createSender(eventQueue);

            sender.send(msg, DeliveryMode.PERSISTENT, 4, 0L);

            sender.close();
            return "OK";
        } catch (Exception e) {
            return e.toString();
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
    }

    
    
	/**
	 * Description of the Class
	 * 
	 * @author junglas
	 * @created 14. Juli 2004
	 */
	private static class ProcessVisitor implements IProcessVisitor {
		StringBuffer m_buffer;

		/**
		 * Constructor for the ProcessVisitor object
		 * 
		 * @param buffer
		 *          Description of the Parameter
		 */
		ProcessVisitor(StringBuffer buffer) {
			m_buffer = buffer;
		}

		/**
		 * @param process
		 *          Description of the Parameter
		 * @param processSource
		 *          Description of the Parameter
		 * @exception RepositoryException
		 *              Description of the Exception
		 * @see de.objectcode.canyon.bpe.repository.IProcessVisitor#visit(de.objectcode.canyon.bpe.engine.activities.BPEProcess,
		 *      java.io.Serializable)
		 */
		public void visit(BPEProcess process, Serializable processSource)
				throws RepositoryException {
			m_buffer.append("<tr><td>").append(process.getId()).append("</td><td>")
					.append(process.getName()).append("</td></tr>");
		}
	}

	private static class ProcessInstanceMigrationVisitor implements
			IProcessInstanceVisitor {
		private int fHydrationSchema;

		private int fDydrationSchema;

		private StringBuffer fMessages;

		private int fCounter = 0;

		ProcessInstanceMigrationVisitor(IProcessRepository processRepository,
				StringBuffer messages, int dehydrationSchema, int hydrationSchema) {
			fHydrationSchema = hydrationSchema;
			fDydrationSchema = dehydrationSchema;
			fMessages = messages;
			m_processRepository = processRepository;
		}

		private Map m_processCache = new HashMap();

		private IProcessRepository m_processRepository;

		private long fOldTotalSize;

		private long fNewTotalSize;

		/**
		 * @param processInstance
		 *          Description of the Parameter
		 * @exception RepositoryException
		 *              Description of the Exception
		 * @see de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor#visit(de.objectcode.canyon.bpe.repository.ProcessInstance)
		 */
		public void visit(ProcessInstance processInstance)
				throws RepositoryException {
			fCounter++;
			Long key = new Long(processInstance.getProcessEntityOid());
			BPEProcess process = (BPEProcess) m_processCache.get(key);

			if (process == null) {
				process = m_processRepository.getProcess(processInstance
						.getProcessEntityOid());

				m_processCache.put(key, process);
			}

			HydrationContext hydrationContext = new HydrationContext(fHydrationSchema);
			HydrationContext dehydrationContext = new HydrationContext(
					fDydrationSchema);

			boolean mismatch = false;
			try {
				process.setProcessInstanceId(processInstance.getProcessInstanceId());
				byte[] oldProcessState = processInstance.getProcessState();
				try {
					process.hydrate(hydrationContext, new ObjectInputStream(
						new ByteArrayInputStream(oldProcessState)));
				} catch (EOFException e) {
					fMessages.append("<TR><TD>").append(process.getId())
					.append("</TD><TD>").append(processInstance.getProcessInstanceId())
					.append("</TD><TD>ERROR: EOF</TD></TR>");
					log.error("Shrink " + processInstance.getProcessInstanceId());
					mismatch = true;
				} catch (OptionalDataException e) {
					fMessages.append("<TR><TD>").append(process.getId())
					.append("</TD><TD>").append(processInstance.getProcessInstanceId())
					.append("</TD><TD>ERROR: ODE</TD></TR>");
					log.error("Shrink " + processInstance.getProcessInstanceId());
					mismatch = true;
				}
				if (mismatch)
					return;

				ByteArrayOutputStream bos = new ByteArrayOutputStream();

				try {
					ObjectOutputStream oos = new ObjectOutputStream(bos);

					process.dehydrate(dehydrationContext, oos);
					oos.close();
				} catch (IOException e) {
					log.error("Exception", e);
					throw new RepositoryException(e);
				}

				
				
				byte[] newProcessState = bos.toByteArray();
				processInstance.setProcessState(newProcessState);
				processInstance.setParentProcessInstanceId(process
						.getParentProcessInstanceId());

				fOldTotalSize += oldProcessState.length;
				fNewTotalSize += newProcessState.length;
				fMessages.append("<TR><TD>").append(process.getId())
						.append("</TD><TD>").append(processInstance.getProcessInstanceId())
						.append("</TD><TD>").append(oldProcessState.length).append("=>")
						.append(newProcessState.length).append("</TD></TR>");
				log.info("Shrink " + processInstance.getProcessInstanceId() + ":"
						+ oldProcessState.length + "=>" + newProcessState.length);
			} catch (IOException e) {
				log.error("Exception", e);
				throw new RepositoryException(e);
			}
		}

		public long getNewTotalSize() {
			return fNewTotalSize;
		}

		public long getOldTotalSize() {
			return fOldTotalSize;
		}

	}

	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation description="Show details of a process instance"
	 * @jmx.managed-parameter name="processInstanceId" type="java.lang.String"
	 *                        description="Process instance id"
	 * @jmx.managed-parameter name="url" type="java.lang.String"
	 *                        description="Url of XSLT"
	 * 
	 * @param processInstanceId
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *              Description of the Exception
	 */
	public String showProcessInstance(String processInstanceId, String url)
			throws Exception {
		try {
			URL transformerUrl = new URL(url);
			return showProcessInstance(processInstanceId, transformerUrl);
		} catch (Exception e) {
			log.error("Exception", e);
			throw e;
		}
	}

	public String showProcessInstance(String processInstanceId, URL url)
			throws Exception {
		try {
			beginTransaction();

			BPEProcess process = m_bpeEngine.getProcessInstance(processInstanceId);

			TransformerFactory factory = TransformerFactory.newInstance();

			Transformer transformer = factory.newTransformer(new StreamSource(url
					.openStream()));

			StringWriter transformed = new StringWriter();
			transformer.transform(new DocumentSource(DomSerializer
					.toDocument(process)), new StreamResult(transformed));

			return transformed.toString();
		} catch (Exception e) {
			log.error("Exception", e);
			throw e;
		} finally {
			endTransaction();
		}
	}
	
	/**
	 * Description of the Class
	 * 
	 * @author junglas
	 * @created 14. Juli 2004
	 */
	private static class ProcessInstanceVisitor implements
			IProcessInstanceVisitor {
		StringBuffer m_buffer;

		/**
		 * Constructor for the ProcessInstanceVisitor object
		 * 
		 * @param buffer
		 *          Description of the Parameter
		 */
		ProcessInstanceVisitor(StringBuffer buffer) {
			m_buffer = buffer;
		}

		/**
		 * @param processInstance
		 *          Description of the Parameter
		 * @exception RepositoryException
		 *              Description of the Exception
		 * @see de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor#visit(de.objectcode.canyon.bpe.repository.ProcessInstance)
		 */
		public void visit(ProcessInstance processInstance)
				throws RepositoryException {
			m_buffer.append("<tr><td>")
					.append(processInstance.getProcessInstanceId()).append("</td><td>");
			m_buffer.append(processInstance.getProcessId()).append("</td><td>");
			m_buffer.append(processInstance.getState().getTag()).append("</td></tr>");
		}
	}
	
	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation description="Turn Event Logging on"
	 * 
	 * @param onlyOpen
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *              Description of the Exception
	 */
	public String turnEventLoggingOn() throws Exception {
		
		m_svcMgr.getBpeEventBroker().addBPEEventListener(new LoggingBPEEventListener());
		return "OK";
	}

  /**
   * Description of the Method
   *
   * @jmx.managed-operation description="Reanimate all fishy process instances"
	 * @jmx.managed-parameter name="readOnly" type="boolean" description="ReadOnly"
   *
   * @param processInstanceId
   *          Description of the Parameter
   * @return Description of the Return Value
   * @exception Exception
   *              Description of the Exception
   */
  public String reanimateProcessInstances(boolean readOnly) throws Exception {
        try {
        		List result = m_bpeEngine.reanimateProcessInstances(readOnly);
        		StringBuilder buffy = new StringBuilder();
        		buffy.append("<UL>");
        		for (Iterator iter = result.iterator(); iter.hasNext();) {
							String message = (String) iter.next();
							buffy.append("<LI>").append(message).append("</LI>");
						}
        		buffy.append("</UL>");
        		return buffy.toString();
        } catch (Exception e) {
              log.error("Exception", e);
              throw e;
        } finally {
              endTransaction();
        }

  }
	
}
