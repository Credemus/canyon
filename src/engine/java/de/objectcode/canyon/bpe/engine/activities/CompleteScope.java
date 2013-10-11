package de.objectcode.canyon.bpe.engine.activities;

import de.objectcode.canyon.bpe.engine.EngineException;

/**
 * @author junglas
 */
public class CompleteScope  extends Activity 
{
  static final long serialVersionUID = -2888942344740528959L;
  
  public CompleteScope ( String name, Scope scope )
  {
    super ( name, scope );
  }
  
  /**
   * @see de.objectcode.canyon.bpe.engine.activities.Activity#start()
   */
  public void start ( ) throws EngineException
  {
    super.start();
        
    complete();
    
    m_scope.deactivate();
  }

  
  public String getElementName()
  {
    return "complete-scope";
  }
}
