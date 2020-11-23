package de.objectcode.canyon.model.process;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class TimeEstimation implements Serializable
{
	static final long serialVersionUID = -5233828596087725680L;
	
	private  Duration  m_waitingTime;
  private  Duration  m_workingTime;
  private  Duration  m_duration;


  /**
   * @param duration
   */
  public void setDuration( Duration duration )
  {
    m_duration = duration;
  }


  /**
   * @param duration
   */
  public void setWaitingTime( Duration duration )
  {
    m_waitingTime = duration;
  }


  /**
   * @param duration
   */
  public void setWorkingTime( Duration duration )
  {
    m_workingTime = duration;
  }


  /**
   * @return
   */
  public Duration getDuration()
  {
    return m_duration;
  }


  /**
   * @return
   */
  public Duration getWaitingTime()
  {
    return m_waitingTime;
  }


  /**
   * @return
   */
  public Duration getWorkingTime()
  {
    return m_workingTime;
  }

}
