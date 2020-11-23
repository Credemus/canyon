package de.objectcode.canyon.spi.filter;

import java.sql.Types;
import java.util.Date;

/**
 * @author    junglas
 * @created   25.03.2003
 * @version   $Id: CompareFilter.java,v 1.1 2003/12/02 16:38:10 junglas Exp $
 */
public class CompareFilter implements IFilter
{
  public final static int EQ = 0;
  public final static int GT = 1;
  public final static int GE = 2;
  public final static int LT = 3;
  public final static int LE = 4;
  public final static int NE = 5;
  
  private  String  m_attributeName;
  private  int m_operation;
  private  Object  m_value;
  private  int     m_type;

  public CompareFilter (String attributeName, int operation, Object value )
  {
    m_attributeName = attributeName;
    m_operation = operation;
    m_value = value;
    if ( value instanceof String )
      m_type = Types.VARCHAR;
    else if (value instanceof Double )
      m_type = Types.DOUBLE;
    else if ( value instanceof Long )
      m_type = Types.BIGINT;
    else if ( value instanceof Integer)
      m_type = Types.INTEGER;
    else if ( value instanceof Short )
      m_type = Types.SMALLINT;
    else if ( value instanceof Date )
      m_type = Types.DATE;
  }

  /**
   *Constructor for the EqualsFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value          Description of the Parameter
   */
  public CompareFilter( String attributeName, int operation, String value )
  {
    m_type = Types.VARCHAR;
    m_attributeName = attributeName;
    m_operation = operation;
    m_value = value;
  }


  /**
   *Constructor for the EqualsFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value          Description of the Parameter
   */
  public CompareFilter( String attributeName, int operation, double value )
  {
    m_type = Types.DOUBLE;
    m_attributeName = attributeName;
    m_operation = operation;
    m_value = new Double( value );
  }


  /**
   *Constructor for the EqualsFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value          Description of the Parameter
   */
  public CompareFilter( String attributeName, int operation, int value )
  {
    m_type = Types.INTEGER;
    m_attributeName = attributeName;
    m_operation = operation;
    m_value = new Integer( value );
  }


  /**
   *Constructor for the EqualsFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value          Description of the Parameter
   */
  public CompareFilter( String attributeName, int operation, short value )
  {
    m_type = Types.SMALLINT;
    m_attributeName = attributeName;
    m_operation = operation;
    m_value = new Short( value );
  }


  /**
   *Constructor for the EqualsFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value          Description of the Parameter
   */
  public CompareFilter( String attributeName, int operation, long value )
  {
    m_type = Types.BIGINT;
    m_attributeName = attributeName;
    m_operation = operation;
    m_value = new Long( value );
  }


  /**
   *Constructor for the EqualsFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value          Description of the Parameter
   */
  public CompareFilter( String attributeName, int operation, Date value )
  {
    m_type = Types.DATE;
    m_attributeName = attributeName;
    m_operation = operation;
    m_value = value;
  }


  /**
   *Constructor for the EqualsFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param value          Description of the Parameter
   */
  public CompareFilter( String attributeName, int operation, boolean value )
  {
    m_type = Types.BOOLEAN;
    m_attributeName = attributeName;
    m_operation = operation;
    m_value = new Boolean( value );
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
        filterBuilder.compareExpr( m_attributeName, m_operation, ( String ) m_value );
        break;
      case Types.INTEGER:
        filterBuilder.compareExpr( m_attributeName, m_operation, ( ( Integer ) m_value ).intValue() );
        break;
      case Types.BIGINT:
        filterBuilder.compareExpr( m_attributeName, m_operation, ( ( Long ) m_value ).intValue() );
        break;
      case Types.DOUBLE:
        filterBuilder.compareExpr( m_attributeName, m_operation, ( ( Double ) m_value ).doubleValue() );
        break;
      case Types.DATE:
        filterBuilder.compareExpr( m_attributeName, m_operation, ( Date ) m_value );
        break;
      case Types.BOOLEAN:
        filterBuilder.compareExpr( m_attributeName, m_operation, ( ( Boolean ) m_value ).booleanValue() );
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
    StringBuffer  sb  = new StringBuffer( "equals (" );

    sb.append( m_attributeName ).append( ",'" );
    sb.append( m_value ).append( "'" );

    sb.append( ")" );
    return sb.toString();
  }
}
