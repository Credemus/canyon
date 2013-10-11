package de.objectcode.canyon.worklist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.api.worklist.BPEEvent;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

/**
 * @author    junglas
 * @created   17. Februar 2004
 */
public abstract class AbstractWorklistEngine implements IWorklistEngine
{
  private final static  Log             log               = LogFactory.getLog( AbstractWorklistEngine.class );
  protected             ServiceManager  m_serviceManager;
  protected             List            m_listeners;


  /**
   *Constructor for the AbstractWorklistEngine object
   *
   * @param serviceManager  Description of the Parameter
   */
  protected AbstractWorklistEngine( ServiceManager serviceManager )
  {
    m_serviceManager = serviceManager;
    m_listeners = new ArrayList();
  }


  /**
   * @param listener  The feature to be added to the WorklistListener attribute
   * @see             de.objectcode.canyon.worklist.IWorklistEngine#addWorklistListener(de.objectcode.canyon.worklist.IWorklistListener)
   */
  public void addWorklistListener( IWorklistListener listener )
  {
    m_listeners.add( listener );
  }


  /**
   * @param listener  Description of the Parameter
   * @see             de.objectcode.canyon.worklist.IWorklistEngine#removeWorklistListener(de.objectcode.canyon.worklist.IWorklistListener)
   */
  public void removeWorklistListener( IWorklistListener listener )
  {
    m_listeners.remove( listener );
  }


  /**
   * Description of the Method
   *
   * @param workItem                 Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  protected void fireWorkItemStarted( IWorkItem workItem )
    throws WMWorkflowException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "fireWorkItemStarted: " + workItem.getWorkItemId() );
    }
    m_serviceManager.getBpeEngine().getEventHub().fireManualActivityStateChanged(workItem,BPEEvent.Type.MANUAL_ACTIVITY_ACCEPTED);

    Iterator  it  = m_listeners.iterator();

    while ( it.hasNext() ) {
      IWorklistListener  listener  = ( IWorklistListener ) it.next();

      listener.workItemStarted( workItem );
    }
  }


  /**
   * Description of the Method
   *
   * @param workItem                 Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  protected void fireWorkItemCompleted( IWorkItem workItem )
    throws WMWorkflowException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "fireWorkItemCompleted: " + workItem.getWorkItemId() );
    }
    m_serviceManager.getBpeEngine().getEventHub().fireManualActivityStateChanged(workItem,BPEEvent.Type.MANUAL_ACTIVITY_COMPLETED);

    Iterator  it  = m_listeners.iterator();

    while ( it.hasNext() ) {
      IWorklistListener  listener  = ( IWorklistListener ) it.next();

      listener.workItemCompleted( workItem );
    }
  }


  /**
   * Description of the Method
   *
   * @param workItem                 Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  protected void fireWorkItemAborted( IWorkItem workItem )
    throws WMWorkflowException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "fireWorkItemAborted: " + workItem.getWorkItemId() );
    }
    m_serviceManager.getBpeEngine().getEventHub().fireManualActivityStateChanged(workItem,BPEEvent.Type.MANUAL_ACTIVITY_ABORTED);    

    Iterator  it  = m_listeners.iterator();

    while ( it.hasNext() ) {
      IWorklistListener  listener  = ( IWorklistListener ) it.next();

      listener.workItemAborted( workItem );
    }
  }


  /**
   * Description of the Method
   *
   * @param workItem                 Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  protected void fireWorkItemTerminated( IWorkItem workItem )
    throws WMWorkflowException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "fireWorkItemTerminated: " + workItem.getWorkItemId() );
    }
    m_serviceManager.getBpeEngine().getEventHub().fireManualActivityStateChanged(workItem,BPEEvent.Type.MANUAL_ACTIVITY_ABORTED);    

    Iterator  it  = m_listeners.iterator();

    while ( it.hasNext() ) {
      IWorklistListener  listener  = ( IWorklistListener ) it.next();

      listener.workItemTerminated( workItem );
    }
  }
}
