/*
 * Generated by XDoclet - Do not edit!
 */
package de.objectcode.canyon.api.bpe;

/**
 * Home interface for BPEProcess.
 * @author junglas
 * @created 13. Juli 2004
 */
public interface BPEProcessHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/BPEProcess";
   public static final String JNDI_NAME="de/objectcode/canyon/ejb/bpe/Process";

   public de.objectcode.canyon.api.bpe.BPEProcess create()
      throws javax.ejb.CreateException,java.rmi.RemoteException;

}
