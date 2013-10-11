package de.objectcode.canyon.bpe.engine.evaluator;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

/**
 * @author    junglas
 * @created   19. Juli 2004
 */
public class OtherwiseCondition implements Serializable,ICondition
{
  static final long serialVersionUID = 2325708379858453764L;
  
  private  List  m_conditions;


  /**
   *Constructor for the OtherwiseCondition object
   */
  public OtherwiseCondition()
  {
    m_conditions = new ArrayList();
  }


  /**
   * @return   The elementName value
   * @see      de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName()
  {
    return "otherwise-condition";
  }


  /**
   * Adds a feature to the Condition attribute of the OtherwiseCondition object
   *
   * @param condition  The feature to be added to the Condition attribute
   */
  public void addCondition( ICondition condition )
  {
    m_conditions.add( condition );
  }


  /**
   * @param activity             Description of the Parameter
   * @return                     Description of the Return Value
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.evaluator.ICondition#eval(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public boolean eval( Activity activity )
    throws EngineException
  {
    Iterator  it  = m_conditions.iterator();

    while ( it.hasNext() ) {
      ICondition  condition  = ( ICondition ) it.next();

      if ( condition.eval( activity ) ) {
        return false;
      }
    }
    return true;
  }


  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    Iterator  it  = m_conditions.iterator();

    while ( it.hasNext() ) {
      ICondition  condition  = ( ICondition ) it.next();

      condition.toDom( element.addElement( condition.getElementName() ) );
    }
  }
}
