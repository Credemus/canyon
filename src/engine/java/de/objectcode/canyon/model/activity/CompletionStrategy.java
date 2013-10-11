package de.objectcode.canyon.model.activity;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   27. November 2003
 */
public class CompletionStrategy extends AbstractEnum
{
	static final long serialVersionUID = -5853944401786978599L;
	
	public final static   int                   ALL_INT   = 0;
  public final static   int                   ANY_INT   = 1;
  public final static   int                   ONE_INT   = 2;
  public final static   CompletionStrategy    ALL       = new CompletionStrategy( ALL_INT, "ALL" );
  public final static   CompletionStrategy    ANY       = new CompletionStrategy( ANY_INT, "ANY" );
  public final static   CompletionStrategy    ONE       = new CompletionStrategy( ONE_INT, "ONE" );

  private final static  CompletionStrategy[]  g_values  = {
      ALL,
      ANY,
      ONE
      };


  /**
   * Constructs a new CompletionStrategy instance.
   *
   * @param value  The value.
   * @param tag    Description of the Parameter
   */
  private CompletionStrategy( int value, String tag )
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
  public static CompletionStrategy fromString( String tag )
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
  public static CompletionStrategy fromInt( int value )
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
