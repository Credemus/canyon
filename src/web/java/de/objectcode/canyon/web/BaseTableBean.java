package de.objectcode.canyon.web;

import java.util.List;

/**
 * @author    junglas
 * @created   24. September 2003
 */
public class BaseTableBean
{
  protected final static  int          PAGE_SIZE                = 20;
  protected final static  int          PAGE_INTERVAL_SIZE       = 6;
  public static           String       KEY_NAME_INTERVAL_END    = "intervalEnd";
  public static           String       KEY_NAME_INTERVAL_START  = "intervalStart";
  protected               SessionData  m_processData;
  protected               int          m_rowCount;
  protected               int          m_currentPageIdx;
  protected               List         m_currentPage;
  protected               PageBean[]   m_currentPageInterval;
  private                 int          m_intervalStart;
  private                 int          m_intervalEnd;


  /**
   *Constructor for the BaseTableBean object
   *
   * @param processData  Description of the Parameter
   */
  protected BaseTableBean( SessionData processData )
  {
    m_processData = processData;
  }


  /**
   * Sets the currentPageIdx attribute of the BaseTableBean object
   *
   * @param pageIdx  The new currentPageIdx value
   */
  public void setCurrentPageIdx( int pageIdx )
  {
    if ( pageIdx < 0 || pageIdx > getPageCount() ) {
      return;
    }
    if ( pageIdx == getPageCount() ) {
      m_currentPageIdx = pageIdx - 1;
    } else {
      m_currentPageIdx = pageIdx;
    }
    initPageInterval();
    m_currentPage = null;
  }


  /**
   * Sets the pageInterval attribute of the BaseTableBean object
   *
   * @param intervalStart  The new pageInterval value
   * @param intervalEnd    The new pageInterval value
   */
  public void setPageInterval( int intervalStart, int intervalEnd )
  {
    int  count  = getPageCount();
    if ( m_currentPageIdx < 0 || m_currentPageIdx >= count || ( m_currentPageIdx >= intervalStart && m_currentPageIdx <= intervalEnd ) ) {
      m_intervalStart = intervalStart;
      if ( intervalEnd == count && intervalEnd != 0 ) {
        m_intervalEnd = intervalEnd - 1;
      } else {
        m_intervalEnd = intervalEnd;
      }
      m_currentPageInterval = getPageInterval( m_intervalStart, m_intervalEnd );
      return;
    }
    if ( count < PAGE_INTERVAL_SIZE ) {
      m_intervalStart = 0;
      m_intervalEnd = count - 1;
      m_currentPageInterval = getPageInterval( m_intervalStart, m_intervalEnd );
      return;
    }
    if ( m_currentPageIdx == 0 ) {
      m_intervalStart = 0;
      m_intervalEnd = PAGE_INTERVAL_SIZE - 1;
      m_currentPageInterval = getPageInterval( m_intervalStart, m_intervalEnd );
      return;
    }
    if ( m_currentPageIdx == count - 1 ) {
      m_intervalEnd = count - 1;
      if ( count % PAGE_INTERVAL_SIZE == 0 ) {
        m_intervalStart = count - PAGE_INTERVAL_SIZE;
      } else {
        m_intervalStart = count - ( count % PAGE_INTERVAL_SIZE );
      }
      m_currentPageInterval = getPageInterval( m_intervalStart, m_intervalEnd );
      return;
    }
    if ( m_currentPageIdx > intervalEnd ) {
      m_intervalStart = m_currentPageIdx;
      m_intervalEnd = m_intervalStart + PAGE_INTERVAL_SIZE < count ? m_intervalStart + PAGE_INTERVAL_SIZE - 1 : m_intervalStart + ( count - m_intervalStart - 1 );
      m_currentPageInterval = getPageInterval( m_intervalStart, m_intervalEnd );
      return;
    }
    if ( m_currentPageIdx < intervalStart ) {
      m_intervalEnd = m_currentPageIdx;
      m_intervalStart = m_currentPageIdx - PAGE_INTERVAL_SIZE + 1;
      m_currentPageInterval = getPageInterval( m_intervalStart, m_intervalEnd );
      return;
    }
  }


  /**
   * Gets the pageInterval attribute of the BaseTableBean object
   *
   * @param start  Description of the Parameter
   * @param end    Description of the Parameter
   * @return       The pageInterval value
   */
  private PageBean[] getPageInterval( int start, int end )
  {
    PageBean[]  pageBean   = new PageBean[start == 0 ? end + 1 : end - start + 1];
    int         pageValue  = start;
    for ( int i = 0, n = pageBean.length; i < n; ++i ) {
      pageBean[i] = new PageBean( pageValue, Integer.toString( pageValue + 1 ) );
      pageValue += 1;
    }
    return pageBean;
  }


  /**
   * Gets the rowCount attribute of the BaseTableBean object
   *
   * @return   The rowCount value
   */
  public int getRowCount()
  {
    return m_rowCount;
  }


  /**
   * Gets the pageCount attribute of the BaseTableBean object
   *
   * @return   The pageCount value
   */
  public int getPageCount()
  {
    int  pageCount;
    if ( m_rowCount == 0 ) {
      return 1;
    }
    pageCount = m_rowCount / PAGE_SIZE;
    if ( ( m_rowCount % PAGE_SIZE ) > 0 ) {
      pageCount += 1;
    }
    return pageCount;
  }


  /**
   * Gets the currentPageIdx attribute of the BaseTableBean object
   *
   * @return   The currentPageIdx value
   */
  public int getCurrentPageIdx()
  {
    return m_currentPageIdx;
  }


  /**
   * Gets the currentPage attribute of the BaseTableBean object
   *
   * @return   the currentPage value
   */
  public PageBean[] getCurrentPageInterval()
  {

    return m_currentPageInterval;
  }


  /**
   * @return
   */
  public int getIntervalEnd()
  {
    return m_intervalEnd;
  }


  /**
   * @return
   */
  public int getIntervalStart()
  {
    return m_intervalStart;
  }


  /**
   * Description of the Method
   */
  private void initPageInterval()
  {
    int  count  = getPageCount();
    if ( count < PAGE_INTERVAL_SIZE ) {
      m_intervalStart = 0;
      m_intervalEnd = count - 1;
      m_currentPageInterval = getPageInterval( m_intervalStart, m_intervalEnd );
      return;
    } else {
      m_intervalStart = 0;
      m_intervalEnd = PAGE_INTERVAL_SIZE - 1;
      m_currentPageInterval = getPageInterval( m_intervalStart, m_intervalEnd );
      return;
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   9. Dezember 2003
   */
  public class PageBean
  {
    private  int     m_value;
    private  String  m_label;


    /**
     *Constructor for the PageBean object
     *
     * @param value  Description of the Parameter
     * @param label  Description of the Parameter
     */
    public PageBean( int value, String label )
    {
      m_value = value;
      m_label = label;
    }


    /**
     * Gets the value attribute of the PageBean object
     *
     * @return   The value value
     */
    public int getValue()
    {
      return m_value;
    }


    /**
     * Gets the label attribute of the PageBean object
     *
     * @return   The label value
     */
    public String getLabel()
    {
      return m_label;
    }

  }
}
