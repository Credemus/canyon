package de.objectcode.canyon.model.transition;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class TransitionRef implements Serializable
{
	static final long serialVersionUID = 6190385463670307454L;
	
	private  String  m_id;


  /**
   * @param string
   */
  public void setId( String string )
  {
    m_id = string;
  }


  /**
   * @return
   */
  public String getId()
  {
    return m_id;
  }

}
