package de.objectcode.canyon.bpe.engine.activities;


/**
 * @author    junglas
 * @created   17. Juni 2004
 */
public class Reply extends Activity
{
  final static  long  serialVersionUID  = 8958494929111897334L;


  /**
   *Constructor for the Reply object
   *
   * @param name   Description of the Parameter
   * @param scope  Description of the Parameter
   */
  public Reply( String name, Scope scope )
  {
    super( name, scope );
  }


  /**
   * Gets the elementName attribute of the Reply object
   *
   * @return   The elementName value
   */
  public String getElementName()
  {
    return "reply";
  }
}
