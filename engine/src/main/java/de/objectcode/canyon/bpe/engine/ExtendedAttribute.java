package de.objectcode.canyon.bpe.engine;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   7. Juni 2004
 */
public class ExtendedAttribute implements Serializable
{
  static final long serialVersionUID = -2044453474277506267L;
  
  private  String  m_name;
  private  String  m_value;


  /**
   *Constructor for the ExtendedAttribute object
   *
   * @param name   Description of the Parameter
   * @param value  Description of the Parameter
   */
  public ExtendedAttribute( String name, String value )
  {
    m_name = name;
    m_value = value;
  }


  /**
   * @param name  The name to set.
   */
  public void setName( String name )
  {
    m_name = name;
  }


  /**
   * @param value  The value to set.
   */
  public void setValue( String value )
  {
    m_value = value;
  }


  /**
   * @return   Returns the name.
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * @return   Returns the value.
   */
  public String getValue()
  {
    return m_value;
  }
}
