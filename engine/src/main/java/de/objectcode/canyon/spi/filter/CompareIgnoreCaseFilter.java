package de.objectcode.canyon.spi.filter;

import java.sql.Types;

/**
 * @author    mrolvering
 * @created   30. Juni 2003
 */
public class CompareIgnoreCaseFilter implements IFilter
{
  private  String  m_attributeName;
  private int m_operation;
  private  Object  m_value;
  private  int     m_type;


  /**
   *Constructor for the EqualsFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value          Description of the Parameter
   */
  public CompareIgnoreCaseFilter( String attributeName, int operation, String value )
  {
    m_type = Types.VARCHAR;
    m_attributeName = attributeName;
    m_operation = operation;
    m_value = value;
  }


  /**
   * Description of the Method
   *
   * @param filterBuilder  Description of the Parameter
   */
  public void toBuilder( IFilterBuilder filterBuilder )
  {
    switch ( m_type ) {
      case Types.VARCHAR:
        filterBuilder.compareIgnoreCaseExpr( m_attributeName, m_operation, ( String ) m_value );
        break;
    }
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String toString()
  {
    StringBuffer  buffer  = new StringBuffer( "equalsIgnoreCase (" );

    buffer.append( m_attributeName ).append( ",'" );
    buffer.append( m_value ).append( "'" );

    buffer.append( ")" );
    return buffer.toString();
  }
}
