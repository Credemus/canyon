package de.objectcode.canyon.bpe.engine.evaluator;

import java.io.Serializable;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.activities.Link;
import de.objectcode.canyon.bpe.engine.activities.LinkState;

/**
 * @author junglas
 */
public class AndJoinCondition implements Serializable, IJoinCondition
{
  static final long serialVersionUID = 2308021319982780082L;
  
  /**
   * @see de.objectcode.canyon.bpe.engine.evaluator.ICondition#eval(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public boolean eval (Activity activity)
  {
    Link[] links = activity.getIncomingLinks();
    int i;
    
    for ( i = 0; i < links.length; i++ ) {
      if ( links[i].getState() != LinkState.TRUE )
        return false;
    }
    
    return true;
  }
  
  public String getElementName() 
  {
    return "and-join-condition";
  }
  
  public void toDom(Element element)
  {
  }
}
