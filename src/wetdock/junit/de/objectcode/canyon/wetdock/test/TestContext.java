package de.objectcode.canyon.wetdock.test;

import de.objectcode.canyon.api.worklist.ProcessInstanceData;

/**
 * @author junglas
 */
public class TestContext
{
  private String              m_userId;
  private String              m_clientId;
  private ProcessInstanceData m_processInstance;

  /**
   * @return Returns the clientId.
   */
  public String getClientId()
  {
    return m_clientId;
  }

  /**
   * @param clientId
   *          The clientId to set.
   */
  public void setClientId( String clientId )
  {
    m_clientId = clientId;
  }

  /**
   * @return Returns the userId.
   */
  public String getUserId()
  {
    return m_userId;
  }

  /**
   * @param userId
   *          The userId to set.
   */
  public void setUserId( String userId )
  {
    m_userId = userId;
  }
  
  
  /**
   * @return Returns the processInstance.
   */
  public ProcessInstanceData getProcessInstance()
  {
    return m_processInstance;
  }
  /**
   * @param processInstance The processInstance to set.
   */
  public void setProcessInstance( ProcessInstanceData processInstance )
  {
    m_processInstance = processInstance;
  }
}
