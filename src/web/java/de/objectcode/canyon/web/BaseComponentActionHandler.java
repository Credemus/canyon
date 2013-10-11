package de.objectcode.canyon.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author    junglas
 * @created   24. September 2003
 */
public abstract class BaseComponentActionHandler implements IComponentActionHandler
{
  private static  Log      log        = LogFactory.getLog( BaseComponentActionHandler.class );

  protected       Class    m_clazz    = this.getClass();

  protected       HashMap  m_methods  = new HashMap();

  protected       Class    m_types[]    = {
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
