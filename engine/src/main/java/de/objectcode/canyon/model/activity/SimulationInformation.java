package de.objectcode.canyon.model.activity;

import java.io.Serializable;

import de.objectcode.canyon.model.process.TimeEstimation;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class SimulationInformation implements Serializable
{
	static final long serialVersionUID = 6780825091911249735L;
	
	private  InstantiationType  m_instantiationType;
  private  String             m_cost;
  private  TimeEstimation     m_timeEstimation;


  /**
   * @param string
   */
  public void setCost( String string )
  {
    m_cost = string;
  }


  /**
   * @param type
   */
  public void setInstantiationType( InstantiationType type )
  {
    m_instantiationType = type;
  }


  /**
   * @param estimation
   */
  public void setTimeEstimation( TimeEstimation estimation )
  {
    m_timeEstimation = estimation;
  }


  /**
   * @return
   */
  public String getCost()
  {
    return m_cost;
  }


  /**
   * @return
   */
  public InstantiationType getInstantiationType()
  {
    return m_instantiationType;
  }


  /**
   * @return
   */
  public TimeEstimation getTimeEstimation()
  {
    return m_timeEstimation;
  }

}
