package de.objectcode.canyon.model.activity;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   12. August 2004
 */
public class AssignStrategy extends AbstractEnum
{
	static final long serialVersionUID = 8723589812418543662L;
	
	public final static   int               LEAST_INT  = 0;
  public final static   int               ALL_INT    = 1;
  public final static   AssignStrategy    LEAST      = new AssignStrategy( LEAST_INT, "ONE" );
  public final static   AssignStrategy    ALL        = new AssignStrategy( ALL_INT, "ALL" );

  private final static  AssignStrategy[]  g_values   = {
      LEAST,
      ALL,
      };


  /**
   * Constructs a new CompletionStrategy instance.
   *
   * @param value  The value.
   * @param tag    Description of the Parameter
   */
  private AssignStrategy( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * Converts the specified string to a CompletionStrategy object.  If there
   * is no matching CompletionStrategy for the given string then this method
   * returns null.
   *
   * @param tag  The string
   * @return     The CompletionStrategy object
   */
  public static AssignStrategy fromString( String tag )
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
  public static AssignStrategy fromInt( int value )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getValue() == value ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( String.valueOf( value ) );
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public Object readResolve()
  {
    return fromInt( m_value );
  }

}
