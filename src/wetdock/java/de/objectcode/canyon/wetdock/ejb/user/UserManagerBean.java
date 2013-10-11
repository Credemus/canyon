package de.objectcode.canyon.wetdock.ejb.user;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.wetdock.api.user.ClientData;
import de.objectcode.canyon.wetdock.api.user.RoleData;
import de.objectcode.canyon.wetdock.api.user.UserData;
import de.objectcode.canyon.wetdock.user.IUserManager;

/**
 * @ejb.bean name="WetdockUserManager" type="Stateless"
 *           jndi-name="de/objectcode/canyon/wetdock/ejb/user/WetdockUserManager"
 *           local-jndi-name="de/objectcode/canyon/wetdock/ejb/user/WetdockUserManagerLocal"
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
public class UserManagerBean implements SessionBean
{
  private final static String      USER_MANAGER_COMP_NAME = "java:comp/env/UserManager";

  protected transient Log          m_log;
  protected SessionContext         m_ctx;

  protected transient IUserManager m_userManager;

  /**
   * @ejb.interface-method
   */
  public UserData getUser( String userId )
      throws WMWorkflowException
  {
    try {
      return m_userManager.getUser( userId );
    } catch (Exception e) {
      m_log.error( "Exception", e );
      throw new WMWorkflowException ( e );
    }
  }

  /**
   * @ejb.interface-method
   */
  public void createUser( UserData userData )
      throws WMWorkflowException
  {
    try {
      m_userManager.createUser( userData );
    } catch (Exception e) {
      m_log.error( "Exception", e );
      throw new WMWorkflowException ( e );
    }
  }

  /**
   * @ejb.interface-method
   */
  public RoleData getRole( String roleId )
      throws WMWorkflowException
  {
    try {
      return m_userManager.getRole( roleId );
    } catch (Exception e) {
      m_log.error( "Exception", e );
      throw new WMWorkflowException ( e );
    }
  }

  /**
   * @ejb.interface-method
   */
  public void createRole( RoleData roleData )
      throws WMWorkflowException
  {
    try {
      m_userManager.createRole( roleData );
    } catch (Exception e) {
      m_log.error( "Exception", e );
      throw new WMWorkflowException ( e );
    }
  }

  /**
   * @ejb.interface-method
   */
  public ClientData getClient( String clientId )
      throws WMWorkflowException
  {
    try {
      return m_userManager.getClient( clientId );
    } catch (Exception e) {
      m_log.error( "Exception", e );
      throw new WMWorkflowException ( e );
    }
  }

  /**
   * @ejb.interface-method
   */
  public void createClient( ClientData clientData )
      throws WMWorkflowException
  {
    try {
      m_userManager.createClient( clientData );
    } catch (Exception e) {
      m_log.error( "Exception", e );
      throw new WMWorkflowException ( e );
    }
  }

  /**
   * @ejb.interface-method
   */
  public void addUserRole( String userId, String roleId, String clientId )
      throws WMWorkflowException
  {
    try {
      m_userManager.addUserRole( userId, roleId, clientId );
    } catch (Exception e) {
      m_log.error( "Exception", e );
      throw new WMWorkflowException ( e );
    }
  }

  /**
   * @ejb.interface-method
   */
  public void removeUserRole( String userId, String roleId, String clientId )
      throws WMWorkflowException
  {
    try {
      m_userManager.removeUserRole( userId, roleId, clientId );
    } catch (Exception e) {
      m_log.error( "Exception", e );
      throw new WMWorkflowException ( e );
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
