package de.objectcode.canyon.drydock.bpe.repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;

import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.repository.IProcessRepository;
import de.objectcode.canyon.bpe.repository.IProcessVisitor;
import de.objectcode.canyon.spi.RepositoryException;

/**
 * @author    junglas
 * @created   14. Juni 2004
 */
public class MemProcessRepository implements IProcessRepository
{
  protected  Map  m_processes;
  protected  Map  m_processesById;
  protected  int  m_instanceCounter;


  /**
   *Constructor for the MemProcessRepository object
   */
  public MemProcessRepository()
  {
    m_processes = new HashMap();
    m_processesById = new HashMap();
    m_instanceCounter = 0;
  }

  public synchronized BPEProcess getProcess ( long processEntityOid )
  	throws RepositoryException
  {
    ProcessHolder holder = (ProcessHolder)m_processesById.get(new Long(processEntityOid));
    
    if ( holder != null)
      return holder.getProcess();
    
    throw new RepositoryException( "Process '" + processEntityOid + "' not found" );
  }

  /**
   * @param processId                Description of the Parameter
   * @return                         The process value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.bpe.repository.IProcessRepository#getProcess(java.lang.String)
   */
  public synchronized BPEProcess getProcess( String processId )
    throws RepositoryException
  {
    ProcessHolder holder = (ProcessHolder)m_processes.get(processId);
    
    if ( holder != null)
      return holder.getProcess();

    throw new RepositoryException( "Process '" + processId + "' not found" );
  }

  public Serializable getProcessSource ( String processId ) throws RepositoryException
  {
    return null;
  }

  /**
   * @param processId                Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.bpe.repository.IProcessRepository#lockProcess(java.lang.String)
   */
  public void lockProcess( String processId )
    throws RepositoryException
  {
    // TODO Auto-generated method stub

  }


  /**
   * @param process                  Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.bpe.repository.IProcessRepository#saveProcess(de.objectcode.canyon.bpe.engine.activities.BPEProcess)
   */
  public synchronized void saveProcess( BPEProcess process, Serializable processSource )
    throws RepositoryException
  {
    long entityOid = m_instanceCounter++;
    
    ProcessHolder holder = new ProcessHolder(entityOid, process);
    
    m_processes.put(process.getId(), holder);
    m_processesById.put(new Long(entityOid), holder);
  }


  /**
   * @param process                  Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.bpe.repository.IProcessRepository#updateProcess(de.objectcode.canyon.bpe.engine.activities.BPEProcess)
   */
  public synchronized void updateProcess( BPEProcess process, Serializable processSource )
    throws RepositoryException
  {
    ProcessHolder holder = new ProcessHolder(process.getProcessEntityOid(), process);
    
    m_processes.put(process.getId(), holder);
    m_processesById.put(new Long(process.getProcessEntityOid()), holder);
  }

  public synchronized int countProcesses ( ) throws RepositoryException
  {
    return m_processes.size();
  }

  
  /**
   * @see de.objectcode.canyon.bpe.repository.IProcessRepository#iterateProcesses(de.objectcode.canyon.bpe.engine.activities.ActivityState, de.objectcode.canyon.bpe.repository.IProcessVisitor)
   */
  public synchronized void iterateProcesses (IProcessVisitor visitor, boolean onlyActive)
      throws RepositoryException
  {
    Iterator it = m_processes.values().iterator();
    
    while ( it.hasNext() ) {
      ProcessHolder holder = (ProcessHolder)it.next();
      
      visitor.visit(holder.getProcess(), null);
    }
  }
  
  private static class ProcessHolder
  {
    long m_entityOid;
    byte[] m_processData;
    
    ProcessHolder ( long entityOid, BPEProcess process ) 
    {
      m_entityOid = entityOid;
      m_processData = SerializationUtils.serialize( process );
    }
    
    public BPEProcess getProcess()
    {
      BPEProcess process = ( BPEProcess ) SerializationUtils.deserialize( m_processData );
      
      process.setProcessEntityOid(m_entityOid);
      
      return process;
    }
  }

	public Serializable getProcessSource(String processId, long packageRevOid) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Serializable getProcessSource(String processId, String version) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public BPEProcess getProcess(String processId, String processVersion) throws RepositoryException {
		return null;
	}

	public long createPackageRevision(String packageId, String packageVersion) throws RepositoryException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Long getPackageRevisionOid(long processEntityOid) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public BPEProcess getProcess(String processId, Long packageReleaseOid) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public void createProcess(long packageReleaseOid, BPEProcess process, Serializable processSource) throws RepositoryException {
		// TODO Auto-generated method stub
		
	}
}
