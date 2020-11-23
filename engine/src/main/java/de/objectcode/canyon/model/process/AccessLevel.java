package de.objectcode.canyon.model.process;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   26. November 2003
 */
public class AccessLevel extends AbstractEnum
{
	static final long serialVersionUID = 2559316134045356598L;
	
	public final static   int            PUBLIC_INT   = 0;
  public final static   int            PRIVATE_INT  = 1;
  public final static   AccessLevel    PUBLIC       = new AccessLevel( PUBLIC_INT, "PUBLIC" );
  public final static   AccessLevel    PRIVATE      = new AccessLevel( PRIVATE_INT, "PRIVATE" );

  private final static  AccessLevel[]  g_values     = {
      PUBLIC,
      PRIVATE
      };


  /**
   * Construct a new AccessLevel type object.
   *
   * @param value  The int value
   * @param tag    Description of the Parameter
   */
  private AccessLevel( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * Convert the specified type String to an AccessLevel type object.  If
   * the specified type string can not be converted to an AccessLevel
   * object then this method will return null.
   *
   * @param tag  The type String (PUBLIC or PRIVATE)
   * @return     The AccessLevel object or null
   */
  public static AccessLevel fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }

  public static AccessLevel fromInt( int value )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getValue() == value ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( String.valueOf( value ) );
  }
  
  public Object readResolve()
  {
    return fromInt ( m_value );
  }
}
