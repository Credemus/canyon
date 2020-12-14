package de.objectcode.canyon.jmx.async;

import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMWorkflowException;

/**
 * @author    junglas
 * @created   15. Juli 2004
 */
public class AsyncCompleteBPEActivity extends AsyncRequest
{
	static final long serialVersionUID = -3587357681824856243L;
	
	private final static  Log     log                  = LogFactory.getLog( AsyncCompleteBPEActivity.class );

  private final         String  m_processInstanceId;
  private final         String  m_activityId;


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
    if ( log.isDebugEnabled() ) {
      log.debug( "execute:" );
    }

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
    if ( log.isDebugEnabled() ) {
      log.debug( "fail:" );
    }
    if (System
				.getProperty("de.objectcode.canyon.jmx.bpe.BPEEngineService.migrationMode") == null) {

			try {
				BPEEngine bpeEngine = serviceManager.getBpeEngine();
				BPEProcess process = bpeEngine.getProcessInstance(m_processInstanceId);

				Activity activity = process.getActivity(m_activityId);

				activity.abort();

				process.startActivatedActivities();

				bpeEngine.updateProcessInstance(process);
			} catch (Exception e) {
			}
		}
  }
}
