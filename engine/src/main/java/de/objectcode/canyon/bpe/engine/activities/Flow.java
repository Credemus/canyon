package de.objectcode.canyon.bpe.engine.activities;

import de.objectcode.canyon.bpe.engine.EngineException;

import java.util.Iterator;

import org.dom4j.Element;

/**
 * @author    junglas
 * @created   7. Juni 2004
 */
public class Flow extends CompositeActivity
{
  final static  long     serialVersionUID  = -3666023322491490358L;

  private       boolean  m_nonBlocked;


  /**
   *Constructor for the Flow object
   *
   * @param name   Description of the Parameter
   * @param scope  Description of the Parameter
   */
  public Flow( String name, Scope scope )
  {
    super( name, scope );
  }

  public Flow ( String id, String name, Scope scope )
  {
    super (id, name, scope);
  }

  /**
   * @param nonBlocked  The nonBlocked to set.
   */
  public void setNonBlocked( boolean nonBlocked )
  {
    m_nonBlocked = nonBlocked;
  }


  /**
   * @return   Returns the nonBlocked.
   */
  public boolean isNonBlocked()
  {
    return m_nonBlocked;
  }


  /**
   * Gets the elementName attribute of the Flow object
   *
   * @return   The elementName value
   */
  public String getElementName()
  {
    return "flow";
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
      Iterator  it  = m_childActivities.iterator();

      while ( it.hasNext() ) {
        Activity  activity  = ( Activity ) it.next();

        if ( activity.getState() == ActivityState.OPEN && !activity.hasIncomingLinks() ) {
          activity.activate();
        }
      }
    }
  }


  /**
   * Description of the Method
   *
   * @param element  Description of the Parameter
   */
  public void toDom( Element element )
  {
    super.toDom( element );

    element.addAttribute("non-blocked", String.valueOf(m_nonBlocked));
    
    Iterator  it  = m_childActivities.iterator();

    while ( it.hasNext() ) {
      Activity  activity  = ( Activity ) it.next();

      activity.toDom( element.addElement( activity.getElementName() ) );
    }
  }
}
