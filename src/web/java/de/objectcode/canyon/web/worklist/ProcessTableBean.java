package de.objectcode.canyon.web.worklist;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.api.bpe.BPEProcess;
import de.objectcode.canyon.web.BaseTableBean;
import de.objectcode.canyon.web.SessionData;

/**
 * @author    junglas
 * @created   11. Dezember 2003
 */
public class ProcessTableBean extends BaseTableBean
{
  private final static  Log      log        = LogFactory.getLog( ProcessTableBean.class );

  private               BPEProcess  m_process;


  /**
   *Constructor for the WorkItemTableBean object
   *
   * @param processData  Description of the Parameter
   * @param process      Description of the Parameter
   */
  public ProcessTableBean( SessionData processData, BPEProcess process )
  {
    super( processData );

    m_process = process;

    try {
      m_rowCount = m_process.countProcesses();

      if ( log.isDebugEnabled() ) {
        log.debug( "Rows: " + m_rowCount );
      }
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
      e.printStackTrace();
    }
    setCurrentPageIdx( 0 );
  }


  /**
   * Gets the currentPage attribute of the WorkItemTableBean object
   *
   * @return   The currentPage value
   */
  public List getCurrentPage()
  {
    if ( m_currentPage == null ) {
      try {
        m_currentPage = Arrays.asList(
        m_process.listProcesses( m_currentPageIdx * PAGE_SIZE, PAGE_SIZE ) );
      }
      catch ( Exception e ) {
        log.error( "Exception", e );
        e.printStackTrace();
      }
    }

    return m_currentPage;
  }


  /**
   * Description of the Method
   */
  public void clear()
  {
    m_currentPage = null;
  }

}
