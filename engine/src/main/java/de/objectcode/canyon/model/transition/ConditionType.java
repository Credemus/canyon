package de.objectcode.canyon.model.transition;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 * @created   26. November 2003
 */
public class ConditionType extends AbstractEnum
{
	static final long serialVersionUID = -7112481063049446815L;
	
	public final static  int              CONDITION_INT         = 0;
  public final static  int              OTHERWISE_INT         = 1;
  public final static  int              EXCEPTION_INT         = 2;
  public final static  int              DEFAULTEXCEPTION_INT  = 3;
  public final static  ConditionType    CONDITION             = new ConditionType( CONDITION_INT, "CONDITION" );
  public final static  ConditionType    OTHERWISE             = new ConditionType( OTHERWISE_INT, "OTHERWISE" );
  public final static  ConditionType    EXCEPTION             = new ConditionType( EXCEPTION_INT, "EXCEPTION" );
  public final static  ConditionType    DEFAULTEXCEPTION      = new ConditionType( DEFAULTEXCEPTION_INT, "DEFAULTEXCEPTION" );

  public final static  ConditionType[]  g_values              = {
      CONDITION,
      OTHERWISE,
      EXCEPTION,
      DEFAULTEXCEPTION
      };


  /**
   * Construct a new ConditionType instance.
   *
   * @param value  The value
   * @param tag    Description of the Parameter
   */
  private ConditionType( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * Convert the specified String to an ConditionType object.  If there
   * no matching ConditionType for the given String then this method
   * returns null.
   *
   * @param tag  The String
   * @return     The ConditionType object
   */
  public static ConditionType fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }

  public static ConditionType fromInt( int value )
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
