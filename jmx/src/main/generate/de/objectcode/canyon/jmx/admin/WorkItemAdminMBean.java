/*
 * Generated file - Do not edit!
 */
package de.objectcode.canyon.jmx.admin;

/**
 * MBean interface.
 * @author junglas
 * @created 4. Februar 2004
 */
public interface WorkItemAdminMBean {

  void setJndiName(java.lang.String string) ;

  java.lang.String getJndiName() ;

  java.lang.String listWorkItems(java.lang.String processInstanceId,java.lang.String activityInstanceId) throws java.lang.Exception;

   /**
    * Description of the Method
    * @param workItemId Description of the Parameter
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String showWorkItem(java.lang.String workItemId) throws java.lang.Exception;

   /**
    * Description of the Method
    * @param workItemId Description of the Parameter
    * @param parameterName
    * @param value
    * @return Description of the Return Value
    * @exception Exception Description of the Exception    */
  java.lang.String updateWorkItem(java.lang.String workItemId,java.lang.String parameterName,java.lang.String value) throws java.lang.Exception;

  void completeWorkItem(java.lang.String workItemId) throws java.lang.Exception;

  boolean handleEvent(java.lang.String clientId,java.lang.String eventType,java.lang.String eventParameters) throws java.lang.Exception;

   /**
    * Description of the Method
    * @exception Exception Description of the Exception    */
  void start() throws java.lang.Exception;

  void stop() throws java.lang.Exception;

}
