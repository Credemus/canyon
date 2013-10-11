package de.objectcode.canyon.model.transition;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class Join implements Serializable
{
	static final long serialVersionUID = 4658495867824282938L;
	
	private  JoinType  m_type;


  /**
   * @param type
   */
  public void setType( JoinType type )
  {
    m_type = type;
  }


  /**
   * @return
   */
  public JoinType getType()
  {
    return m_type;
  }

}
