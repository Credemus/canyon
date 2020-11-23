package de.objectcode.canyon.model.transition;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class JoinType extends AbstractEnum
{
	static final long serialVersionUID = -7457730481011332701L;
	
	public final static   int         AND_INT   = 0;
  public final static   int         XOR_INT   = 1;
  public final static   JoinType    AND       = new JoinType( AND_INT, "AND" );
  public final static   JoinType    XOR       = new JoinType( XOR_INT, "XOR" );

  private final static  JoinType[]  g_values  = {
      AND,
      XOR
      };


  /**
   *Constructor for the JoinType object
   *
   * @param value  Description of the Parameter
   * @param tag    Description of the Parameter
   */
  private JoinType( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * Description of the Method
   *
   * @param tag  Description of the Parameter
   * @return     Description of the Return Value
   */
  public static JoinType fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }

  public static JoinType fromInt( int value )
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
