package de.objectcode.canyon.spi.process;

import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.filter.IFilter;

/**
 * @author    junglas
 * @created   15. Oktober 2003
 */
public interface IProcessRepository
{
	public void beginTransaction() throws RepositoryException;
	
	public void endTransaction(boolean flush) throws RepositoryException;
	
  /**
   * Creates a package using the supplied content.
   *
   * @param pkg                                                 The pre-parsed package object.
   * @param state                                               Description of the Parameter
   * @exception RepositoryException                             Description of the Exception
   * @throws org.obe.client.api.repository.RepositoryException
   */
  public void createPackage( WorkflowPackage pkg, int state )
    throws RepositoryException;


  /**
   * Permanently deletes the specified package.
   *
   * @param packageId                                           The package ID.
   * @exception RepositoryException                             Description of the Exception
   * @throws org.obe.client.api.repository.RepositoryException
   */
  public void deletePackage( String packageId )
    throws RepositoryException;


  /**
   * Sets the content of the specified package.
   *
   * @param pkg                                                 The package to write.
   * @exception RepositoryException                             Description of the Exception
   * @throws org.obe.client.api.repository.RepositoryException
   */
  public void updatePackage( WorkflowPackage pkg )
    throws RepositoryException;


  /**
   * Count the number of packages matching some filter criteria.
   *
   *
   * @param filter
   * @return
   * @throws RepositoryException
   */
  public int countPackages( IFilter filter )
    throws RepositoryException;


  /**
   * Retrieves a set of packages.
   *
   * @param filter                   A package filter specification.
   * @return                         An array of matching packages.
   * @exception RepositoryException  Description of the Exception
   */
  public WorkflowPackage[] findPackages( IFilter filter )
    throws RepositoryException;


  /**
   * Retrieves the latest version of a package.
   *
   * @param packageId                The ID of the package to retrieve.
   * @return                         The requested package.
   * @exception RepositoryException  Description of the Exception
   */
  public WorkflowPackage findPackage( String packageId )
    throws RepositoryException;


  /**
   * Retrieves a specific version of a package.
   *
   *
   * @param packageId             Description of the Parameter
   * @param version               Description of the Parameter
   * @return
   * @throws RepositoryException
   */
  public WorkflowPackage findPackage( String packageId, String version )
    throws RepositoryException;


  /**
   * Retrieve a process definition in executable form.
   *
   * @param processDefinitionId      The process definition ID.
   * @return                         The requested process definitions.
   * @exception RepositoryException  Description of the Exception
   */
  public WorkflowProcess findWorkflowProcess( IProcessDefinitionID processDefinitionId )
    throws RepositoryException;


  /**
   * Retrieve the latest version of a process definition.
   *
   * @param processDefinitionId      Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public WorkflowProcess findWorkflowProcess( String processDefinitionId )
    throws RepositoryException;


  /**
   * Count the number of process definitions matching some filter criteria.
   *
   *
   * @param filter
   * @return
   * @throws RepositoryException
   */
  public int countWorkflowProcesses( IFilter filter )
    throws RepositoryException;


  /**
   * Retrieve a list of process definitions.
   *
   * @param filter                   A process definition filter specification.
   * @return                         An array of matching process definitions.
   * @exception RepositoryException  Description of the Exception
   */
  public WorkflowProcess[] findWorkflowProcesses( IFilter filter )
    throws RepositoryException;


  /**
   * Returns the state of a process definition.
   *
   * @param processDefinitionId                                 The process definition id
   * @return                                                    The process definition state, as defined in
   * {@link org.wfmc.wapi.WMProcessDefinitionState}
   * @exception RepositoryException                             Description of the Exception
   * @throws org.obe.client.api.repository.RepositoryException
   */
  public int findProcessDefinitionState( String processDefinitionId )
    throws RepositoryException;


  /**
   * Change the process definition state.
   *
   * @param processDefinitionId                                 The process definition id
   * @param newState                                            The new process definition state
   * @exception RepositoryException                             Description of the Exception
   * @throws org.obe.client.api.repository.RepositoryException  Workflow client exception
   */
  public void updateProcessDefinitionState( String processDefinitionId,
      int newState )
    throws RepositoryException;
}
