package de.objectcode.canyon.spi.instance;

import java.beans.PropertyDescriptor;
import java.util.Date;

import de.objectcode.canyon.base.ProcessInstanceAttributes;
import de.objectcode.canyon.spi.util.ServiceUtils;

/**
 * @author    junglas
 * @created   20. Oktober 2003
 */
public interface IProcessInstance extends IAttributedEntity, ProcessInstanceAttributes
{
  /**
   * Property descriptors for ProcessInstance.
   */
  public final static  PropertyDescriptor[]  PROPERTYDESCRIPTORS  = ServiceUtils.introspect( IProcessInstance.class, IAttributedEntity.class );

  /**
   * Attributes for ProcessInstance.
   */
  public final static  String[]              ATTRIBUTES           = ServiceUtils.getPropertyNames( PROPERTYDESCRIPTORS );


  /**
   * Gets the processInstanceId attribute of the IProcessInstance object
   *
   * @return   The processInstanceId value
   */
  public String getProcessInstanceId();


  /**
   * Gets the state attribute of the IProcessInstance object
   *
   * @return   The state value
   */
  public int getState();


  /**
   * Sets the state attribute of the IProcessInstance object
   *
   * @param state  The new state value
   */
  public void setState( int state );


  /**
   * Gets the priority attribute of the IProcessInstance object
   *
   * @return   The priority value
   */
  public int getPriority();


  /**
   * Sets the priority attribute of the IProcessInstance object
   *
   * @param priority  The new priority value
   */
  public void setPriority( int priority );


  /**
   * Gets the createdDate attribute of the IProcessInstance object
   *
   * @return   The createdDate value
   */
  public Date getCreatedDate();


  /**
   * Gets the dueDate attribute of the IProcessInstance object
   *
   * @return   The dueDate value
   */
  public Date getDueDate();


  /**
   * Sets the dueDate attribute of the IProcessInstance object
   *
   * @param dueDate  The new dueDate value
   */
  public void setDueDate( Date dueDate );


  /**
   * Gets the startedDate attribute of the IProcessInstance object
   *
   * @return   The startedDate value
   */
  public Date getStartedDate();

	/**
		* Sets the startedDate attribute of the IProcessInstance object
		*
		* @return   The startedDate value
		*/
	 public void setStartedDate( Date startedDate );


  /**
   * Gets the completedDate attribute of the IProcessInstance object
   *
   * @return   The completedDate value
   */
  public Date getCompletedDate();


  /**
   * Sets the completedDate attribute of the IProcessInstance object
   *
   * @param completedDate  The new completedDate value
   */
  public void setCompletedDate( Date completedDate );


  /**
   * Gets the name attribute of the IProcessInstance object
   *
   * @return   The name value
   */
  public String getName();


  /**
   * Gets the participants attribute of the IProcessInstance object
   *
   * @return   The participants value
   */
  public String[] getParticipants();


  /**
   * Gets the parentActivityInstance attribute of the IProcessInstance object
   *
   * @return   The parentActivityInstance value
   */
  public IActivityInstance getParentActivityInstance();
}
