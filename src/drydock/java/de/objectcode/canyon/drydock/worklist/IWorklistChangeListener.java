package de.objectcode.canyon.drydock.worklist;

/**
 * @author junglas
 */
public interface IWorklistChangeListener
{
  public void worklistChanged();
  
  public void workItemCompleted ( WorkItem workItem );
}
