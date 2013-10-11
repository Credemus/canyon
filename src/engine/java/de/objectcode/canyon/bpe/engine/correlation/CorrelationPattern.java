package de.objectcode.canyon.bpe.engine.correlation;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author junglas
 */
public class CorrelationPattern extends AbstractEnum
{
  static final long serialVersionUID = -1082102987025268378L;
  
  public final static int IN_INT = 0;
  public final static CorrelationPattern IN = new CorrelationPattern ( IN_INT, "IN");
  public final static int OUT_INT = 1;
  public final static CorrelationPattern OUT = new CorrelationPattern ( OUT_INT, "OUT");
  public final static int INOUT_INT = 2;
  public final static CorrelationPattern INOUT = new CorrelationPattern ( INOUT_INT, "INOUT");
  
  private final static  CorrelationPattern[]  g_values       = {
      IN,
      OUT,
      INOUT
      };

  private CorrelationPattern ( int value, String tag ) 
  {
    super ( value, tag );
  }


  /**
   * @return   Description of the Return Value
   * @see      de.objectcode.canyon.model.AbstractEnum#readResolve()
   */
  public Object readResolve()
  {
    return fromInt( m_value );
  }


  /**
   * Description of the Method
   *
   * @param tag  Description of the Parameter
   * @return     Description of the Return Value
   */
  public static CorrelationPattern fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }


  /**
   * Description of the Method
   *
   * @param value  Description of the Parameter
   * @return       Description of the Return Value
   */
  public static CorrelationPattern fromInt( int value )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getValue() == value ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( String.valueOf( value ) );
  }
}
