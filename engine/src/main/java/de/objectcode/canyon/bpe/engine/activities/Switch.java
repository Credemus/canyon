package de.objectcode.canyon.bpe.engine.activities;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.evaluator.ICondition;
import de.objectcode.canyon.bpe.util.HydrationContext;

/**
 * @author    junglas
 * @created   9. Juni 2004
 */
public class Switch extends Activity implements IActivityContainer
{
  static final long serialVersionUID = 3977601137806435607L;
  
  private  List      m_cases;
  private  Activity  m_otherwise;


  /**
   *Constructor for the Switch object
   *
   * @param name   Description of the Parameter
   * @param scope  Description of the Parameter
   */
  public Switch( String name, Scope scope )
  {
    super( name, scope );

    m_cases = new ArrayList();
  }


  /**
   * @param otherwise  The otherwise to set.
   */
  public void setOtherwise( Activity otherwise )
  {
    otherwise.setParentActivity(this);
    m_otherwise = otherwise;
  }


  /**
   * @return   Returns the otherwise.
   */
  public Activity getOtherwise()
  {
    return m_otherwise;
  }


  /**
   * Gets the elementName attribute of the Switch object
   *
   * @return   The elementName value
   */
  public String getElementName()
  {
    return "switch";
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
   * @see                        de.objectcode.canyon.bpe.engine.activities.CompositeActivity#childAborted(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public void childAborted( Activity childActivity )
    throws EngineException
  {
    if ( m_state != ActivityState.RUNNING )
      return;
    
    complete();
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
    
    complete();
  }

  public void childSkiped( Activity childActivity )
  throws EngineException
  {
  }


  /**
   * Adds a feature to the Case attribute of the Switch object
   *
   * @param condition  The feature to be added to the Case attribute
   * @param activity   The feature to be added to the Case attribute
   */
  public void addCase( ICondition condition, Activity activity )
  {
    activity.setParentActivity(this);
    m_cases.add( new Case( condition, activity ) );
  }


  /**
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.activities.Activity#start()
   */
  public void start()
    throws EngineException
  {
    super.start();

    Iterator  it  = m_cases.iterator();

    while ( it.hasNext() ) {
      Case  c  = ( Case ) it.next();

      if ( c.getCondition().eval( this ) ) {
        c.getActivity().activate();
        
        it = m_cases.iterator();
        
        while ( it.hasNext() ) {
          Case c1 = (Case)it.next();
          
          if ( c1 != c ) {
            c1.getActivity().deactivate();
          }
        }
        m_otherwise.deactivate();
        
        return;
      }
    }

    if ( m_otherwise != null ) {
      m_otherwise.activate();

      it = m_cases.iterator();
      
      while ( it.hasNext() ) {
        Case c1 = (Case)it.next();
        
        c1.getActivity().deactivate();
      }
      
      return;
    }
    
    complete();
  }


  /**
   * @see de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom (Element element)
  {
    super.toDom(element);
    
    Iterator it = m_cases.iterator();
    
    while ( it.hasNext() ) {
      Case c = (Case)it.next();
      Element caseElement = element.addElement("case");
      
      c.toDom(caseElement);
    }
    
    if ( m_otherwise != null ) {
      Element otherwiseElement = element.addElement("otherwise");
      
      m_otherwise.toDom(otherwiseElement.addElement(m_otherwise.getElementName()));
    }
  }
  
  /**
   * @see de.objectcode.canyon.bpe.util.IStateHolder#hydrate(java.io.ObjectInput)
   */
  public void hydrate ( HydrationContext context, ObjectInput in) throws IOException
  {
    super.hydrate(context,in);

    Iterator it = m_cases.iterator();
    
    while ( it.hasNext() ) {
      Case c = (Case)it.next();
      
      c.getActivity().hydrate(context, in);
    }
    if (m_otherwise != null )
      m_otherwise.hydrate(context, in);
  }
  
  /**
   * @see de.objectcode.canyon.bpe.util.IStateHolder#dehydrate(java.io.ObjectOutput)
   */
  public void dehydrate (HydrationContext context, ObjectOutput out) throws IOException
  {
    super.dehydrate(context, out);
    
    Iterator it = m_cases.iterator();
    
    while ( it.hasNext() ) {
      Case c = (Case)it.next();
      
      c.getActivity().dehydrate(context, out);
    }
    if (m_otherwise != null )
      m_otherwise.dehydrate(context, out);
  }
  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   9. Juni 2004
   */
  private static class Case implements Serializable
  {
  	static final long serialVersionUID = 1186865794730872178L;
  	
  	private  ICondition  m_condition;
    private  Activity    m_activity;


    /**
     *Constructor for the Case object
     *
     * @param condition  Description of the Parameter
     * @param activity   Description of the Parameter
     */
    private Case( ICondition condition, Activity activity )
    {
      m_condition = condition;
      m_activity = activity;
    }


    /**
     * @return   Returns the activity.
     */
    public Activity getActivity()
    {
      return m_activity;
    }


    /**
     * @return   Returns the condition.
     */
    public ICondition getCondition()
    {
      return m_condition;
    }
    
    public void toDom ( Element element ) 
    {
      m_condition.toDom(element.addElement(m_condition.getElementName()));
      m_activity.toDom(element.addElement(m_activity.getElementName()));
    }
  }
}
