package de.objectcode.canyon.bpe.engine.event;

import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.activities.ActivityState;

import java.util.EventObject;

/**
 * @author    junglas
 * @created   21. Juni 2004
 */
public class ActivityStateEvent extends EventObject
{
	static final long serialVersionUID = -3054068798261955305L;
	
	private  ActivityState  m_newState;


  /**
   *Constructor for the ActivityStateEvent object
   *
   * @param activity  Description of the Parameter
   * @param newState  Description of the Parameter
   */
  public ActivityStateEvent( Activity activity, ActivityState newState )
  {
    super( activity );

    m_newState = newState;
  }


  /**
   * @return   Returns the newState.
   */
  public ActivityState getNewState()
  {
    return m_newState;
  }


  /**
   * Gets the activty attribute of the ActivityStateEvent object
   *
   * @return   The activty value
   */
  public Activity getActivty()
  {
    return ( Activity ) source;
  }
}
