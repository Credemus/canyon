package de.objectcode.canyon.spi.filter;

import java.sql.Types;

/**
 * @author    junglas
 * @created   26.03.2003
 * @version   $Id: LikeFilter.java,v 1.1 2003/10/17 09:07:30 junglas Exp $
 */
public class LikeFilter implements IFilter
{
  private  String  m_attributeName;
  private  Object  m_value;
  private  int     m_type;


  /**
   *Constructor for the LikeFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value          Description of the Parameter
   */
  public LikeFilter( String attributeName, String value )
  {
    m_type = Types.VARCHAR;
    m_attributeName = attributeName;
    m_value = value;
  }


  /**
   * Description of the Method
   *
   * @param filterBuilder  Description of the Parameter
   */
  public void toBuilder( IFilterBuilder filterBuilder )
  {
    filterBuilder.likeExpr( m_attributeName, ( String ) m_value );
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String toString()
  {
    StringBuffer  buffer  = new StringBuffer( "like (" );

    buffer.append( m_attributeName ).append( ",'" );
    buffer.append( m_value ).append( "'" );

    buffer.append( ")" );

    return buffer.toString();
  }
}
