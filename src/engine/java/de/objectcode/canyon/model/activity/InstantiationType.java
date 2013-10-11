package de.objectcode.canyon.model.activity;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class InstantiationType extends AbstractEnum
{
	static final long serialVersionUID = 8230709409860159142L;
	
	public final static   int                  ONCE_INT      = 0;
  public final static   int                  MULTIPLE_INT  = 1;
  public final static   InstantiationType    ONCE          = new InstantiationType( ONCE_INT, "ONCE" );
  public final static   InstantiationType    MULTIPLE      = new InstantiationType( MULTIPLE_INT, "MULTIPLE" );

  private final static  InstantiationType[]  g_values      = {
      ONCE,
      MULTIPLE
      };


  /**
   *Constructor for the InstantiationType object
   *
   * @param value  Description of the Parameter
   * @param tag    Description of the Parameter
   */
  private InstantiationType( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * Description of the Method
   *
   * @param tag  Description of the Parameter
   * @return     Description of the Return Value
   */
  public static InstantiationType fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }

  public static InstantiationType fromInt( int value )
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
