package de.objectcode.canyon.bpe.engine.activities;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.evaluator.IExpression;
import de.objectcode.canyon.bpe.engine.handler.IAlarmReceiver;
import de.objectcode.canyon.bpe.util.DeadlineHelper;
import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.model.process.Duration;
import de.objectcode.canyon.model.process.DurationUnit;

/**
 * @author junglas
 * @created 9. Juni 2004
 */
public class Wait extends Activity implements IAlarmReceiver {
  final static long serialVersionUID = -3882893221791467285L;

  private final static Log log = LogFactory.getLog(Wait.class);

  private long m_alarmTime;
  private IExpression m_expression;
  protected long m_forDuration; // NOT USED. DO NOT DELETE FOR BACKWARD SERIALIZABILITY
  protected Date m_untilDate;
  protected DurationUnit m_defaultDurationUnit = DurationUnit.DAY;
  protected Duration m_duration;

  /**
   * Constructor for the Wait object
   *
   * @param name  Description of the Parameter
   * @param scope Description of the Parameter
   */
  public Wait(String name, Scope scope) {
    super(name, scope);

    m_scope.getProcess().addAlarmReceiver(this);
  }

  public void setExpression(IExpression forExpression, DurationUnit defaultDurationUnit) {
    m_expression = forExpression;
    m_defaultDurationUnit = defaultDurationUnit;
  }


  /**
   * Gets the elementName attribute of the Wait object
   *
   * @return The elementName value
   */
  public String getElementName() {
    return "wait";
  }

  /**
   * @see de.objectcode.canyon.bpe.engine.handler.IAlarmReceiver#isActive()
   */
  public boolean isActive() {
    return m_state == ActivityState.RUNNING;
  }

  /**
   * @see de.objectcode.canyon.bpe.engine.handler.IAlarmReceiver#getAlarmTime()
   */
  public long getAlarmTime() {
    return m_alarmTime;
  }

  /**
   * @see de.objectcode.canyon.bpe.engine.handler.IAlarmReceiver#onAlarm()
   */
  public void onAlarm()
          throws EngineException {
    if (log.isInfoEnabled()) {
      log.info("wait onAlarm:" + new Date(m_alarmTime));
    }
    complete();
  }

  /**
   * @see de.objectcode.canyon.bpe.engine.activities.Activity#start()
   */
  public void start()
          throws EngineException {
    super.start();

    m_alarmTime = DeadlineHelper.computeDeadline(m_scope, m_expression, m_untilDate, m_duration, m_defaultDurationUnit);
    if (log.isInfoEnabled()) {
      log.info("Starting wait alarm:" + new Date(m_alarmTime));
    }

  }

  public void dehydrate(HydrationContext context, ObjectOutput out)
          throws IOException {
    super.dehydrate(context, out);

    out.writeLong(m_alarmTime);
  }

  public void hydrate(HydrationContext context, ObjectInput in)
          throws IOException {
    super.hydrate(context, in);

    m_alarmTime = in.readLong();
  }

  public void toDom(Element element) {
    super.toDom(element);

    if (m_expression != null) {
      m_expression.toDom(element.addElement(m_expression.getElementName()));
    } else if (m_untilDate != null) {
      element.addAttribute("until", m_untilDate.toGMTString());
    } else {
      element.addAttribute("for", String.valueOf(m_duration));
    }
  }

  public long getForDuration() {
    return m_forDuration;
  }

  public void setForDuration(Duration duration, DurationUnit defaultDurationUnit) {
//		m_forDuration = forDuration;
    m_duration = duration;
    m_defaultDurationUnit = defaultDurationUnit;
  }

  public Date getUntilDate() {
    return m_untilDate;
  }

  public void setUntilDate(Date untilDate) {
    m_untilDate = untilDate;
  }
}
