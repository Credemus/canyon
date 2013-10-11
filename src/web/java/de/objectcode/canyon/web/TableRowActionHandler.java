package de.objectcode.canyon.web;

import java.util.Enumeration;
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
public class TableRowActionHandler extends BaseComponentActionHandler
{
  private static final Log log = LogFactory.getLog(TableRowActionHandler.class);
  
  private  int  intervalStart;
  private  int  intervalEnd;


  /**
   * Description of the Method
   *
   * @param mapping        Description of the Parameter
   * @param form           Description of the Parameter
   * @param request        Description of the Parameter
   * @param response       Description of the Parameter
   * @param component      Description of the Parameter
   * @param methodParam    Description of the Parameter
   * @return               Description of the Return Value
   * @exception Exception  Description of the Exception
   */
  public ActionForward select(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response,
      Object component,
      Map methodParam
       )
    throws Exception
  {
    if ( log.isDebugEnabled() ) {
      log.debug ( "select: " + form + " " + component + " " + methodParam );
    }
    
    if ( methodParam.get( "id" ) != null ) {
      String          id                 = methodParam.get( "id" ).toString();
      Enumeration    _enum                = request.getParameterNames();
      String         key                 = "";
      int            selectedPageNumber  = 0;
      while ( _enum.hasMoreElements() ) {
        key = ( String ) _enum.nextElement();
        if ( key.endsWith( BaseTableBean.KEY_NAME_INTERVAL_START ) ) {
          intervalStart = Integer.parseInt( ( String ) request.getParameterValues( key )[0] );
        }
        if ( key.endsWith( BaseTableBean.KEY_NAME_INTERVAL_END ) ) {
          intervalEnd = Integer.parseInt( ( String ) request.getParameterValues( key )[0] );
        }
        if ( key.endsWith( "currentPageIdx" ) ) {
          selectedPageNumber = Integer.parseInt( ( String ) request.getParameterValues( key )[0] );
        }
      }

      BaseEditorForm editorForm = (BaseEditorForm)form;
      BaseTableBean  tableBean           = editorForm.getTableBean();

      tableBean.setCurrentPageIdx( selectedPageNumber );
      tableBean.setPageInterval( intervalStart, intervalEnd );
      
      
      editorForm.setCurrentId(id);
    }
    return mapping.findForward( "success" );
  }
}
