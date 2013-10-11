package de.objectcode.canyon.persistent.jmx;

import javax.management.ObjectName;

/**
 * @author junglas @created 16. Oktober 2003
 */
public interface WSRepositoriesMBean
{
  /**
   * @return
   */
  public String getServiceManagerJndiName ( );

  /**
   * @param string
   */
  public void setServiceManagerJndiName ( String string );

  /**
   * @return
   */
  public String getHibernateJndiName ( );

  /**
   * @param string
   */
  public void setHibernateJndiName ( String string );

  /**
   * Description of the Method
   * 
   * @exception Exception
   *              Description of the Exception
   */
  public void start ( ) throws Exception;

  /**
   * Description of the Method
   * 
   * @exception Exception
   *              Description of the Exception
   */
  public void stop ( ) throws Exception;

  /**
   * @return
   */
  public ObjectName getTimerService ( );

  /**
   * @return Returns the asyncInterval.
   */
  public int getAsyncInterval ( );

  /**
   * @param asyncInterval
   *          The asyncInterval to set.
   */
  public void setAsyncInterval ( int asyncInterval );

  /**
   * @return Returns the notificationInterval.
   */
  public int getNotificationInterval ( );

  /**
   * @param notificationInterval
   *          The notificationInterval to set.
   */
  public void setNotificationInterval ( int notificationInterval );

  /**
   * @param name
   */
  public void setTimerService ( ObjectName name );

  /**
   * @return Returns the timerBasedAsyncManager.
   */
  public boolean isTimerBasedAsyncManager ( );

  /**
   * @param timerBasedAsyncManager The timerBasedAsyncManager to set.
   */
  public void setTimerBasedAsyncManager ( boolean timerBasedAsyncManager );
}
