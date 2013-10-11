package de.objectcode.canyon;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author    junglas
 * @created   24. November 2003
 */
public class CanyonException extends Exception
{
	static final long serialVersionUID = -2881262933524551015L;
	
	private final static  boolean    JDK1_4;

  private               Throwable  m_cause;


  /**
   * Construct a new OBEException.
   */
  public CanyonException()
  {
    super();
  }


  /**
   * Construct a new OBEException with the given message.
   *
   * @param message  The message
   */
  public CanyonException( String message )
  {
    super( message );
  }


  /**
   * Construct a new OBEException with the given nested error.
   *
   * @param t  The nested error
   */
  public CanyonException( Throwable t )
  {
    super( t.getMessage() );
    m_cause = t;
  }


  /**
   * Construct a new OBEException with the given messager and nested
   * error.
   *
   * @param message  The message
   * @param t        The nested error
   */
  public CanyonException( String message, Throwable t )
  {
    super( message );
    m_cause = t;
  }


  /**
   * Get the nested error.
   *
   * @return   The nested error
   */
  public Throwable getCause()
  {
    return m_cause;
  }


  /**
   * Description of the Method
   */
  public final void printStackTrace()
  {
    printStackTrace( System.err );
  }


  /**
   * Description of the Method
   *
   * @param stream  Description of the Parameter
   */
  public final void printStackTrace( PrintStream stream )
  {
    super.printStackTrace( stream );
    // Only print causal stack traces if pre-JDK 1.4.
    if ( !JDK1_4 ) {
      Throwable  t  = getCause();
      while ( t != null ) {
        stream.println( "Caused by: " + t );
        t.printStackTrace( stream );
        if ( t instanceof CanyonException ) {
          t = ( ( CanyonException ) t ).getCause();
        } else {
          break;
        }
      }
    }
  }


  /**
   * Description of the Method
   *
   * @param writer  Description of the Parameter
   */
  public final void printStackTrace( PrintWriter writer )
  {
    super.printStackTrace( writer );
    // Only print causal stack traces if pre-JDK 1.4.
    if ( !JDK1_4 ) {
      Throwable  t  = getCause();
      while ( t != null ) {
        writer.println( "Caused by: " + t );
        t.printStackTrace( writer );
        if ( t instanceof CanyonException ) {
          t = ( ( CanyonException ) t ).getCause();
        } else {
          break;
        }
      }
    }
  }

  static {
    boolean  jdk1_4;
    try {
      Throwable.class.getMethod( "getCause", new Class[]{} );
      jdk1_4 = true;
    }
    catch ( NoSuchMethodException e ) {
      jdk1_4 = false;
    }
    JDK1_4 = jdk1_4;
  }
}
