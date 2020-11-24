package de.objectcode.canyon.ejb.bpe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.api.worklist.ProcessData;
import de.objectcode.canyon.api.worklist.ProcessInstanceData;
import de.objectcode.canyon.api.worklist.ProcessInstanceData.Attribute;
import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.BPERuntimeContext;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor;
import de.objectcode.canyon.bpe.repository.IProcessRepository;
import de.objectcode.canyon.bpe.repository.IProcessVisitor;
import de.objectcode.canyon.bpe.repository.ProcessInstance;
import de.objectcode.canyon.ejb.BaseServiceManagerBean;
import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.data.BasicType;
import de.objectcode.canyon.model.data.FormalParameter;
import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spiImpl.tool.bpe.BPEFacade;

/**
 * @ejb.bean name="BPEProcess" type="Stateless"
 *           jndi-name="de/objectcode/canyon/ejb/bpe/Process"
 *           local-jndi-name="de/objectcode/canyon/ejb/bpe/ProcessLocal"
 *           transaction-type="Bean"
 *           view-type="both"
 * @ejb.permission unchecked="true"
 *
 * @ejb.resource-ref res-ref-name="ServiceManager" res-type="de.objectcode.canyon.spi.ServiceManager"
 *   res-auth="Application"
 * @jboss.resource-ref res-ref-name="ServiceManager" jndi-name="java:/canyon/ServiceManager"
 *
 * @author    junglas
 * @created   13. Juli 2004
 */
public class ProcessBean extends BaseServiceManagerBean
{
	static final long serialVersionUID = 9067988689544551203L;

	protected transient   BPEFacade  m_bpeFacade;

