package de.objectcode.canyon.bpe.engine.activities;

import java.io.Serializable;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   23. Juni 2004
 */
public class LinkState extends AbstractEnum implements Serializable
{
  final static          long         serialVersionUID  = -7705973957625135591L;

  public final static   int          UNKNOWN_INT       = 0;
  public final static   int          TRUE_INT          = 1;
  public final static   int          FALSE_INT         = 2;
  public final static   LinkState    UNKNOWN           = new LinkState( UNKNOWN_INT, "UNKNOWN" );
  public final static   LinkState    TRUE              = new LinkState( TRUE_INT, "TRUE" );
  public final static   LinkState    FALSE             = new LinkState( FALSE_INT, "FALSE" );

  private final static  LinkState[]  g_values          = {
      UNKNOWN,
      TRUE,
      FALSE
      };


  /**
   *Constructor for the ActivityState object
   *
   * @param value  Description of the Parameter
   * @param tag    Description of the Parameter
   */
  private LinkState( int value, String tag )
  {
    super( value, tag );
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
  public static LinkState fromString( String tag )
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
  public static LinkState fromInt( int value )
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
