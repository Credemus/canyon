package de.objectcode.canyon.model.transition;

import de.objectcode.canyon.model.BaseElement;
import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.IValidatable;
import de.objectcode.canyon.model.ValidationErrors;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.activity.ExecutionType;
import de.objectcode.canyon.model.activity.IActivityContainer;

/**
 * @author    junglas
 * @created   21. November 2003
 */
public class Transition extends BaseElement implements IValidatable
{
	static final long serialVersionUID = -5486222071173616923L;
	
	private final static  String              EXECUTION       = "Execution";

  private               IActivityContainer  m_container;
  private               String              m_to;
  private               String              m_from;
  private               Activity            m_toActivity;
  private               Activity            m_fromActivity;
  private               Condition           m_condition;
  private               ExecutionType       m_execution = ExecutionType.SYNCHRONOUS;


  /**
   * @param string
   */
  public void setFrom( String string )
  {
    m_from = string;
  }


  /**
   * @param string
   */
  public void setTo( String string )
  {
    m_to = string;
  }


  /**
   * @param container
   */
  public void setContainer( IActivityContainer container )
  {
    m_container = container;
  }


  /**
   * @param condition
   */
  public void setCondition( Condition condition )
  {
    m_condition = condition;
  }


  /**
   * @return
   */
  public String getFrom()
  {
    return m_from;
  }


  /**
   * @return
   */
  public String getTo()
  {
    return m_to;
  }


  /**
   * @return
   */
  public IActivityContainer getContainer()
  {
    return m_container;
  }


  /**
   * @return
   */
  public Condition getCondition()
  {
    return m_condition;
  }


  /**
   * @return
   */
  public Activity getFromActivity()
  {
    return m_fromActivity;
  }


  /**
   * @return
   */
  public Activity getToActivity()
  {
    return m_toActivity;
  }


  /**
   * @return
   */
  public ExecutionType getExecution()
  {
    return m_execution;
  }


  /**
   * @return   Description of the Return Value
   * @see      de.objectcode.canyon.model.IValidatable#validate()
   */
  public ValidationErrors validate()
  {
    ValidationErrors  errors  = new ValidationErrors();

    m_toActivity = m_container.getActivity( m_to );

    if ( m_toActivity == null ) {
      errors.addMessage( "transition.undefined.to", new Object[]{m_id, m_to} );
    } else {
      m_toActivity.addInboundTransition( this );
    }

    m_fromActivity = m_container.getActivity( m_from );

    if ( m_fromActivity == null ) {
      errors.addMessage( "transition.undefined.from", new Object[]{m_id, m_from} );
    } else {
      m_fromActivity.addOutboundTransition( this );
    }

    if ( m_extendedAttributes.containsKey( EXECUTION ) ) {
      ExtendedAttribute  attr  = ( ExtendedAttribute ) m_extendedAttributes.get( EXECUTION );

      m_execution = ExecutionType.fromString( attr.getValue().toUpperCase() );
    }

    return errors;
  }

}
