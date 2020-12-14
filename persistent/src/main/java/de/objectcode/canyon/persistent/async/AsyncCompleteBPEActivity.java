package de.objectcode.canyon.persistent.async;

import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;

/**
 * @author junglas
 */
public class AsyncCompleteBPEActivity extends AsyncRequest
{
	static final long serialVersionUID = -4939495142986421716L;
	
	private final  String  m_processInstanceId;
  private final  String  m_activityId;


  /**
   *Constructor for the AsyncCompleteBPEActivity object
   *
   * @param processInstanceId  Description of the Parameter
   * @param activityId         Description of the Parameter
   */
  public AsyncCompleteBPEActivity( String processInstanceId, String activityId )
  {
    m_processInstanceId = processInstanceId;
    m_activityId = activityId;
  }


  /**
   * @param serviceManager           Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   * @see                            de.objectcode.canyon.jmx.async.AsyncRequest#execute(de.objectcode.canyon.spi.ServiceManager)
   */
  public void execute( ServiceManager serviceManager )
    throws WMWorkflowException
  {

    try {
      BPEEngine   bpeEngine  = serviceManager.getBpeEngine();
      BPEProcess  process    = bpeEngine.getProcessInstance( m_processInstanceId );

      Activity    activity   = process.getActivity( m_activityId );

      activity.complete();

      process.startActivatedActivities();

      bpeEngine.updateProcessInstance( process );
    }
    catch ( EngineException e ) {
      throw new WMWorkflowException( e );
    }
    catch ( RepositoryException e ) {
      throw new WMWorkflowException( e );
    }
  }


  /**
   * @param serviceManager  Description of the Parameter
   * @see                   de.objectcode.canyon.jmx.async.AsyncRequest#fail(de.objectcode.canyon.spi.ServiceManager)
   */
  public void fail( ServiceManager serviceManager )
  {

    try {
      BPEEngine   bpeEngine  = serviceManager.getBpeEngine();
      BPEProcess  process    = bpeEngine.getProcessInstance( m_processInstanceId );

      Activity    activity   = process.getActivity( m_activityId );

      activity.complete();

      process.startActivatedActivities();

      bpeEngine.updateProcessInstance( process );
    }
    catch ( Exception e ) {
    }
  }
}
