package de.objectcode.canyon.model.transition;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   26. November 2003
 */
public class Xpression implements Serializable
{
	static final long serialVersionUID = -3333163408682916075L;
	
	private  String  m_value;


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
  public String getValue()
  {
    return m_value;
  }

}
