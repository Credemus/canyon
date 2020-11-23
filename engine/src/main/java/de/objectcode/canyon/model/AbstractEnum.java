package de.objectcode.canyon.model;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public abstract class AbstractEnum implements Serializable
{
	static final long serialVersionUID = -7647847800430896535L;
	
  protected final  int     m_value;
  protected final  String  m_tag;


  /**
   *Constructor for the AbstractEnum object
   *
   * @param value  Description of the Parameter
   * @param tag    Description of the Parameter
   */
  protected AbstractEnum( int value, String tag )
  {
    m_value = value;
    m_tag = tag;
  }


  /**
   * Gets the value attribute of the AbstractEnum object
   *
   * @return   The value value
   */
  public int getValue()
  {
    return m_value;
  }


  /**
   * Gets the tag attribute of the AbstractEnum object
   *
   * @return   The tag value
   */
  public String getTag()
  {
    return m_tag;
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String toString()
  {
    return m_tag;
  }
  
  public abstract Object readResolve();
}
