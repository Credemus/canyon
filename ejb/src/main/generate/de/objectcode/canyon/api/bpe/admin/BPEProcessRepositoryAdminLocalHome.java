/*
 * Generated by XDoclet - Do not edit!
 */
package de.objectcode.canyon.api.bpe.admin;

/**
 * Local home interface for BPEProcessRepositoryAdmin.
 * @author junglas
 * @created 20. Juni 2003
 */
public interface BPEProcessRepositoryAdminLocalHome
   extends javax.ejb.EJBLocalHome
{
   public static final String COMP_NAME="java:comp/env/ejb/BPEProcessRepositoryAdminLocal";
   public static final String JNDI_NAME="de/objectcode/canyon/ejb/bpe/admin/ProcessRepositoryAdminLocal";

   public de.objectcode.canyon.api.bpe.admin.BPEProcessRepositoryAdminLocal create()
      throws javax.ejb.CreateException;

}
