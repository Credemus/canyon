package de.objectcode.canyon.model.process;

import java.io.Serializable;
import java.util.Date;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class ProcessHeader implements Serializable
{
	static final long serialVersionUID = -4419942573705794116L;
	
	private final static  int             DEFAULT_PRIORITY  = 5;

  private               Date            m_created;
  private               int             m_priority        = DEFAULT_PRIORITY;
  private               Duration        m_limit;
  private               Duration        m_validFrom;
  private               Duration        m_validTo;
  private               TimeEstimation  m_timeEstimation;
  private               String          m_description;
  private               DurationUnit    m_durationUnit;


  /**
   * @param string
   */
  public void setDescription( String string )
  {
    m_description = string;
  }


  /**
   * @param date
   */
  public void setCreated( Date date )
  {
    m_created = date;
  }


  /**
   * @param unit
   */
  public void setDurationUnit( DurationUnit unit )
  {
    m_durationUnit = unit;
  }


  /**
   * @param duration
   */
  public void setLimit( Duration duration )
  {
    m_limit = duration;
  }



  /**
   * @param estimation
   */
  public void setTimeEstimation( TimeEstimation estimation )
  {
    m_timeEstimation = estimation;
  }


  /**
   * @param duration
   */
  public void setValidFrom( Duration duration )
  {
    m_validFrom = duration;
  }


  /**
   * @param duration
   */
  public void setValidTo( Duration duration )
  {
    m_validTo = duration;
  }


  /**
   * @param i
   */
  public void setPriority( int i )
  {
    m_priority = i;
  }


  /**
   * @return
   */
  public String getDescription()
  {
    return m_description;
  }


  /**
   * @return
   */
  public Date getCreated()
  {
    return m_created;
  }


  /**
   * @return
   */
  public DurationUnit getDurationUnit()
  {
    return m_durationUnit;
  }


  /**
   * @return
   */
  public Duration getLimit()
  {
    return m_limit;
  }


  /**
   * @return
   */
  public TimeEstimation getTimeEstimation()
  {
    return m_timeEstimation;
  }


  /**
   * @return
   */
  public Duration getValidFrom()
  {
    return m_validFrom;
  }


  /**
   * @return
   */
  public Duration getValidTo()
  {
    return m_validTo;
  }


  /**
   * @return
   */
  public int getPriority()
  {
    return m_priority;
  }

}
