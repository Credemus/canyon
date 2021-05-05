package de.objectcode.canyon.bpe.repository;

import de.objectcode.canyon.spi.RepositoryException;

/**
 * @author junglas
 */
public interface IProcessInstanceRepository {
  ProcessInstance getProcessInstance(String processInstanceId) throws RepositoryException;

  String saveProcessInstance(ProcessInstance processInstance) throws RepositoryException;

  void updateProcessInstance(ProcessInstance processInstance) throws RepositoryException;

  void updateProcessInstances(IProcessInstanceVisitor visitor)
          throws RepositoryException;

  void migrateProcessInstances(IProcessInstanceVisitor visitor)
          throws RepositoryException;

  void iterateProcessInstances(boolean onlyOpenRunning, IProcessInstanceVisitor visitor) throws RepositoryException;

  void iterateSubProcessInstances(String parentProcessInstanceId, IProcessInstanceVisitor visitor) throws RepositoryException;

  void iterateProcessInstances(String processId, boolean onlyOpenRunning, IProcessInstanceVisitor visitor) throws RepositoryException;
}
