package de.objectcode.canyon.bpe.engine.activities;

import de.objectcode.canyon.bpe.engine.EngineException;

/**
 * @author    junglas
 * @created   14. Juni 2004
 */
public class Terminate extends Activity
{
  final static  long  serialVersionUID  = -4808420167364232887L;


  /**
   *Constructor for the Terminate object
   *
   * @param name   Description of the Parameter
   * @param scope  Description of the Parameter
   */
  public Terminate( String name, Scope scope )
  {
    super( name, scope );
  }


  /**
   * Gets the elementName attribute of the Terminate object
   *
   * @return   The elementName value
   */
  public String getElementName()
  {
    return "terminate";
  }


  /**
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.activities.Activity#start()
   */
  public void start()
    throws EngineException
  {
    super.start();

    complete();

    m_scope.getProcess().terminateProcess();
  }
}
