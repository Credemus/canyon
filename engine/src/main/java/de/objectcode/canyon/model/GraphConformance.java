package de.objectcode.canyon.model;


/**
 * Description of the Class
 *
 * @author    junglas
 * @created   20. November 2003
 */
public final class GraphConformance extends AbstractEnum
{
	static final long serialVersionUID = -2833878097679346013L;
	
	public final static   int                 NON_BLOCKED_INT   = 0;
  public final static   int                 LOOP_BLOCKED_INT  = 1;
  public final static   int                 FULL_BLOCKED_INT  = 2;
  public final static   GraphConformance    NON_BLOCKED       = new GraphConformance( NON_BLOCKED_INT, "NON_BLOCKED" );
  public final static   GraphConformance    LOOP_BLOCKED      = new GraphConformance( LOOP_BLOCKED_INT, "LOOP_BLOCKED" );
  public final static   GraphConformance    FULL_BLOCKED      = new GraphConformance( FULL_BLOCKED_INT, "FULL_BLOCKED" );

  private final static  GraphConformance[]  g_values          = {
      NON_BLOCKED,
      LOOP_BLOCKED,
      FULL_BLOCKED
      };


  /**
   * Constructs a new ConformanceClass type.
   *
   * @param value  The int value
   * @param tag    Description of the Parameter
   */
  private GraphConformance( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * Description of the Method
   *
   * @param tag  Description of the Parameter
   * @return     Description of the Return Value
   */
  public static GraphConformance fromString( String tag )
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
  public static GraphConformance fromInt( int value )
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
