package de.objectcode.canyon.model.data;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   4. Februar 2004
 */
public class EnumerationValue implements Serializable
{
	static final long serialVersionUID = 346407607895733127L;
	
	private  String  m_name;


  /**
   * @param name  The name to set.
   */
  public void setName( String name )
  {
    m_name = name;
  }


  /**
   * @return   Returns the name.
   */
  public String getName()
  {
    return m_name;
  }

}
