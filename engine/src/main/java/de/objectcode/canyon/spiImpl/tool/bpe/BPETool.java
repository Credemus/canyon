/*
 * Created on 14.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.objectcode.canyon.spiImpl.tool.bpe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.BPERuntimeContext;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor;
import de.objectcode.canyon.bpe.repository.IProcessRepository;
import de.objectcode.canyon.bpe.repository.ProcessInstance;
import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.tool.Parameter;

/**
 * @author xylander
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class BPETool {
  private static Log LOGGER = LogFactory.getLog(BPETool.class);

  private final static String BPEENGINE_COMP_NAME = "java:/canyon/BPEEngine";

  private BPEEngine m_bpeEngine;

  protected BPEEngine getBPEEngine() throws Exception {
    if (m_bpeEngine == null) {
      InitialContext ctx = new InitialContext();

      BPEEngine bpeEngine = (BPEEngine) ctx.lookup(BPEENGINE_COMP_NAME);

      m_bpeEngine = bpeEngine;
    }
    return m_bpeEngine;
  }

  public String startProcess(BPEEngine bpeEngine, String clientId,
      String userId, String processId, Parameter[] parameters) throws Exception {
    ComplexType type = new ComplexType("flow-request");
    Message message = new Message(processId + "-init", type);
    int i;

    for (i = 0; i < parameters.length; i++) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Parameter " + i + ") " + parameters[i].getName() + "="
            + parameters[i].getValue());
      }
      if (parameters[i].mode == ParameterMode.IN || parameters[i].mode == ParameterMode.INOUT)
        message.getContent().set(parameters[i].getName(),
            parameters[i].getValue());
    }

    String processInstanceId = bpeEngine.handleMessage(new BPERuntimeContext(
        userId, clientId), message);

    BPEProcess process = bpeEngine.getProcessInstance(processInstanceId);
    return processInstanceId;
  }

  public String startProcess(String clientId, String userId, String processId,
      Parameter[] parameters) throws Exception {
    return startProcess(getBPEEngine(), clientId, userId, processId, parameters);
  }

  private class ProcessInstanceVisitor implements IProcessInstanceVisitor {
    private List m_processInstanceIds = new ArrayList();

    private IProcessRepository m_processRepository;

    private String m_clientId;

    private Parameter[] m_parameters;

    /**
     * Constructor for the ProcessInstanceVisitor object
     * 
     * @param processRepository
     *          Description of the Parameter
     * @param clientId
     *          Description of the Parameter
     */
    ProcessInstanceVisitor(IProcessRepository processRepository,
        String clientId, Parameter[] parameters) {
      m_processRepository = processRepository;
      m_clientId = clientId;
      m_parameters = parameters;
    }

    /**
     * @return Returns the processInstanceDatas.
     */
    public List getProcessInstanceIds() {
      return m_processInstanceIds;
    }

    private boolean matchesParameters(BPEProcess process) {
      for (int i = 0; i < m_parameters.length; i++) {
        IVariable v = process.getVariable(m_parameters[i].getName());
        if (v == null)
          continue; // todo
        Object value = v.getValue();
        // todo make this type safe
        if (value == null || !value.equals(m_parameters[i].getValue()))
          return false;
      }
      return true;
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
      BPEProcess process = m_processRepository.getProcess(processInstance.getProcessEntityOid()); //.getProcessId() war falsch.
      
      try {
        process.setProcessInstanceId(processInstance.getProcessInstanceId());
        process.hydrate( new HydrationContext(), new ObjectInputStream(new ByteArrayInputStream(
            processInstance.getProcessState())));

        if (m_clientId == null || m_clientId.equals(process.getClientId())) {
          if (matchesParameters(process))
            m_processInstanceIds.add(processInstance.getProcessInstanceId());
        }
      } catch (IOException e) {
        LOGGER.error("Exception", e);
        throw new RepositoryException(e);
      }
    }
  }

  public String findProcessInstanceIds(String clientId, String processId,
      boolean onlyOpen, Parameter[] parameters) throws Exception {
    ProcessInstanceVisitor visitor = new ProcessInstanceVisitor(getBPEEngine()
        .getProcessRepository(), clientId, parameters);

    getBPEEngine().getProcessInstanceRepository().iterateProcessInstances(
        processId, onlyOpen, visitor);

    List result = visitor.getProcessInstanceIds();

    StringBuffer buffy = new StringBuffer();
    for (int i = 0; i < result.size(); i++) {
      if (i!=0)
        buffy.append(";");
      buffy.append(result.get(i));
    }
    return buffy.toString();
  }

  public Object getParameterValue(String processInstanceId,
      String parameterName) throws Exception {
    BPEProcess process = getBPEEngine().getProcessInstance(processInstanceId);
    IVariable var =  process.getVariable(parameterName);
    if (var != null)
      return var.getValue();
    else
      return null;
  }

  public void terminateProcessInstance(String processInstanceId)
      throws Exception {
    getBPEEngine().terminateProcessInstance(processInstanceId);
  }
}