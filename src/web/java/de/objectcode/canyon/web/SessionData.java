package de.objectcode.canyon.web;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.lang.SerializationUtils;

/**
 * @author junglas
 */
public class SessionData 
{
	private HashMap m_values;
	
	public SessionData ( String encoded )
	{
		if ( encoded == null ) {
			m_values = new HashMap();
		} else {
			m_values = (HashMap)SerializationUtils.deserialize(Base64.altDecode(encoded.toCharArray()));
		}
	}

	public Serializable getValue ( String name )
	{
		return (Serializable)m_values.get(name);
	}
	
	public void setValue ( String name, Serializable value )
	{
		m_values.put(name, value);
	}
	
	public String getEncoded ( )
	{
		return new String(Base64.altEncode(SerializationUtils.serialize(m_values)));
	}
}
