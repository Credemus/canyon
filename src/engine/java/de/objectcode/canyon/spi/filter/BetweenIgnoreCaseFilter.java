package de.objectcode.canyon.spi.filter;

import java.sql.Types;

/**
 * @author    mrolvering
 * @created   30. Juni 2003
 */
public class BetweenIgnoreCaseFilter implements IFilter
{

  private         String  m_attributeName;

  private         Object  m_value1;
  private         Object  m_value2;

  private         int    m_type;

  /**
   *Constructor for the EqualsFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value1         Description of the Parameter
   * @param value2         Description of the Parameter
   */
  public BetweenIgnoreCaseFilter(
      String attributeName, String value1, String value2 )
  {
    m_type = Types.VARCHAR;
    m_attributeName = attributeName;
    m_value1 = value1;
    m_value2 = value2;
  }

  public void toBuilder(IFilterBuilder filterBuilder) {
	  switch ( m_type ) {
		  case Types.VARCHAR:
			  filterBuilder.betweenIgnoreCaseExpr(
					  m_attributeName,
					  (String) m_value1, (String) m_value2);
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
    StringBuffer  buffer  = new StringBuffer( "betweenIgnoreCase (" );

    buffer.append( m_attributeName ).append( ",'" );
    buffer.append( m_value1 );
    buffer.append( " - " );
    buffer.append( m_value2 ).append( "')" );

    return buffer.toString();
  }

}
