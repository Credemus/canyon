package de.objectcode.canyon.api.worklist;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.wfmc.wapi.WMWorkItemState;

import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.data.BasicType;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

/**
 * @author    junglas
 * @created   5. Dezember 2003
 */
public class WorkItemData implements Serializable
{
  /**
	 *
	 */
	private static final long serialVersionUID = -249186785334246643L;

	private  String           m_id;
  private  String           m_processId;
  private  String           m_processVersion;
  private  String           m_processInstanceId;
  private  String						m_processInstanceIdPath;
  private  String           m_name;
  private  Date             m_createdDate;
  private  Date             m_startedDate;
  private  Date             m_completeDate;
  private  Date             m_dueDate;
  private  int              m_priority;
  private  String           m_participant;
  private  String           m_performer;
  private  String           m_clientId;
  private  ApplicationData[]  m_applicationData;
  private  WMWorkItemState  m_state;
  private  String           m_activityId;
  private	 String					  m_processInstanceName;
  private  long							m_processEntityOid;

  /**
   *Constructor for the WorkItemData object
   */
  public WorkItemData() { }


  /**
   * Description of the Method
   *
   * @param workItem                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public static WorkItemData createWorkItemData( IWorkItem workItem )
    throws RepositoryException
  {
    IApplicationData[]  dataSet             = workItem.getApplicationData();
    ApplicationData[]   applicationDataSet  = new ApplicationData[dataSet.length];
    for (int k = 0; k < dataSet.length; k++) {
        ApplicationData   applicationData  = null;
	    IApplicationData  data             = dataSet[k];

	    if ( data != null ) {
	      ArrayList  params      = new ArrayList();
	      Parameter  parameters[]  = data.getParameters();
	      int        i;

	      for ( i = 0; i < parameters.length; i++ ) {

	        params.add( new ApplicationData.Parameter( parameters[i].formalName,
	            parameters[i].mode.getValue(), ( ( BasicType ) parameters[i].dataType ).getValue(), parameters[i].description, parameters[i].value ) );

	      }

		  ExtendedAttribute[]  attributes  = data.getExtendedAttributes();
		  ArrayList attrs = new ArrayList();
		  for ( i = 0; i < attributes.length; i++ ) {
			attrs.add( new ApplicationData.Attribute( attributes[i].getName(), attributes[i].getValue() ) );
		  }
		  applicationData = new ApplicationData(data.getId(), data.getName(),  data.getDescription(), params, attrs );
	    }
	    applicationDataSet[k] = applicationData;
    }

    return new WorkItemData(
        workItem.getWorkItemId(),
        0l,
        workItem.getProcessDefinitionId().getId(),
        workItem.getProcessDefinitionId().getVersion(),
				workItem.getProcessInstanceId(),
        workItem.getName(),
        workItem.getPriority(),
        WMWorkItemState.fromInt( workItem.getState() ),
        workItem.getCreatedDate(),
        workItem.getStartedDate(),
        workItem.getCompletedDate(),
				workItem.getDueDate(),
        workItem.getParticipant(),
        workItem.getPerformer(),
        workItem.getClientId(),
        applicationDataSet,
		    workItem.getActivityDefinitionId(),
		    workItem.getProcessName(),
		    workItem.getParentProcessInstanceIdPath());
  }

  /**
   *Constructor for the WorkItemData object
   *
   * @param id                 Description of the Parameter
   * @param name               Description of the Parameter
   * @param priority           Description of the Parameter
   * @param state              Description of the Parameter
   * @param startedDate        Description of the Parameter
   * @param dueDate            Description of the Parameter
   * @param applicationData    Description of the Parameter
   * @param processInstanceId  Description of the Parameter
   * @param createdDate        Description of the Parameter
   * @param participant        Description of the Parameter
   * @param performer          Description of the Parameter
   * @param processId          Description of the Parameter
   * @param processVersion     Description of the Parameter
   * @param completeDate       Description of the Parameter
   * @param clientId           Description of the Parameter
   * @param activityId         Description of the Parameter
   */
  private WorkItemData( String id, long processEntityOid, String processId, String processVersion, String processInstanceId, String name, int priority, WMWorkItemState state,
      Date createdDate, Date startedDate, Date completeDate, Date dueDate, String participant, String performer, String clientId,
      ApplicationData[] applicationData, String activityId, String processName, String processInstanceIdPath )
  {
    m_id = id;
    m_processId = processId;
    m_processVersion = processVersion;
    m_processInstanceId = processInstanceId;
    m_name = name;
    m_priority = priority;
    m_state = state;
    m_createdDate = convertDate(createdDate);
    m_startedDate = convertDate(startedDate);
    m_completeDate = convertDate(completeDate);
    m_dueDate = convertDate(dueDate);
    m_participant = participant;
    m_performer = performer;
    m_clientId = clientId;
    m_applicationData = applicationData;
    m_activityId = activityId;
    m_processInstanceName = processName;
    if (processInstanceIdPath!=null && processInstanceIdPath.length()>0)
    	m_processInstanceIdPath = new StringBuffer(processInstanceIdPath).append("_").append(processInstanceId).toString();
    else
    	m_processInstanceIdPath = processInstanceId;
  }


