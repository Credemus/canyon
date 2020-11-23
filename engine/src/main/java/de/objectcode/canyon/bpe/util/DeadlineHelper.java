/*
 * Created on 07.10.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.objectcode.canyon.bpe.util;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Scope;
import de.objectcode.canyon.bpe.engine.evaluator.IExpression;
import de.objectcode.canyon.bpe.engine.handler.OnAlarmHandler;
import de.objectcode.canyon.model.process.Duration;
import de.objectcode.canyon.model.process.DurationUnit;
import de.objectcode.canyon.spi.calendar.IBusinessCalendar;

/**
 * @author xylander
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DeadlineHelper {
  
  private final static Log log = LogFactory.getLog(OnAlarmHandler.class);
  
  
  public static long computeDeadline(Scope scope, long start, Duration forDuration, DurationUnit defaultDurationUnit) throws EngineException {
    IBusinessCalendar calendar = scope.getEngine().getBusinessCalendar();
    String client = scope.getProcess().getClientId();
    return calendar.add(client, start ,Duration.getQualifiedDuration(forDuration,defaultDurationUnit));
  }
  
  public static long computeDeadline(Scope scope, IExpression expression, DurationUnit defaultDurationUnit) throws EngineException {
    IBusinessCalendar calendar = scope.getEngine().getBusinessCalendar();
    String client = scope.getProcess().getClientId();
    long currentUntil = Long.MAX_VALUE;
      Object value = expression.eval( scope );
      if ( value != null ) {
      	if (value instanceof Date) {
      		currentUntil = ((Date) value).getTime();
      	} else {
      		Duration duration = null;
      		if (value instanceof Duration)
      			duration = (Duration) value;
      		else 
      			duration = Duration.parse(value.toString());
      		if (duration.isAbsolute())
      			currentUntil = duration.getMillis(defaultDurationUnit);
      		else
      			currentUntil = calendar.add(client, System.currentTimeMillis(),Duration.getQualifiedDuration(duration,defaultDurationUnit));
      	}
      }
      else {
        log.warn("Expression '" + expression + "' has null result");
      }
    return currentUntil;
  }

  public static long computeDeadline(Scope scope, Date untilDate) throws EngineException {
    return untilDate.getTime();
  }
  
  public static long computeDeadline(Scope scope, IExpression expression, Date untilDate, Duration forDuration, DurationUnit defaultDurationUnit) throws EngineException {
    if ( expression != null ) {
    	return computeDeadline(scope,expression,defaultDurationUnit);
    } else if ( untilDate != null ) {
      return computeDeadline(scope, untilDate);
    } else {
    	return computeDeadline(scope,System.currentTimeMillis(), forDuration, defaultDurationUnit);
    }
  }
}
