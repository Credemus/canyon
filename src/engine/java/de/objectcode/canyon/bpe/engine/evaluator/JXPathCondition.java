package de.objectcode.canyon.bpe.engine.evaluator;

import java.io.Serializable;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;

/**
 * @author junglas
 */
public class JXPathCondition implements Serializable,ICondition
{
  static final long  serialVersionUID = 6554127138593692750L;
  
  private String m_expression;

  public JXPathCondition ( String expression )
  {
    m_expression = expression;
  }
  
  /**
   * @see de.objectcode.canyon.bpe.engine.evaluator.ICondition#eval(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public boolean eval (Activity activity) throws EngineException
  {
    // TODO Auto-generated method stub
    return false;
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
