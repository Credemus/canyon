package de.objectcode.canyon.bpe.engine;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import de.objectcode.canyon.bpe.util.IDomSerializable;

/**
 * @author    junglas
 * @created   9. Juni 2004
 */
public abstract class ExtensibleElement implements Serializable, IDomSerializable
{
  static final long serialVersionUID = 590250300228880772L;  
  
  protected  Map  m_extendedAttributes;


  /**
   *Constructor for the ExtensibleElement object
   */
  public ExtensibleElement()
  {
    m_extendedAttributes = new LinkedHashMap();
  }


  /**
   * Adds a feature to the ExtendedAttribute attribute of the ExtensibleElement object
   *
   * @param attribute  The feature to be added to the ExtendedAttribute attribute
   */
  public void addExtendedAttribute( ExtendedAttribute attribute )
  {
    m_extendedAttributes.put( attribute.getName(), attribute );
  }
}
