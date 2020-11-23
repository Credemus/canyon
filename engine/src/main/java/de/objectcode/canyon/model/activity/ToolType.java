package de.objectcode.canyon.model.activity;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class ToolType extends AbstractEnum
{
	static final long serialVersionUID = -8535376639245691773L;
	
	public final static   int         APPLICATION_INT  = 0;
  public final static   int         PROCEDURE_INT    = 1;

  /**
   * ToolType representing an application.  An application is an executable
   * tool that executes in its own environment (i.e., not in the workflow
   * engine's JVM).
   */
  public final static   ToolType    APPLICATION      = new ToolType( APPLICATION_INT, "APPLICATION" );
  /**
   * ToolType representing a procedure.  A procedure is a tool that is
   * executed in the workflow engine's JVM.
   */
  public final static   ToolType    PROCEDURE        = new ToolType( PROCEDURE_INT, "PROCEDURE" );

  private final static  ToolType[]  g_values         = {
      APPLICATION,
      PROCEDURE
      };


  /**
   *Constructor for the ToolType object
   *
   * @param value  Description of the Parameter
   * @param tag    Description of the Parameter
   */
  private ToolType( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * Convert the specified String to an ToolType object.  If there
   * no matching ToolType for the given String then this method
   * returns null.
   *
   * @param tag  The String
   * @return     The ToolType object
   */
  public static ToolType fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }

  public static ToolType fromInt( int value )
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
