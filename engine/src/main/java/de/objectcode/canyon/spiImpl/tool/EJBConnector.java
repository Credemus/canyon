package de.objectcode.canyon.spiImpl.tool;

import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.BasicType;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.tool.IToolConnector;
import de.objectcode.canyon.spi.tool.BPEContext;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.spi.tool.ReturnValue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.security.auth.login.LoginContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author junglas
 * @created 28. Oktober 2003
 */
public class EJBConnector implements IToolConnector {
	private final static Log log = LogFactory.getLog(EJBConnector.class);

	protected String m_jndiName;

	protected String m_homeClassName;

	protected String m_className;

	protected boolean m_local;

	protected String m_methodName;

	protected String m_userName;

	protected String m_password;

	protected String m_policyName;

	/**
	 * Constructor for the EJBConnector object
	 * 
	 * @param jndiName
	 *            Description of the Parameter
	 * @param homeClassName
	 *            Description of the Parameter
	 * @param className
	 *            Description of the Parameter
	 * @param local
	 *            Description of the Parameter
	 * @param methodName
	 *            Description of the Parameter
	 * @param serviceManager
	 *            Description of the Parameter
	 */
	public EJBConnector(String jndiName, String homeClassName,
			String className, boolean local, String methodName) {
		m_jndiName = jndiName;
		m_homeClassName = homeClassName;
		m_className = className;
		m_local = local;
		m_methodName = methodName;
	}

	/**
	 * Constructor for the EJBConnector object
	 * 
	 * @param userName
	 *            Description of the Parameter
	 * @param password
	 *            Description of the Parameter
	 * @param policyName
	 *            Description of the Parameter
	 * @param jndiName
	 *            Description of the Parameter
	 * @param homeClassName
	 *            Description of the Parameter
	 * @param className
	 *            Description of the Parameter
	 * @param local
	 *            Description of the Parameter
	 * @param methodName
	 *            Description of the Parameter
	 * @param serviceManager
	 *            Description of the Parameter
	 */
	public EJBConnector(String userName, String password, String policyName,
			String jndiName, String homeClassName, String className,
			boolean local, String methodName) {
		m_jndiName = jndiName;
		m_homeClassName = homeClassName;
		m_className = className;
		m_local = local;
		m_methodName = methodName;
		m_userName = userName;
		m_password = password;
		m_policyName = policyName;
	}

	public Object getSemaphore(BPEContext context) {
		return this;
	}

