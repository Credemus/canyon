package de.objectcode.canyon.web;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author    poseidon
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 * @created   9. Dezember 2003
 */
public class TableIntervalActionHandler extends BaseComponentActionHandler
{
  public static  String  KEY_NAME_METHOD  = "selectPage";
  private        int     intervalStart;
  private        int     intervalEnd;
  private        long    currentOid;


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
  public ActionForward selectPage( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Object component, Map methodParams )
    throws Exception
  {
    Enumeration     _enum                = request.getParameterNames();
    String          key                 = "";
    int             selectedPageNumber  = 0;

    while ( _enum.hasMoreElements() ) {
      key = ( String ) _enum.nextElement();

      if ( key.endsWith( KEY_NAME_METHOD ) ) {
        selectedPageNumber = Integer.parseInt( ( String ) request.getParameterValues( key )[0] ) - 1;
      }

      if ( key.endsWith( BaseTableBean.KEY_NAME_INTERVAL_START ) ) {
        intervalStart = Integer.parseInt( ( String ) request.getParameterValues( key )[0] );
      }

      if ( key.endsWith( BaseTableBean.KEY_NAME_INTERVAL_END ) ) {
        intervalEnd = Integer.parseInt( ( String ) request.getParameterValues( key )[0] );
      }

      if ( key.endsWith( "currentOid" ) ) {
        currentOid = Long.parseLong( ( String ) request.getParameterValues( key )[0] );
      }
    }

    BaseTableBean tableBean = (BaseTableBean)component;

    tableBean.setCurrentPageIdx( selectedPageNumber );
    tableBean.setPageInterval( intervalStart, intervalEnd );
    
    return mapping.findForward( "success" );
  }
}
