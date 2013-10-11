package de.objectcode.canyon.drydock.worklist;

import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.process.IProcessDefinitionID;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

import java.util.Date;
import java.util.Map;

import org.wfmc.wapi.WMWorkItemState;

/**
 * @author    junglas
 * @created   15. Juni 2004
 */
public class WorkItem implements IWorkItem
{
  String            m_engineId;
  String            m_clientId;
  int               m_status;
  String            m_name;
  String            m_activityId;
  String            m_processId;
  String			m_parentProcessInstanceIdPath;
  Participant[]     m_performers;
  IApplicationData[]  m_applicationData;


  /**
   *Constructor for the WorkItem object
   *
   * @param name             Description of the Parameter
   * @param activityId       Description of the Parameter
   * @param processId        Description of the Parameter
   * @param performers       Description of the Parameter
   * @param engineId         Description of the Parameter
   * @param applicationData  Description of the Parameter
   */
  WorkItem( String engineId, String clientId, String name, String activityId, String processId, String parentProcessInstanceIdPath, Participant[] performers, IApplicationData[] applicationData )
  {
    m_engineId = engineId;
    m_clientId = clientId;
    m_status = WMWorkItemState.OPEN_RUNNING_INT;
    m_name = name;
    m_activityId = activityId;
    m_processId = processId;
    m_performers = performers;
    m_applicationData = applicationData;
  }


  /**
   * @param applicationData          The new applicationData value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.worklist.spi.worklist.IWorkItem#setApplicationData(de.objectcode.canyon.worklist.spi.worklist.IApplicationData)
   */
  public void setApplicationData( IApplicationData[] applicationData )
    throws RepositoryException
  {
    m_applicationData = applicationData;
  }


  /**
   * @param completedDate  The new completedDate value
   * @see                  de.objectcode.canyon.worklist.spi.worklist.IWorkItem#setCompletedDate(java.util.Date)
   */
  public void setCompletedDate( Date completedDate )
  {
    // TODO Auto-generated method stub

  }


  /**
   * @param dueDate  The new dueDate value
   * @see            de.objectcode.canyon.worklist.spi.worklist.IWorkItem#setDueDate(java.util.Date)
   */
  public void setDueDate( Date dueDate )
  {
    // TODO Auto-generated method stub

  }


  /**
   * @param name  The new name value
   * @see         de.objectcode.canyon.worklist.spi.worklist.IWorkItem#setName(java.lang.String)
   */
  public void setName( String name )
  {
    m_name = name;
  }


  /**
   * @param participant  The new participant value
   * @see                de.objectcode.canyon.worklist.spi.worklist.IWorkItem#setParticipant(java.lang.String)
   */
  public void setParticipant( String participant )
  {
    // TODO Auto-generated method stub

  }


  /**
   * @param priority  The new priority value
   * @see             de.objectcode.canyon.worklist.spi.worklist.IWorkItem#setPriority(int)
   */
  public void setPriority( int priority )
  {
    // TODO Auto-generated method stub

  }


  /**
   * @param startedDate  The new startedDate value
   * @see                de.objectcode.canyon.worklist.spi.worklist.IWorkItem#setStartedDate(java.util.Date)
   */
  public void setStartedDate( Date startedDate )
  {
    // TODO Auto-generated method stub

  }


  /**
   * @param state  The new state value
   * @see          de.objectcode.canyon.worklist.spi.worklist.IWorkItem#setState(int)
   */
  public void setState( int state )
  {
    m_status = state;
  }


  /**
   * @param status  The status to set.
   */
  public void setStatus( int status )
  {
    m_status = status;
  }


  /**
   * @return   Returns the engineId.
   */
  public String getEngineId()
  {
    return m_engineId;
  }


  /**
   * @return Returns the clientId.
   */
  public String getClientId ( )
  {
    return m_clientId;
  }
  /**
   * @return   The activityDefinitionId value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getActivityDefinitionId()
   */
  public String getActivityDefinitionId()
  {
    // TODO Auto-generated method stub
    return null;
  }


  /**
   * @return   The activityInstanceId value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getActivityInstanceId()
   */
  public String getActivityInstanceId()
  {
    return m_activityId;
  }


