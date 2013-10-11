package de.objectcode.canyon.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * @author    junglas
 * @created   24. September 2003
 */
public abstract class ComponentDispatchAction extends DispatchAction implements IComponentActionHandler
{
  protected final  String   SUBMIT_DETECT  = "submit.";

  private static   Log      log            = LogFactory.getLog( ComponentDispatchAction.class );

  protected        Class    m_clazz        = this.getClass();

  protected        HashMap  m_methods      = new HashMap();

  protected        Class    m_types[]        = {
      ActionMapping.class, ActionForm.class,
      HttpServletRequest.class, HttpServletResponse.class,
      Object.class, Map.class};


  /**
   * Gets the method attribute of the BaseComponentActionHandler object
   *
   * @param name                       Description of the Parameter
   * @return                           The method value
   * @exception NoSuchMethodException  Description of the Exception
   */
  protected Method getMethod( String name )
    throws NoSuchMethodException
  {

    synchronized ( m_methods ) {
      Method  method  = ( Method ) m_methods.get( name );
      if ( method == null ) {
        method = m_clazz.getMethod( name, m_types );
        m_methods.put( name, method );
      }
      return method;
    }

  }


  /**
   * Description of the Method
   *
   * @param mapping        Description of the Parameter
   * @param form           Description of the Parameter
   * @param request        Description of the Parameter
   * @param response       Description of the Parameter
   * @return               Description of the Return Value
   * @exception Exception  Description of the Exception
   */
  public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response )
    throws Exception
  {
    String                   componentPath  = null;
    String                   methodName     = null;
    String                   methodParam    = null;

    Enumeration              _enum           = request.getParameterNames();

    while ( _enum.hasMoreElements() ) {
      String  key  = ( String ) _enum.nextElement();
      int     idx;

      if ( ( idx = key.indexOf( SUBMIT_DETECT ) ) >= 0 ) {
        componentPath = key.substring( 0, idx );
        methodName = key.substring( idx + SUBMIT_DETECT.length() );

        if ( componentPath.endsWith( "." ) ) {
          componentPath = componentPath.substring( 0, componentPath.length() - 1 );
        }
        if ( methodName.endsWith( ".x" ) || methodName.endsWith( ".y" ) ) {
          methodName = methodName.substring( 0, methodName.length() - 2 );
        }

        idx = methodName.indexOf( '.' );

        if ( idx > 0 ) {
        	methodParam = methodName.substring(idx + 1);
          methodName = methodName.substring( 0, idx );
        }
        break;
      }
    }

    if ( componentPath == null ) {
      return defaultExecute( mapping, form, request, response );
    }

		Map methodParams = new HashMap();
		
		if ( methodParam != null )
			methodParams.put("", methodParam);
			
		_enum = request.getParameterNames();
		
		while ( _enum.hasMoreElements() ) {
			String  key  = ( String ) _enum.nextElement();
			
			if ( key.startsWith(componentPath) && key.indexOf(SUBMIT_DETECT) < 0 ) {
				String name = key.substring(componentPath.length());
				
				if ( name.startsWith("."))
					name = name.substring(1);
					
				methodParams.put(name, request.getParameter(key));
			}
		}
		
    IComponentActionHandler  handler        = this;
    Object                   component      = form;

    if ( componentPath.length() > 0 ) {
      handler = ( IComponentActionHandler ) PropertyUtils.getProperty( this, componentPath );
      component = PropertyUtils.getProperty( form, componentPath );
    }

    return handler.handle( methodName, methodParams, component, mapping, form, request, response );
  }


  /**
   * Description of the Method
   *
   * @param mapping        Description of the Parameter
   * @param form           Description of the Parameter
   * @param request        Description of the Parameter
   * @param response       Description of the Parameter
   * @return               Description of the Return Value
   * @exception Exception  Description of the Exception
   */
  public abstract ActionForward defaultExecute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response )
    throws Exception;


  /*
   *  (non-Javadoc)
   *  @see de.neutrasoft.saints.core.web.IComponentActionHandler#handle(java.lang.String, java.lang.String, java.lang.Object, org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  /**
   * Description of the Method
   *
   * @param methodName     Description of the Parameter
   * @param methodParam    Description of the Parameter
   * @param component      Description of the Parameter
   * @param mapping        Description of the Parameter
   * @param form           Description of the Parameter
   * @param request        Description of the Parameter
   * @param response       Description of the Parameter
   * @return               Description of the Return Value
   * @exception Exception  Description of the Exception
   */
  public ActionForward handle(
      String methodName,
      Map methodParams,
      Object component,
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response )
    throws Exception
  {
    // Identify the method object to be dispatched to
    Method         method   = null;
    try {
      method = getMethod( methodName );
    }
    catch ( NoSuchMethodException e ) {
      log.error( "Exception", e );
      response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          e.getMessage() );
      return null;
    }

    ActionForward  forward  = null;
    try {
      Object  args[]  = {mapping, form, request, response, component, methodParams};
      forward = ( ActionForward ) method.invoke( this, args );
    }
    catch ( ClassCastException e ) {
      log.error( "Exception", e );
      response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          e.getMessage() );
      return null;
    }
    catch ( IllegalAccessException e ) {
      log.error( "Exception", e );
      response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          e.getMessage() );
      return null;
    }
    catch ( InvocationTargetException e ) {
      Throwable  t  = e.getTargetException();
      if ( t instanceof Exception ) {
        throw ( ( Exception ) t );
      } else {
        log.error( "Exception", e );
        response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            e.getMessage() );
        return null;
      }
    }

    // Return the returned ActionForward instance
    return forward;
  }
}
