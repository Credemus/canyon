/*
 * Generated file - Do not edit!
 */
package de.objectcode.canyon.jmx.bpe.admin;

/**
 * MBean interface.
 * @author junglas
 * @created 14. Juli 2004
 */
public interface ProcessAdminMBean {

  void setBPEJndiName(java.lang.String jndiName) throws java.lang.Exception;

  java.lang.String getBPEJndiName() ;

   /**
    * Description of the Method
    * @exception Exception Description of the Exception    */
  void start() throws java.lang.Exception;

  void stop() throws java.lang.Exception;

   /**
    * Description of the Method
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String engineStatus() throws java.lang.Exception;

   /**
    * Description of the Method
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String listProcesses() throws java.lang.Exception;

   /**
    * Description of the Method
    * @param processId Description of the Parameter
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String showProcessXML(java.lang.String processId) throws java.lang.Exception;

   /**
    * Description of the Method
    * @param onlyOpen Description of the Parameter
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String listProcessInstances(boolean onlyOpen) throws java.lang.Exception;

   /**
    * Description of the Method
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String migrateProcessInstances() throws java.lang.Exception;

   /**
    * Description of the Method
    * @param processInstanceId Description of the Parameter
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String showProcessInstance(java.lang.String processInstanceId) throws java.lang.Exception;

   /**
    * Description of the Method
    * @param processInstanceId Description of the Parameter
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String showProcessInstanceXML(java.lang.String processInstanceId) throws java.lang.Exception;

   /**
    * Description of the Method
    * @param messageOperation Description of the Parameter
    * @param userId Description of the Parameter
    * @param clientId Description of the Parameter
    * @exception Exception Description of the Exception    */
  void sendMessage(java.lang.String userId,java.lang.String clientId,java.lang.String messageOperation) throws java.lang.Exception;

   /**
    * Description of the Method
    * @param messageXML Description of the Parameter
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String sendMessageXML(java.lang.String messageXML) throws java.lang.Exception;

   /**
    * Description of the Method
    * @param messageXML Description of the Parameter
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String restartActivity(java.lang.String processInstanceId,java.lang.String activityId) throws java.lang.Exception;

   /**
    * Description of the Method
    * @param processInstanceId Description of the Parameter
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String terminateProcessInstance(java.lang.String processInstanceId) throws java.lang.Exception;

   /**
    * Description of the Method
    * @param processInstanceId Description of the Parameter
    * @param variableName
    * @param value
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String updateProcessInstance(java.lang.String processInstanceId,java.lang.String variableName,java.lang.String value) throws java.lang.Exception;

   /**
    * Description of the Method
    * @param processInstanceId Description of the Parameter
    * @param variableName
    * @param value
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String sendEvent(java.lang.String clientId,java.lang.String engineId,java.lang.String userId,java.lang.String eventType,java.lang.String action,java.lang.String processId,java.lang.String param1Name,java.lang.String param1Value,java.lang.String param2Name,java.lang.String param2Value,java.lang.String param3Name,java.lang.String param3Value,java.lang.String param4Name,java.lang.String param4Value) ;

   /**
    * Description of the Method
    * @param processInstanceId Description of the Parameter
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String showProcessInstance(java.lang.String processInstanceId,java.lang.String url) throws java.lang.Exception;

   /**
    * Description of the Method
    * @param onlyOpen Description of the Parameter
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String turnEventLoggingOn() throws java.lang.Exception;

   /**
    * Description of the Method
    * @param processInstanceId Description of the Parameter
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String reanimateProcessInstances(boolean readOnly) throws java.lang.Exception;

}
