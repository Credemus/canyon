/*
 * Generated by XDoclet - Do not edit!
 */
package de.objectcode.canyon.api.worklist;

/**
 * Home interface for Worklist.
 * @author junglas
 * @created 5. Dezember 2003
 */
public interface WorklistHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/Worklist";
   public static final String JNDI_NAME="de/objectcode/canyon/ejb/worklist/Worklist";

   public de.objectcode.canyon.api.worklist.Worklist create()
      throws javax.ejb.CreateException,java.rmi.RemoteException;

}