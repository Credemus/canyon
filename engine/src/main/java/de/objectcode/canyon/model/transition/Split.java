package de.objectcode.canyon.model.transition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class Split implements Serializable
{
	static final long serialVersionUID = -7022985662692441919L;
	
	private  SplitType  m_type;
  private  List       m_transitionRefs;


  /**
   *Constructor for the Split object
   */
  public Split()
  {
    m_transitionRefs = new ArrayList();
  }


  /**
   * @param type
   */
  public void setType( SplitType type )
  {
    m_type = type;
  }


  /**
   * Gets the transitionRefs attribute of the Split object
   *
   * @return   The transitionRefs value
   */
  public TransitionRef[] getTransitionRefs()
  {
    TransitionRef  ret[]  = new TransitionRef[m_transitionRefs.size()];

    m_transitionRefs.toArray( ret );

    return ret;
  }


  /**
   * @return
   */
  public SplitType getType()
  {
    return m_type;
  }


  /**
   * Adds a feature to the TransitionRef attribute of the Split object
   *
   * @param transitionRef  The feature to be added to the TransitionRef attribute
   */
  public void addTransitionRef( TransitionRef transitionRef )
  {
    m_transitionRefs.add( transitionRef );
  }

}
