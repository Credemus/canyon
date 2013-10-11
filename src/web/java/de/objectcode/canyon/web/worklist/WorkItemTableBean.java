package de.objectcode.canyon.web.worklist;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.api.worklist.Worklist;
import de.objectcode.canyon.web.BaseTableBean;
import de.objectcode.canyon.web.SessionData;

/**
 * @author    junglas
 * @created   9. Dezember 2003
 */
public class WorkItemTableBean extends BaseTableBean
{
  private final static  Log       log         = LogFactory.getLog( WorkItemTableBean.class );

  private               Worklist  m_worklist;


  /**
   *Constructor for the WorkItemTableBean object
   *
   * @param processData  Description of the Parameter
   * @param worklist     Description of the Parameter
   */
  public WorkItemTableBean( SessionData processData, Worklist worklist )
  {
    super( processData );

    m_worklist = worklist;

    try {
      m_rowCount = m_worklist.countWorkItems( true );

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
            m_worklist.listWorkItems( m_currentPageIdx * PAGE_SIZE, PAGE_SIZE, true ) );
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
