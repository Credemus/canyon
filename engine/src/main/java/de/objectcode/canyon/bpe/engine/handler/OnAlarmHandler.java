package de.objectcode.canyon.bpe.engine.handler;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.ActivityState;
import de.objectcode.canyon.bpe.engine.evaluator.IExpression;
import de.objectcode.canyon.bpe.util.DeadlineHelper;
import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.model.process.Duration;
import de.objectcode.canyon.model.process.DurationUnit;

/**
 * @author    junglas
 * @created   9. September 2004
 */
public class OnAlarmHandler extends BaseHandler implements IAlarmReceiver
{
  static final long serialVersionUID = -1602393932220806987L;
  
  private final static Log log = LogFactory.getLog(OnAlarmHandler.class);
  
  protected            IExpression  m_expression;
  protected            long         m_forDuration;
  protected            Date         m_untilDate;
  protected						 DurationUnit m_defaultDurationUnit = DurationUnit.DAY;
  protected						 Duration			m_duration;
  protected transient  long         m_currentUntil;
  protected transient  boolean      m_active;
  protected transient  boolean      m_doNotReactOnDeactivate=false;


  /**
   *Constructor for the OnAlarmHandler object
   *
   * @param expr  Description of the Parameter
   */
  public OnAlarmHandler( IExpression expr, DurationUnit defaultDurationUnit )
  {
    m_expression = expr;
    m_untilDate = null;
    m_currentUntil = Long.MAX_VALUE;
    m_defaultDurationUnit = defaultDurationUnit;
    m_active = false;
  }


  /**
   *Constructor for the OnAlarmHandler object
   *
   * @param forDuration  Description of the Parameter
   */
  public OnAlarmHandler( Duration forDuration, DurationUnit defaultDurationUnit )
  {
//    m_forDuration = forDuration;
  	m_duration = forDuration;
  	m_defaultDurationUnit = defaultDurationUnit;
    m_untilDate = null;
  }


  /**
   *Constructor for the OnAlarmHandler object
   *
   * @param untilDate  Description of the Parameter
   */
  public OnAlarmHandler( Date untilDate )
  {
    m_forDuration = -1L;
    m_untilDate = untilDate;
  }


  /**
   * @return   The elementName value
   * @see      de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName()
  {
    return "onAlarm";
  }


  /**
   * @return   The nonBlocked value
   * @see      de.objectcode.canyon.bpe.engine.activities.IActivityContainer#isNonBlocked()
   */
  public boolean isNonBlocked()
  {
    return false;
  }


  /**
   * @return   The active value
   * @see      de.objectcode.canyon.bpe.engine.handler.IAlarmReceiver#isActive()
   */
  public boolean isActive()
  {
    return m_active;
  }


  /**
   * Gets the alarmTime attribute of the OnAlarmHandler object
   *
   * @return   The alarmTime value
   */
  public long getAlarmTime()
  {
    return m_currentUntil;
  }


  protected void computeAlarmTime() throws EngineException {
    m_currentUntil = DeadlineHelper.computeDeadline(m_scope, m_expression, m_untilDate, m_duration, m_defaultDurationUnit);  	
  }
  
  /**
   * Description of the Method
   *
   * @exception EngineException  Description of the Exception
   */
  public void activate()
    throws EngineException
  {
    m_active = true;
    computeAlarmTime();
    if (log.isInfoEnabled()) {
      log.info("Activating alarm:" + new Date(m_currentUntil));
    }    
    if (m_currentUntil == Long.MAX_VALUE)
        m_active = false;
    
    if ( m_activity.getState() == ActivityState.COMPLETED || 
        m_activity.getState() == ActivityState.ABORT || 
        m_activity.getState() == ActivityState.SKIPED )
      m_activity.reopen();
    
  }

  public void deactivate()
  	throws EngineException
  {
    if (log.isDebugEnabled()) {
      log.debug("Dectivating alarm: doNotReact=" + m_doNotReactOnDeactivate);
    }    
    if (!m_doNotReactOnDeactivate) {
	    m_active = false;
	    if ( m_activity.getState() == ActivityState.OPEN ) {
	      m_activity.deactivate();
	    }
    }
  }

  /**
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.handler.IAlarmReceiver#onAlarm()
   */
  public void onAlarm()
    throws EngineException
  {
    if (log.isInfoEnabled()) {
      log.info("onAlarm:" + new Date(m_currentUntil));
    }
    m_active = false;
    // turn off deactivation
    // m_scope.handlerFired() 
    // => m_scope.m_activity.abort() 
    // => m_scope.m_activity.m_parent=m_scope.abort() 
    // => m_scope.deactivate => this.deactivate() => m_activity.deactivate()
    // => m_activty.m_state = DEACT
    // SO (*) will fail otherwiese
    m_activity.activate();
    
    m_doNotReactOnDeactivate=true;
    m_scope.handlerFired();
    m_doNotReactOnDeactivate=false;
  }


  /**
   * @param out              Description of the Parameter
   * @exception IOException  Description of the Exception
   * @see                    de.objectcode.canyon.bpe.util.IStateHolder#dehydrate(java.io.ObjectOutput)
   */
  public void dehydrate( HydrationContext context, ObjectOutput out )
    throws IOException
  {
    super.dehydrate( context, out );

    out.writeBoolean( m_active );
    out.writeLong( m_currentUntil );
  }


  /**
   * @param in               Description of the Parameter
   * @exception IOException  Description of the Exception
   * @see                    de.objectcode.canyon.bpe.util.IStateHolder#hydrate(java.io.ObjectInput)
   */
  public void hydrate( HydrationContext context, ObjectInput in )
    throws IOException
  {
    super.hydrate( context, in );

    m_active = in.readBoolean();
    m_currentUntil = in.readLong();
  }


  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    super.toDom( element );

    element.addAttribute( "active", String.valueOf( m_active ) );
    element.addAttribute( "current-until", String.valueOf( m_currentUntil ) );
    if ( m_expression != null ) {
      m_expression.toDom( element.addElement( m_expression.getElementName() ) );
    } else if ( m_untilDate != null ) {
      element.addAttribute( "until", m_untilDate.toGMTString() );
    } else {
      element.addAttribute( "for", String.valueOf( m_forDuration ) );
    }

  }

}
