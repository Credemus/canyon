package de.objectcode.canyon.spi.filter;


/**
 * @author    junglas
 * @created   27.05.2003
 * @version   $Id: IsNullFilter.java,v 1.1 2003/10/17 09:07:30 junglas Exp $
 */
public class IsNullFilter implements IFilter
{
  private  String  m_attributeName;


  /**
   *Constructor for the IsNullFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   */
  public IsNullFilter( String attributeName )
  {
    m_attributeName = attributeName;
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String toString()
  {
    StringBuffer  buffer  = new StringBuffer( "equals (" );

    buffer.append( m_attributeName );

    buffer.append( ")" );
    return buffer.toString();
  }


  /**
   * Description of the Method
   *
   * @param filterBuilder  Description of the Parameter
   */
  public void toBuilder( IFilterBuilder filterBuilder )
  {
    filterBuilder.isNullExpr( m_attributeName );
  }
}
