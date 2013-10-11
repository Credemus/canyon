package de.objectcode.canyon.model.activity;

import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.model.transition.Transition;

/**
 * @author    junglas
 * @created   21. November 2003
 */
public interface IActivityContainer
{
  public WorkflowProcess getWorkflowProcess();
  
  /**
   * Gets the activity attribute of the IActivityContainer object
   *
   * @param id  Description of the Parameter
   * @return    The activity value
   */
  public Activity getActivity( String id );


  /**
   * Gets the transition attribute of the IActivityContainer object
   *
   * @param id  Description of the Parameter
   * @return    The transition value
   */
  public Transition getTransition( String id );
}