  /**
   * @ejb.interface-method
   *
   * @param processInstanceId        Description of the Parameter
   * @return                         The processInstances value
   * @exception WMWorkflowException  Description of the Exception
   */
  public ProcessInstanceData getProcessInstance( String processInstanceId )
    throws WMWorkflowException
  {
    try {
      beginTransaction();
      return m_bpeFacade.getProcessInstance(processInstanceId);
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }

  /**
   * @ejb.interface-method
   *
   * @param processInstanceId        Description of the Parameter
   * @return                         The processInstances value
   * @exception WMWorkflowException  Description of the Exception
   */
  public ProcessInstanceData getProcessInstanceReadOnly( String processInstanceId )
    throws WMWorkflowException
  {
    try {
      beginTransaction();
      return m_bpeFacade.getProcessInstanceReadOnly(processInstanceId);
    }
    catch ( RepositoryException e ) {
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
          rollbackTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }

  /**
   * @ejb.interface-method
   *
   * @param processId                Description of the Parameter
   * @return                         The process value
   * @exception WMWorkflowException  Description of the Exception
   */
  public ProcessData getProcessDefinition( String processId )
    throws WMWorkflowException
  {
    try {
      beginTransaction();
      return m_bpeFacade.getProcessDefinition(processId);
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }

  /**
   * @ejb.interface-method
   *
   * @param processId                Description of the Parameter
   * @return                         The process value
   * @exception WMWorkflowException  Description of the Exception
   */
  public ProcessData getProcessDefinition( String processId, String version)
    throws WMWorkflowException
  {
    try {
      beginTransaction();
      return m_bpeFacade.getProcessDefinition(processId, version);
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }

  /**
   * @ejb.interface-method
   *
   * @param processId                Description of the Parameter
   * @return                         The process value
   * @exception WMWorkflowException  Description of the Exception
   */
  public WorkflowProcess getProcess( String processId, String processVersion )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("getProcess: " + processId);
    }
    try {
      beginTransaction();
      return m_bpeFacade.getProcess(processId, processVersion);
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }


  /**
   * @ejb.interface-method
   *
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public int countProcesses()
    throws WMWorkflowException
  {
    try {
      beginTransaction();
      return m_bpeFacade.countProcesses();
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }


  /**
   * @ejb.interface-method
   *
   * @param processInstanceId        Description of the Parameter
   * @param participantId            Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public String[] findMembers( String processInstanceId, String participantId, String clientId )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug( "findMembers " + processInstanceId + "," + participantId );
    }

    try {
      beginTransaction();
      return m_bpeFacade.findMembers(processInstanceId, participantId, clientId);
    }
    catch ( WMWorkflowException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw e;
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }


  /**
   * @ejb.interface-method
   *
   * @param offset                   Description of the Parameter
   * @param length                   Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public ProcessData[] listActiveProcesses()
    throws WMWorkflowException
  {
      if ( m_log.isDebugEnabled() ) {
        m_log.debug("listActiveProcesses:");
      }
      try {
        beginTransaction();
        return m_bpeFacade.listProcesses(true,0,Integer.MAX_VALUE);
      }
      catch ( RepositoryException e ) {
        rollbackTransaction();
        m_log.error( "Exception", e );
        if ( e.getCause() != null ) {
          m_log.error( "Cause", e.getCause() );
        }
        throw new WMWorkflowException( e );
      }
      finally {
        try {
          commitTransaction();
        }
        catch ( Exception e ) {
          m_log.error( "Exception", e );
        }
      }
  }
  
 /**
   * @ejb.interface-method
   *
   * @param offset                   Description of the Parameter
   * @param length                   Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public ProcessData[] listProcesses( int offset, int length )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("listProcesses: " + offset + " " + length);
    }
    try {
      beginTransaction();
      return m_bpeFacade.listProcesses(offset,length);
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }
  
  /**
   * @ejb.interface-method
   *
   * @param offset                   Description of the Parameter
   * @param length                   Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public ProcessData[] listCompactProcesses( int offset, int length )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("listProcesses: " + offset + " " + length);
    }
    try {
      beginTransaction();
      return m_bpeFacade.listCompactProcesses(offset,length);
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }
  

  /**
   * @ejb.interface-method
   *
   * @param offset                   Description of the Parameter
   * @param length                   Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public ProcessData[] listProcessData( int offset, int length )
    throws WMWorkflowException
  {
    return listProcesses(offset,length);
  }
  

  /**
   * @ejb.interface-method
   *
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public WorkflowProcess[] listProcesses()
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("listProcesses:");
    }
    try {
      beginTransaction();
      return m_bpeFacade.listProcesses();
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }


  /**
   * @ejb.interface-method
   *
   * @param processData              Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public ProcessInstanceData createAndStartProcessInstance( ProcessData processData )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug( "start an create instance process '" + processData.getName() + "'" );
    }
    String  processInstanceId  = null;
    try {
      beginTransaction();
      return m_bpeFacade.createAndStartProcessInstance(processData);
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    catch ( EngineException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }

    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }


  /**
   * @ejb.interface-method
   *
   * @param processData              Description of the Parameter
   * @param userId                   Description of the Parameter
   * @param clientId                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public ProcessInstanceData createAndStartProcessInstance( String userId, String clientId, ProcessData processData )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug( "start an create instance process '" + processData.getName() + "' " + userId + " " + clientId );
    }
    try {
      beginTransaction();
      return m_bpeFacade.createAndStartProcessInstance(userId, clientId,processData,null);
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    catch ( EngineException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }

    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }

  /**
   * @ejb.interface-method
   *
   * @param processData              Description of the Parameter
   * @param userId                   Description of the Parameter
   * @param clientId                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public void terminateProcessInstance( String processInstanceId )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug( "terminate instance process '" + processInstanceId + "'" );
    }
    try {
      beginTransaction();
      m_bpeFacade.terminateProcessInstance(processInstanceId);
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    catch ( EngineException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }

    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }

  /**
   * @ejb.interface-method
   *
   * @param processData              Description of the Parameter
   * @param userId                   Description of the Parameter
   * @param clientId                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public void updateProcessInstance( ProcessInstanceData processInstanceData )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug( "updateProcessInstance process '" + processInstanceData.getId() + "'" );
    }
    try {
      beginTransaction();
      m_bpeFacade.updateProcessInstance(processInstanceData);
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }

    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }

  /**
   * @ejb.interface-method
   *
   * @param processId                Description of the Parameter
   * @param offset                   Description of the Parameter
   * @param length                   Description of the Parameter
   * @param onlyOpen                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public ProcessInstanceData[] listProcessInstances( String processId, int offset, int length, boolean onlyOpen )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("listProcessInstances: " + processId + " " + offset + " " + length + " " + onlyOpen);
    }
    try {
      beginTransaction();
      return m_bpeFacade.listProcessInstances(processId, offset, length, onlyOpen);
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }


  /**
   * @ejb.interface-method
   *
   * @param processId                Description of the Parameter
   * @param offset                   Description of the Parameter
   * @param length                   Description of the Parameter
   * @param onlyOpen                 Description of the Parameter
   * @param clientId                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public ProcessInstanceData[] listProcessInstances( String processId, String clientId, int offset, int length, boolean onlyOpen )
    throws WMWorkflowException
  {
    try {
      beginTransaction();
      return m_bpeFacade.listProcessInstances(processId, clientId, offset, length, onlyOpen);
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }




  /**
   * Create a new ZReihe SessionBean.
   *
   * @exception CreateException  on error
   *
   * @ejb.create-method
   */
  public void ejbCreate()
    throws CreateException
  {
    m_log.debug( "Create" );

    obtainServiceManager();
  }


  /**
   * @see   de.objectcode.canyon.ejb.BaseServiceManagerBean#obtainServiceManager()
   */
  protected void obtainServiceManager()
  {
    super.obtainServiceManager();

    m_bpeFacade = new BPEFacade(m_serviceManager.getBpeEngine());
  }

}