  protected Date convertDate(Date d) {
  	if (d==null)
  		return null;
  	if (d instanceof Timestamp)
  		return d;
  	else
  		return new Timestamp(d.getTime());
  }

  /**
   * @param state
   */
  public void setState( WMWorkItemState state )
  {
    m_state = state;
  }


  /**
   * @param date  The m_createdDate to set.
   */
  public void setCreatedDate( Date date )
  {
    m_createdDate = date;
  }


  /**
   * @param date  The m_completeDate to set.
   */
  public void setCompleteDate( Date date )
  {
    m_completeDate = date;
  }


  /**
   * @return   Returns the clientId.
   */
  public String getClientId()
  {
    return m_clientId;
  }


  /**
   * @return   Returns the processVersion.
   */
  public String getProcessVersion()
  {
    return m_processVersion;
  }


  /**
   * @return
   */
  public Date getDueDate()
  {
    return m_dueDate;
  }


  /**
   * @return
   */
  public String getId()
  {
    return m_id;
  }


  /**
   * @return
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * @return
   */
  public Date getStartedDate()
  {
    return m_startedDate;
  }


  /**
   * Return the priority.
   *
   * @return The stored priority
   *
   * @see #setPriority(int)
   */
  public int getPriority()
  {
    return m_priority;
  }

  /**
   * Set the priority.
   *
   * @param priority The new priority.
   *
   * @see #getPriority()
   */
  public void setPriority(final int priority)
  {
    m_priority = priority;
  }

  /**
   * @return
   */
  public WMWorkItemState getState()
  {
    return m_state;
  }


  /**
   * @return
   */
  public ApplicationData[] getApplicationData()
  {
    return m_applicationData;
  }


  /**
   * @return   Returns the participant.
   */
  public String getParticipant()
  {
    return m_participant;
  }


  /**
   * @return   Returns the performer.
   */
  public String getPerformer()
  {
    return m_performer;
  }


  /**
   * @return   Returns the processInstanceId.
   */
  public String getProcessInstanceId()
  {
    return m_processInstanceId;
  }


  /**
   * @return   activity (definition) id
   */
  public String getActivityId()
  {
    return m_activityId;
  }


  /**
   * @return   Returns the m_createdDate.
   */
  public Date getCreatedDate()
  {
    return m_createdDate;
  }


  /**
   * @return   Returns the m_completeDate.
   */
  public Date getCompleteDate()
  {
    return m_completeDate;
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String toString()
  {
    StringBuffer  buffer  = new StringBuffer( "WorkItemData@" );
    buffer.append( Integer.toHexString( super.hashCode() ) ).append( " [" );
    buffer.append( " id=" ).append( m_id );
    buffer.append( " name=" ).append( m_name );
    buffer.append( " priority=" ).append( m_priority );
    buffer.append( " state=" ).append( m_state );
    buffer.append( " participant=" ).append( m_participant );
    buffer.append( " performer=" ).append( m_performer );
    buffer.append( " activityId=" ).append( m_activityId );
    if ( m_applicationData != null ) {
      buffer.append( " applicationData=" ).append( m_applicationData );
    }
    buffer.append( " ]" );

    return buffer.toString();
  }


	public String getProcessInstanceName() {
		return m_processInstanceName;
	}


	public void setProcessInstanceName(String processName) {
		m_processInstanceName = processName;
	}


	public String getProcessId() {
		return m_processId;
	}


	public void setProcessId(String processId) {
		m_processId = processId;
	}


	public String getProcessInstanceIdPath() {
		return m_processInstanceIdPath;
	}


	public void setProcessInstanceIdPath(String processInstanceIdPath) {
		m_processInstanceIdPath = processInstanceIdPath;
	}


	public long getProcessEntityOid() {
		return m_processEntityOid;
	}
}
