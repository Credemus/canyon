package de.objectcode.canyon.bpe.engine.event;

/**
 * @author junglas
 */
public interface IActivtyStateListener
{
  public void activityStarted ( ActivityStateEvent event );
  
  public void activityCompleted ( ActivityStateEvent event );
  
  public void activityAborted ( ActivityStateEvent event );
  
  public void activityReopened ( ActivityStateEvent event );
}
