package de.objectcode.canyon.spi.instance;

import java.beans.PropertyDescriptor;
import java.util.Date;

import de.objectcode.canyon.base.ActivityInstanceAttributes;
import de.objectcode.canyon.spi.util.ServiceUtils;

/**
 * @author    junglas
 * @created   20. Oktober 2003
 */
public interface IActivityInstance extends IAttributedEntity, ActivityInstanceAttributes
{
  /**
   * Property descriptors for WorkItem.
   */
  public final static  PropertyDescriptor[]  PROPERTYDESCRIPTORS  = ServiceUtils.introspect( IActivityInstance.class, IAttributedEntity.class );

  /**
   * Attributes for WorkItem.
   */
  public final static  String[]              ATTRIBUTES           = ServiceUtils.getPropertyNames( PROPERTYDESCRIPTORS );


  /**
   * Gets the activityInstanceId attribute of the IActivityInstance object
   *
   * @return   The activityInstanceId value
   */
  public String getActivityInstanceId();


  /**
   * Gets the processInstance attribute of the IActivityInstance object
   *
   * @return   The processInstance value
   */
  public IProcessInstance getProcessInstance();


  /**
   * Gets the join attribute of the IActivityInstance object
   *
   * @return   The join value
   */
  public IJoinedTransition[] getJoinedTransitions();


  /**
   * Gets the joinType attribute of the IActivityInstance object
   *
   * @return   The joinType value
   */
  public int getJoinType();


  /**
   * Gets the blockActivityIterator attribute of the IActivityInstance object
   *
   * @return   The blockActivityIterator value
   */
  public IPersistentIterator getBlockActivityIterator();


  /**
   * Gets the blockActivityInstanceId attribute of the IActivityInstance object
   *
   * @return   The blockActivityInstanceId value
   */
  public String getBlockActivityInstanceId();


  /**
   * Gets the activityDefinitionId attribute of the IActivityInstance object
   *
   * @return   The activityDefinitionId value
   */
  public String getActivityDefinitionId();


  /**
   * Gets the state attribute of the IActivityInstance object
   *
   * @return   The state value
   */
  public int getState();


  /**
   * Sets the state attribute of the IActivityInstance object
   *
   * @param state  The new state value
   */
  public void setState( int state );


  /**
   * Gets the priority attribute of the IActivityInstance object
   *
   * @return   The priority value
   */
  public int getPriority();


  /**
   * Sets the priority attribute of the IActivityInstance object
   *
   * @param priority  The new priority value
   */
  public void setPriority( int priority );


  /**
   * Gets the startedDate attribute of the IActivityInstance object
   *
   * @return   The startedDate value
   */
  public Date getStartedDate();


  /**
   * Sets the startedDate attribute of the IActivityInstance object
   *
   * @param startedDate  The new startedDate value
   */
  public void setStartedDate( Date startedDate );


  /**
   * Gets the completedDate attribute of the IActivityInstance object
   *
   * @return   The completedDate value
   */
  public Date getCompletedDate();


  /**
   * Sets the completedDate attribute of the IActivityInstance object
   *
   * @param completedDate  The new completedDate value
   */
  public void setCompletedDate( Date completedDate );


  /**
   * Gets the dueDate attribute of the IActivityInstance object
   *
   * @return   The dueDate value
   */
  public Date getDueDate();


  /**
   * Sets the dueDate attribute of the IActivityInstance object
   *
   * @param dueDate  The new dueDate value
   */
  public void setDueDate( Date dueDate );


  /**
   * Gets the name attribute of the IActivityInstance object
   *
   * @return   The name value
   */
  public String getName();


  /**
   * Sets the name attribute of the IActivityInstance object
   *
   * @return  
   */
  public void setName(String name);

  /**
   * Sets the participants attribute of the IActivityInstance object
   *
   * @param participants  The new participants value
   */
  public void setParticipants( String[] participants );


  /**
   * Gets the participants attribute of the IActivityInstance object
   *
   * @return   The participants value
   */
  public String[] getParticipants();
}
