package de.objectcode.canyon.spi.lock;

import de.objectcode.canyon.spi.RepositoryException;

/**
 * @author junglas
 */
public interface ILockManager
{
	void lock(String id) throws RepositoryException;
	
	void unlock(String id) throws RepositoryException;
	
	void releaseAllLocks() throws RepositoryException;

	String dumpAllLocks() throws RepositoryException;

}
