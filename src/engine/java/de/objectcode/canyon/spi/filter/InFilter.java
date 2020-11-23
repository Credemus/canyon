package de.objectcode.canyon.spi.filter;

import java.sql.Types;
import java.util.Date;

/**
 * @author    mrolvering
 * @created   30. Juni 2003
 */
public class InFilter implements IFilter
{
  private  String    m_attributeName;
  private  Object[]  m_values;
  private  int       m_type;

  public InFilter ( String attributeName, Object[] values )
  {
    m_attributeName = attributeName;
    m_values = values;
    
    if ( values[0] instanceof String )
      m_type = Types.VARCHAR;
    else if ( values[0] instanceof Double )
      m_type = Types.DOUBLE;
    else if ( values[0] instanceof Long )
      m_type = Types.BIGINT;
    else if ( values[0] instanceof Integer)
      m_type = Types.INTEGER;
    else if ( values[0] instanceof Short )
      m_type = Types.SMALLINT;
    else if ( values[0] instanceof Date )
      m_type = Types.DATE;
  }

  /**
   *Constructor for the EqualsFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param values         Description of the Parameter
   */
  public InFilter(
      String attributeName, String[] values )
  {
    m_type = Types.VARCHAR;
    m_attributeName = attributeName;
    m_values = values;
  }


  /**
   *Constructor for the InFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param values         Description of the Parameter
   */
  public InFilter(
      String attributeName, double[] values )
  {
    m_type = Types.DOUBLE;
    m_attributeName = attributeName;
    m_values = new Object[values.length];

    for ( int i = values.length - 1; i >= 0; i-- ) {
      m_values[i] = new Double( values[i] );
    }
  }


  /**
   *Constructor for the InFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param values         Description of the Parameter
   */
  public InFilter(
      String attributeName, int[] values )
  {
    m_type = Types.INTEGER;
    m_attributeName = attributeName;
    m_values = new Object[values.length];

    for ( int i = values.length - 1; i >= 0; i-- ) {
      m_values[i] = new Integer( values[i] );
    }
  }


  /**
   *Constructor for the InFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param values         Description of the Parameter
   */
  public InFilter(
      String attributeName, short[] values )
  {
    m_type = Types.SMALLINT;
    m_attributeName = attributeName;
    m_values = new Object[values.length];

    for ( int i = values.length - 1; i >= 0; i-- ) {
      m_values[i] = new Short( values[i] );
    }
  }


  /**
   *Constructor for the InFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param values         Description of the Parameter
   */
  public InFilter(
      String attributeName, long[] values )
  {
    m_type = Types.BIGINT;
    m_attributeName = attributeName;
    m_values = new Object[values.length];

    for ( int i = values.length - 1; i >= 0; i-- ) {
      m_values[i] = new Long( values[i] );
    }
  }


  /**
   *Constructor for the InFilterExpr object
   *
   * @param attributeName  Description of the Parameter
   * @param values         Description of the Parameter
   */
  public InFilter( String attributeName, Date[] values )
  {
    m_type = Types.DATE;
    m_attributeName = attributeName;
    m_values = values;
  }


  /**
   * Description of the Method
   *
   * @param filterBuilder  Description of the Parameter
   */
  public void toBuilder( IFilterBuilder filterBuilder )
  {
    int  length;

    switch ( m_type ) {
      case Types.VARCHAR:
        filterBuilder.inExpr( m_attributeName,
            ( String[] ) m_values );
        break;
      case Types.INTEGER:
        length = m_values.length;
        int  intValues[] = new int[length];

        for ( int i = 0; i < length; i++ ) {
          intValues[i] = ( ( Integer ) m_values[i] ).intValue();
        }

        filterBuilder.inExpr( m_attributeName,
            intValues );
        break;
      case Types.DOUBLE:
        length = m_values.length;
        double doubleValues[] = new double[length];

        for ( int i = 0; i < length; i++ ) {
          doubleValues[i] = ( ( Double ) m_values[i] ).doubleValue();
        }

        filterBuilder.inExpr( m_attributeName,
            doubleValues );
        break;
      case Types.DATE:
        filterBuilder.inExpr( m_attributeName,
            ( Date[] ) m_values );
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
    StringBuffer  buffer   = new StringBuffer( "in (" );

    buffer.append( m_attributeName ).append( ',' );

    int           i;

    for ( i = 0; i < m_values.length; i++ ) {
      buffer.append( '\'' );
      buffer.append( m_values[i] );
      buffer.append( "', " );
    }

    // letztes ", " löschen
    int           bufSize  = buffer.length();
    buffer.delete( bufSize - 2, bufSize );

    buffer.append( ')' );

    return buffer.toString();
  }
}
