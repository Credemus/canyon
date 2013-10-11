package de.objectcode.canyon.drydock.worklist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.wfmc.wapi.WMWorkItemState;

/**
 * @author    junglas
 * @created   15. Juni 2004
 */
public class Worklist
{
  private  List  m_workItems;
  private  List  m_listeners;


  /**
   *Constructor for the Worklist object
   */
  public Worklist()
  {
    m_workItems = new ArrayList();
    m_listeners = new ArrayList();
  }


  /**
   * Gets the workItem attribute of the Worklist object
   *
   * @param index  Description of the Parameter
   * @return       The workItem value
   */
  public WorkItem getWorkItem( int index )
  {
    return ( WorkItem ) m_workItems.get( index );
  }
  
  public void completeWorkItem ( int index )
  {
    WorkItem workItem = ( WorkItem ) m_workItems.get( index );
    
    workItem.setStatus(WMWorkItemState.CLOSED_COMPLETED_INT);
    
    fireWorkItemCompleted ( workItem );
  }


  /**
   * Gets the workItemCount attribute of the Worklist object
   *
   * @return   The workItemCount value
   */
  public int getWorkItemCount()
  {
    return m_workItems.size();
  }


  /**
   * Description of the Method
   *
   * @param processInstanceId   Description of the Parameter
   * @param activityInstanceId  Description of the Parameter
   * @return                    Description of the Return Value
   */
  public WorkItem findWorkItem( String processInstanceId, String activityInstanceId )
  {
    Iterator  it  = m_workItems.iterator();

    while ( it.hasNext() ) {
      WorkItem  workItem  = ( WorkItem ) it.next();

      if ( workItem.getActivityId().equals( activityInstanceId ) && workItem.getProcessId().equals( processInstanceId ) ) {
        return workItem;
      }
    }

    return null;
  }


  /**
   * Adds a feature to the WorkItem attribute of the Worklist object
   *
   * @param workItem  The feature to be added to the WorkItem attribute
   */
  public void addWorkItem( WorkItem workItem )
  {
    m_workItems.add( workItem );
    fireWorklistChanged();
  }


  /**
   * Adds a feature to the WorklistListener attribute of the WorkItem object
   *
   * @param listener  The feature to be added to the WorklistListener attribute
   */
  public void addWorklistChangeListener( IWorklistChangeListener listener )
  {
    m_listeners.add( listener );
  }


  /**
   * Description of the Method
   *
   * @param listener  Description of the Parameter
   */
  public void removeWorklistChangeListener( IWorklistChangeListener listener )
  {
    m_listeners.remove( listener );
  }


  /**
   * Description of the Method
   */
  void fireWorklistChanged()
  {
    Iterator  it  = m_listeners.iterator();

    while ( it.hasNext() ) {
      IWorklistChangeListener  listener  = ( IWorklistChangeListener ) it.next();

      listener.worklistChanged();
    }
  }
  
  void fireWorkItemCompleted ( WorkItem workItem )
  {
    Iterator  it  = m_listeners.iterator();

    while ( it.hasNext() ) {
      IWorklistChangeListener  listener  = ( IWorklistChangeListener ) it.next();

      listener.workItemCompleted(workItem);
    }    
  }
}
