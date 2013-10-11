package de.objectcode.canyon.persistent.jmx;

import javax.management.ObjectName;

/**
 * @author junglas
 */
public interface BPERepositoriesMBean
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

  public void setBpeEngineJndiName( String bpeEngineJndiName );

  public String getBpeEngineJndiName( );
  
  /**
   * @return
   */
  public ObjectName getTimerService ( );
  
  /**
   * @param name
   */
  public void setTimerService ( ObjectName name );

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

}
