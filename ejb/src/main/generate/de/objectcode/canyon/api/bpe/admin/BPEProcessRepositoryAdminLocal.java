/*
 * Generated by XDoclet - Do not edit!
 */
package de.objectcode.canyon.api.bpe.admin;

/**
 * Local interface for BPEProcessRepositoryAdmin.
 * @author junglas
 * @created 20. Juni 2003
 */
public interface BPEProcessRepositoryAdminLocal
   extends javax.ejb.EJBLocalObject
{

   public void createPackage( de.objectcode.canyon.model.WorkflowPackage pkg ) throws org.wfmc.wapi.WMWorkflowException;

   public void createPackage( byte[] content,java.lang.String contentType ) throws org.wfmc.wapi.WMWorkflowException;

   public void createOrReplacePackage( byte[] content,java.lang.String contentType ) throws org.wfmc.wapi.WMWorkflowException;

}
