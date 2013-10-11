package de.objectcode.canyon.bpe.engine.activities;

import de.objectcode.canyon.bpe.engine.EngineException;

/**
 * @author junglas
 */
public class Empty extends Activity 
{
  static final long serialVersionUID = -2888942344740528958L;
  
  public Empty ( String name, Scope scope )
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
  }

  public String getElementName()
  {
    return "empty";
  }
}
