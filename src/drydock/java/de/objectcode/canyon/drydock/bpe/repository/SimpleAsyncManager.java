package de.objectcode.canyon.drydock.bpe.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.async.IAsyncManager;
import de.objectcode.canyon.spi.instance.IActivityInstance;
import de.objectcode.canyon.spi.instance.IProcessInstance;

/**
 * @author    junglas
 * @created   15. Juli 2004
 */
public class SimpleAsyncManager implements IAsyncManager
{
  private  List  m_requests  = new ArrayList();

  public synchronized void executeRequests ( BPEEngine bpeEngine )  throws EngineException, RepositoryException
  {
    List requests = m_requests;
    
    m_requests = new ArrayList();
    
    Iterator it = requests.iterator();
    
    while ( it.hasNext() ) {
      AsyncCompleteBPEActivity request = (AsyncCompleteBPEActivity)it.next();
      
      request.execute(bpeEngine);
    }
  }

  /**
   * @param processInstanceId  Description of the Parameter
   * @param activityId         Description of the Parameter
   * @see                      de.objectcode.canyon.spi.async.IAsyncManager#asyncCompleteBPEActivity(java.lang.String, java.lang.String)
   */
  public synchronized void asyncCompleteBPEActivity( String processInstanceId,
      String activityId )
  {
    m_requests.add( new AsyncCompleteBPEActivity( processInstanceId, activityId ) );
  }


  /**
   * @param instance  Description of the Parameter
   * @see             de.objectcode.canyon.spi.async.IAsyncManager#asyncExecuteActivity(de.objectcode.canyon.spi.instance.IActivityInstance)
   */
  public synchronized void asyncExecuteActivity( IActivityInstance instance ) { }


  /**
   * @param processInstanceId   Description of the Parameter
   * @param activityInstanceId  Description of the Parameter
   * @param transitionId        Description of the Parameter
   * @see                       de.objectcode.canyon.spi.async.IAsyncManager#asyncNotification(java.lang.String, java.lang.String, java.lang.String)
   */
  public synchronized void asyncNotification( String processInstanceId,
      String activityInstanceId, String transitionId ) { }


  /**
   * @param instance  Description of the Parameter
   * @see             de.objectcode.canyon.spi.async.IAsyncManager#asyncStartActivity(de.objectcode.canyon.spi.instance.IActivityInstance)
   */
  public synchronized void asyncStartActivity( IActivityInstance instance ) { }


  /**
   * @param instance  Description of the Parameter
   * @see             de.objectcode.canyon.spi.async.IAsyncManager#asyncStartProcess(de.objectcode.canyon.spi.instance.IProcessInstance)
   */
  public synchronized void asyncStartProcess( IProcessInstance instance ) { }


  /**
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.async.IAsyncManager#beginTransaction()
   */
  public void beginTransaction()
    throws RepositoryException { }


  /**
   * @param flush                    Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.async.IAsyncManager#endTransaction(boolean)
   */
  public void endTransaction( boolean flush )
    throws RepositoryException { }
}
