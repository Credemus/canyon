package de.objectcode.canyon.bpe.engine.evaluator;

import java.io.Serializable;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.util.IDomSerializable;

/**
 * @author junglas
 */
public interface IExpression extends IDomSerializable, Serializable
{
  public Object eval ( Activity activity ) throws EngineException;
  
  public Object eval ( Message message ) throws EngineException;
}
