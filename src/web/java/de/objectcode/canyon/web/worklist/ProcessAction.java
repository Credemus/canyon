package de.objectcode.canyon.web.worklist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.objectcode.canyon.web.ComponentDispatchAction;
import de.objectcode.canyon.web.TableActionHandler;

/**
 * @struts.action name="processList" path="/worklist/processList"
 *   scope="request" validate="false"
 * @struts.action-forward name="success" path="/worklist/processList.jsp"
 *
 *
 * @author    junglas
 * @created   11. Dezember 2003
 */
public class ProcessAction extends ComponentDispatchAction
{
  private final static  Log                 log              = LogFactory.getLog( WorklistAction.class );

  private               TableActionHandler  m_workItemTable  = new TableActionHandler();


  /**
   * Gets the workItemTable attribute of the WorklistAction object
   *
   * @return   The workItemTable value
   */
  public TableActionHandler getWorkItemTable()
  {
    return m_workItemTable;
  }


  /**
   * @param mapping        Description of the Parameter
   * @param form           Description of the Parameter
   * @param request        Description of the Parameter
   * @param response       Description of the Parameter
   * @return               Description of the Return Value
   * @exception Exception  Description of the Exception
   * @see                  de.objectcode.canyon.web.ComponentDispatchAction#defaultExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  public ActionForward defaultExecute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response )
    throws Exception
  {
    log.warn( "defaultExecute: " + form );

    return null;
  }

}
