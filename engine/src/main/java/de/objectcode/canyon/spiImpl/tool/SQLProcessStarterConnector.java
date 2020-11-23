package de.objectcode.canyon.spiImpl.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.api.worklist.ProcessData;
import de.objectcode.canyon.api.worklist.ProcessInstanceData;
import de.objectcode.canyon.bpe.util.ExtendedAttributeHelper;
import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.BasicType;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.tool.BPEContext;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.spi.tool.ReturnValue;
import de.objectcode.canyon.spiImpl.tool.bpe.BPEFacade;

public class SQLProcessStarterConnector extends SQLConnector {
  private final static Log LOGGER = LogFactory
      .getLog(SQLProcessStarterConnector.class);

  private final static String SERVICEMANAGER_JNDI_NAME = "java:/canyon/ServiceManager";

  protected String m_processId;
  
  protected boolean m_enforceUniqueness;

  protected String m_userId;

  private BPEFacade fBPEFacade;

  public void init(Application application) throws Exception {
    super.init(application);
    ExtendedAttributeHelper eah = new ExtendedAttributeHelper(application.getExtendedAttributes());
    m_processId = eah.getMandatoryValue("canyon:sqlProcessStarterConnector:processId");
    m_userId = eah.getMandatoryValue("canyon:sqlProcessStarterConnector:userId");
    m_enforceUniqueness = eah.getOptionalBooleanValue("canyon:sqlProcessStarterConnector:enforceUniqueness",true);
  }

  protected BPEFacade getBPEFacade() {

    if (fBPEFacade == null) {
      try {
        InitialContext ctx = new InitialContext();
        ServiceManager serviceManager = (ServiceManager) ctx
            .lookup(SERVICEMANAGER_JNDI_NAME);
        fBPEFacade = new BPEFacade(serviceManager.getBpeEngine());

      } catch (Throwable t) {
        handleException("Cannot get ServiceManager", t);
      }

    }
    return fBPEFacade;
  }

  protected void handleException(String message, Throwable t)
      throws RuntimeException {
    if (t instanceof RuntimeException) {
      throw (RuntimeException) t;
    } else {
      LOGGER.error(message, t);
      throw new RuntimeException(message, t);
    }
  }

  public ReturnValue[] invoke(BPEContext context, Parameter[] parameters)
      throws Exception {
    String contextDescriptor = null;
    if (LOGGER.isDebugEnabled()) {
      contextDescriptor = getContextDescriptor(context, parameters);
    }

    ReturnValue[] ret = null;
    Connection connection = null;
    PreparedStatement stmnt = null;
    ResultSet rs = null;
    try {
      connection = getConnection(context);
      stmnt = prepareStatement(parameters, connection);
      rs = stmnt.executeQuery(); // TODO yx handle update, insert

      List processIds = new ArrayList();
      StringBuffer buffy = new StringBuffer();
      while (rs.next()) {
        String processId = startProcess(context, rs);
        if (processId != null) {
					processIds.add(processId);
					if (buffy.length() == 0) {
						buffy.append(";");
					}
					buffy.append(processId);
				}
      }

      Parameter singleStringOutParameter = getSingleOutParameter(parameters,
          BasicType.STRING);
      // TODO handle Arrays

      if (singleStringOutParameter != null) {
        ret = new ReturnValue[] { new ReturnValue(
            singleStringOutParameter.actualName, buffy.toString()) };
      } else {
        ret = new ReturnValue[0];
      }
      return ret;
    } catch (Exception e) {
      LOGGER.error("Error in SQLProcessStarterConnector [" + contextDescriptor
          + "]", e);
      throw e;
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (Exception e) {
        }
      }
      if (stmnt != null) {
        try {
          stmnt.close();
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

  protected String startProcess(BPEContext context, ResultSet rs)
      throws Exception {
    try {
    	// jv 02.10.2006 18:51:11
		// This gets package-revision compatible definition
//      ProcessData pd = getBPEFacade().getProcessDefinition(getProcessId());
      ProcessData pd = getBPEFacade().getMatchingProcessDefinition(getProcessId(), 
    		  context.getParentProcessInstanceIdPath());
      ProcessData.Parameter[] parameters = pd.getParameters();
      for (int i = 0; i < parameters.length; i++) {
        ProcessData.Parameter parameter = parameters[i];
        int type = parameter.getType();
        switch (type) {
        case BasicType.STRING_INT:
          parameter.setValue(rs.getString(parameter.getName()));
          break;
        case BasicType.FLOAT_INT:
          parameter.setValue(new Float(rs.getFloat(parameter.getName())));
          break;
        case BasicType.INTEGER_INT:
          parameter.setValue(new Integer(rs.getInt(parameter.getName())));
          break;
        case BasicType.DATETIME_INT:
          parameter.setValue(rs.getDate(parameter.getName()));
          break;
        case BasicType.BOOLEAN_INT:
          parameter.setValue(new Boolean(rs.getBoolean(parameter.getName())));
          break;
        case BasicType.REFERENCE_INT:
          LOGGER.warn("Unsupported parameter type 'reference'");
          break;
        case BasicType.PERFORMER_INT:
          LOGGER.warn("Unsupported parameter type 'performer'");
          break;
        }
      }
      // TODO check signature
      if (m_enforceUniqueness) {
	      ProcessInstanceData pid = getBPEFacade().createAndStartUniqueProcessInstance(
	          m_userId, context.getClientId(), pd, context.getParentProcessInstanceIdPath());
	      if (pid!=null)
	      	return pid.getId();
	      else
	      	return null;
      } else {
        ProcessInstanceData pid = getBPEFacade().createAndStartProcessInstance(
            m_userId, context.getClientId(), pd, context.getParentProcessInstanceIdPath());
        return pid.getId();      	
      }
    } catch (Exception e) {
      handleException("Cannot start process", e);
    }
    return null;
  }

  public String getProcessId() {
    return m_processId;
  }
}