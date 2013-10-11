package de.objectcode.canyon.spi.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author    junglas
 * @created   25.03.2003
 * @version   $Id: OrFilter.java,v 1.1 2003/10/29 12:58:55 junglas Exp $
 */
public class OrFilter implements IFilter
{
  private  List  m_filterExprs;


  /**
   *Constructor for the OrFilterExpr object
   */
  public OrFilter()
  {
    m_filterExprs = new ArrayList();
  }
  
  public void addFilter ( IFilter filter )
  {
    m_filterExprs.add(filter);
  }


  /**
   * Description of the Method
   *
   * @param filterBuilder  Description of the Parameter
   */
  public void toBuilder( IFilterBuilder filterBuilder )
  {
    Iterator  it     = m_filterExprs.iterator();
    boolean   first  = true;

    while ( it.hasNext() ) {
      IFilter  filterExpr  = ( IFilter ) it.next();

      filterExpr.toBuilder( filterBuilder );
      if ( !first ) {
        filterBuilder.orExpr();
      }
      first = false;
    }
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String toString()
  {
    StringBuffer  buffer  = new StringBuffer( "or (" );

    Iterator      it      = m_filterExprs.iterator();

    while ( it.hasNext() ) {
      IFilter  filterExpr  = ( IFilter ) it.next();

      buffer.append( filterExpr );
    }

    buffer.append( ")" );
    return buffer.toString();
  }
}
