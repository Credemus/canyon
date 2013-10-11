package de.objectcode.canyon.model.data;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class ParameterMode extends AbstractEnum
{
	static final long serialVersionUID = -1796086376283762930L;
	
	public final static   int              IN_INT     = 0;
  public final static   int              OUT_INT    = 1;
  public final static   int              INOUT_INT  = 2;
  public final static   ParameterMode    IN         = new ParameterMode( IN_INT, "IN" );
  public final static   ParameterMode    OUT        = new ParameterMode( OUT_INT, "OUT" );
  public final static   ParameterMode    INOUT      = new ParameterMode( INOUT_INT, "INOUT" );

  private final static  ParameterMode[]  g_values   = {
      IN,
      OUT,
      INOUT
      };


  /**
   *Constructor for the ParameterMode object
   *
   * @param value  Description of the Parameter
   * @param tag    Description of the Parameter
   */
  private ParameterMode( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * Description of the Method
   *
   * @param tag  Description of the Parameter
   * @return     Description of the Return Value
   */
  public static ParameterMode fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }
  
  public static ParameterMode fromInt ( int value )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getValue() == value ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( String.valueOf(value) );
  }
  
  public Object readResolve()
  {
    return fromInt ( m_value );
  }

}
