package de.objectcode.canyon.bpe.engine.evaluator;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.util.IDomSerializable;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   22. Juni 2004
 */
public interface ICondition extends IDomSerializable, Serializable
{
  /**
   * Description of the Method
   *
   * @param activity             Description of the Parameter
   * @return                     Description of the Return Value
   * @exception EngineException  Description of the Exception
   */
  public boolean eval( Activity activity )
    throws EngineException;
}
