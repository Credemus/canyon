package de.objectcode.canyon.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author    junglas
 * @created   30. September 2003
 */
public abstract class BaseEditorForm extends ActionForm
{
  protected  SessionData  m_processData;

  /**
   * Gets the processData attribute of the BaseEditorForm object
   *
   * @return   The processData value
   */
  public String getProcessData()
  {
    return m_processData.getEncoded();
  }

	public SessionData getProcessDataObj()
	{
		return m_processData;
	}
 
  public abstract void setCurrentId ( String id );

  public abstract BaseTableBean getTableBean();
  
  /**
   * Description of the Method
   *
   * @param mapping  Description of the Parameter
   * @param request  Description of the Parameter
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    if ( request.getAttribute( "processData" ) != null ) {
      m_processData = ( SessionData ) request.getAttribute( "processData" );
    } else {
      m_processData = new SessionData( request.getParameter( "processData" ) );
      request.setAttribute( "processData", m_processData );
    }
  }
}
