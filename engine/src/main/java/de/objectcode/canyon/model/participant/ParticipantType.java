package de.objectcode.canyon.model.participant;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class ParticipantType extends AbstractEnum
{
	static final long serialVersionUID = -149633585525352951L;
	
	public final static  int                RESOURCE_SET_INT         = 0;
  public final static  int                RESOURCE_INT             = 1;
  public final static  int                ROLE_INT                 = 2;
  public final static  int                ORGANIZATIONAL_UNIT_INT  = 3;
  public final static  int                HUMAN_INT                = 4;
  public final static  int                SYSTEM_INT               = 5;
  public final static  ParticipantType    RESOURCE_SET             = new ParticipantType( RESOURCE_SET_INT, "RESOURCE_SET" );
  public final static  ParticipantType    RESOURCE                 = new ParticipantType( RESOURCE_INT, "RESOURCE" );
  public final static  ParticipantType    ROLE                     = new ParticipantType( ROLE_INT, "ROLE" );
  public final static  ParticipantType    ORGANIZATIONAL_UNIT      = new ParticipantType( ORGANIZATIONAL_UNIT_INT, "ORGANIZATIONAL_UNIT" );
  public final static  ParticipantType    HUMAN                    = new ParticipantType( HUMAN_INT, "HUMAN" );
  public final static  ParticipantType    SYSTEM                   = new ParticipantType( SYSTEM_INT, "SYSTEM" );

  public final static  ParticipantType[]  g_values                 = {
      RESOURCE_SET,
      RESOURCE,
      ROLE,
      ORGANIZATIONAL_UNIT,
      HUMAN,
      SYSTEM
      };


  /**
   *Constructor for the ParticipantType object
   *
   * @param value  Description of the Parameter
   * @param tag    Description of the Parameter
   */
  private ParticipantType( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * Description of the Method
   *
   * @param tag  Description of the Parameter
   * @return     Description of the Return Value
   */
  public static ParticipantType fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }

  public static ParticipantType fromInt( int value )
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
