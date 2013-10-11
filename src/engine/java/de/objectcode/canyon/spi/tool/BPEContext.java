/*
 * Created on 09.08.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.objectcode.canyon.spi.tool;

import java.util.HashMap;
import java.util.Map;

import de.objectcode.canyon.model.ExtendedAttribute;

/**
 * @author xylander
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class BPEContext {

  protected String m_activityId;
  
  protected String m_clientId;

  protected String m_parentProcessInstanceIdPath;

  protected String m_processInstanceId;
  
  protected String m_processId;

  protected ExtendedAttribute[] m_extendedAttributes;

  protected Map m_extendedAttributeMap = new HashMap();

  public BPEContext(String clientId, ExtendedAttribute[] extendedAttributes,
      String processInstanceId, String parentProcessInstanceIdPath, String processId, String activityId) {
    m_clientId = clientId;
    m_extendedAttributes = extendedAttributes;
    for (int i = 0; i < extendedAttributes.length; i++) {
      ExtendedAttribute attribute = extendedAttributes[i];
      m_extendedAttributeMap.put(attribute.getName(), attribute);

    }
    m_parentProcessInstanceIdPath = parentProcessInstanceIdPath;
    m_activityId = activityId;
  }

  public ExtendedAttribute getExtendedAttribute(String attributeName) {
    return (ExtendedAttribute) m_extendedAttributeMap.get(attributeName);
  }
  
  /**
   * @return Returns the clientId.
   */
  public String getClientId() {
    return m_clientId;
  }

  public String toString() {
    StringBuffer buffy = new StringBuffer("[");
    buffy.append("clientId='").append(m_clientId).append("'");
    buffy.append(", processId='").append(m_processId).append("'");
    buffy.append(", activityId='").append(m_activityId).append("'");
    buffy.append(", processInstanceId=").append(m_processInstanceId);
    buffy.append(", parentProcessInstanceIdPath=").append(
        m_parentProcessInstanceIdPath);
    buffy.append("]");
    return buffy.toString();
  }

  public ExtendedAttribute[] getExtendedAttributes() {
    return m_extendedAttributes;
  }

  public String getParentProcessInstanceIdPath() {
    return m_parentProcessInstanceIdPath;
  }
  
}