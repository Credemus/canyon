package de.objectcode.canyon.bpe.repository;

import java.io.Serializable;

import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.spi.RepositoryException;

/**
 * Process instance repository for all active bpe processes.
 * 
 * @author junglas
 */
public interface IProcessRepository
{
	public long createPackageRevision(String packageId, String packageVersion) throws RepositoryException;
	
  public Long getPackageRevisionOid ( long processEntityOid ) throws RepositoryException;

  public BPEProcess getProcess ( long processEntityOid ) throws RepositoryException;

  public BPEProcess getProcess ( String processId ) throws RepositoryException;
  
  public BPEProcess getProcess ( String processId, String processVersion ) throws RepositoryException;

  public BPEProcess getProcess ( String processId, Long packageRevisionOid ) throws RepositoryException;

  public Serializable getProcessSource ( String processId ) throws RepositoryException;
  
  public Serializable getProcessSource ( String processId, long packageRevisionOid ) throws RepositoryException;
  
  public Serializable getProcessSource ( String processId, String version ) throws RepositoryException;

  public void createProcess ( long packageRevisionOid, BPEProcess process, Serializable processSource ) throws RepositoryException;
  
  public void updateProcess ( BPEProcess process, Serializable processSource ) throws RepositoryException;

  public int countProcesses ( ) throws RepositoryException;
  
  public void iterateProcesses ( IProcessVisitor visitor, boolean onlyActive ) throws RepositoryException;

  public void iterateProcessesNoSource ( IProcessNoSourceVisitor visitor ) throws RepositoryException;

  public void iterateCompactProcesses ( IProcessVisitor visitor, boolean onlyActive ) throws RepositoryException; 
}
