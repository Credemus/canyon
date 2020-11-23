package de.objectcode.canyon.spiImpl.tool;

import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.tool.IToolConnector;
import de.objectcode.canyon.spi.tool.IToolRepository;

/**
 * @author junglas
 * @created 28. Oktober 2003
 */
public class DefaultToolRepository implements IToolRepository {
  ServiceManager m_serviceManager;

  public DefaultToolRepository(ServiceManager serviceManager) {
    m_serviceManager = serviceManager;
  }

  private String getExtendedAttributeStringValue(Application toolDefinition,
      String attributeName) {
    return toolDefinition.getExtendedAttribute(attributeName) != null ? (String) toolDefinition
        .getExtendedAttribute(attributeName).getValue()
        : null;

  }

  /**
   * @param toolDefinition
   *          Description of the Parameter
   * @param id
   *          Description of the Parameter
   * @return Description of the Return Value
   * @exception RepositoryException
   *              Description of the Exception
   * @see de.objectcode.canyon.spi.tool.IToolRepository#findTool(org.obe.application.ToolDefinition,
   *      java.lang.String)
   */
  public IToolConnector findTool(Application toolDefinition, String id)
      throws RepositoryException {
    if (toolDefinition.getExtendedAttribute("canyon:toolConnectorClass") != null) {
      try {
        String connectorClassName = getExtendedAttributeStringValue(
            toolDefinition, "canyon:toolConnectorClass");
        IToolConnector connector = (IToolConnector) Class.forName(
            connectorClassName).newInstance();
        connector.init(toolDefinition);
        return connector;
      } catch (Exception e) {
        throw new RepositoryException("Cannot create connector", e);
      }
    } else if (toolDefinition.getExtendedAttribute("canyon:javaClass") != null) {
      String className = toolDefinition
          .getExtendedAttribute("canyon:javaClass").getValue();
      String methodName = toolDefinition.getExtendedAttribute(
          "canyon:javaMethod").getValue();

      if (methodName == null) {
        methodName = id;
      }

      return new JavaClassConnector(className, methodName);
    } else if ( toolDefinition.getExtendedAttribute( "canyon:bpeMethod" ) != null ) {
      String   method     = getExtendedAttributeStringValue( toolDefinition, "canyon:bpeMethod" );
      String   clientId       = getExtendedAttributeStringValue( toolDefinition, "canyon:bpeClientId" );
      String   userId       = getExtendedAttributeStringValue( toolDefinition, "canyon:bpeUserId" );
      String   processId       = getExtendedAttributeStringValue( toolDefinition, "canyon:bpeProcessId" );

      return new BPEConnector(clientId, userId, method, processId );
    } else if (toolDefinition.getExtendedAttribute("canyon:ejbJndi") != null) {
      String jndiName = toolDefinition.getExtendedAttribute("canyon:ejbJndi")
          .getValue();
      String homeClassName = toolDefinition.getExtendedAttribute(
          "canyon:ejbHome").getValue();
      String className = toolDefinition.getExtendedAttribute("canyon:ejbClass")
          .getValue();
      boolean local = "true".equalsIgnoreCase(toolDefinition
          .getExtendedAttribute("canyon:ejbLocal").getValue());
      String methodName = toolDefinition.getExtendedAttribute(
          "canyon:ejbMethod").getValue();
      String userName = toolDefinition.getExtendedAttribute("canyon:userName") != null ? toolDefinition
          .getExtendedAttribute("canyon:userName").getValue()
          : null;
      String password = toolDefinition.getExtendedAttribute("canyon:password") != null ? toolDefinition
          .getExtendedAttribute("canyon:password").getValue()
          : null;
      String policyName = toolDefinition
          .getExtendedAttribute("canyon:policyName") != null ? toolDefinition
          .getExtendedAttribute("canyon:policyName").getValue() : null;

      if (methodName == null) {
        methodName = id;
      }

      if (userName != null && password != null) {
        return new EJBConnector(userName, password, policyName, jndiName,
            homeClassName, className, local, methodName);
      } else {
        return new EJBConnector(jndiName, homeClassName, className, local,
            methodName);
      }
    }
    return null;
  }

}