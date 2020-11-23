package de.objectcode.canyon.spi;

/**
 * @author junglas
 */
public class ObjectNotFoundException extends RepositoryException {
	static final long serialVersionUID = 4658900994763765942L;
	
	public ObjectNotFoundException() {
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }
}