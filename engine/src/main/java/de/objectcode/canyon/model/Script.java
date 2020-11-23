package de.objectcode.canyon.model;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class Script implements Serializable
{
	static final long serialVersionUID = -1138328760123320159L;
	
	private  String  m_type;
  private  String  m_version;
  private  String  m_grammar;


  /**
   * @param string
   */
  public void setGrammar( String string )
  {
    m_grammar = string;
  }


  /**
   * @param string
   */
  public void setType( String string )
  {
    m_type = string;
  }


  /**
   * @param string
   */
  public void setVersion( String string )
  {
    m_version = string;
  }


  /**
   * @return
   */
  public String getGrammar()
  {
    return m_grammar;
  }


  /**
   * @return
   */
  public String getType()
  {
    return m_type;
  }


  /**
   * @return
   */
  public String getVersion()
  {
    return m_version;
  }

}
