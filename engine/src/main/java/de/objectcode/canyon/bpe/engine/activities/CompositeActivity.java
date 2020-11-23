package de.objectcode.canyon.bpe.engine.activities;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.util.HydrationContext;

/**
 * Abstract base class of all activities that contain other activities.
 *
 * @author    junglas
 * @created   7. Juni 2004
 */
public abstract class CompositeActivity extends Activity implements IActivityContainer
{
  static final long serialVersionUID = -3911303387877356979L;
  
  protected  List  m_childActivities;


  /**
   *Constructor for the CompositeActivity object
   *
   * @param name   Description of the Parameter
   * @param scope  Description of the Parameter
   */
  protected CompositeActivity( String name, Scope scope )
  {
    super( name, scope );

    m_childActivities = new ArrayList();
  }


  /**
   *Constructor for the CompositeActivity object
   *
   * @param id     Description of the Parameter
   * @param name   Description of the Parameter
   * @param scope  Description of the Parameter
   */
  protected CompositeActivity( String id, String name, Scope scope )
  {
    super( id, name, scope );

    m_childActivities = new ArrayList();
  }


  /**
   * Adds a feature to the Activity attribute of the CompositeActivity object
   *
   * @param activity  The feature to be added to the Activity attribute
   */
  public void addActivity( Activity activity )
  {
    activity.setParentActivity( this );
    m_childActivities.add( activity );
  }

  public void setScope(Scope scope) {
  	super.setScope(scope);
    Iterator  it  = m_childActivities.iterator();

    while ( it.hasNext() ) {
      Activity  activity  = ( Activity ) it.next();
      activity.setScope(scope);
    }
  }
  

  /**
   * Called by a child activity once completed.
   *
   * @param childActivity        Description of the Parameter
   * @exception EngineException  Description of the Exception
   */
  public void childCompleted( Activity childActivity )
    throws EngineException
  {
    if ( m_state != ActivityState.RUNNING )
      return;
    
    Iterator  it  = m_childActivities.iterator();

    while ( it.hasNext() ) {
      Activity  activity  = ( Activity ) it.next();

      if ( activity.getState() != ActivityState.COMPLETED &&
          activity.getState() != ActivityState.ABORT &&
          activity.getState() != ActivityState.SKIPED ) {
        return;
      }
    }

    complete();
  }


  /**
   * Called by a child activity once aborted.
   *
   * @param childActivity        Description of the Parameter
   * @exception EngineException  Description of the Exception
   */
  public void childAborted( Activity childActivity )
    throws EngineException
  {
    if ( m_state != ActivityState.RUNNING )
      return;
    
    Iterator  it  = m_childActivities.iterator();

    while ( it.hasNext() ) {
      Activity  activity  = ( Activity ) it.next();

      if ( activity.getState() != ActivityState.COMPLETED &&
          activity.getState() != ActivityState.ABORT &&
          activity.getState() != ActivityState.SKIPED ) {
        return;
      }
    }

    complete();
  }


  /**
   * Called by a child activity once aborted.
   *
   * @param childActivity        Description of the Parameter
   * @exception EngineException  Description of the Exception
   */
  public void childSkiped( Activity childActivity )
    throws EngineException
  {
    if ( m_state != ActivityState.RUNNING && m_state != ActivityState.OPEN )
      return;
    
    Iterator  it  = m_childActivities.iterator();

    boolean allSkipped = true;
    while ( it.hasNext() ) {
      Activity  activity  = ( Activity ) it.next();

      if ( activity.getState() != ActivityState.COMPLETED &&
          activity.getState() != ActivityState.ABORT &&
          activity.getState() != ActivityState.SKIPED ) {
        return;
      }
      
      if (activity.getState() == ActivityState.COMPLETED || activity.getState() == ActivityState.ABORT)
        allSkipped = false;
    }
    
    if (allSkipped) {
      // TODO yx REFACTOR This is a dirty hack to cope with SubprocessLoopTest
      if (m_state == ActivityState.OPEN)
        super.deactivate();
      skip();
    }
    else
      complete();

  }

  public void complete ( ) throws EngineException
  {
  	// TODO yx REFACTOR
    // Composite activities might be completed if still open
    // This should be refactored
    if ( m_state == ActivityState.OPEN )
      m_state = ActivityState.RUNNING;
    
    super.complete();
  }
  

  /**
   * @see de.objectcode.canyon.bpe.engine.activities.Activity#deactivate()
   */
  public void skip ( ) throws EngineException
  {
    super.skip();
    
    Iterator it = m_childActivities.iterator();
    
    while ( it.hasNext()  ) {
      Activity activity = (Activity)it.next();
      
      if ( activity.getState() == ActivityState.OPEN )
        activity.deactivate();
    }
  }
  
  public void abort ( ) throws EngineException
  {
    super.abort();
    
    Iterator it = m_childActivities.iterator();
    
    while ( it.hasNext()  ) {
      Activity activity = (Activity)it.next();
      
      // TODO yx REFACTOR
      if ( activity.getState() == ActivityState.RUNNING )
        activity.abort();
      else if ( activity.getState() == ActivityState.OPEN )
        activity.deactivate();
    }
  }

  public void reopen ( ) throws EngineException
  {
    super.reopen();

    Iterator it = m_childActivities.iterator();
    
    while ( it.hasNext()  ) {
      Activity activity = (Activity)it.next();
      
      if ( activity.getState() == ActivityState.COMPLETED || 
          activity.getState() == ActivityState.ABORT || 
          activity.getState() == ActivityState.SKIPED )
        activity.reopen();
    }
  }
  
  public void deactivate ( ) throws EngineException
  {
  	// TODO yx CHECK
  	// super.deactivate();
  	
    Iterator it = m_childActivities.iterator();
    
    while ( it.hasNext()  ) {
      Activity activity = (Activity)it.next();
      
      if ( activity.getState() == ActivityState.OPEN )
        activity.deactivate();
      else if ( activity.getState() == ActivityState.RUNNING )
        activity.abort();
    }
  }
  
  /**
   * @param in               Description of the Parameter
   * @exception IOException  Description of the Exception
   * @see                    de.objectcode.canyon.bpe.util.IStateHolder#hydrate(java.io.ObjectInput)
   */
  public void hydrate( HydrationContext context, ObjectInput in )
    throws IOException
  {
    super.hydrate( context, in );

    Iterator  it  = m_childActivities.iterator();

    while ( it.hasNext() ) {
      Activity  activity  = ( Activity ) it.next();

      activity.hydrate( context, in );
    }
  }


  /**
   * @param out              Description of the Parameter
   * @exception IOException  Description of the Exception
   * @see                    de.objectcode.canyon.bpe.util.IStateHolder#dehydrate(java.io.ObjectOutput)
   */
  public void dehydrate( HydrationContext context, ObjectOutput out )
    throws IOException
  {
    super.dehydrate( context, out );

    Iterator  it  = m_childActivities.iterator();

    while ( it.hasNext() ) {
      Activity  activity  = ( Activity ) it.next();

      activity.dehydrate( context, out );
    }
  }
}
