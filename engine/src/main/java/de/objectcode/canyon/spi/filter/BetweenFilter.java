package de.objectcode.canyon.spi.filter;

import java.sql.Types;
import java.util.Date;

/**
 * @author    mrolvering
 * @created   30. Juni 2003
 */
public class BetweenFilter implements IFilter
{
  private  String  m_attributeName;
  private  Object  m_value1;
  private  Object  m_value2;
  private  int     m_type;

  public BetweenFilter ( String attributeName, Object value1, Object value2 )
  {
    m_attributeName = attributeName;
    m_value1 = value1;
    m_value2 = value2;

    if ( value1 instanceof String )
      m_type = Types.VARCHAR;
    else if (value1 instanceof Double )
      m_type = Types.DOUBLE;
    else if ( value1 instanceof Long )
      m_type = Types.BIGINT;
    else if ( value1 instanceof Integer)
      m_type = Types.INTEGER;
    else if ( value1 instanceof Short )
      m_type = Types.SMALLINT;
    else if ( value1 instanceof Date )
      m_type = Types.DATE;
  }

  /**
   *Constructor for the EqualsFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value1         Description of the Parameter
   * @param value2         Description of the Parameter
   */
  public BetweenFilter(
      String attributeName, String value1, String value2 )
  {
    m_type = Types.VARCHAR;
    m_attributeName = attributeName;
    m_value1 = value1;
    m_value2 = value2;
  }


  /**
   *Constructor for the BetweenFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value1         Description of the Parameter
   * @param value2         Description of the Parameter
   */
  public BetweenFilter(
      String attributeName, double value1, double value2 )
  {
    m_type = Types.DOUBLE;
    m_attributeName = attributeName;
    m_value1 = new Double( value1 );
    m_value2 = new Double( value2 );
  }


  /**
   *Constructor for the BetweenFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value1         Description of the Parameter
   * @param value2         Description of the Parameter
   */
  public BetweenFilter(
      String attributeName, int value1, int value2 )
  {
    m_type = Types.INTEGER;
    m_attributeName = attributeName;
    m_value1 = new Integer( value1 );
    m_value2 = new Integer( value2 );
  }


  /**
   *Constructor for the BetweenFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value1         Description of the Parameter
   * @param value2         Description of the Parameter
   */
  public BetweenFilter(
      String attributeName, short value1, short value2 )
  {
    m_type = Types.SMALLINT;
    m_attributeName = attributeName;
    m_value1 = new Short( value1 );
    m_value2 = new Short( value2 );
  }


  /**
   *Constructor for the BetweenFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value1         Description of the Parameter
   * @param value2         Description of the Parameter
   */
  public BetweenFilter(
      String attributeName, long value1, long value2 )
  {
    m_type = Types.BIGINT;
    m_attributeName = attributeName;
    m_value1 = new Long( value1 );
    m_value2 = new Long( value2 );
  }


  /**
   *Constructor for the BetweenFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value1         Description of the Parameter
   * @param value2         Description of the Parameter
   */
  public BetweenFilter( String attributeName, Date value1, Date value2 )
  {
    m_type = Types.DATE;
    m_attributeName = attributeName;
    m_value1 = value1;
    m_value2 = value2;
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
        filterBuilder.betweenExpr( m_attributeName,
            ( String ) m_value1, ( String ) m_value2 );
        break;
      case Types.INTEGER:
        filterBuilder.betweenExpr( m_attributeName,
            ( ( Integer ) m_value1 ).intValue(),
            ( ( Integer ) m_value2 ).intValue() );
        break;
      case Types.DOUBLE:
        filterBuilder.betweenExpr( m_attributeName,
            ( ( Double ) m_value1 ).doubleValue(),
            ( ( Double ) m_value2 ).doubleValue() );
        break;
      case Types.DATE:
        filterBuilder.betweenExpr( m_attributeName,
            ( Date ) m_value1, ( Date ) m_value2 );
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
    StringBuffer  buffer  = new StringBuffer( "between (" );

    buffer.append( m_attributeName ).append( ",'" );
    buffer.append( m_value1 );
    buffer.append( " - " );
    buffer.append( m_value2 ).append( "')" );

    return buffer.toString();
  }
}
