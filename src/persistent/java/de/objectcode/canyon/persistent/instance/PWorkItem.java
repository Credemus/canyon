package de.objectcode.canyon.persistent.instance;

import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.worklist.IActivityInfo;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

import java.util.Date;

/**
 * @hibernate.joined-subclass table="PWORKITEMS"
 * @hibernate.joined-subclass-key column="WORKITEMID"
 *
 * @author    junglas
 * @created   20. Oktober 2003
 */
public class PWorkItem extends PAttributedEntity implements IWorkItem
{
  private  String        m_engineId;
  private  String        m_clientId;
  private  int           m_state;
  private  int           m_priority;
  private  String        m_name;
  private  Date          m_createdDate;
  private  Date          m_startedDate;
  private  Date          m_completedDate;
  private  Date          m_dueDate;
  private  String        m_activityInstanceId;
  private  String        m_activityDefinitionId;
  private  String        m_processInstanceId;
  private  String        m_parentProcessInstanceIdPath;
  private  String        m_participant;
  private  String        m_performer;
  private  PObjectValue  m_applicationDataBlob;

  private  int           m_completionStrategy;
  private	 String				 m_processName;

  private  String        enforceMaxLength(String s, int length) {
  	if (s!= null && s.length()>length)
  		return s.substring(0,length);
  	else
  		return s;
  }

  /**
   *Constructor for the PWorkItem object
   */
  public PWorkItem()
  {
    super( IWorkItem.PROPERTYDESCRIPTORS );
  }


  /**
   *Constructor for the PWorkItem object
   *
   * @param state                    Description of the Parameter
   * @param performer                Description of the Parameter
   * @param participant              Description of the Parameter
   * @param applicationData          Description of the Parameter
   * @param engineId                 Description of the Parameter
   * @param clientId                 Description of the Parameter
   * @param activityInfo             Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   */
  public PWorkItem( String engineId, String clientId, IActivityInfo activityInfo, int state, String performer, String participant, IApplicationData[] applicationData)
    throws RepositoryException
  {
    super( IWorkItem.PROPERTYDESCRIPTORS, activityInfo.getProcessDefinitionId() );

    m_engineId = engineId;
    m_clientId = clientId;
    m_activityInstanceId = activityInfo.getActivityInstanceId();
    m_activityDefinitionId = activityInfo.getActivityDefinitionId();
    m_processInstanceId = activityInfo.getProcessInstanceId();
    m_parentProcessInstanceIdPath = activityInfo.getProcessInstanceIdPath();
    m_completionStrategy = activityInfo.getCompletionStrategy();
    m_priority = activityInfo.getPriority();
    m_name = enforceMaxLength(activityInfo.getName(),64);
    m_state = state;
    m_performer = performer;
    m_participant = participant;
    m_createdDate = new Date();
    m_dueDate = activityInfo.getDueDate();
    m_processName = enforceMaxLength(activityInfo.getProcessName(),128);

    if ( applicationData != null ) {
      setApplicationData( applicationData );
    }
  }


  /**
   * @param engineId  The engineId to set.
   */
  public void setEngineId( String engineId )
  {
    m_engineId = engineId;
  }


  /**
   * @param date
   */
  public void setCompletedDate( Date date )
  {
    m_completedDate = date;
  }


  /**
   * @param date
   */
  public void setDueDate( Date date )
  {
    m_dueDate = date;
  }


  /**
   * @param string
   */
  public void setName( String string )
  {
    m_name = enforceMaxLength(string,512);
  }


  /**
   * @param date
   */
  public void setStartedDate( Date date )
  {
    m_startedDate = date;
  }


  /**
   * @param i
   */
  public void setState( int i )
  {
    m_state = i;
  }


  /**
   * @param string
   */
  public void setParticipant( String string )
  {
    m_participant = string;
  }


  /**
   * @param string
   */
  public void setPerformer( String string )
  {
    m_performer = string;
  }


  /**
   * @param i
   */
  public void setPriority( int i )
  {
    m_priority = i;
  }


  /**
   * @param string
   */
  public void setProcessInstanceId( String string )
  {
    m_processInstanceId = enforceMaxLength(string,128);
  }


  /**
   * @param string
   */
  public void setParentProcessInstanceIdPath( String string )
  {
    m_parentProcessInstanceIdPath = string;
  }


  /**
   * @param string
   */
  public void setActivityDefinitionId( String string )
  {
    m_activityDefinitionId = string;
  }


  /**
   * @param date
   */
  public void setCreatedDate( Date date )
  {
    m_createdDate = date;
  }


