package de.objectcode.canyon.bpe.engine.evaluator;

import java.io.Serializable;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.correlation.Message;

/**
 * @author junglas
 */
public class JXPathExpression implements Serializable, IExpression
{
  static final long  serialVersionUID = 7729645137783793505L;  
  private String m_expression;
  
  public JXPathExpression ( String expression )
  {
    m_expression = expression;
  }
  
  
  /**
   * @see de.objectcode.canyon.bpe.engine.evaluator.IExpression#eval(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public Object eval (Activity activity) throws EngineException
  {
    // TODO Auto-generated method stub
    return null;
  }
  /**
   * @see de.objectcode.canyon.bpe.engine.evaluator.IExpression#eval(de.objectcode.canyon.bpe.engine.correlation.Message)
   */
  public Object eval (Message message) throws EngineException
  {
    // TODO Auto-generated method stub
    return null;
  }
  /**
   * @see de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName ( )
  {
    return "jxpath-expression";
  }
  /**
   * @see de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom (Element element)
  {
    element.addAttribute("expression", m_expression);
  }
}
