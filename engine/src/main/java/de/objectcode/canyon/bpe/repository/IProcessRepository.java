package de.objectcode.canyon.bpe.repository;

import java.io.Serializable;

import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.spi.RepositoryException;

/**
 * Process instance repository for all active bpe processes.
 *
 * @author junglas
 */
public interface IProcessRepository {
  long createPackageRevision(String packageId, String packageVersion) throws RepositoryException;

  Long getPackageRevisionOid(long processEntityOid) throws RepositoryException;

  BPEProcess getProcess(long processEntityOid) throws RepositoryException;

  BPEProcess getProcess(String processId) throws RepositoryException;

  BPEProcess getProcess(String processId, String processVersion) throws RepositoryException;

  BPEProcess getProcess(String processId, Long packageRevisionOid) throws RepositoryException;

  Serializable getProcessSource(String processId) throws RepositoryException;

  Serializable getProcessSource(String processId, long packageRevisionOid) throws RepositoryException;

  Serializable getProcessSource(String processId, String version) throws RepositoryException;

  void createProcess(long packageRevisionOid, BPEProcess process, Serializable processSource) throws RepositoryException;

  void updateProcess(BPEProcess process, Serializable processSource) throws RepositoryException;

  int countProcesses() throws RepositoryException;

  void iterateProcesses(IProcessVisitor visitor, boolean onlyActive) throws RepositoryException;

  void iterateProcessesNoSource(IProcessNoSourceVisitor visitor) throws RepositoryException;

  void iterateCompactProcesses(IProcessVisitor visitor, boolean onlyActive) throws RepositoryException;
}
