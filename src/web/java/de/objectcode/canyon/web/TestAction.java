package de.objectcode.canyon.web;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @struts.action name="testForm" path="/test/testAction"
 *   scope="request" validate="false"
 * @struts.action-forward name="success" path="/worklist/processList.jsp"
 * 
 * @author junglas
 */
public class TestAction extends Action
{
  /**
   * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  public ActionForward execute ( ActionMapping arg0, ActionForm arg1,
      HttpServletRequest arg2, HttpServletResponse arg3 ) throws Exception
  {
    System.out.println(">>>>>>>>> GO!!!!");
    
    try {
      InitialContext ctx = new InitialContext();
      
      Context securityCtx = (Context) ctx.lookup("java:comp/env/security");
      Object securityMgr = securityCtx.lookup("securityMgr");
      
      System.out.println(securityMgr);
      System.out.println(securityMgr.getClass());
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }
    return super.execute(arg0, arg1, arg2, arg3);
  }

}
