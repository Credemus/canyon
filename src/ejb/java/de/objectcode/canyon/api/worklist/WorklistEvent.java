package de.objectcode.canyon.api.worklist;

import java.io.Serializable;
import java.util.Map;

/**
 * @author    junglas
 * @created   5. April 2004
 */
public class WorklistEvent implements Serializable
{
	static final long serialVersionUID = 2858774714845997272L;
	
	private  String  m_clientId;
  private  String  m_eventType;
  private  Map     m_eventParameters;


  /**
   *Constructor for the WorklistEvent object
   */
  public WorklistEvent() { }


  /**
   *Constructor for the WorklistEvent object
   *
   * @param eventType        Description of the Parameter
   * @param eventParameters  Description of the Parameter
   */
  public WorklistEvent( String clientId, String eventType, Map eventParameters )
  {
    m_clientId = clientId;
    m_eventType = eventType;
    m_eventParameters = eventParameters;
  }


  /**
   * @return   Returns the eventParameters.
   */
  public Map getEventParameters()
  {
    return m_eventParameters;
  }


  /**
   * @return   Returns the eventType.
   */
  public String getEventType()
  {
    return m_eventType;
  }

  public String getClientId() {
    return m_clientId;
  }
  public void setClientId(String clientId) {
    m_clientId = clientId;
  }
}
