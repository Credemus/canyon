package de.objectcode.canyon.bpe.engine.handler;

import de.objectcode.canyon.bpe.engine.EngineException;

/**
 * @author junglas
 */
public interface IAlarmReceiver {
  boolean isActive();

  long getAlarmTime();

  void onAlarm() throws EngineException;
}
