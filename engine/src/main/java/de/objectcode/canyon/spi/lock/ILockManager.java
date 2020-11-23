package de.objectcode.canyon.spi.lock;

import de.objectcode.canyon.spi.RepositoryException;

/**
 * @author junglas
 */
public interface ILockManager
{
	public void lock ( String id ) throws RepositoryException;
	
	public void unlock ( String id ) throws RepositoryException;
	
	public void releaseAllLocks ( ) throws RepositoryException;

	public String dumpAllLocks ( ) throws RepositoryException;

}
