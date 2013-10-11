package de.objectcode.canyon.spi.parser;

import de.objectcode.canyon.model.ValidationErrors;

/**
 * @author    junglas
 * @created   26. November 2003
 */
public class InvalidPackageException extends Exception
{
	static final long serialVersionUID = -8175274348529057748L;
	
	private final static  String            NEWLINE   = System.getProperty( "line.separator", "\n" );
  private               ValidationErrors  m_errors;


  /**
   *Constructor for the InvalidPackageException object
   *
   * @param id      Description of the Parameter
   * @param errors  Description of the Parameter
   */
  public InvalidPackageException( String id, ValidationErrors errors )
  {
    super( "Package '" + id + "' contains errors" );

    m_errors = errors;
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String toString()
  {
    StringBuffer  sb        = new StringBuffer( super.toString() );
    String[]      messages  = m_errors.getMessages();
    int           i;

    for ( i = 0; i < messages.length; i++ ) {
      sb.append( NEWLINE );
      sb.append( messages[i] );
    }
    sb.append( NEWLINE );
    sb.append( "Either correct the errors or set the publication status to UNDER_REVISION and re-save" );
    return sb.toString();
  }
}
