package de.objectcode.canyon.web.worklist;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.wfmc.wapi.WMWorkItemState;

import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.web.ComponentDispatchAction;
import de.objectcode.canyon.web.TableActionHandler;

/**
 * @struts.action name="worklistList" path="/worklist/list"
 *   scope="request" validate="false"
 * @struts.action-forward name="success" path="/worklist/list.jsp"
 *
 * @author    junglas
 * @created   9. Dezember 2003
 */
public class WorklistAction extends ComponentDispatchAction
{
  private final static  Log                 log                = LogFactory.getLog( WorklistAction.class );

  private               TableActionHandler  m_workItemTable    = new TableActionHandler();

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


  /**
   * Description of the Method
   *
   * @param mapping        Description of the Parameter
   * @param form           Description of the Parameter
   * @param request        Description of the Parameter
   * @param response       Description of the Parameter
   * @param component      Description of the Parameter
   * @param methodParams   Description of the Parameter
   * @return               Description of the Return Value
   * @exception Exception  Description of the Exception
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Object component, Map methodParams )
    throws Exception
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "save" );
    }

    WorklistForm  editorForm  = ( WorklistForm ) form;

    editorForm.getWorklist().updateWorkItem( editorForm.getWorkItemDetail().getCurrentWorkItem() );

    return mapping.findForward( "success" );
  }


  /**
   * Description of the Method
   *
   * @param mapping        Description of the Parameter
   * @param form           Description of the Parameter
   * @param request        Description of the Parameter
   * @param response       Description of the Parameter
   * @param component      Description of the Parameter
   * @param methodParams   Description of the Parameter
   * @return               Description of the Return Value
   * @exception Exception  Description of the Exception
   */
  public ActionForward complete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Object component, Map methodParams )
    throws Exception
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "complete" );
    }

    WorklistForm  editorForm  = ( WorklistForm ) form;

    WorkItemData  workItem    = editorForm.getWorkItemDetail().getCurrentWorkItem();

    workItem.setState( WMWorkItemState.CLOSED_COMPLETED );

    editorForm.getWorklist().updateWorkItem( workItem );
    editorForm.getWorkItemTable().clear();

    return mapping.findForward( "success" );
  }
}
