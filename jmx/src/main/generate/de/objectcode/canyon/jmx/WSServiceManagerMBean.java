/*
 * Generated file - Do not edit!
 */
package de.objectcode.canyon.jmx;

/**
 * MBean interface.
 * @author junglas
 * @created 16. Oktober 2003
 */
public interface WSServiceManagerMBean {

  void setJndiName(java.lang.String jndiName) throws java.lang.Exception;

  void setAsyncJndiName(java.lang.String string) ;

  void setJmsBasedAsyncManager(boolean jmsBasedAsyncManager) ;

  java.lang.String getJndiName() ;

  java.lang.String getAsyncJndiName() ;

  boolean isJmsBasedAsyncManager() ;

  void start() throws java.lang.Exception;

  void stop() throws java.lang.Exception;

}
