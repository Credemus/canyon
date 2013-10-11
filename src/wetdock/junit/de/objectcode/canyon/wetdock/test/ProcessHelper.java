package de.objectcode.canyon.wetdock.test;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.api.bpe.BPEProcess;
import de.objectcode.canyon.api.bpe.BPEProcessHome;
import de.objectcode.canyon.api.worklist.IParameter;
import de.objectcode.canyon.api.worklist.ProcessData;
import de.objectcode.canyon.api.worklist.ProcessInstanceData;

/**
 * @author junglas
 */
public class ProcessHelper
{
  private BPEProcess m_process;

  public ProcessHelper()
      throws NamingException, RemoteException, CreateException
  {
    InitialContext ctx = new InitialContext();

    BPEProcessHome processHome = (BPEProcessHome) PortableRemoteObject.narrow( ctx
        .lookup( BPEProcessHome.JNDI_NAME ), BPEProcessHome.class );

    m_process = processHome.create();
  }

  /**
   * @return Returns the process.
   */
  public BPEProcess getProcess()
  {
    return m_process;
  }

  public ProcessInstanceData startProcess( String userId, String clientId, String processId, Map parameters )
      throws RemoteException, WMWorkflowException, IllegalAccessException
  {
    ProcessData processData = m_process.getProcessDefinition( processId );

    if ( parameters != null ) {
      ProcessData.Parameter params[] = processData.getParameters();
      int i;
      
      for ( i = 0; i < params.length; i++ ) {
          params[i].setValue(parameters.get(params[i].getName()));
      }
    }
    
    return m_process.createAndStartProcessInstance( userId, clientId, processData );
  }
  
  public ProcessData[] getActiveProcesses() throws RemoteException, WMWorkflowException {
  	return m_process.listActiveProcesses();
  }
  
  public ProcessInstanceData getProcessInstance ( String processInstanceId )
      throws RemoteException, WMWorkflowException
  {
    return m_process.getProcessInstance(processInstanceId);
  }
  
  public ProcessInstanceData getProcessInstanceReadOnly(String processInstanceId)
			throws RemoteException, WMWorkflowException {
		return m_process.getProcessInstanceReadOnly(processInstanceId);
	}

  public ProcessInstanceData[] listProcessInstances ( String processId, String clientId, boolean onlyOpen )
      throws RemoteException, WMWorkflowException
  {
    return m_process.listProcessInstances(processId, clientId, 0, 1000, onlyOpen);
  }
}
