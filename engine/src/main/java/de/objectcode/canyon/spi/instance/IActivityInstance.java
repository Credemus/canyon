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
  PropertyDescriptor[]  PROPERTYDESCRIPTORS  = ServiceUtils.introspect( IActivityInstance.class, IAttributedEntity.class );

  /**
   * Attributes for WorkItem.
   */
  String[]              ATTRIBUTES           = ServiceUtils.getPropertyNames( PROPERTYDESCRIPTORS );


  /**
   * Gets the activityInstanceId attribute of the IActivityInstance object
   *
   * @return   The activityInstanceId value
   */
  String getActivityInstanceId();


  /**
   * Gets the processInstance attribute of the IActivityInstance object
   *
   * @return   The processInstance value
   */
  IProcessInstance getProcessInstance();


  /**
   * Gets the join attribute of the IActivityInstance object
   *
   * @return   The join value
   */
  IJoinedTransition[] getJoinedTransitions();


  /**
   * Gets the joinType attribute of the IActivityInstance object
   *
   * @return   The joinType value
   */
  int getJoinType();


  /**
   * Gets the blockActivityIterator attribute of the IActivityInstance object
   *
   * @return   The blockActivityIterator value
   */
  IPersistentIterator getBlockActivityIterator();


  /**
   * Gets the blockActivityInstanceId attribute of the IActivityInstance object
   *
   * @return   The blockActivityInstanceId value
   */
  String getBlockActivityInstanceId();


  /**
   * Gets the activityDefinitionId attribute of the IActivityInstance object
   *
   * @return   The activityDefinitionId value
   */
  String getActivityDefinitionId();


  /**
   * Gets the state attribute of the IActivityInstance object
   *
   * @return   The state value
   */
  int getState();


  /**
   * Sets the state attribute of the IActivityInstance object
   *
   * @param state  The new state value
   */
  void setState(int state);


  /**
   * Gets the priority attribute of the IActivityInstance object
   *
   * @return   The priority value
   */
  int getPriority();


  /**
   * Sets the priority attribute of the IActivityInstance object
   *
   * @param priority  The new priority value
   */
  void setPriority(int priority);


  /**
   * Gets the startedDate attribute of the IActivityInstance object
   *
   * @return   The startedDate value
   */
  Date getStartedDate();


  /**
   * Sets the startedDate attribute of the IActivityInstance object
   *
   * @param startedDate  The new startedDate value
   */
  void setStartedDate(Date startedDate);


  /**
   * Gets the completedDate attribute of the IActivityInstance object
   *
   * @return   The completedDate value
   */
  Date getCompletedDate();


  /**
   * Sets the completedDate attribute of the IActivityInstance object
   *
   * @param completedDate  The new completedDate value
   */
  void setCompletedDate(Date completedDate);


  /**
   * Gets the dueDate attribute of the IActivityInstance object
   *
   * @return   The dueDate value
   */
  Date getDueDate();


  /**
   * Sets the dueDate attribute of the IActivityInstance object
   *
   * @param dueDate  The new dueDate value
   */
  void setDueDate(Date dueDate);


  /**
   * Gets the name attribute of the IActivityInstance object
   *
   * @return   The name value
   */
  String getName();


  /**
   * Sets the name attribute of the IActivityInstance object
   *
   * @return  
   */
  void setName(String name);

  /**
   * Sets the participants attribute of the IActivityInstance object
   *
   * @param participants  The new participants value
   */
  void setParticipants(String[] participants);


  /**
   * Gets the participants attribute of the IActivityInstance object
   *
   * @return   The participants value
   */
  String[] getParticipants();
}
