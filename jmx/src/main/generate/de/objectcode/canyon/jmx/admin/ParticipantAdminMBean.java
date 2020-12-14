/*
 * Generated file - Do not edit!
 */
package de.objectcode.canyon.jmx.admin;

/**
 * MBean interface.
 * @author Ruth
 * @created 26.03.2004
 */
public interface ParticipantAdminMBean {

  void setJndiName(java.lang.String string) ;

  java.lang.String getJndiName() ;

  void start() throws java.lang.Exception;

  void stop() throws java.lang.Exception;

   /**
    * Description of the Method
    * @param processDefinitionId Description of the Parameter
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String getUser(java.lang.String workItemId) throws java.lang.Exception;

}
