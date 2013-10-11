package de.objectcode.canyon.model;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class ExtendedAttribute implements Serializable
{
	static final long serialVersionUID = 1106193965055233272L;
	
	private  String  m_name;
  private  String  m_value;

  public ExtendedAttribute ()
  {
  }
  
  public ExtendedAttribute ( String name, String value )
  {
    m_name = name;
    m_value = value;
  }

  /**
   * @param string
   */
  public void setName( String string )
  {
    m_name = string;
  }


  /**
   * @param string
   */
  public void setValue( String string )
  {
    m_value = string;
  }


  /**
   * @return
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * @return
   */
  public String getValue()
  {
    return m_value;
  }

}
