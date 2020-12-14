/*
 * Generated file - Do not edit!
 */
package de.objectcode.canyon.jmx.bpe;

/**
 * MBean interface.
 * @author junglas
 * @created 12. Juli 2004
 */
public interface BPEEngineServiceMBean {

  void setTimerService(javax.management.ObjectName timerService) ;

  void setNotificationInterval(int notificationInterval) ;

  void setServiceManagerJndiName(java.lang.String serviceManagerJndiName) ;

  void setJndiName(java.lang.String jndiName) throws java.lang.Exception;

  javax.management.ObjectName getTimerService() ;

  int getNotificationInterval() ;

  java.lang.String getServiceManagerJndiName() ;

  java.lang.String getJndiName() ;

  void start() throws java.lang.Exception;

  void stop() throws java.lang.Exception;

  java.lang.String dumpAllLocks() throws java.lang.Exception;

  java.lang.String getVersion() ;

}
