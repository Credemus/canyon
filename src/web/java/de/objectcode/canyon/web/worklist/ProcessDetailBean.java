package de.objectcode.canyon.web.worklist;

import de.objectcode.canyon.api.worklist.ProcessData;
import de.objectcode.canyon.web.SessionData;

/**
 * @author    junglas
 * @created   11. Dezember 2003
 */
public class ProcessDetailBean
{
  private  SessionData  m_processData;


  /**
   *Constructor for the WorkItemDetailBean object
   *
   * @param processData  Description of the Parameter
   */
  public ProcessDetailBean( SessionData processData )
  {
    m_processData = processData;
  }


  /**
   * Gets the currentWorkItem attribute of the WorkItemDetailBean object
   *
   * @return   The currentWorkItem value
   */
  public ProcessData getCurrentWorkItem()
  {
    ProcessData  valueObject  = ( ProcessData ) m_processData.getValue( "currentProcess" );

    return valueObject;
  }

}
