package de.objectcode.canyon.spi;

/**
 * @author    junglas
 * @created   27. November 2003
 */
public class ObjectAlreadyExistsException extends RepositoryException
{
	static final long serialVersionUID = 2762075722544395303L;
	
	/**
   *Constructor for the ObjectAlreadyExistsException object
   */
  public ObjectAlreadyExistsException() { }


  /**
   *Constructor for the ObjectAlreadyExistsException object
   *
   * @param message  Description of the Parameter
   */
  public ObjectAlreadyExistsException( String message )
  {
    super( message );
  }

}
