package de.objectcode.canyon.bpe.engine.activities;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.variable.IVariable;

/**
 * @author junglas
 */
public interface IActivityContainer
{
  /**
   * Check if the activity container is non-blocked.
   * If the container is non-blocked links may contain loops.
   * 
   */
  boolean isNonBlocked();
  
  /**
   * Called by a child activity once completed.
   *
   * @param childActivity  Description of the Parameter
   */
  void childCompleted(Activity childActivity) throws EngineException;


  /**
   * Called by a child activity once aborted.
   *
   * @param childActivity  Description of the Parameter
   */
  void childAborted(Activity childActivity) throws EngineException;

  /**
   * Called by a child activity once skiped.
   *
   * @param childActivity  Description of the Parameter
   */
  void childSkiped(Activity childActivity) throws EngineException;
  
  IVariable[] getVariables();
  
  IVariable getVariable(String name);
  
  IActivityContainer getParentActivity();
  
  Scope getScope();
}