  /**
   * Sets the applicationData attribute of the PWorkItem object
   *
   * @param applicationData          The new applicationData value
   * @exception RepositoryException  Description of the Exception
   */
  public void setApplicationData( IApplicationData[] applicationData )
    throws RepositoryException
  {
    if ( getApplicationDataBlob() != null ) {
      getApplicationDataBlob().setValue( applicationData );
    } else {
      setApplicationDataBlob( new PObjectValue( applicationData ) );
    }

    /*
     *  addAttributeInstance("application.name", BasicType.STRING_INT, applicationData.getName());
     *  addAttributeInstance("application.id", BasicType.STRING_INT, applicationData.getId());
     *  Parameter[] parameters = applicationData.getParameters();
     *  int i;
     *  Map attrs = getAttributeInstances();
     *  for ( i = 0; i < parameters.length; i++ ) {
     *  String attrName = "application.";
     *  if ( parameters[i].mode == ParameterMode.IN)
     *  attrName += "in.";
     *  else if ( parameters[i].mode == ParameterMode.INOUT )
     *  attrName += "inout.";
     *  else if ( parameters[i].mode == ParameterMode.OUT )
     *  attrName += "out.";
     *  attrName += parameters[i].formalName;
     *  try {
     *  IAttributeInstance attr = this.getAttributeInstance(attrName);
     *  attr.setValue(((BasicType)parameters[i].dataType).getValue(), parameters[i].value);
     *  }
     *  catch ( ObjectNotFoundException e ) {
     *  addAttributeInstance ( attrName, ((BasicType)parameters[i].dataType).getValue(), parameters[i].value);
     *  }
     *  catch ( AttributeReadOnlyException e ) {
     *  throw new RepositoryException(e );
     *  }
     *  }
     */
  }


  /**
   * @param applicationDataBlob  The applicationDataBlob to set.
   */
  public void setApplicationDataBlob( PObjectValue applicationDataBlob )
  {
    m_applicationDataBlob = applicationDataBlob;
  }


  /**
   * @param completionStrategy  The completionStrategy to set.
   */
  public void setCompletionStrategy( int completionStrategy )
  {
    m_completionStrategy = completionStrategy;
  }


  /**
   * @param activityInstanceId  The activityInstanceId to set.
   */
  public void setActivityInstanceId( String activityInstanceId )
  {
    m_activityInstanceId = activityInstanceId;
  }


  /**
   * @param clientId  The clientId to set.
   */
  public void setClientId( String clientId )
  {
    m_clientId = clientId;
  }


  /**
   * @hibernate.property column="ENGINEID" type="string" length="64"
   *
   * @return   Returns the engineId.
   */
  public String getEngineId()
  {
    return m_engineId;
  }


  /**
   * @hibernate.property type="string"
   * @hibernate.column name="CLIENTID" type="string" length="64" index="PWORKITEM_IDX2"
   *
   * @return   Returns the engineId.
   */
  public String getClientId()
  {
    return m_clientId;
  }


  /**
   * Gets the workItemId attribute of the HibWorkItem object
   *
   * @return   The workItemId value
   */
  public String getWorkItemId()
  {
    return getEntityId();
  }


  /**
   * Gets the activityDefinitionId attribute of the HibWorkItem object
   *
   * @hibernate.property column="ACTIVITYDEFINITIONID" type="string" length="64"
   *
   * @return   The activityDefinitionId value
   */
  public String getActivityDefinitionId()
  {
    return m_activityDefinitionId;
  }


  /**
   * @hibernate.property  type="string" 
   * @hibernate.column name="PROCESSINSTANCEID" length="64" index="PWORKITEM_IDX1"
   *
   * @return   The processInstanceId value
   * @see      org.obe.spi.model.AttributedEntity#getProcessInstanceId()
   */
  public String getProcessInstanceId()
  {
    return m_processInstanceId;
  }

  /**
   * @hibernate.property column="PROCESSINSTANCEIDPATH" type="string" length="128"
   *
   * @return   The processInstanceId value
   * @see      org.obe.spi.model.AttributedEntity#getProcessInstanceId()
   */
  public String getParentProcessInstanceIdPath()
  {
    return m_parentProcessInstanceIdPath;
  }

  
  /**
   * @hibernate.property column="COMPLETEDDATE" type="timestamp" not-null="false"
   *
   * @return
   */
  public Date getCompletedDate()
  {
    return m_completedDate;
  }


  /**
   * @hibernate.property column="DUEDATE" type="timestamp" not-null="false"
   *
   * @return
   */
  public Date getDueDate()
  {
    return m_dueDate;
  }


  /**
   * @hibernate.property column="NAME" type="string" length="512" not-null="true"
   *
   * @return
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * @hibernate.property column="STARTEDDATE" type="timestamp" not-null="false"
   *
   * @return
   */
  public Date getStartedDate()
  {
    return m_startedDate;
  }


