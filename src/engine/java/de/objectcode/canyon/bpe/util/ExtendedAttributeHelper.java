/* 
 * $Id: Developer-StyleGudie.txt,v 1.6 2004/08/09 06:56:14 joerg Exp $
 * 
 * Copyright (C) 2004 ObjectCode GmbH
 * All rights reserved. 
 */

package de.objectcode.canyon.bpe.util;

import java.util.HashMap;
import java.util.Map;

import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.activity.Activity;

/**
 *  
 * Helper class to access extended attributes
 * @author xylander
 * @date 19.04.2005
 * @version $Revision: 1.6 $
 */
public class ExtendedAttributeHelper {
	
	
	// Package
	
	// Process
	
	// Activity 
	public static String DUE_DATE_IS_RELATIVE_TO_ACTIVITY_START = "canyon:DueDateIsRelativeToActivityStart";

	// Tool
	
  private Map m_extendedAttributes = new HashMap();

  public ExtendedAttributeHelper() {
  }
  
  public void addAttributes(ExtendedAttribute[] extendedAttributes) {
    if (extendedAttributes!=null) {
	    for (int i = 0; i < extendedAttributes.length; i++) {
	      m_extendedAttributes.put(extendedAttributes[i].getName(),
	          extendedAttributes[i]);
	    }
    }
  }
  	
  public ExtendedAttributeHelper(Activity activity) {
  	// TODO use package here?
  	addAttributes(activity.getWorkflowProcess().getExtendedAttributes());
  	addAttributes(activity.getExtendedAttributes());
  }

  	/**
   *  
   */
  public ExtendedAttributeHelper(ExtendedAttribute[] extendedAttributes) {
    addAttributes(extendedAttributes);
  }
  
  public ExtendedAttribute getAttribute(String attributeName) {
    return (ExtendedAttribute) m_extendedAttributes.get(attributeName);
  }

  
  public boolean hasAttribute(String attributeName ) {
  	return m_extendedAttributes.get(attributeName) != null;
  }
  
  public boolean hasAttributeValue(String attributeName ) {
  	ExtendedAttribute extendedAttribute = (ExtendedAttribute) m_extendedAttributes.get(attributeName);
  	return extendedAttribute != null && extendedAttribute.getValue() != null;
  }
  
  public String getValue(String attributeName) {
	  return getValue(attributeName, null, false);
  }
  
  private String getValue(String attributeName, String defaultValue, boolean isMandatory) {
    ExtendedAttribute attribute =getAttribute(attributeName);
    if (attribute == null) {
      if (isMandatory)
        throw new RuntimeException("Mandatory extended attribute '" + attributeName + "' missing");
      else
        return null;
    } else {
      String value = attribute.getValue();
      if (value == null) {
        if (isMandatory)
          throw new RuntimeException("Mandatory extended attribute '" + attributeName + "' missing");
        else
          return defaultValue;
      } else
        return value;
    }
  }

  public String getMandatoryValue(String attributeName) {
    return getValue(attributeName, null, true);
  }
  
  public String getOptionalValue(String attributeName, String defaultValue) {
    return getValue(attributeName, defaultValue, false);
  }
  
  public boolean getOptionalBooleanValue(String attributeName, boolean defaultValue) {
  	if (hasAttributeValue(attributeName))
  		return "TRUE".equalsIgnoreCase(getOptionalValue(attributeName));
  	else
  		return defaultValue;
  }

  public String getOptionalValue(String attributeName) {
    return getValue(attributeName, null, false);
  }
  
}

/*
 * $Log: Developer-StyleGudie.txt,v $ $Revision 1.6 2004/08/09 06:56:14 joerg
 * $no message $
 */