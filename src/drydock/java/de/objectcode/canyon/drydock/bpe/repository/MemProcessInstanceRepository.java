package de.objectcode.canyon.drydock.bpe.repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;

import de.objectcode.canyon.bpe.engine.activities.ActivityState;
import de.objectcode.canyon.bpe.repository.IProcessInstanceRepository;
import de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor;
import de.objectcode.canyon.bpe.repository.ProcessInstance;
import de.objectcode.canyon.spi.RepositoryException;

/**
 * @author    junglas
 * @created   21. Juni 2004
 */
public class MemProcessInstanceRepository implements IProcessInstanceRepository
{
  private    Map  m_instances;
  protected  int  m_instanceCounter;


  /**
   *Constructor for the MemProcessInstanceRepository object
   */
  public MemProcessInstanceRepository()
  {
    m_instances = new HashMap();
    m_instanceCounter = 0;
  }


  /**
   * @param processInstanceId        Description of the Parameter
   * @return                         The processInstance value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.bpe.repository.IProcessInstanceRepository#getProcessInstance(java.lang.String)
   */
  public synchronized ProcessInstance getProcessInstance( String processInstanceId )
    throws RepositoryException
  {
    byte[] data = (byte[])m_instances.get(processInstanceId);
    
    return ( ProcessInstance ) SerializationUtils.deserialize(data);
  }


  /**
   * @param processInstanceId        Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.bpe.repository.IProcessInstanceRepository#lockProcessInstance(java.lang.String)
   */
  public synchronized void lockProcessInstance( String processInstanceId )
    throws RepositoryException
  {
    // TODO Auto-generated method stub
  }


  /**
   * @param processInstance          Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.bpe.repository.IProcessInstanceRepository#saveProcessInstance(de.objectcode.canyon.bpe.repository.ProcessInstance)
   */
  public synchronized String saveProcessInstance( ProcessInstance processInstance )
    throws RepositoryException
  {
    m_instanceCounter++;

    processInstance.setProcessInstanceId( String.valueOf( m_instanceCounter ) );

    m_instances.put( processInstance.getProcessInstanceId(), SerializationUtils.serialize(processInstance) );

    System.out.println(">>>>>>>>>>>>>>>>>> " + processInstance.getProcessInstanceId() + " "  + processInstance.getProcessState().length);
    return processInstance.getProcessInstanceId();
  }


  /**
   * @param processInstance          Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.bpe.repository.IProcessInstanceRepository#updateProcessInstance(de.objectcode.canyon.bpe.repository.ProcessInstance)
   */
  public synchronized void updateProcessInstance( ProcessInstance processInstance )
    throws RepositoryException
  {
    m_instances.put( processInstance.getProcessInstanceId(), SerializationUtils.serialize(processInstance) );
    System.out.println(">>>>>>>>>>>>>>>>>> " + processInstance.getProcessInstanceId() + " " + processInstance.getProcessState().length);
  }
  
  
  /**
   * @see de.objectcode.canyon.bpe.repository.IProcessInstanceRepository#iterateProcessInstances(de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor)
   */
  public void iterateProcessInstances (boolean onlyOpenRunning, IProcessInstanceVisitor visitor)
      throws RepositoryException
  {
    Iterator it = m_instances.values().iterator();
    
    while ( it.hasNext() ) {
      byte[] data = (byte[])it.next();
      ProcessInstance processInstance = (ProcessInstance)SerializationUtils.deserialize(data);
      
      if ( !onlyOpenRunning || processInstance.getState() == ActivityState.OPEN || processInstance.getState() == ActivityState.RUNNING )
        visitor.visit(processInstance);
    }
  }
  
  
  /**
   * @see de.objectcode.canyon.bpe.repository.IProcessInstanceRepository#iterateProcessInstances(java.lang.String, boolean, de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor)
   */
  public void iterateProcessInstances (String processId,
                                       boolean onlyOpenRunning,
                                       IProcessInstanceVisitor visitor)
      throws RepositoryException
  {
    Iterator it = m_instances.values().iterator();
    
    while ( it.hasNext() ) {
      byte[] data = (byte[])it.next();
      ProcessInstance processInstance = (ProcessInstance)SerializationUtils.deserialize(data);
      
      if ( processId.equals(processInstance.getProcessId()) && (!onlyOpenRunning || processInstance.getState() == ActivityState.OPEN || processInstance.getState() == ActivityState.RUNNING ))
        visitor.visit(processInstance);
    }    
  }


	public void updateProcessInstances(IProcessInstanceVisitor visitor) throws RepositoryException {
		// TODO Auto-generated method stub
		
	}


	public void migrateProcessInstances(IProcessInstanceVisitor visitor) throws RepositoryException {
		// TODO Auto-generated method stub
		
	}


	public void iterateSubProcessInstances(String parentProcessInstanceId, IProcessInstanceVisitor visitor) throws RepositoryException {
		// TODO Auto-generated method stub
		
	}
}
