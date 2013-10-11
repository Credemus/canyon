package de.objectcode.canyon.spi.filter;

import java.sql.Types;

/**
 * @author    mrolvering
 * @created   30. Juni 2003
 */
public class LikeIgnoreCaseFilter implements IFilter
{

  private  String  m_attributeName;
  private  Object  m_value;
  private  int     m_type;


  /**
   *Constructor for the LikeIgnoreCaseFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value          Description of the Parameter
   */
  public LikeIgnoreCaseFilter( String attributeName, String value )
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
    filterBuilder.likeIgnoreCaseExpr( m_attributeName,
        ( String ) m_value );
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String toString()
  {
    StringBuffer  buffer  = new StringBuffer( "likeIgnoreCase (" );

    buffer.append( m_attributeName ).append( ",'" );
    buffer.append( m_value ).append( "'" );

    buffer.append( ")" );

    return buffer.toString();
  }
}
