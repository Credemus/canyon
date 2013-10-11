package de.objectcode.canyon.model.transition;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class SplitType extends AbstractEnum
{
	static final long serialVersionUID = -5247916240355451220L;
	
	public final static   int          AND_INT   = 0;
  public final static   int          XOR_INT   = 1;
  public final static   SplitType    AND       = new SplitType( AND_INT, "AND" );
  public final static   SplitType    XOR       = new SplitType( XOR_INT, "XOR" );

  private final static  SplitType[]  g_values  = {
      AND,
      XOR
      };


  /**
   *Constructor for the JoinType object
   *
   * @param value  Description of the Parameter
   * @param tag    Description of the Parameter
   */
  private SplitType( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * Description of the Method
   *
   * @param tag  Description of the Parameter
   * @return     Description of the Return Value
   */
  public static SplitType fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }

  public static SplitType fromInt( int value )
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
