package de.objectcode.canyon.model.activity;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class AutomationMode extends AbstractEnum
{
	static final long serialVersionUID = 8571817515461414235L;
	
	public final static   int               AUTOMATIC_INT  = 0;
  public final static   int               MANUAL_INT     = 1;
  public final static   AutomationMode    AUTOMATIC      = new AutomationMode( AUTOMATIC_INT, "AUTOMATIC" );
  public final static   AutomationMode    MANUAL         = new AutomationMode( MANUAL_INT, "MANUAL" );

  private final static  AutomationMode[]  g_values       = {
      AUTOMATIC,
      MANUAL
      };


  /**
   *  Constructs a new AutomationMode instance.
   *
   * @param value  The value
   * @param tag    Description of the Parameter
   */
  private AutomationMode( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * Converts the specified String to an AutomationMode object.  If there
   * is no matching AutomationMode for the given string then this method
   * returns null.
   *
   * @param tag  The string
   * @return     The AutomationMode object
   */
  public static AutomationMode fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }

  public static AutomationMode fromInt( int value )
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
