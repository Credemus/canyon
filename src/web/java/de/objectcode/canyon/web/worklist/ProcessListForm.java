package de.objectcode.canyon.web.worklist;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;

import de.objectcode.canyon.api.bpe.BPEProcess;
import de.objectcode.canyon.api.worklist.ProcessData;
import de.objectcode.canyon.api.bpe.BPEProcessHome;
import de.objectcode.canyon.web.BaseEditorForm;
import de.objectcode.canyon.web.BaseTableBean;

/**
 * @struts.form name="processList"
 *
 * @author    junglas
 * @created   11. Dezember 2003
 */
public class ProcessListForm extends BaseEditorForm
{
  private final static  Log                log              = LogFactory.getLog( ProcessListForm.class );

  private               BPEProcess            m_process;
  private               ProcessTableBean   m_processTable;
  private               ProcessDetailBean  m_processDetail;


  /**
   * @param id  The new currentId value
   * @see       de.objectcode.canyon.web.BaseEditorForm#setCurrentId(java.lang.String)
   */
  public void setCurrentId( String id )
  {
    if ( id != null && id.length() > 0 ) {
      if ( m_processDetail.getCurrentWorkItem() == null || !m_processDetail.getCurrentWorkItem().getId().equals( id ) ) {
        try {
          ProcessData  process  = m_process.getProcessDefinition( id );

          m_processData.setValue( "currentProcess", process );
        }
        catch ( Exception e ) {
          log.error( "Exception", e );
        }
      }
    }
  }


  /**
   * @return
   */
  public BPEProcess getProcess()
  {
    return m_process;
  }


  /**
   * @return
   */
  public ProcessDetailBean getProcessDetail()
  {
    return m_processDetail;
  }


  /**
   * @return
   */
  public ProcessTableBean getProcessTable()
  {
    return m_processTable;
  }


  /**
   * @return   The tableBean value
   * @see      de.objectcode.canyon.web.BaseEditorForm#getTableBean()
   */
  public BaseTableBean getTableBean()
  {
    return m_processTable;
  }


  /**
   * Description of the Method
   *
   * @param mapping  Description of the Parameter
   * @param request  Description of the Parameter
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );

    try {
      InitialContext  ctx   = new InitialContext();

      BPEProcessHome     home  = ( BPEProcessHome ) PortableRemoteObject.narrow( ctx.lookup( BPEProcessHome.JNDI_NAME ), BPEProcessHome.class );

      m_process = home.create();

      if ( m_processData.getValue( "currentProcess" ) == null ) {
        if ( request.getParameter( "currentId" ) != null && request.getParameter( "currentId" ).length() > 0 ) {
          ProcessData  process  = m_process.getProcessDefinition( request.getParameter( "currentId" ) );

          m_processData.setValue( "currentProcess", process );
        }
      }

      m_processDetail = new ProcessDetailBean( m_processData );
      m_processTable = new ProcessTableBean( m_processData, m_process );
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
    }
  }

}
