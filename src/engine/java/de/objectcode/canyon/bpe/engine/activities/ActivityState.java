package de.objectcode.canyon.bpe.engine.activities;

import java.io.Serializable;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * State of an activity.
 * Currently there are six activity states:
 * <table>
 *   <tr><td>OPEN</td><td>The activity has not started yet</td></tr>
 *   <tr><td>ACTIVATED</td><td>The activity is about to be started (see startActivatedActivities in BPEProcess)</td></tr>
 *   <tr><td>DEACTIVATED</td><td>The activity is about to be skiped (see startActivatedActivities in BPEProcess)</td></tr>
 *   <tr><td>RUNNING</td><td>The activity has been started and is waiting for some event</td></tr>
 *   <tr><td>COMPLETED</td><td>The actvity has been completed</td></tr>
 *   <tr><td>ABORTED</td><td>The activity has been aborted due to some error</td></tr>
 *   <tr><td>SKIPED</td><td>The activity has been skiped since the join condition failed</td></tr>
 * </table>
 *
 * Allowed transitions:
 * <table>
 *   <tr><th>From</th><th>To</th></tr>
 *   <tr>OPEN</tr><tr>ACTIVATED</tr>
 *   <tr>ACTIVATED</tr><tr>STARTED</tr>
 *   <tr>DEACTIVATED</tr><tr>SKIPED</tr>
 *   <tr>STARTED</tr><tr>COMPLETED, ABORTED</tr>
 *   <tr>COMPLETED</tr><tr>OPEN</tr>
 *   <tr>ABORTED</tr><tr>OPEN</tr>
 *   <tr>SKIPED</tr><tr>OPEN</tr>
 * <table>
 *
 * @author    junglas
 * @created   7. Juni 2004
 */
public class ActivityState extends AbstractEnum implements Serializable
{
  final static          long             serialVersionUID  = -7705973957625135590L;

  public final static   int              OPEN_INT          = 0;
  public final static   int              ACTIVATED_INT     = 1;
  public final static   int              DEACTIVATED_INT   = 2;
  public final static   int              RUNNING_INT       = 3;
  public final static   int              COMPLETED_INT     = 4;
  public final static   int              ABORT_INT         = 5;
  public final static   int              SKIPED_INT        = 6;
  public final static   ActivityState    OPEN              = new ActivityState( OPEN_INT, "OPEN" );
  public final static   ActivityState    ACTIVATED         = new ActivityState( ACTIVATED_INT, "ACTIVATED" );
  public final static   ActivityState    DEACTIVATED       = new ActivityState( DEACTIVATED_INT, "DEACTIVATED" );
  public final static   ActivityState    RUNNING           = new ActivityState( RUNNING_INT, "RUNNING" );
  public final static   ActivityState    COMPLETED         = new ActivityState( COMPLETED_INT, "COMPLETED" );
  public final static   ActivityState    ABORT             = new ActivityState( ABORT_INT, "ABORT" );
  public final static   ActivityState    SKIPED            = new ActivityState( SKIPED_INT, "SKIPED" );

  private final static  ActivityState[]  g_values          = {
      OPEN,
      ACTIVATED,
      DEACTIVATED,
      RUNNING,
      COMPLETED,
      ABORT,
      SKIPED
      };


  /**
   *Constructor for the ActivityState object
   *
   * @param value  Description of the Parameter
   * @param tag    Description of the Parameter
   */
  private ActivityState( int value, String tag )
  {
    super( value, tag );
  }


  /**
   * @return   Description of the Return Value
   * @see      de.objectcode.canyon.model.AbstractEnum#readResolve()
   */
  public Object readResolve()
  {
    return fromInt( m_value );
  }


  /**
   * Description of the Method
   *
   * @param tag  Description of the Parameter
   * @return     Description of the Return Value
   */
  public static ActivityState fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }


  /**
   * Description of the Method
   *
   * @param value  Description of the Parameter
   * @return       Description of the Return Value
   */
  public static ActivityState fromInt( int value )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getValue() == value ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( String.valueOf( value ) );
  }

}
