package de.objectcode.canyon.web.worklist;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;

import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.api.worklist.Worklist;
import de.objectcode.canyon.api.worklist.WorklistHome;
import de.objectcode.canyon.web.BaseEditorForm;
import de.objectcode.canyon.web.BaseTableBean;

/**
 * @struts.form name="worklistList"
 *
 * @author    junglas
 * @created   9. Dezember 2003
 */
public class WorklistForm extends BaseEditorForm
{
  private final static  Log                 log               = LogFactory.getLog( WorklistForm.class );

  private               WorkItemTableBean   m_workItemTable;
  private               WorkItemDetailBean  m_workItemDetail;
  private               Worklist            m_worklist;


  /**
   * Sets the currentId attribute of the WorklistForm object
   *
   * @param id  The new currentId value
   */
  public void setCurrentId( String id )
  {
    if ( id != null && id.length() > 0 ) {
      if ( m_workItemDetail.getCurrentWorkItem() == null || !m_workItemDetail.getCurrentWorkItem().getId().equals( id ) ) {
        try {
          WorkItemData  workItem  = m_worklist.getWorkItem( id );

          m_processData.setValue( "currentWorkItem", workItem );
        }
        catch ( Exception e ) {
          log.error( "Exception", e );
        }
      }
    }
  }


  /**
   * Gets the currentId attribute of the WorklistForm object
   *
   * @return   The currentId value
   */
  public String getCurrentId()
  {
    if ( m_workItemDetail.getCurrentWorkItem() != null ) {
      return m_workItemDetail.getCurrentWorkItem().getId();
    } else {
      return "";
    }
  }


  /**
   * @return
   */
  public WorkItemTableBean getWorkItemTable()
  {
    return m_workItemTable;
  }


  /**
   * Gets the tableBean attribute of the WorklistForm object
   *
   * @return   The tableBean value
   */
  public BaseTableBean getTableBean()
  {
    return m_workItemTable;
  }


  /**
   * @return
   */
  public WorkItemDetailBean getWorkItemDetail()
  {
    return m_workItemDetail;
  }


  /**
   * @return
   */
  public Worklist getWorklist()
  {
    return m_worklist;
  }


  /**
   * @param mapping  Description of the Parameter
   * @param request  Description of the Parameter
   * @see            org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );

    try {
      InitialContext  ctx   = new InitialContext();

      WorklistHome    home  = ( WorklistHome ) PortableRemoteObject.narrow( ctx.lookup( WorklistHome.JNDI_NAME ), WorklistHome.class );

      m_worklist = home.create();

      if ( m_processData.getValue( "currentWorkItem" ) == null ) {
        if ( request.getParameter( "currentId" ) != null && request.getParameter( "currentId" ).length() > 0 ) {
          WorkItemData  workItem  = m_worklist.getWorkItem( request.getParameter( "currentId" ) );

          m_processData.setValue( "currentWorkItem", workItem );
        }
      }

      m_workItemDetail = new WorkItemDetailBean( m_processData );
      m_workItemTable = new WorkItemTableBean( m_processData, m_worklist );
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
    }
  }

}