  /**
   * @hibernate.property type="integer"
   * @hibernate.column name="STATE" length="2" not-null="true" index="PWORKITEM_IDX2"
   *
   * @return
   */
  public int getState()
  {
    return m_state;
  }


  /**
   * @hibernate.property type="string"
   * @hibernate.column name="PARTICIPANT" length="64" not-null="false" index="PWORKITEM_IDX2"
   *
   * @return
   */
  public String getParticipant()
  {
    return m_participant;
  }


  /**
   * @hibernate.property column="PERFORMER" type="string" length="64" not-null="false"
   *
   * @return
   */
  public String getPerformer()
  {
    return m_performer;
  }


  /**
   * @hibernate.property column="PRIORITY" type="integer" length="2" not-null="true"
   *
   * @return
   */
  public int getPriority()
  {
    return m_priority;
  }


  /**
   * @hibernate.property column="CREATEDDATE" type="timestamp" not-null="false"
   *
   * @return
   */
  public Date getCreatedDate()
  {
    return m_createdDate;
  }


  /**
   * Gets the applicationData attribute of the PWorkItem object
   *
   * @return                         The applicationData value
   * @exception RepositoryException  Description of the Exception
   */
  public IApplicationData[] getApplicationData()
    throws RepositoryException
  {
    if ( getApplicationDataBlob() != null ) {
      return ( IApplicationData[] ) getApplicationDataBlob().getValue();
    }
    return null;
    /*
     *  Map attributes = getAttributeInstances();
     *  if ( attributes.containsKey( "application.name" ) ) {
     *  ArrayList       parameters  = new ArrayList();
     *  String    name        = ( ( IAttributeInstance ) attributes.get( "application.name" ) ).getValue().toString();
     *  String id = null;
     *  if ( attributes.containsKey( "application.id" ) ) {
     *  id = ( ( IAttributeInstance ) attributes.get( "application.id" ) ).getValue().toString();
     *  }
     *  Iterator  it          = attributes.keySet().iterator();
     *  while ( it.hasNext() ) {
     *  String  attrName  = ( String ) it.next();
     *  if ( attrName.startsWith( "application.in." ) ) {
     *  IAttributeInstance  attr      = ( IAttributeInstance ) attributes.get( attrName );
     *  String  paramName  = attrName.substring( 15 );
     *  parameters.add( new Parameter(paramName, paramName, BasicType.fromInt(attr.getType()), ParameterMode.IN, attr.getValue()));
     *  } else if ( attrName.startsWith( "application.inout." ) ) {
     *  IAttributeInstance  attr      = ( IAttributeInstance ) attributes.get( attrName );
     *  String  paramName  = attrName.substring( 18 );
     *  parameters.add( new Parameter(paramName, paramName, BasicType.fromInt(attr.getType()), ParameterMode.INOUT, attr.getValue()));
     *  } else if ( attrName.startsWith( "application.out." ) ) {
     *  IAttributeInstance  attr      = ( IAttributeInstance ) attributes.get( attrName );
     *  String  paramName  = attrName.substring( 16 );
     *  parameters.add( new Parameter(paramName, paramName, BasicType.fromInt(attr.getType()), ParameterMode.OUT, attr.getValue()));
     *  }
     *  }
     *  Parameter[] params = new Parameter[parameters.size()];
     *  parameters.toArray(params);
     *  return new DefaultApplicationData(id, name, params);
     *  }
     *  return null;
     */
  }


  /**
   * @hibernate.many-to-one not-null="false" class="de.objectcode.canyon.persistent.instance.PObjectValue" column="BLOBID" cascade="all"
   *
   * @return   Returns the applicationDataBlob.
   */
  public PObjectValue getApplicationDataBlob()
  {
    return m_applicationDataBlob;
  }


  /**
   * @hibernate.property column="COMPLETION" type="integer" length="2" not-null="true"
   *
   * @return   The completionStrategy value
   * @see      de.objectcode.canyon.worklist.spi.worklist.IActivityInfo#getCompletionStrategy()
   */
  public int getCompletionStrategy()
  {
    return m_completionStrategy;
  }


  /**
   * @hibernate.property type="string" 
   * @hibernate.column name="ACTIVITYINSTANCEID" not-null="true" length="64" index="PWORKITEM_IDX1"
   *
   * @return   The activityInstanceOid value
   */
  public String getActivityInstanceId()
  {
    return m_activityInstanceId;
  }

  /**
   * Gets the process name attribute of the HibWorkItem object
   *
   * @hibernate.property column="PROCESSNAME" type="string" length="512"
   *
   * @return   The process name value
   */
	public String getProcessName() {
		return m_processName;
	}


	public void setProcessName(String processName) {
		m_processName = enforceMaxLength(processName,512);
	}
}
