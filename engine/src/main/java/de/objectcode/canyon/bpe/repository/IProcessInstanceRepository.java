package de.objectcode.canyon.bpe.repository;

import de.objectcode.canyon.spi.RepositoryException;

/**
 * @author junglas
 */
public interface IProcessInstanceRepository
{
  public ProcessInstance getProcessInstance ( String processInstanceId ) throws RepositoryException;
  
  public String saveProcessInstance ( ProcessInstance processInstance ) throws RepositoryException;
  
  public void updateProcessInstance ( ProcessInstance processInstance ) throws RepositoryException;

  public void updateProcessInstances( IProcessInstanceVisitor visitor )
  throws RepositoryException;
  
  public void migrateProcessInstances( IProcessInstanceVisitor visitor )
  throws RepositoryException;

  public void iterateProcessInstances ( boolean onlyOpenRunning, IProcessInstanceVisitor visitor ) throws RepositoryException;

  public void iterateSubProcessInstances ( String parentProcessInstanceId, IProcessInstanceVisitor visitor ) throws RepositoryException;

  public void iterateProcessInstances ( String processId, boolean onlyOpenRunning, IProcessInstanceVisitor visitor ) throws RepositoryException;
}
