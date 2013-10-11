
package de.objectcode.canyon.wetdock.jmx;

/**
 * @author junglas
 */
public interface WetdockRepositoriesMBean
{
  public String getHibernateJndiName();

  public void setHibernateJndiName( String hibernateJndiName );
  
  public String getServiceManagerJndiName();
  
  public void setServiceManagerJndiName( String serviceManagerJndiName );
  
  public String getUserManagerJndiName();
  
  public void setUserManagerJndiName( String userManagerJndiName );
  
  public void start() throws Exception;
  
  public void stop() throws Exception;
}
