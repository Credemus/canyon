package de.objectcode.canyon.wetdock.ejb.setup;

import java.rmi.RemoteException;
import java.sql.PreparedStatement;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.wetdock.user.IUserManager;
import de.objectcode.canyon.worklist.IWorklistEngine;

/**
 * @ejb.bean name="WetdockSetup" type="Stateless"
 *           jndi-name="de/objectcode/canyon/wetdock/ejb/setup/WetdockSetup"
 *           local-jndi-name="de/objectcode/canyon/wetdock/ejb/setup/WetdockSetupLocal"
 *           transaction-type="Container" view-type="both"
 * @ejb.permission unchecked="true"
 * 
 * @ejb.resource-ref res-ref-name="ServiceManager"
 *                   res-type="de.objectcode.canyon.spi.ServiceManager"
 *                   res-auth="Application"
 * @jboss.resource-ref res-ref-name="ServiceManager"
 *                     jndi-name="java:/canyon/ServiceManager"
 * @ejb.resource-ref res-ref-name="UserManager"
 *                   res-type="de.objectcode.canyon.wetdock.user.IUserManager"
 *                   res-auth="Application"
 * @jboss.resource-ref res-ref-name="UserManager"
 *                     jndi-name="java:/canyon/wetdock/UserManager"
 * 
 * @author junglas
 */
public class SetupBean implements SessionBean
{
  private final static String         USER_MANAGER_COMP_NAME    = "java:comp/env/UserManager";
  private final static String         SERVICE_MANAGER_COMP_NAME = "java:comp/env/ServiceManager";
  private final static String         HIBERNATE_SESSION_JNDI_NAME = "java:/canyon/HibernateFactory";
  
  protected transient Log             m_log;
  protected SessionContext            m_ctx;

  protected transient IUserManager    m_userManager;
  protected transient ServiceManager  m_serviceManager;
  protected transient IWorklistEngine m_worklistEngine;

  /**
   * NEVEREVER use this in a production system.
   * 
   * @ejb.interface-method
   */
  public void clearRepositories ( boolean youSure, boolean youRealySure, boolean youRealyRealySure )
  {
    if ( !youSure || !youRealySure || !youRealyRealySure )
      return;
    
    try {
      m_userManager.clearRepository();
    }
    catch ( Exception e ) {
      m_log.error("Exception", e);
    }

    Session session = null;
    try {
      InitialContext ctx = new InitialContext();
      SessionFactory factory = (SessionFactory)ctx.lookup(HIBERNATE_SESSION_JNDI_NAME);
      
      session = factory.openSession();
      
//    session.delete("from o in class de.objectcode.canyon.persistent.bpe.repository.PBPEProcess");
      session.connection().prepareStatement("delete from PBPEPROCESSES").execute();
      session.delete("from o in class de.objectcode.canyon.persistent.bpe.repository.PBPEProcessInstance");
      session.delete("from o in class de.objectcode.canyon.persistent.instance.PWorkItem");
      session.delete("from o in class de.objectcode.canyon.persistent.participant.PProcessInstanceToParticipant");
      
      
      session.flush();
    }
    catch ( Exception e ) {
      m_log.error("Exception", e);
    }
    finally {
      try {
        if ( session != null )
          session.close();
      }
      catch ( Exception e ) {
      }
    }
  }
  
  /**
   * Create a new ZReihe SessionBean.
   * 
   * @exception CreateException
   *              on error
   * 
   * @ejb.create-method
   */
  public void ejbCreate()
      throws CreateException
  {
    m_log.debug( "Create" );
  }

  /**
   * @param ctx
   *          The new sessionContext value
   * @exception EJBException
   *              Description of the Exception
   * @exception RemoteException
   *              Description of the Exception
   * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
   */
  public void setSessionContext( SessionContext sessionCtx )
      throws EJBException, RemoteException
  {
    m_ctx = sessionCtx;
    if ( m_log == null ) {
      m_log = LogFactory.getLog( getClass() );
    }

    try {
      InitialContext ctx = new InitialContext();

      m_userManager = (IUserManager) ctx.lookup( USER_MANAGER_COMP_NAME );
      ServiceManager svcMgr = (ServiceManager) ctx.lookup( SERVICE_MANAGER_COMP_NAME );

      m_serviceManager = svcMgr;
      m_worklistEngine = svcMgr.getWorklistEngine();
    } catch (NamingException e) {
      m_log.fatal( "Exception", e );
    }
  }

  /**
   * @exception EJBException
   *              Description of the Exception
   * @exception RemoteException
   *              Description of the Exception
   * @see javax.ejb.SessionBean#ejbActivate()
   */
  public void ejbActivate()
      throws EJBException, RemoteException
  {
    if ( m_log == null ) {
      m_log = LogFactory.getLog( getClass() );
    }
    m_log.debug( "Activate" );

    try {
      InitialContext ctx = new InitialContext();

      m_userManager = (IUserManager) ctx.lookup( USER_MANAGER_COMP_NAME );
      ServiceManager svcMgr = (ServiceManager) ctx.lookup( SERVICE_MANAGER_COMP_NAME );

      m_serviceManager = svcMgr;
      m_worklistEngine = svcMgr.getWorklistEngine();
    } catch (NamingException e) {
      m_log.fatal( "Exception", e );
    }
  }

  /**
   * @exception EJBException
   *              Description of the Exception
   * @exception RemoteException
   *              Description of the Exception
   * @see javax.ejb.SessionBean#ejbPassivate()
   */
  public void ejbPassivate()
      throws EJBException, RemoteException
  {
    m_log.debug( "Passivate" );
  }

  /**
   * @exception EJBException
   *              Description of the Exception
   * @exception RemoteException
   *              Description of the Exception
   * @see javax.ejb.SessionBean#ejbRemove()
   */
  public void ejbRemove()
  {
    m_log.debug( "Remove" );
  }

}
