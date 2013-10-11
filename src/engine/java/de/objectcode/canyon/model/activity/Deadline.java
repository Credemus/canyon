package de.objectcode.canyon.model.activity;

import java.io.Serializable;
import java.util.Iterator;

import de.objectcode.canyon.model.IValidatable;
import de.objectcode.canyon.model.ValidationErrors;
import de.objectcode.canyon.model.process.Duration;
import de.objectcode.canyon.model.transition.ConditionType;
import de.objectcode.canyon.model.transition.Transition;

/**
 * @author    junglas
 * @created   21. November 2003
 */
public class Deadline implements Serializable, IValidatable
{
  static final long serialVersionUID = 2220258712427817964L;

  private  Duration       m_deadlineCondition;
  private  String         m_exceptionName;
  private  ExecutionType  m_executionType;
  private  Activity       m_activity;
  private  Transition     m_transition;


  /**
   * @param string
   */
  public void setExceptionName( String string )
  {
    m_exceptionName = string;
  }

  public void setExecution ( String execution )
  {
    m_executionType = ExecutionType.fromString(execution);
  }

  /**
   * @param type
   */
  public void setExecutionType( ExecutionType type )
  {
    m_executionType = type;
  }


  /**
   * @param duration
   */
  public void setDeadlineCondition( Duration duration )
  {
    m_deadlineCondition = duration;
  }


  /**
   * @param activity
   */
  public void setActivity( Activity activity )
  {
    m_activity = activity;
  }



  /**
   * @return
   */
  public String getExceptionName()
  {
    return m_exceptionName;
  }


  /**
   * @return
   */
  public ExecutionType getExecutionType()
  {
    return m_executionType;
  }


  /**
   * @return
   */
  public Duration getDeadlineCondition()
  {
    return m_deadlineCondition;
  }


  /**
   * @return
   */
  public Activity getActivity()
  {
    return m_activity;
  }


  /**
   * @return
   */
  public Transition getTransition()
  {
    return m_transition;
  }


  /**
   * @return   Description of the Return Value
   * @see      de.objectcode.canyon.model.IValidatable#validate()
   */
  public ValidationErrors validate()
  {
    ValidationErrors  errors  = new ValidationErrors();

    Iterator          it      = m_activity.getOutboundTransitions().values().iterator();

    while ( it.hasNext() ) {
      Transition  transition  = ( Transition ) it.next();

      if ( transition.getCondition() != null &&
          transition.getCondition().getType() == ConditionType.EXCEPTION &&
          m_exceptionName.equals( transition.getCondition().getValue() ) ) {
        m_transition = transition;
        break;
      }
    }

    if ( m_transition == null ) {
      errors.addMessage( "activity.deadline.noTransition", new Object[]{m_activity.getId(), m_exceptionName} );
    }

    return errors;
  }

}
