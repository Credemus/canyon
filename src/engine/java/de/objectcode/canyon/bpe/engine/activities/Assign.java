package de.objectcode.canyon.bpe.engine.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.evaluator.IAssignableExpression;
import de.objectcode.canyon.bpe.engine.evaluator.IExpression;

/**
 * @author    junglas
 * @created   7. Juni 2004
 */
public class Assign extends Activity
{
  static final long serialVersionUID = 2020836348248036031L;
  
  protected  List  m_copies;


  /**
   *Constructor for the Assign object
   *
   * @param name   Description of the Parameter
   * @param scope  Description of the Parameter
   */
  public Assign( String name, Scope scope )
  {
    super( name, scope );

    m_copies = new ArrayList();
  }


  /**
   * Adds a feature to the Copy attribute of the Assign object
   *
   * @param fromExpression  The feature to be added to the Copy attribute
   * @param toExpression    The feature to be added to the Copy attribute
   */
  public void addCopy( IExpression fromExpression, IAssignableExpression toExpression )
  {
    m_copies.add( new Copy( fromExpression, toExpression ) );
  }


  /**
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.activities.Activity#start()
   */
  public void start()
    throws EngineException
  {
    super.start();

    Iterator  it  = m_copies.iterator();

    while ( it.hasNext() ) {
      Copy  copy  = ( Copy ) it.next();

      copy.getToExpression().assign(this, copy.getFromExpression().eval(this));
    }
    complete();
  }

  public String getElementName()
  {
    return "assign";
  }
  

    /**
   * @see de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom (Element element)
  {
    super.toDom(element);
    
    Iterator it = m_copies.iterator();
    
    while ( it.hasNext() ) {
      Copy copy = (Copy)it.next();
      Element copyElement = element.addElement("copy");
      
      copy.toDom(copyElement);
    }
  }
  
  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   7. Juni 2004
   */
  private static class Copy implements Serializable
  {
  	static final long serialVersionUID = 7682222909505893152L;
  	
  	IExpression  m_fromExpression;
    IAssignableExpression  m_toExpression;


    /**
     *Constructor for the Copy object
     *
     * @param fromExpression  Description of the Parameter
     * @param toExpression    Description of the Parameter
     */
    Copy( IExpression fromExpression, IAssignableExpression toExpression )
    {
      m_fromExpression = fromExpression;
      m_toExpression = toExpression;
    }


    /**
     * @return   Returns the fromExpression.
     */
    public IExpression getFromExpression()
    {
      return m_fromExpression;
    }


    /**
     * @return   Returns the toExpression.
     */
    public IAssignableExpression getToExpression()
    {
      return m_toExpression;
    }
    
    public void toDom ( Element element )
    {
      Element fromElement = element.addElement("from");
      Element toElement = element.addElement("to");
      
      m_fromExpression.toDom(fromElement);
      m_toExpression.toDom(toElement);
    }
  }
}
