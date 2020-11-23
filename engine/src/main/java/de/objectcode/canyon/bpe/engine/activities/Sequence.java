package de.objectcode.canyon.bpe.engine.activities;

import java.util.Iterator;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;

/**
 * @author    junglas
 * @created   7. Juni 2004
 */
public class Sequence extends CompositeActivity
{
  static final long serialVersionUID = -665262878162425645L;
  
  /**
   *Constructor for the Sequence object
   *
   * @param name   Description of the Parameter
   * @param scope  Description of the Parameter
   */
  public Sequence( String name, Scope scope )
  {
    super( name, scope );
  }

  /**
   * @see de.objectcode.canyon.bpe.engine.activities.IActivityContainer#isNonBlocked()
   */
  public boolean isNonBlocked ( )
  {
    return false;
  }

  /**
   * @param childActivity        Description of the Parameter
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.activities.CompositeActivity#childCompleted(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public void childCompleted( Activity childActivity )
    throws EngineException
  {
    if ( m_state != ActivityState.RUNNING )
      return;
    
    int  idx  = m_childActivities.indexOf( childActivity );

    if ( idx >= 0 && idx < m_childActivities.size() - 1 ) {
      ( ( Activity ) m_childActivities.get( idx + 1 ) ).activate();
    } else {
      super.childCompleted( childActivity );
    }
  }


  /**
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.activities.Activity#start()
   */
  public void start()
    throws EngineException
  {
    super.start();

    if ( m_childActivities.isEmpty() ) {
      complete();
    } else {
      ( ( Activity ) m_childActivities.get( 0 ) ).activate();
    }
  }

  public String getElementName()
  {
    return "sequence";
  }

  public void toDom ( Element element )
  {
    super.toDom(element);
    
    Iterator it = m_childActivities.iterator();
    
    while ( it.hasNext() ) {
      Activity activity = (Activity)it.next();
      
      activity.toDom(element.addElement(activity.getElementName()));
    }
  }
}
