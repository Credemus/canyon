package de.objectcode.canyon.model;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class ExternalPackage implements Serializable
{
	static final long serialVersionUID = 7285061640948024410L;
	
	private  String  m_href;


  /**
   * @param string
   */
  public void setHref( String string )
  {
    m_href = string;
  }


  /**
   * @return
   */
  public String getHref()
  {
    return m_href;
  }

}
