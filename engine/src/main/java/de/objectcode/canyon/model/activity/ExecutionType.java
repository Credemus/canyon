package de.objectcode.canyon.model.activity;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   21. November 2003
 */
public class ExecutionType extends AbstractEnum
{
	static final long serialVersionUID = -2222842382758118758L;
	
	public final static  int              SYNCHRONOUS_INT   = 0;
  public final static  int              ASYNCHRONOUS_INT  = 1;
  public final static  ExecutionType    SYNCHRONOUS       = new ExecutionType( SYNCHRONOUS_INT, "SYNCHR" );
  public final static  ExecutionType    ASYNCHRONOUS      = new ExecutionType( ASYNCHRONOUS_INT, "ASYNCHR" );

  public final static  ExecutionType[]  g_values          = {
      SYNCHRONOUS,
      ASYNCHRONOUS
      };


  /**
   * Construct a new ExecutionType instance.
   *
   * @param value  The value
   * @param tag    Description of the Parameter
   */
  private ExecutionType( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * Convert the specified String to an ExecutionType object.  If there
   * no matching ExecutionType for the given String then this method
   * returns null.
   *
   * @param tag  The String
   * @return     The ExecutionType object
   */
  public static ExecutionType fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }

  public static ExecutionType fromInt( int value )
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
