/*
 * Created on 16.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.objectcode.canyon.spiImpl.tool.bpe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.api.worklist.ProcessData;
import de.objectcode.canyon.api.worklist.ProcessInstanceData;
import de.objectcode.canyon.api.worklist.ProcessInstanceData.Attribute;
import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.BPERuntimeContext;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.ActivityState;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor;
import de.objectcode.canyon.bpe.repository.IProcessRepository;
import de.objectcode.canyon.bpe.repository.IProcessVisitor;
import de.objectcode.canyon.bpe.repository.ProcessInstance;
import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.data.BasicType;
import de.objectcode.canyon.model.data.FormalParameter;
import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;

/**
 * @author xylander
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class BPEFacade {

	private static Log LOGGER = LogFactory.getLog(BPEFacade.class);

	private BPEEngine m_bpeEngine;

	public BPEFacade(BPEEngine engine) {
		m_bpeEngine = engine;
	}

	public ProcessInstanceData getProcessInstanceReadOnly(
			String processInstanceId) throws RepositoryException {
		BPEProcess process = m_bpeEngine
				.getProcessInstanceReadOnly(processInstanceId);

		return createProcessInstanceData(process);
	}

	
	public ProcessInstanceData getProcessInstance(String processInstanceId)
			throws RepositoryException {
		BPEProcess process = m_bpeEngine.getProcessInstance(processInstanceId);

		return createProcessInstanceData(process);
	}

	public ProcessData getMatchingProcessDefinition(String processId, 
			String parentProcessInstanceIdPath) throws WMWorkflowException, RepositoryException {

		Long packageRevisionOid = m_bpeEngine.getPackageRevisionOid(parentProcessInstanceIdPath);

		WorkflowProcess process = (WorkflowProcess) m_bpeEngine
		  .getProcessRepository().getProcessSource(processId, packageRevisionOid.longValue());
		if (process == null) {
			throw new WMWorkflowException(new Exception("Cannot find process " + processId));
		}
		return createProcessData(process);
	}

	public ProcessData getProcessDefinition(String processId) throws WMWorkflowException, RepositoryException {
		WorkflowProcess process = (WorkflowProcess) m_bpeEngine.getProcessRepository().getProcessSource(processId);
		if (process == null) {
			throw new WMWorkflowException(new Exception("Cannot find process " + processId));
		}
		return createProcessData(process);
	}

	public ProcessData getProcessDefinition(String processId, String version)
			throws WMWorkflowException, RepositoryException {
		WorkflowProcess process = (WorkflowProcess) m_bpeEngine
				.getProcessRepository().getProcessSource(processId, version);
		if (process == null) {
			throw new WMWorkflowException(new Exception("Cannot find process "
					+ processId));
		}
		return createProcessData(process);
	}

	public WorkflowProcess getProcess(String processId, String version)
			throws WMWorkflowException, RepositoryException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getProcess: " + processId);
		}
		WorkflowProcess process = (WorkflowProcess) m_bpeEngine
				.getProcessRepository().getProcessSource(processId,version);

		return process;
	}

	public int countProcesses() throws WMWorkflowException, RepositoryException {
		int count = m_bpeEngine.getProcessRepository().countProcesses();

		return count;
	}

	public String[] findMembers(String processInstanceId, String participantId,
			String clientId) throws WMWorkflowException, RepositoryException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("findMembers " + processInstanceId + "," + participantId);
		}

		ProcessInstance processInstance = null;
		try {
			processInstance = m_bpeEngine.getProcessInstanceRepository()
					.getProcessInstance(processInstanceId);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}

		WorkflowProcess workflowProcess = null;
		try {
			workflowProcess = (WorkflowProcess) m_bpeEngine.getProcessRepository()
					.getProcessSource(processInstance.getProcessId());
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}

		Participant participant = workflowProcess.getParticipant(participantId);

		String[] user = m_bpeEngine.getParticipantRepository().findMembers(
				participant, clientId);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("found " + user.length + " user");
		}

		return user;
	}

	public ProcessData[] listProcesses(int offset, int length)
			throws WMWorkflowException, RepositoryException {
		return listProcesses(false, offset, length);
	}
    
    

	public ProcessData[] listProcesses(boolean onlyActive, int offset, int length)
			throws WMWorkflowException, RepositoryException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("listProcesses: " + offset + " " + length);
		}

		ProcessVisitor visitor = new ProcessVisitor();

		m_bpeEngine.getProcessRepository().iterateProcesses(visitor, onlyActive);

		List result = null;
		List fullResult = visitor.getProcessDatas();
		if (offset < fullResult.size()) {
			int oddset = offset + length;
			if (oddset > fullResult.size()) {
				oddset = fullResult.size();
			}
			result = fullResult.subList(offset, oddset);
		} else {
			result = new ArrayList();
		}
		ProcessData[] processes = new ProcessData[result.size()];

		result.toArray(processes);

		return processes;
	}
    
    public ProcessData[] listCompactProcesses(int offset, int length)
            throws WMWorkflowException, RepositoryException {
        return listCompactProcesses(false, offset, length);
    }
    
    public ProcessData[] listCompactProcesses(boolean onlyActive, int offset, int length)
        throws WMWorkflowException, RepositoryException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("listProcesses: " + offset + " " + length);
        }
        
        CompactProcessVisitor visitor = new CompactProcessVisitor();
        
        m_bpeEngine.getProcessRepository().iterateCompactProcesses(visitor, onlyActive);
        
        List result = null;
        List fullResult = visitor.getProcessDatas();
        if (offset < fullResult.size()) {
            int oddset = offset + length;
            if (oddset > fullResult.size()) {
                oddset = fullResult.size();
            }
            result = fullResult.subList(offset, oddset);
        } else {
            result = new ArrayList();
        }
        ProcessData[] processes = new ProcessData[result.size()];
        
        result.toArray(processes);
        
        return processes;
    }
    

	public WorkflowProcess[] listProcesses() throws WMWorkflowException,
			RepositoryException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("listProcesses:");
		}
		ProcessSourceVisitor visitor = new ProcessSourceVisitor();

		m_bpeEngine.getProcessRepository().iterateProcesses(visitor, false);

		List result = visitor.getProcessSources();

		WorkflowProcess[] processes = new WorkflowProcess[result.size()];

		result.toArray(processes);

		return processes;
	}

	public ProcessInstanceData createAndStartProcessInstance(
			ProcessData processData) throws RepositoryException, EngineException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("start an create instance process '" + processData.getName()
					+ "'");
		}
		String processInstanceId = null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("process instance created" + processInstanceId);
		}

		ComplexType type = new ComplexType("flow-request");
		Message message = new Message(processData.getId() + "-init", type);
		ProcessData.Parameter[] parameters = processData.getParameters();
		int i;

		for (i = 0; i < parameters.length; i++) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Parameter " + i + ") " + parameters[i].getName() + "="
						+ parameters[i].getValue());
			}
			message.getContent().set(parameters[i].getName(),
					parameters[i].getValue());
		}

		processInstanceId = m_bpeEngine.handleMessage(new BPERuntimeContext(null,
				null), message);

		BPEProcess process = m_bpeEngine.getProcessInstance(processInstanceId);

		return createProcessInstanceData(process);
	}

	/*
	 * Before starting the process instance check that there is no running process with same formal parameter values
	 */
	public ProcessInstanceData createAndStartUniqueProcessInstance(String userId,
			String clientId, ProcessData processData,
			String parentProcessInstanceIdPath) throws WMWorkflowException,
			RepositoryException, EngineException {
		long start = System.currentTimeMillis();
		ProcessInstanceData pid = null;

		MatchingProcessInstanceVisitor visitor = new MatchingProcessInstanceVisitor(m_bpeEngine
				.getProcessRepository(),processData,clientId);

		m_bpeEngine.getProcessInstanceRepository().iterateProcessInstances(
				processData.getId(),true, visitor);
		
		
		if (visitor.getNumberOfMatchingProcessInstances()==0) {
			pid = createAndStartProcessInstance(userId,clientId,processData,parentProcessInstanceIdPath);
		} else {
			LOGGER.info("Did not create process instance for process '" + processData.getId() + "'. A matching instance is already running");
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Split BPEFacade.createAndStartUniqueProcessInstance:" +(System.currentTimeMillis()-start)+" ms");
		}
		return pid;
	}
	
	public ProcessInstanceData createAndStartProcessInstance(String userId,
			String clientId, ProcessData processData,
			String parentProcessInstanceIdPath) throws WMWorkflowException,
			RepositoryException, EngineException {
		String processInstanceId = null;


		ComplexType type = new ComplexType("flow-request");
		Message message = new Message(processData.getId() + "-init", type);
		ProcessData.Parameter[] parameters = processData.getParameters();
		int i;

		for (i = 0; i < parameters.length; i++) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Parameter " + i + ") " + parameters[i].getName() + "="
						+ parameters[i].getValue());
			}
			message.getContent().set(parameters[i].getName(),
					parameters[i].getValue());
		}
		if (parentProcessInstanceIdPath != null)
			message.getContent().set("parentProcessIdPath",
					parentProcessInstanceIdPath);

		processInstanceId = m_bpeEngine.handleMessage(new BPERuntimeContext(userId,
				clientId), message);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("process instance created" + processInstanceId + " "
					+ userId + " " + clientId);
		}

		BPEProcess process = m_bpeEngine.getProcessInstance(processInstanceId);

		return createProcessInstanceData(process);
	}

	public void terminateProcessInstance(String processInstanceId)
			throws WMWorkflowException, RepositoryException, EngineException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("terminate instance process '" + processInstanceId + "'");
		}
		m_bpeEngine.terminateProcessInstance(processInstanceId);
	}

	/**
	 * @ejb.interface-method
	 *
	 * @param processData
	 *          Description of the Parameter
	 * @param userId
	 *          Description of the Parameter
	 * @param clientId
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception WMWorkflowException
	 *              Description of the Exception
	 * @throws RepositoryException
	 */
	public void updateProcessInstance(ProcessInstanceData processInstanceData)
			throws WMWorkflowException, RepositoryException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("updateProcessInstance process '"
					+ processInstanceData.getId() + "'");
		}
		BPEProcess process = m_bpeEngine.getProcessInstance(processInstanceData
				.getId());
		updateProcessFromProcessInstanceData(process, processInstanceData);
		m_bpeEngine.updateProcessInstance(process);
	}

	/**
	 * @ejb.interface-method
	 *
	 * @param processId
	 *          Description of the Parameter
	 * @param offset
	 *          Description of the Parameter
	 * @param length
	 *          Description of the Parameter
	 * @param onlyOpen
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception WMWorkflowException
	 *              Description of the Exception
	 * @throws RepositoryException
	 */
	public ProcessInstanceData[] listProcessInstances(String processId,
			int offset, int length, boolean onlyOpen) throws WMWorkflowException,
			RepositoryException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("listProcessInstances: " + processId + " " + offset + " "
					+ length + " " + onlyOpen);
		}
		ProcessInstanceVisitor visitor = new ProcessInstanceVisitor(m_bpeEngine
				.getProcessRepository());

		m_bpeEngine.getProcessInstanceRepository().iterateProcessInstances(
				processId, onlyOpen, visitor);

		List fullResult = visitor.getProcessInstanceDatas();
		List result = null;
		if (offset < fullResult.size()) {
			int oddset = offset + length;
			if (oddset > fullResult.size()) {
				oddset = fullResult.size();
			}
			result = fullResult.subList(offset, oddset);
		} else {
			result = new ArrayList();
		}

		ProcessInstanceData ret[] = new ProcessInstanceData[result.size()];

		result.toArray(ret);

		return ret;
	}

	/**
	 * @ejb.interface-method
	 *
	 * @param processId
	 *          Description of the Parameter
	 * @param offset
	 *          Description of the Parameter
	 * @param length
	 *          Description of the Parameter
	 * @param onlyOpen
	 *          Description of the Parameter
	 * @param clientId
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception WMWorkflowException
	 *              Description of the Exception
	 * @throws RepositoryException
	 */
	public ProcessInstanceData[] listProcessInstances(String processId,
			String clientId, int offset, int length, boolean onlyOpen)
			throws WMWorkflowException, RepositoryException {
		ProcessInstanceClientIdVisitor visitor = new ProcessInstanceClientIdVisitor(
				m_bpeEngine.getProcessRepository(), clientId);

		m_bpeEngine.getProcessInstanceRepository().iterateProcessInstances(
				processId, onlyOpen, visitor);

		List fullResult = visitor.getProcessInstanceDatas();
		List result = null;
		if (offset < fullResult.size()) {
			int oddset = offset + length;
			if (oddset > fullResult.size()) {
				oddset = fullResult.size();
			}
			result = fullResult.subList(offset, oddset);
		} else {
			result = new ArrayList();
		}

		ProcessInstanceData ret[] = new ProcessInstanceData[result.size()];

		result.toArray(ret);

		return ret;
	}

	private ProcessData createProcessData(WorkflowProcess process)
			throws RepositoryException {
		Map params = new HashMap();
		Map attribs = new HashMap();
		// DataField dataFields[] = process.getPackage().getDataFields();
		FormalParameter[] formalParameters = process.getFormalParameters();

		for (int i = 0; i < formalParameters.length; i++) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("formalParameter:" + formalParameters[i].getId() + "("
						+ formalParameters[i].getDescription() + ") ["
						+ ((BasicType) formalParameters[i].getDataType()).getValue() + "]");
			}
			// TODO: Check, that each formal parameter has a corresponding daat field.
			// XPDL sucks!
			params.put(formalParameters[i].getId(), new ProcessData.Parameter(
					formalParameters[i].getId(), ((BasicType) formalParameters[i]
							.getDataType()).getValue(), formalParameters[i].getDescription(),
					null));
		}

		ProcessData.Parameter parameters[] = new ProcessData.Parameter[params
				.size()];

		params.values().toArray(parameters);

		ExtendedAttribute[] extendedAttributes = process.getExtendedAttributes();

		for (int i = 0; i < extendedAttributes.length; i++) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("extendedAttribute:" + extendedAttributes[i].getName()
						+ " = " + extendedAttributes[i].getValue());
			}
			attribs.put(extendedAttributes[i].getName(), new ProcessData.Parameter(
					extendedAttributes[i].getName(), BasicType.STRING_INT,
					extendedAttributes[i].getValue()));
		}

		ProcessData.Parameter attributes[] = new ProcessData.Parameter[attribs
				.size()];

		attribs.values().toArray(attributes);

		return new ProcessData(process.getId(), process.getName(), process
				.findWorkflowVersion(), process.getPackage().getId(), process
				.getPackage().getName(), process.getState(), parameters, attributes);
	}
    
    private ProcessData createCompactProcessData(WorkflowProcess process)
            throws RepositoryException {
//        Map params = new HashMap();
//        Map attribs = new HashMap();
        // DataField dataFields[] = process.getPackage().getDataFields();
//        FormalParameter[] formalParameters = process.getFormalParameters();

//        for (int i = 0; i < formalParameters.length; i++) {
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("formalParameter:" + formalParameters[i].getId() + "("
//                        + formalParameters[i].getDescription() + ") ["
//                        + ((BasicType) formalParameters[i].getDataType()).getValue() + "]");
//            }
//            // TODO: Check, that each formal parameter has a corresponding daat field.
//            // XPDL sucks!
//            params.put(formalParameters[i].getId(), new ProcessData.Parameter(
//                    formalParameters[i].getId(), ((BasicType) formalParameters[i]
//                            .getDataType()).getValue(), formalParameters[i].getDescription(),
//                    null));
//        }

        ProcessData.Parameter parameters[] = new ProcessData.Parameter[0];//[params.size()];

//        params.values().toArray(parameters);

//        ExtendedAttribute[] extendedAttributes = process.getExtendedAttributes();

//        for (int i = 0; i < extendedAttributes.length; i++) {
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("extendedAttribute:" + extendedAttributes[i].getName()
//                        + " = " + extendedAttributes[i].getValue());
//            }
//            attribs.put(extendedAttributes[i].getName(), new ProcessData.Parameter(
//                    extendedAttributes[i].getName(), BasicType.STRING_INT,
//                    extendedAttributes[i].getValue()));
//        }

        ProcessData.Parameter attributes[] = new ProcessData.Parameter[0]; //[attribs.size()];

//        attribs.values().toArray(attributes);

        return new ProcessData(process.getId(), "KB:"+process.getName(), process
                .findWorkflowVersion(), process.getPackage().getId(), process
                .getPackage().getName(), process.getState(), parameters, attributes);        
    }

	private void updateProcessFromProcessInstanceData(BPEProcess process,
			ProcessInstanceData processInstance) throws RepositoryException {
		Attribute[] attributes = processInstance.getAttributes();
		for (int i = 0; i < attributes.length; i++) {
			process.getVariable(attributes[i].getName()).setValue(
					attributes[i].getValue());
		}
	}

	private ProcessInstanceData createProcessInstanceData(BPEProcess process)
			throws RepositoryException {
		IVariable[] variables = process.getVariables();
		int i;
		Map workflowRelevantData = new TreeMap();

		for (i = 0; i < variables.length; i++) {
			workflowRelevantData.put(variables[i].getName(),
					new ProcessInstanceData.Attribute(variables[i].getName(),
							variables[i].getValue()));
		}

		return new ProcessInstanceData(process.getProcessInstanceId(), process.getParentProcessInstanceIdPath(), process
				.getId(), process.getVersion(), process.getName(), process.getState().getValue(),
				workflowRelevantData,process.getStartedDate(),process.getStartedBy());
	}

	/**
	 * Description of the Class
	 *
	 * @author junglas
	 * @created 13. Juli 2004
	 */
	private class ProcessVisitor implements IProcessVisitor {
		List m_processDatas = new ArrayList();

		/**
		 * @return Returns the processDatas.
		 */
		public List getProcessDatas() {
			return m_processDatas;
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
			m_processDatas.add(createProcessData((WorkflowProcess) processSource));
		}
	}
    
    /**
     * Description of the Class
     *
     * @author junglas
     * @created 13. Juli 2004
     */
    private class CompactProcessVisitor implements IProcessVisitor {
        List m_processDatas = new ArrayList();

        /**
         * @return Returns the processDatas.
         */
        public List getProcessDatas() {
            return m_processDatas;
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
            m_processDatas.add(createCompactProcessData((WorkflowProcess) processSource));
        }
    }    

	/**
	 * Description of the Class
	 *
	 * @author junglas
	 * @created 13. Juli 2004
	 */
	private static class ProcessSourceVisitor implements IProcessVisitor {
		List m_processSources = new ArrayList();

		/**
		 * @return Returns the processSources.
		 */
		public List getProcessSources() {
			return m_processSources;
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
			m_processSources.add(processSource);
		}
	}

	/**
	 * Description of the Class
	 *
	 * @author junglas
	 * @created 5. August 2004
	 */
	private class ProcessInstanceVisitor implements IProcessInstanceVisitor {
		private List m_processInstanceDatas = new ArrayList();

		private Map m_processCache = new HashMap();

		private IProcessRepository m_processRepository;

		/**
		 * Constructor for the ProcessInstanceVisitor object
		 *
		 * @param processRepository
		 *          Description of the Parameter
		 */
		ProcessInstanceVisitor(IProcessRepository processRepository) {
			m_processRepository = processRepository;
		}

		/**
		 * @return Returns the processInstanceDatas.
		 */
		public List getProcessInstanceDatas() {
			return m_processInstanceDatas;
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
			Long key = new Long(processInstance
					.getProcessEntityOid());
			BPEProcess process = (BPEProcess) m_processCache.get(key);

			if (process == null) {
				process = m_processRepository
						.getProcess(processInstance.getProcessEntityOid());

				m_processCache.put(key, process);
			}

			try {
				process.setProcessInstanceId(processInstance.getProcessInstanceId());
				process.hydrate( new HydrationContext(), new ObjectInputStream(new ByteArrayInputStream(
						processInstance.getProcessState())));

				m_processInstanceDatas.add(createProcessInstanceData(process));
			} catch (IOException e) {
				LOGGER.error("Exception", e);
				throw new RepositoryException(e);
			}
		}
	}

	/**
	 * Description of the Class
	 *
	 * @author junglas
	 * @created 5. August 2004
	 */
	private class ProcessInstanceClientIdVisitor implements
			IProcessInstanceVisitor {
		private List m_processInstanceDatas = new ArrayList();

		private Map m_processCache = new HashMap();

		private IProcessRepository m_processRepository;

		private String m_clientId;

		/**
		 * Constructor for the ProcessInstanceClientIdVisitor object
		 *
		 * @param processRepository
		 *          Description of the Parameter
		 * @param clientId
		 *          Description of the Parameter
		 */
		ProcessInstanceClientIdVisitor(IProcessRepository processRepository,
				String clientId) {
			m_processRepository = processRepository;
			m_clientId = clientId;
		}

		/**
		 * @return Returns the processInstanceDatas.
		 */
		public List getProcessInstanceDatas() {
			return m_processInstanceDatas;
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
			Long key = new Long(processInstance
					.getProcessEntityOid());
			BPEProcess process = (BPEProcess) m_processCache.get(key);

			if (process == null) {
				process = m_processRepository
						.getProcess(processInstance.getProcessEntityOid());

				m_processCache.put(key, process);
			}

			try {
				process.setProcessInstanceId(processInstance.getProcessInstanceId());
				process.hydrate( new HydrationContext(), new ObjectInputStream(new ByteArrayInputStream(
						processInstance.getProcessState())));

				if (m_clientId == null || m_clientId.equals(process.getClientId())) {
					m_processInstanceDatas.add(createProcessInstanceData(process));
				}
			} catch (IOException e) {
				LOGGER.error("Exception", e);
				throw new RepositoryException(e);
			}
		}
	}

  private class MatchingProcessInstanceVisitor implements IProcessInstanceVisitor
  {
  	private String m_clientId;
    private  ProcessData 		 m_processData;
    private  IProcessRepository  m_processRepository;
    private Map m_processCache = new HashMap();
    
    private int m_numberOfMatchingProcessInstances=0;

    /**
     *Constructor for the ProcessInstanceClientIdVisitor object
     *
     * @param processRepository  Description of the Parameter
     * @param clientId           Description of the Parameter
     */
    MatchingProcessInstanceVisitor( IProcessRepository processRepository, ProcessData processData, String clientId)
    {
      m_processRepository = processRepository;
      m_processData = processData;
      m_clientId = clientId;
    }


    /**
     * @param processInstance          Description of the Parameter
     * @exception RepositoryException  Description of the Exception
     * @throws 
     * @see                            de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor#visit(de.objectcode.canyon.bpe.repository.ProcessInstance)
     */
    public void visit( ProcessInstance processInstance )
      throws RepositoryException
    {
      BPEProcess  process  = ( BPEProcess ) m_processCache.get( new Long( processInstance.getProcessEntityOid() ) );

      if ( process == null ) {
        process = m_processRepository.getProcess( processInstance.getProcessEntityOid() );

        m_processCache.put( new Long( processInstance.getProcessEntityOid() ), process );
      }


      try {
        process.setBPEEngine( m_bpeEngine );      	
        // ???
        process.setProcessInstanceId( processInstance.getProcessInstanceId() );
        process.hydrate( new HydrationContext(), new ObjectInputStream( new ByteArrayInputStream( processInstance.getProcessState() ) ) );
        

        // TODO do we have to check for completed instances, too?
        if ( (m_clientId == null ||
        	m_clientId.equals( process.getClientId())
        	&& (process.getState() == ActivityState.RUNNING)) ) {
      			ProcessData.Parameter[] parameters = m_processData.getParameters();
      			if (matchesParameters(process, parameters)) {
      				m_numberOfMatchingProcessInstances++;
          }
        }
      }
      catch ( IOException e ) {
        LOGGER.error( "Exception", e );
        throw new RepositoryException( e );
      }
    }


		private boolean matchesParameters(BPEProcess process, ProcessData.Parameter[] parameters) {
			boolean matches = true;
			for (int i = 0; i < parameters.length; i++) {
				ProcessData.Parameter parameter = parameters[i];
			  String parameterName = parameter.getName();
			  Object parameterValue = parameter.getValue();
			  IVariable variable = process.getVariable(parameterName);
			  if (variable==null) {
			    matches = false;
			    break;
			  } else {
			    Object variableValue = variable.getValue();
			    if (parameterValue!=null && variableValue!=null && !parameterValue.equals(variableValue)) {
			      matches = false;
			      break;
			    } 
			  }
			}
			return matches;
		}
		
		public Map getProcessCache() {
			return m_processCache;
		}


		public int getNumberOfMatchingProcessInstances() {
			return m_numberOfMatchingProcessInstances;
		}
  }
	
}
