package de.objectcode.canyon.bpe.engine.variable;

import java.io.Serializable;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.util.IStateHolder;

/**
 * @author    junglas
 * @created   9. Juni 2004
 */
public interface IVariable extends Serializable, IStateHolder
{
  /**
   * Gets the name attribute of the IVariable object
   *
   * @return   The name value
   */
  public String getName();


  /**
   * Gets the value attribute of the IVariable object
   *
   * @return   The value value
   */
  public Object getValue();


  /**
   * Gets the valueClass attribute of the IVariable object
   *
   * @return   The valueClass value
   */
  public Class getValueClass();


  /**
   * Sets the value attribute of the IVariable object
   *
   * @param value  The new value value
   */
  public void setValue( Object value );


  /**
   * Gets the elementName attribute of the IVariable object
   *
   * @return   The elementName value
   */
  public String getElementName();


  /**
   * Description of the Method
   *
   * @param element  Description of the Parameter
   */
  public void toDom( Element element );
}
