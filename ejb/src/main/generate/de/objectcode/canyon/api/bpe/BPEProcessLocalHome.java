/*
 * Generated by XDoclet - Do not edit!
 */
package de.objectcode.canyon.api.bpe;

/**
 * Local home interface for BPEProcess.
 * @author junglas
 * @created 13. Juli 2004
 */
public interface BPEProcessLocalHome
   extends javax.ejb.EJBLocalHome
{
   public static final String COMP_NAME="java:comp/env/ejb/BPEProcessLocal";
   public static final String JNDI_NAME="de/objectcode/canyon/ejb/bpe/ProcessLocal";

   public de.objectcode.canyon.api.bpe.BPEProcessLocal create()
      throws javax.ejb.CreateException;

}