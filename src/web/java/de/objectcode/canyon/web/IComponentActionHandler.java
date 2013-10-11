package de.objectcode.canyon.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author    junglas
 * @created   24. September 2003
 */
public interface IComponentActionHandler
{
  /**
   * Description of the Method
   *
   * @param methodName     Description of the Parameter
   * @param methodParam    Description of the Parameter
   * @param mapping        Description of the Parameter
   * @param form           Description of the Parameter
   * @param request        Description of the Parameter
   * @param response       Description of the Parameter
   * @return               Description of the Return Value
   * @exception Exception  Description of the Exception
   */
  public ActionForward handle( String methodName, Map methodParams, Object component,
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response )
    throws Exception;
}
