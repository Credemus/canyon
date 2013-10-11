
package de.objectcode.canyon.model;


/**
 * Description of the Class
 *
 * @author    junglas
 * @created   20. November 2003
 */
public final class PublicationStatus extends AbstractEnum
{
	static final long serialVersionUID = -8469285359164995525L;
	
	public final static   int                  UNDER_REVISION_INT  = 0;
  public final static   int                  UNDER_TEST_INT      = 1;
  public final static   int                  RELEASED_INT        = 2;
  public final static   PublicationStatus    UNDER_REVISION      = new PublicationStatus( UNDER_REVISION_INT, "UNDER_REVISION" );
  public final static   PublicationStatus    UNDER_TEST          = new PublicationStatus( UNDER_TEST_INT, "UNDER_TEST" );
  public final static   PublicationStatus    RELEASED            = new PublicationStatus( RELEASED_INT, "RELEASED" );

  private final static  PublicationStatus[]  g_values            = new PublicationStatus[]{
      UNDER_REVISION,
      UNDER_TEST,
      RELEASED
      };


  /**
   *Constructor for the PublicationStatus object
   *
   * @param value  Description of the Parameter
   * @param tag    Description of the Parameter
   */
  private PublicationStatus( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * Description of the Method
   *
   * @param tag  Description of the Parameter
   * @return     Description of the Return Value
   */
  public static PublicationStatus fromString( String tag )
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
  public static PublicationStatus fromInt( int value )
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
