/*
 * Generated by XDoclet - Do not edit!
 */
package de.objectcode.canyon.api.worklist;

/**
 * Local interface for Participant.
 * @author ruth
 * @created 11. Dezember 2003
 */
public interface ParticipantLocal
   extends javax.ejb.EJBLocalObject
{

   public java.lang.String[] findParticipants( de.objectcode.canyon.api.worklist.WorkItemData workItemData ) throws org.wfmc.wapi.WMWorkflowException;

   public java.lang.String[] findMembers( java.lang.String processInstanceId,java.lang.String participantId,java.lang.String clientId ) throws org.wfmc.wapi.WMWorkflowException;

}