  /**
   * @return                         The applicationData value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getApplicationData()
   */
  public IApplicationData[] getApplicationData()
    throws RepositoryException
  {
    return m_applicationData;
  }


  /**
   * @return   The completedDate value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getCompletedDate()
   */
  public Date getCompletedDate()
  {
    return new Date();
  }


  /**
   * @return   The completionStrategy value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getCompletionStrategy()
   */
  public int getCompletionStrategy()
  {
    return 0;
  }


  /**
   * @return   The createdDate value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getCreatedDate()
   */
  public Date getCreatedDate()
  {
    return new Date();
  }


  /**
   * @return   The dueDate value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getDueDate()
   */
  public Date getDueDate()
  {
    return new Date();
  }


  /**
   * @return   The participant value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getParticipant()
   */
  public String getParticipant()
  {
    // TODO Auto-generated method stub
    return null;
  }


  /**
   * @return   The priority value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getPriority()
   */
  public int getPriority()
  {
    return 0;
  }


  /**
   * @return   The processInstanceId value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getProcessInstanceId()
   */
  public String getProcessInstanceId()
  {
    return m_processId;
  }


  /**
   * @return   The startedDate value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getStartedDate()
   */
  public Date getStartedDate()
  {
    return new Date();
  }


  /**
   * @return   The state value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getState()
   */
  public int getState()
  {
    return m_status;
  }


  /**
   * @return   The workItemId value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getWorkItemId()
   */
  public String getWorkItemId()
  {
    return "";
  }


  /**
   * @param attributeName            Description of the Parameter
   * @return                         The attributeInstance value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.instance.IAttributedEntity#getAttributeInstance(java.lang.String)
   */
  public IAttributeInstance getAttributeInstance( String attributeName )
    throws RepositoryException
  {
    return null;
  }


  /**
   * @return                         The attributeInstances value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.instance.IAttributedEntity#getAttributeInstances()
   */
  public Map getAttributeInstances()
    throws RepositoryException
  {
    return null;
  }


  /**
   * @return   The entityId value
   * @see      de.objectcode.canyon.spi.instance.IAttributedEntity#getEntityId()
   */
  public String getEntityId()
  {
    return "";
  }


  /**
   * @return   The processDefinitionId value
   * @see      de.objectcode.canyon.spi.instance.IAttributedEntity#getProcessDefinitionId()
   */
  public IProcessDefinitionID getProcessDefinitionId()
  {
    // TODO Auto-generated method stub
    return null;
  }


  /**
   * @return   Returns the status.
   */
  public int getStatus()
  {
    return m_status;
  }


  /**
   * @return   Returns the activityId.
   */
  public String getActivityId()
  {
    return m_activityId;
  }


  /**
   * @return   Returns the name.
   */
  public String getName()
  {
    return m_name;
  }



  /**
   * @return   Returns the processId.
   */
  public String getProcessId()
  {
    return m_processId;
  }


  /**
   * @return   The performer value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getPerformer()
   */
  public String getPerformer()
  {
    StringBuffer  buffer  = new StringBuffer();
    int           i;

    for ( i = 0; i < m_performers.length; i++ ) {
      if ( i > 0 ) {
        buffer.append( "," );
      }
      buffer.append( m_performers[i].getId() );
    }

    return buffer.toString();
  }


  /**
   * @param attributeName            The feature to be added to the AttributeInstance attribute
   * @param attributeType            The feature to be added to the AttributeInstance attribute
   * @param attributeValue           The feature to be added to the AttributeInstance attribute
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.instance.IAttributedEntity#addAttributeInstance(java.lang.String, int, java.lang.Object)
   */
  public IAttributeInstance addAttributeInstance( String attributeName,
      int attributeType,
      Object attributeValue )
    throws RepositoryException
  {
    return null;
  }


  /* (non-Javadoc)
   * @see de.objectcode.canyon.worklist.spi.worklist.IWorkItem#getParentProcessInstanceIdPath()
   */
  public String getParentProcessInstanceIdPath() {
    return m_parentProcessInstanceIdPath;
  }


	public String getProcessName() {
		return "";
	}  
}
