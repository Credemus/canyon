package de.objectcode.canyon.spi.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author    junglas
 * @created   25.03.2003
 * @version   $Id: AndFilter.java,v 1.2 2003/10/29 12:58:55 junglas Exp $
 */
public class AndFilter implements IFilter
{
  private  List  m_filterExprs;


  /**
   *Constructor for the AndFilterExpr object
   */
  public AndFilter()
  {
    m_filterExprs = new ArrayList();
  }


  /**
   * Adds a feature to the FilterExpr attribute of the AndFilterExpr object
   *
   * @param filterExpr  The feature to be added to the FilterExpr attribute
   */
  public void addFilter( IFilter filterExpr )
  {
    m_filterExprs.add( filterExpr );
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
        filterBuilder.andExpr();
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
    StringBuffer  buffer  = new StringBuffer( "and (" );

    Iterator      it      = m_filterExprs.iterator();

    while ( it.hasNext() ) {
      IFilter  filterExpr  = ( IFilter ) it.next();

      buffer.append( filterExpr );
    }

    buffer.append( ")" );
    return buffer.toString();
  }

}
