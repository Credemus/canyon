/*
 * Created on 08.04.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.objectcode.canyon.api.worklist;

import java.io.Serializable;

/**
 * 
 * @author ruth
 * 
 * Minimum attributes for needs
 * TODO transition needed for future 
 * 
 */
public class ActivityData implements Serializable {

	static final long serialVersionUID = -5478729874603189939L;
	
	private String m_id;
	private String m_name;
	private ExtendedAttribute[] m_extendedAttributes;
	
	public ActivityData(){}

/**
 * 
 * @param id
 * @param name
 * @param extendedAttributes
 */
	public ActivityData(String id, String name, ExtendedAttribute[] extendedAttributes){
		m_id		= id;
		m_name	= name;
		m_extendedAttributes = extendedAttributes;
	}

	/**
	 * @param string
	 */
	public void setId(String string) {
		m_id = string;
	}

	/**
	 * @return
	 */
	public String getId() {
		return m_id;
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
	public String getName() {
		return m_id;
	}

	/**
	 * @return
	 */
	public ExtendedAttribute[] getExtendedAttributes() {
		return m_extendedAttributes;
	}

	/**
	 * 
	 * @param extendedAttributes
	 */
	public void setExtendedAttributes( ExtendedAttribute[] extendedAttributes) {
		m_extendedAttributes = extendedAttributes;
	}
		
}
