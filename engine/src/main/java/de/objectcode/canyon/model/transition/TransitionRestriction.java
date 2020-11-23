package de.objectcode.canyon.model.transition;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class TransitionRestriction implements Serializable
{
	static final long serialVersionUID = -6364133698803710388L;
	
	private  Join   m_join;
  private  Split  m_split;


  /**
   * @param join
   */
  public void setJoin( Join join )
  {
    m_join = join;
  }


  /**
   * @param split
   */
  public void setSplit( Split split )
  {
    m_split = split;
  }


  /**
   * @return
   */
  public Join getJoin()
  {
    return m_join;
  }


  /**
   * @return
   */
  public Split getSplit()
  {
    return m_split;
  }

}
