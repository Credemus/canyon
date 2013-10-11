package de.objectcode.canyon.spi;

import de.objectcode.canyon.CanyonException;

/**
 * @author    junglas
 * @created   27. November 2003
 */
public class RepositoryException extends CanyonException
{
	static final long serialVersionUID = 1923234912392951122L;
	
	/**
   *Constructor for the RepositoryException object
   */
  public RepositoryException() { }


  /**
   *Constructor for the RepositoryException object
   *
   * @param message  Description of the Parameter
   */
  public RepositoryException( String message )
  {
    super( message );
  }


  /**
   *Constructor for the RepositoryException object
   *
   * @param t  Description of the Parameter
   */
  public RepositoryException( Throwable t )
  {
    super( t );
  }


  /**
   *Constructor for the RepositoryException object
   *
   * @param message  Description of the Parameter
   * @param t        Description of the Parameter
   */
  public RepositoryException( String message, Throwable t )
  {
    super( message, t );
  }

}
