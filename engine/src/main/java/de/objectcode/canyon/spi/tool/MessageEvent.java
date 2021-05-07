/*
 * Created on 12.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.objectcode.canyon.spi.tool;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xylander
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MessageEvent {
  
  Map<String, Object>  m_eventParams;
  String   m_engineId;
  String   m_eventType;
  String   m_processId;
  String   m_userId;
  String   m_clientId;
  String   m_parentProcessInstanceIdPath;
  String   m_action;
  
  public static String ACTION_START = "START";
  public static String ACTION_TERMINATE = "TERMINATE";
  public static String ACTION_NOTIFY = "NOTIFY";

  public static String MULTIPLICITY_EXACTLY_ONE = "EXACTLY_ONE";
  public static String MULTIPLICITY_NOT_ZERO = "NOT_ZERO";
  public static String MULTIPLICITY_ONE = "ONE";
  public static String MULTIPLICITY_ALL = "ALL";
  
  public MessageEvent() {
  }
  
  /**
   * @param eventParams
   * @param engineId
   * @param eventType
   * @param startProcessId
   * @param userId
   * @param clientId
   * @param parentProcessInstanceIdPath
   */
  public MessageEvent(Map<String, Object> eventParams, String engineId, String eventType,
      String processId, String action, String userId, String clientId,
      String parentProcessInstanceIdPath) {
    super();
    m_eventParams = eventParams;
    m_engineId = engineId;
    m_eventType = eventType;
    m_processId = processId;
    m_action = action;
    m_userId = userId;
    m_clientId = clientId;
    m_parentProcessInstanceIdPath = parentProcessInstanceIdPath;
  }
  
  public String getClientId() {
    return m_clientId;
  }
  public void setClientId(String clientId) {
    m_clientId = clientId;
  }
  public String getEngineId() {
    return m_engineId;
  }
  public void setEngineId(String engineId) {
    m_engineId = engineId;
  }
  public Map<String, Object> getEventParams() {
    return m_eventParams;
  }
  public void setEventParams(Map eventParams) {
    m_eventParams = eventParams;
  }
  public String getEventType() {
    return m_eventType;
  }
  public void setEventType(String eventType) {
    m_eventType = eventType;
  }
  public String getParentProcessInstanceIdPath() {
    return m_parentProcessInstanceIdPath;
  }
  public void setParentProcessInstanceIdPath(String parentProcessInstanceIdPath) {
    m_parentProcessInstanceIdPath = parentProcessInstanceIdPath;
  }
  public String getProcessId() {
    return m_processId;
  }
  
  public void setProcessId(String startProcessId) {
    m_processId = startProcessId;
  }
  public String getUserId() {
    return m_userId;
  }
  public void setUserId(String userId) {
    m_userId = userId;
  }
}
