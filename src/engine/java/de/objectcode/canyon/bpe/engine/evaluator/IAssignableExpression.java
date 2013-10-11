package de.objectcode.canyon.bpe.engine.evaluator;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.correlation.Message;

/**
 * @author junglas
 */
public interface IAssignableExpression extends IExpression
{
  public void assign ( Activity activity, Object value ) throws EngineException;
  
  public void assign ( Message message, Object value ) throws EngineException;
}
