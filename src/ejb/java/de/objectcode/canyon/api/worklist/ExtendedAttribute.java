/*
 * Created on 08.04.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.objectcode.canyon.api.worklist;

import java.io.Serializable;

/**
 * @author ruth
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExtendedAttribute implements Serializable, IExtendedAttribute {
	
	static final long serialVersionUID = -1977945578124307106L;
	
	private String m_name;
	private String m_value;

/**
 * Default Contructor
 *
 */
	public ExtendedAttribute(){}
 
 /**
  * 
  * @param name
  * @param value
  */
  public ExtendedAttribute(String name, String value){
		m_name	= name;
		m_value = value;
  }
	/* (non-Javadoc)
	 * @see de.objectcode.canyon.api.worklist.IExtendedAttribute#getName()
	 */
	public String getName() {
		return m_name;
	}

	/* (non-Javadoc)
	 * @see de.objectcode.canyon.api.worklist.IExtendedAttribute#getValue()
	 */
	public String getValue() {
		return m_value;
	}

}
