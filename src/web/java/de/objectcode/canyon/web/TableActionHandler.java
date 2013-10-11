package de.objectcode.canyon.web;

import java.util.Enumeration;
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
public class TableActionHandler extends BaseComponentActionHandler
{
	private TableRowActionHandler m_rowHandler = new TableRowActionHandler();
	private TableIntervalActionHandler m_intervalHandler = new TableIntervalActionHandler();
	private int intervalStart;
	private int intervalEnd;
	private int currentPageNumber;
	private long currentOid;
	
	
	public IComponentActionHandler getCurrentPage ( int rowIdx )
	{
		return m_rowHandler;
	}
	
	public IComponentActionHandler getCurrentPageInterval(int pageIdx){
		return m_intervalHandler;
	}
	
	private void setIntevalParameter(HttpServletRequest request){
		Enumeration _enum = request.getParameterNames();
	  String key ="";
		while(_enum.hasMoreElements()){
			key=(String)_enum.nextElement();
			if(key.endsWith(BaseTableBean.KEY_NAME_INTERVAL_START)){
				intervalStart=Integer.parseInt((String)request.getParameterValues(key)[0]);
			}
			if(key.endsWith(BaseTableBean.KEY_NAME_INTERVAL_END)){
				intervalEnd=Integer.parseInt((String)request.getParameterValues(key)[0]);
			}
			if(key.endsWith("currentPageIdx")){
				currentPageNumber=Integer.parseInt((String)request.getParameterValues(key)[0]);
			}
			if(key.endsWith("currentOid")){
				currentOid=Long.parseLong((String)request.getParameterValues(key)[0]);
			}
		}
	}
	
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
  public ActionForward first(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response,
      Object component,
      Map methodParams
       )
    throws Exception
  {
  	BaseTableBean tableBean = (BaseTableBean)component;
		setIntevalParameter(request);
  	tableBean.setCurrentPageIdx(0);

		return mapping.findForward("success");
  }

	public ActionForward prev(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response,
			Object component,
			Map methodParams
			 )
		throws Exception
	{
		BaseTableBean tableBean = (BaseTableBean)component;

		setIntevalParameter(request);

    tableBean.setCurrentPageIdx(currentPageNumber-1);
    tableBean.setPageInterval(intervalStart,intervalEnd);
    
		return mapping.findForward("success");
	}
	
	public ActionForward next(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response,
			Object component,
			Map methodParams
			 )
		throws Exception
	{
		BaseTableBean tableBean = (BaseTableBean)component;
		setIntevalParameter(request);

    tableBean.setCurrentPageIdx(currentPageNumber+1);
    tableBean.setPageInterval(intervalStart,intervalEnd);
    
		return mapping.findForward("success");
	}

	public ActionForward last(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response,
			Object component,
			Map methodParams
			 )
		throws Exception
	{
		BaseTableBean tableBean = (BaseTableBean)component;

		setIntevalParameter(request);

    tableBean.setCurrentPageIdx(tableBean.getPageCount()-1);
		tableBean.setPageInterval(intervalStart,intervalEnd);
    
		return mapping.findForward("success");
	}
	
	public ActionForward prevPageInterval(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response,
			Object component,
			Map methodParams
			 )
		throws Exception
	{

		BaseTableBean tableBean = (BaseTableBean)component;

		setIntevalParameter(request);

    tableBean.setCurrentPageIdx(intervalStart-1);
		tableBean.setPageInterval(intervalStart,intervalEnd);
    
		return mapping.findForward("success");
	}
	
	public ActionForward nextPageInterval(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response,
			Object component,
			Map methodParams
			 )
		throws Exception
	{
		BaseTableBean tableBean = (BaseTableBean)component;

		setIntevalParameter(request);

    tableBean.setCurrentPageIdx(intervalEnd+1);
		tableBean.setPageInterval(intervalStart,intervalEnd);
    
		return mapping.findForward("success");
	}
}
