package de.objectcode.canyon.drydock.bpe.repository;

import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.spi.RepositoryException;

/**
 * @author    junglas
 * @created   5. August 2004
 */
public class AsyncCompleteBPEActivity
{
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
   * @param bpeEngine                Description of the Parameter
   * @exception EngineException      Description of the Exception
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.jmx.async.AsyncRequest#execute(de.objectcode.canyon.spi.ServiceManager)
   */
  public void execute( BPEEngine bpeEngine )
    throws EngineException, RepositoryException
  {
    BPEProcess         process   = bpeEngine.getProcessInstance( m_processInstanceId );

    Activity           activity  = process.getActivity( m_activityId );

    activity.complete();

    process.startActivatedActivities();

    bpeEngine.updateProcessInstance( process );
  }
}
