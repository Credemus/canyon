package de.objectcode.canyon.model.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.objectcode.canyon.model.IValidatable;
import de.objectcode.canyon.model.ValidationErrors;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.model.transition.Transition;

/**
 * @author    junglas
 * @created   21. November 2003
 */
public class ActivitySet implements IActivityContainer, IValidatable, Serializable
{
	static final long serialVersionUID = 3237743369261409644L;
	
	private  String           m_id;
  private  Map              m_activities;
  private  Map              m_transitions;
  private  WorkflowProcess  m_workflowProcess;


  /**
   *Constructor for the ActivitySet object
   */
  public ActivitySet()
  {
    m_activities = new HashMap();
    m_transitions = new HashMap();
  }


  /**
   * @param string
   */
  public void setId( String string )
  {
    m_id = string;
  }


  /**
   * @param process
   */
  public void setWorkflowProcess( WorkflowProcess process )
  {
    m_workflowProcess = process;
  }



  /**
   * Gets the activity attribute of the WorkflowProcess object
   *
   * @param id  Description of the Parameter
   * @return    The activity value
   */
  public Activity getActivity( String id )
  {
    return ( Activity ) m_activities.get( id );
  }


  /**
   * Gets the activities attribute of the WorkflowProcess object
   *
   * @return   The activities value
   */
  public Activity[] getActivities()
  {
    Activity  ret[]  = new Activity[m_activities.size()];

    m_activities.values().toArray( ret );

    return ret;
  }


  /**
   * Gets the transition attribute of the ActivitySet object
   *
   * @param id  Description of the Parameter
   * @return    The transition value
   */
  public Transition getTransition( String id )
  {
    return ( Transition ) m_transitions.get( id );
  }


  /**
   * Gets the transitions attribute of the ActivitySet object
   *
   * @return   The transitions value
   */
  public Transition[] getTransitions()
  {
    Transition  ret[]  = new Transition[m_transitions.size()];

    m_transitions.values().toArray( ret );

    return ret;
  }


  /**
   * @return
   */
  public String getId()
  {
    return m_id;
  }


  /**
   * @return
   */
  public WorkflowProcess getWorkflowProcess()
  {
    return m_workflowProcess;
  }


  /**
   * Adds a feature to the Activity attribute of the WorkflowProcess object
   *
   * @param activity  The feature to be added to the Activity attribute
   */
  public void addActivity( Activity activity )
  {
    m_activities.put( activity.getId(), activity );
    activity.setContainer( this );
  }


  /**
   * Adds a feature to the Transition attribute of the ActivitySet object
   *
   * @param transition  The feature to be added to the Transition attribute
   */
  public void addTransition( Transition transition )
  {
    m_transitions.put( transition.getId(), transition );
    transition.setContainer( this );
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public ValidationErrors validate()
  {
    ValidationErrors  errors  = new ValidationErrors();

    errors.check( m_transitions.values() );
    errors.check( m_activities.values() );

    return errors;
  }

}
