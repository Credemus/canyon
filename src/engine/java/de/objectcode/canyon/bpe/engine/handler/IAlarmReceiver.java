package de.objectcode.canyon.bpe.engine.handler;

import de.objectcode.canyon.bpe.engine.EngineException;

/**
 * @author junglas
 */
public interface IAlarmReceiver
{
  public boolean isActive ();
  
  public long getAlarmTime ( );
  
  public void onAlarm ( ) throws EngineException;
}