	/**
	 * Description of the Method
	 * 
	 * @param params
	 *            Description of the Parameter
	 * @param result
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected ReturnValue[] handleResult(Parameter[] params, Object result) {
		if (log.isDebugEnabled()) {
			log.debug("handleResult: " + result);
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
				log.debug("RESULT " + params[i].actualName + " = "
						+ resultElements[j]);
				if (params[i].mode == ParameterMode.OUT
						|| params[i].mode == ParameterMode.INOUT) {
					values.add(new ReturnValue(params[i].actualName,
							resultElements[j++]));
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

	protected Object[] getParameterValues(Parameter[] params) {
		List parameterValueList = new ArrayList();
		int i;

		for (i = 0; i < params.length; i++) {
			if (params[i].mode == ParameterMode.IN
					|| params[i].mode == ParameterMode.INOUT) {
				if (params[i].dataType instanceof BasicType) {
					BasicType type = (BasicType) params[i].dataType;

					switch (type.getValue()) {
					case BasicType.DATETIME_INT:
						parameterValueList.add(params[i].value);
						break;
					case BasicType.BOOLEAN_INT:
						parameterValueList.add(new Boolean(params[i].value
								.toString()));

						break;
					case BasicType.FLOAT_INT:
						parameterValueList.add(new Float(params[i].value
								.toString()));

						break;
					case BasicType.INTEGER_INT:
						parameterValueList.add(new Integer((int) Float
								.parseFloat(params[i].value.toString())));

						break;
					case BasicType.STRING_INT:
						if (params[i].value != null) {
							parameterValueList.add(params[i].value.toString());
						} else {
							parameterValueList.add(null);
						}

						break;
					}
				}
			}
		}

		Object[] parameterValues = new Object[parameterValueList.size()];

		return parameterValueList.toArray(parameterValues);
	}

	protected Class[] getParameterTypes(Parameter[] params) {
		List parameterTypeList = new ArrayList();
		int i;

		for (i = 0; i < params.length; i++) {
			if (params[i].mode == ParameterMode.IN
					|| params[i].mode == ParameterMode.INOUT) {
				if (params[i].dataType instanceof BasicType) {
					BasicType type = (BasicType) params[i].dataType;

					switch (type.getValue()) {
					case BasicType.DATETIME_INT:
						parameterTypeList.add(Date.class);
						break;
					case BasicType.BOOLEAN_INT:
						parameterTypeList.add(Boolean.TYPE);
						break;
					case BasicType.FLOAT_INT:
						parameterTypeList.add(Float.TYPE);
						break;
					case BasicType.INTEGER_INT:
						parameterTypeList.add(Integer.TYPE);
						break;
					case BasicType.STRING_INT:
						parameterTypeList.add(String.class);
						break;
					}
				}
			}
		}

		Class[] parameterTypes = new Class[parameterTypeList.size()];
		parameterTypeList.toArray(parameterTypes);
		return parameterTypes;
	}

	protected LoginContext performLogin(BPEContext context)
			throws Exception {
		LoginContext loginContext = null;

		if (m_userName != null) {
			if (log.isDebugEnabled()) {
				log.debug("USERNAME=" + m_userName);
				log.debug("PASSWORD=" + m_password);
				log.debug("POLICYNAME=" + m_policyName);
			}

			UsernamePasswordHandler handler = new UsernamePasswordHandler(
					m_userName, m_password);
			loginContext = new LoginContext(m_policyName, handler);
			loginContext.login();
		}

		return loginContext;
	}

	protected void performLogout(BPEContext context,
			LoginContext loginContext) throws Exception {
		loginContext.logout();
	}

    
    public class EJBInvoker extends Thread {
        BPEContext context;
        Parameter[] params;
        Throwable throwable;
        ReturnValue[] result;
        public EJBInvoker(BPEContext context, Parameter[] params) {
            super();
            this.context = context;
            this.params = params;
            result = new ReturnValue[0];
        }
        public Throwable getThrowable() {
            return throwable;
        }
        public void run() {
            try {
                if (log.isDebugEnabled()) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("context=").append(context);
                    buffer.append("method=").append(m_methodName);
                    buffer.append(",className=").append(m_className);
                    buffer.append(",homeClassName=").append(m_homeClassName);
                    buffer.append(",jndiName=").append(m_jndiName);
                    buffer.append(",local=").append(m_local);
                    buffer.append(",");
                    int i;

                    for (i = 0; i < params.length; i++) {
                        buffer.append(params[i]).append(',');
                    }

                    log.debug("invoke: " + buffer.toString());
                }

                Class homeClass = Class.forName(m_homeClassName);
                Class clazz = Class.forName(m_className);

                // Currently only create method without parameters
                Method createMethod = homeClass.getMethod("create", new Class[] {});

                InitialContext ctx = new InitialContext();

                synchronized (getSemaphore(context)) {
                    LoginContext loginContext = performLogin(context);

                    Class[] parameterTypes = getParameterTypes(params);

                    Object[] parameterValues = getParameterValues(params);

                    if (m_local) {
                        EJBLocalHome home = (EJBLocalHome) ctx.lookup(m_jndiName);

                        EJBLocalObject ejbObj = (EJBLocalObject) createMethod
                                .invoke(home, new Object[] {});

                        Method method = clazz.getMethod(m_methodName,
                                parameterTypes);

                        Object result_tmp = method.invoke(ejbObj, parameterValues);

                        if (loginContext != null) {
                            performLogout(context, loginContext);
                        }

                        result = handleResult(params, result_tmp);
                        return;
                    } else {
                        EJBHome home = (EJBHome) PortableRemoteObject.narrow(ctx
                                .lookup(m_jndiName), homeClass);

                        EJBObject ejbObj = (EJBObject) createMethod.invoke(home,
                                new Object[] {});

                        Method method = clazz.getMethod(m_methodName,
                                parameterTypes);

                        Object result_tmp = method.invoke(ejbObj, parameterValues);

                        if (loginContext != null) {
                            performLogout(context, loginContext);
                        }

                        result = handleResult(params, result_tmp);
                        return;
                    }
                }
            } catch (Exception e) {
                throwable=e;
                log.error("ERROR:", e);
            }
        }
        public ReturnValue[] getResult() {
            return result;
        }
        
       
    }
	/**
	 * Description of the Method
	 * 
	 * @param params
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public ReturnValue[] invoke(BPEContext context, Parameter[] params)
			throws Exception {
        try {
//        txManager.begin();
          
          EJBInvoker runner = new EJBInvoker(context, params);
          runner.start();
          
          runner.join();
          Throwable th = runner.getThrowable();
          if (th!=null) throw th;
          return runner.getResult();
          
        } catch (Throwable t) {
          throw new RuntimeException(t.getCause().getMessage(), t); //"ROLLBACK:"+
        }        
	}

  /* (non-Javadoc)
   * @see de.objectcode.canyon.spi.tool.IToolConnector#init(de.objectcode.canyon.model.application.Application)
   */
  public void init(Application application) throws Exception {
    // TODO Auto-generated method stub
    
  }
}