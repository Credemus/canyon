package de.objectcode.canyon.bpe.engine.activities;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.handler.Fault;

/**
 * @author    junglas
 * @created   14. Juni 2004
 */
public class Throw extends Activity
{
  final static  long    serialVersionUID  = -2594868750031116264L;
  protected     String  m_faultName;


  /**
   *Constructor for the Throw object
   *
   * @param name   Description of the Parameter
   * @param scope  Description of the Parameter
   */
  public Throw( String name, Scope scope )
  {
    super( name, scope );
  }


  /**
   * Gets the elementName attribute of the Throw object
   *
   * @return   The elementName value
   */
  public String getElementName()
  {
    return "throw";
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

    m_scope.throwFault( new Fault(m_faultName) );
  }


  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    super.toDom( element );

    element.addAttribute( "faultName", m_faultName );
  }
}
