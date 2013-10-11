package de.objectcode.canyon.web.worklist;

import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.web.SessionData;

/**
 * @author    junglas
 * @created   9. Dezember 2003
 */
public class WorkItemDetailBean
{
  private  SessionData  m_processData;


  /**
   *Constructor for the WorkItemDetailBean object
   *
   * @param processData  Description of the Parameter
   */
  public WorkItemDetailBean( SessionData processData )
  {
    m_processData = processData;
  }


  /**
   * Gets the currentWorkItem attribute of the WorkItemDetailBean object
   *
   * @return   The currentWorkItem value
   */
  public WorkItemData getCurrentWorkItem()
  {
    WorkItemData  valueObject  = ( WorkItemData ) m_processData.getValue( "currentWorkItem" );

    return valueObject;
  }
}
