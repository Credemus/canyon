package de.objectcode.canyon.bpe.engine.evaluator;

import java.io.Serializable;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.correlation.Message;

/**
 * @author    junglas
 * @created   14. Juni 2004
 */
public class ConstantExpression implements Serializable, IExpression
{
  static final long serialVersionUID = 7571582255028699440L;
  
  public   Object     m_value;


  /**
   *Constructor for the ConstantExpression object
   *
   * @param type   Description of the Parameter
   * @param value  Description of the Parameter
   */
  public ConstantExpression( Object value )
  {
    m_value = value;
  }

  /**
   * @return   The elementName value
   * @see      de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName()
  {
    return "constant-expression";
  }


  /**
   * @param activity             Description of the Parameter
   * @return                     Description of the Return Value
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.evaluator.IExpression#eval(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public Object eval( Activity activity )
    throws EngineException
  {
    return m_value;
  }


  /**
   * @param message  Description of the Parameter
   * @return         Description of the Return Value
   * @see            de.objectcode.canyon.bpe.engine.evaluator.IExpression#eval(de.objectcode.canyon.bpe.engine.correlation.IMessage)
   */
  public Object eval( Message message )
  {
    return m_value;
  }


  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    if ( m_value != null )
      element.addAttribute( "value", m_value.toString() );
  }
}
