package de.objectcode.canyon.jmx.async;

import java.lang.reflect.Method;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.security.auth.login.LoginContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.engine.variable.ComplexValue;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spiImpl.tool.UsernamePasswordHandler;

/**
 * @author    junglas
 * @created   13. Juli 2004
 */
public class AsyncExecuteEJB extends AsyncRequest
{
  private final static Log log = LogFactory.getLog(AsyncExecuteEJB.class);
  
  final static   long     serialVersionUID     = -1828195700193336893L;

  private final  String   m_processInstanceId;
  private final  String   m_activityId;
  private final  String   m_jndiName;
  private final  String   m_homeClassName;
  private final  String   m_className;
  private final  boolean  m_local;
  private final  String   m_methodName;
  private final  String   m_userName;
  private final  String   m_password;
  private final  String   m_policyName;
  private final  Class[]  m_parameterTypes;
  private final  Object[] m_parameterValues;

  public AsyncExecuteEJB ( String processInstanceId, String activityId, String jndiName, String homeClassName, String className, boolean local,
                           String methodName, String userName, String password, String policyName, 
                           Class[] parameterTypes, Object[] parameterValues )
  {
    m_processInstanceId = processInstanceId;
    m_activityId = activityId;
    m_jndiName = jndiName;
    m_homeClassName = homeClassName;
    m_className = className;
    m_local = local;
    m_methodName = methodName;
    m_userName = userName;
    m_password = password;
    m_policyName = policyName;
    m_parameterTypes = parameterTypes;
    m_parameterValues = parameterValues;
  }

  /**
   * Description of the Method
   *
   * @param serviceManager           Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public void execute( ServiceManager serviceManager )
    throws WMWorkflowException 
  {
    try {
      Class           homeClass           = Class.forName( m_homeClassName );
      Class           clazz               = Class.forName( m_className );
            
      // Currently only create method without parameters
      Method          createMethod        = homeClass.getMethod( "create", new Class[]{} );

      InitialContext  ctx                 = new InitialContext();
      LoginContext    loginContext        = null;

      if ( m_userName != null ) {
        if ( log.isDebugEnabled() ) {
          log.debug( "USERNAME=" + m_userName );
          log.debug( "PASSWORD=" + m_password );
          log.debug( "POLICYNAME=" + m_policyName );
        }

        UsernamePasswordHandler  handler  = new UsernamePasswordHandler( m_userName,
            m_password );
        loginContext = new LoginContext( m_policyName, handler );
        loginContext.login();
      }
      
      Object result = null;
      
      if ( m_local ) {
        EJBLocalHome    home    = ( EJBLocalHome ) ctx.lookup( m_jndiName );

        EJBLocalObject  ejbObj  = ( EJBLocalObject ) createMethod.invoke( home,
            new Object[]{} );

        Method          method  = clazz.getMethod( m_methodName, m_parameterTypes );

        result  = method.invoke( ejbObj, m_parameterValues );

        if ( loginContext != null ) {
          loginContext.logout();
        }
      } else {
        EJBHome    home    = ( EJBHome ) PortableRemoteObject.narrow( ctx.lookup(
            m_jndiName ), homeClass );

        EJBObject  ejbObj  = ( EJBObject ) createMethod.invoke( home, new Object[]{} );

        Method     method  = clazz.getMethod( m_methodName, m_parameterTypes );

        result  = method.invoke( ejbObj, m_parameterValues );

        if ( loginContext != null ) {
          loginContext.logout();
        }        
      }
      
      ComplexValue content = new ComplexValue(new ComplexType("ejb-result"));
      content.set("processId", m_processInstanceId);
      content.set("activityId", m_activityId);
      content.set("result", result);
      Message message = new Message("ejb-result", content);
      
      serviceManager.getBpeEngine().handleMessage(m_processInstanceId, message);
    }
    catch ( Exception e ) {
      log.error("Exception", e);
      throw new WMWorkflowException(e);
    }
  }

  public void fail ( ServiceManager serviceManager ) 
  {
  }
}
