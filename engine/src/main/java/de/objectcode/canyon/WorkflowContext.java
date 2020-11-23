package de.objectcode.canyon;

import de.objectcode.canyon.spi.ServiceManager;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class WorkflowContext
{
  private  ServiceManager  m_serviceManager;
  private  String  m_userId;


  /**
   *Constructor for the WorkflowContext object
   *
   * @param userId  Description of the Parameter
   */
  public WorkflowContext( ServiceManager serviceManager, String userId )
  {
    m_serviceManager = serviceManager;
    m_userId = userId;
  }


  /**
   * @return
   */
  public String getUserId()
  {
    return m_userId;
  }

}
