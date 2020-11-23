package de.objectcode.canyon.model.data;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class ActualParameter implements Serializable
{
	static final long serialVersionUID = 2339247273382414528L;
	
  private  String  m_text;


  /**
   * @param string
   */
  public void setText( String string )
  {
    m_text = string;
  }


  /**
   * @return
   */
  public String getText()
  {
    return m_text;
  }

}
