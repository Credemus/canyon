package de.objectcode.canyon.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author junglas
 * @created 20. November 2003
 */
public class BaseElement implements Serializable {
  static final long serialVersionUID = -3201931691642223185L;

  protected String m_id;
  protected String m_name;
  protected String m_description;
  protected Map<String, ExtendedAttribute> m_extendedAttributes;


  /**
   * Constructor for the BaseElement object
   */
  protected BaseElement() {
    m_extendedAttributes = new HashMap();
  }

  protected BaseElement(String id, String name) {
    this();

    m_id = id;
    m_name = name;
  }


  /**
   * @param string
   */
  public void setDescription(String string) {
    m_description = string;
  }


  /**
   * @param string
   */
  public void setId(String string) {
    m_id = string;
  }


  /**
   * @param string
   */
  public void setName(String string) {
    m_name = string;
  }


  /**
   * @return
   */
  public String getDescription() {
    return m_description;
  }


  /**
   * @return
   */
  public String getId() {
    return m_id;
  }


  /**
   * @return
   */
  public String getName() {
    return m_name;
  }


  /**
   * Gets the extendedAttribute attribute of the BaseElement object
   *
   * @param name Description of the Parameter
   * @return The extendedAttribute value
   */
  public ExtendedAttribute getExtendedAttribute(String name) {
    return m_extendedAttributes.get(name);
  }

  public String getExtendedAttributeValue(String name) {
    ExtendedAttribute attribute = m_extendedAttributes.get(name);

    return attribute != null ? attribute.getValue() : null;
  }

  /**
   * Gets the extendedAttributes attribute of the BaseElement object
   *
   * @return The extendedAttributes value
   */
  public ExtendedAttribute[] getExtendedAttributes() {
    ExtendedAttribute ret[] = new ExtendedAttribute[m_extendedAttributes.size()];

    m_extendedAttributes.values().toArray(ret);

    return ret;
  }


  /**
   * Adds a feature to the ExtendedAttribute attribute of the BaseElement object
   *
   * @param extendedAttribute The feature to be added to the ExtendedAttribute attribute
   */
  public void addExtendedAttribute(ExtendedAttribute extendedAttribute) {
    m_extendedAttributes.put(extendedAttribute.getName(), extendedAttribute);
  }

  public String toString() {
    return getClass().getName() + "[id='" + m_id + "' name='" + m_name + "' description='" + m_description + "']";
  }
}
