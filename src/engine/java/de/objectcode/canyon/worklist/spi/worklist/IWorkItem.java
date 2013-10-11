package de.objectcode.canyon.worklist.spi.worklist;

import java.beans.PropertyDescriptor;
import java.util.Date;

import de.objectcode.canyon.base.WorkItemAttributes;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.instance.IAttributedEntity;
import de.objectcode.canyon.spi.util.ServiceUtils;

/**
 * @author    junglas
 * @created   17. Oktober 2003
 */
public interface IWorkItem extends IAttributedEntity, WorkItemAttributes
{
  /**
   * Property descriptors for WorkItem.
   */
  public final static  PropertyDescriptor[]  PROPERTYDESCRIPTORS  = ServiceUtils.introspect( IWorkItem.class, IAttributedEntity.class );

  /**
   * Attributes for WorkItem.
   */
  public final static  String[]              ATTRIBUTES           = ServiceUtils.getPropertyNames( PROPERTYDESCRIPTORS );

  /**
   * Gets the workItemId attribute of the WorkItem object
   *
   * @return   The workItemId value
   */
  public String getWorkItemId();
  
  

  /**
   * Gets the state attribute of the WorkItem object
   *
   * @return   The state value
   */
  public int getState();


  /**
   * Sets the state attribute of the WorkItem object
   *
   * @param state  The new state value
   */
  public void setState( int state );


  /**
   * Gets the priority attribute of the WorkItem object
   *
   * @return   The priority value
   */
  public int getPriority();


  /**
   * Sets the priority attribute of the WorkItem object
   *
   * @param priority  The new priority value
   */
  public void setPriority( int priority );


  /**
   * Gets the createdDate attribute of the IWorkItem object
   *
   * @return   The createdDate value
   */
  public Date getCreatedDate();


  /**
   * Gets the startedDate attribute of the WorkItem object
   *
   * @return   The startedDate value
   */
  public Date getStartedDate();


  /**
   * Sets the startedDate attribute of the IWorkItem object
   *
   * @param startedDate  The new startedDate value
   */
  public void setStartedDate( Date startedDate );


  /**
   * Gets the completedDate attribute of the WorkItem object
   *
   * @return   The completedDate value
   */
  public Date getCompletedDate();


  /**
   * Sets the completedDate attribute of the WorkItem object
   *
   * @param completedDate  The new completedDate value
   */
  public void setCompletedDate( Date completedDate );


  /**
   * Gets the dueDate attribute of the WorkItem object
   *
   * @return   The dueDate value
   */
  public Date getDueDate();


  /**
   * Sets the dueDate attribute of the WorkItem object
   *
   * @param dueDate  The new dueDate value
   */
  public void setDueDate( Date dueDate );


  /**
   * Gets the name attribute of the WorkItem object
   *
   * @return   The name value
   */
  public String getName();


  /**
   * Sets the name attribute of the WorkItem object
   *
   * @param name  The new name value
   */
  public void setName( String name );


  /**
   * Gets the participant attribute of the WorkItem object
   *
   * @return   The participant value
   */
  public String getParticipant();


  /**
   * Sets the participant attribute of the WorkItem object
   *
   * @param participant  The new participant value
   */
  public void setParticipant( String participant );


  /**
   * Gets the performer attribute of the WorkItem object
   *
   * @return   The performer value
   */
  public String getPerformer();


  /**
   * Gets the applicationData attribute of the IWorkItem object
   *
   * @return   The applicationData value
   */
  public IApplicationData[] getApplicationData() throws RepositoryException;
  
  public void setApplicationData(IApplicationData[] applicationData) throws RepositoryException;

  public String getActivityInstanceId();
  
  public String getActivityDefinitionId();
  
  public String getProcessInstanceId();
  
  public String getParentProcessInstanceIdPath();
  
  public int getCompletionStrategy();
  
  public String getEngineId ( );
  
  public String getClientId();
  
  public String getProcessName();
}
