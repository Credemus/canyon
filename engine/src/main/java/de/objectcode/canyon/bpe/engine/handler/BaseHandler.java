package de.objectcode.canyon.bpe.engine.handler;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.activities.IActivityContainer;
import de.objectcode.canyon.bpe.engine.activities.Scope;
import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.bpe.util.IDomSerializable;
import de.objectcode.canyon.bpe.util.IStateHolder;

/**
 * @author    junglas
 * @created   23. Juni 2004
 */
public abstract class BaseHandler implements IActivityContainer, Serializable, IDomSerializable, IStateHolder
{
  static final long serialVersionUID = -1957406965801076399L;
  
  protected  Scope     m_scope;
  protected  Activity  m_activity;


  /**
   * @param scope  The scope to set.
   */
  public void setScope( Scope scope )
  {
    m_scope = scope;
    if ( m_activity != null )
      m_activity.setParentActivity( this );
  }


  /**
   * Sets the activity attribute of the BaseHandler object
   *
   * @param activity  The new activity value
   */
  public void setActivity( Activity activity )
  {
    activity.setParentActivity( this );
    m_activity = activity;
  }


  /**
   * @return   Returns the scope.
   */
  public Scope getScope()
  {
    return m_scope;
  }


  /**
   * @return   Returns the activity.
   */
  public Activity getActivity()
  {
    return m_activity;
  }


  /**
   * @param childActivity        Description of the Parameter
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.activities.IActivityContainer#childAborted(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public void childAborted( Activity childActivity )
    throws EngineException
  {
    m_scope.complete();
  }


  /**
   * @param childActivity        Description of the Parameter
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.activities.IActivityContainer#childCompleted(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public void childCompleted( Activity childActivity )
    throws EngineException
  {
    m_scope.abort();
  }


  /**
   * @param childActivity        Description of the Parameter
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.activities.IActivityContainer#childSkiped(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public void childSkiped( Activity childActivity )
    throws EngineException
  {
//   Does not work with Deadline. Look at FaultHandler
//  	m_scope.complete();
  }


  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    m_activity.toDom( element.addElement( m_activity.getElementName() ) );
  }


  /**
   * @param in               Description of the Parameter
   * @exception IOException  Description of the Exception
   * @see                    de.objectcode.canyon.bpe.util.IStateHolder#hydrate(java.io.ObjectInput)
   */
  public void hydrate( HydrationContext context, ObjectInput in )
    throws IOException
  {
    m_activity.hydrate( context, in );
  }


  /**
   * @param out              Description of the Parameter
   * @exception IOException  Description of the Exception
   * @see                    de.objectcode.canyon.bpe.util.IStateHolder#dehydrate(java.io.ObjectOutput)
   */
  public void dehydrate( HydrationContext context, ObjectOutput out )
    throws IOException
  {
    m_activity.dehydrate( context, out );
  }
  
  public IVariable[] getVariables() {
      if (m_scope != null)
          return m_scope.getVariables();
      else
          return new IVariable[0];
  }
  
  public IVariable getVariable( String name ) {
      if (m_scope != null) {
          return m_scope.getVariable(name);
      } else
          return null;
  }
  
  public IActivityContainer getParentActivity() {
    return m_scope;
  }
}
