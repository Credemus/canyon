/*
 * Created on 14.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.objectcode.canyon.spiImpl.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.tool.IToolConnector;
import de.objectcode.canyon.spi.tool.BPEContext;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.spi.tool.ReturnValue;
import de.objectcode.canyon.spiImpl.tool.bpe.BPETool;

/**
 * @author xylander
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class BPEConnector implements IToolConnector {

  private static Log LOGGER = LogFactory.getLog(BPEConnector.class);

  private String m_clientId;

  private String m_userId;

  private String m_method;

  private String m_processId;

  public BPEConnector(String clientId, String userId, String method,
      String processId) {
    m_clientId = clientId;
    m_userId = userId;
    m_method = method;
    m_processId = processId;
  }

  private Parameter stripParameter(List params, String name) {
    if (params == null || params.size() == 0)
      return null;

    if (((Parameter) params.get(0)).getName().equalsIgnoreCase(name)) {
      return (Parameter) params.remove(0);
    }
    return null;
  }

  private Parameter[] getParameters(List paramList) {
    Parameter[] parameters = new Parameter[paramList.size()];
    paramList.toArray(parameters);
    return parameters;
  }

  protected ReturnValue[] handleResult(Parameter[] params, Object result) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("handleResult: " + result);
    }

    if (result == null) {
      return new ReturnValue[0];
    }

    List values = new ArrayList();

    if (result.getClass().isArray()) {
      Object[] resultElements = (Object[]) result;

      // TODO Check for number of out parameters
      int j = 0;
      for (int i = 0; i < params.length; i++) {
        LOGGER.debug("RESULT " + params[i].actualName + " = "
            + resultElements[j]);
        if (params[i].mode == ParameterMode.OUT
            || params[i].mode == ParameterMode.INOUT) {
          values
              .add(new ReturnValue(params[i].actualName, resultElements[j++]));
        }
      }
    } else {
      for (int i = 0; i < params.length; i++) {
        if (params[i].mode == ParameterMode.OUT
            || params[i].mode == ParameterMode.INOUT) {
          values.add(new ReturnValue(params[i].actualName, result));
        }
      }
    }

    ReturnValue[] ret = new ReturnValue[values.size()];

    values.toArray(ret);

    return ret;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.objectcode.canyon.spi.tool.IToolConnector#invoke(de.objectcode.canyon.spi.tool.InvocationContext,
   *      de.objectcode.canyon.spi.tool.Parameter[])
   */
  public ReturnValue[] invoke(BPEContext context, Parameter[] parameters)
      throws Exception {
    List paramList = new ArrayList(Arrays.asList(parameters));
    Parameter clientIdParam = stripParameter(paramList, "clientId");
    if (clientIdParam != null)
      m_clientId = (String) clientIdParam.getValue(); // todo check

    Parameter userIdParam = stripParameter(paramList, "userId");
    if (userIdParam != null)
      m_userId = (String) userIdParam.getValue(); // todo check

    BPETool bpeTool = new BPETool();
    if (m_method == null) {
      throw new RuntimeException("Missing bpe method");
    } else if (m_method.equalsIgnoreCase("startProcess")) {
      Parameter processIdParam = stripParameter(paramList, "processId");
      if (processIdParam == null)
        throw new RuntimeException("Missing parameter processId");
      String processId = (String) processIdParam.getValue(); // todo check
      String piId = bpeTool.startProcess(m_clientId, m_userId, processId,
          getParameters(paramList));
      return handleResult(parameters, piId);
    } else if (m_method.equalsIgnoreCase("getParameterValue")) {
      Parameter processInstanceIdParam = stripParameter(paramList, "processInstanceId");
      if (processInstanceIdParam == null)
        throw new RuntimeException("Missing parameter processInstanceId");
      String processInstanceId = (String) processInstanceIdParam.getValue(); // todo check
      
      Parameter parameterNameParam = stripParameter(paramList, "parameterName");
      if (parameterNameParam == null)
        throw new RuntimeException("Missing parameter parameterName");
      String parameterName = (String) parameterNameParam.getValue(); // todo check

      Object result = bpeTool.getParameterValue(processInstanceId, parameterName);
      return handleResult(parameters, result);
    } else if (m_method.equalsIgnoreCase("terminateProcessInstance")) {
      Parameter processInstanceIdParam = stripParameter(paramList, "processInstanceId");
      if (processInstanceIdParam == null)
        throw new RuntimeException("Missing parameter processInstanceId");
      String processInstanceId = (String) processInstanceIdParam.getValue(); // todo check
      Parameter[] params = getParameters(paramList);
      if (params != null && params.length>0)
        throw new RuntimeException("Superflous parameters in terminateProcessInstance");
      
      bpeTool.terminateProcessInstance(processInstanceId);
      return new ReturnValue[0];
    } else if (m_method.equalsIgnoreCase("findProcessInstanceIds")) {
      Parameter processIdParam = stripParameter(paramList, "processId");
      if (processIdParam == null)
        throw new RuntimeException("Missing parameter processId");
      String processId = (String) processIdParam.getValue(); // todo check

      boolean onlyOpen = true;
      Parameter onlyOpenParam = stripParameter(paramList, "onlyOpen");
      if (onlyOpenParam != null)
        onlyOpen = ((Boolean) onlyOpenParam.getValue()).booleanValue(); // todo
      

      String piIds = bpeTool.findProcessInstanceIds(m_clientId, processId,
          onlyOpen, getParameters(paramList));
      return handleResult(parameters, piIds);
    } else
      throw new RuntimeException("Unknown bpe method " + m_method);
  }

  /* (non-Javadoc)
   * @see de.objectcode.canyon.spi.tool.IToolConnector#init(de.objectcode.canyon.model.application.Application)
   */
  public void init(Application application) throws Exception {
  }

}