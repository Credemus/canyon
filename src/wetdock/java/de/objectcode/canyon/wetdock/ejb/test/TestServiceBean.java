package de.objectcode.canyon.wetdock.ejb.test;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.wetdock.user.IUserManager;

/**
 * @ejb.bean name="WetdockTestService" type="Stateless"
 *           jndi-name="de/objectcode/canyon/wetdock/ejb/test/WetdockTestService"
 *           local-jndi-name="de/objectcode/canyon/wetdock/ejb/test/WetdockTestServiceLocal"
 *           transaction-type="Container" view-type="both"
 * @ejb.permission unchecked="true"
 * 
 * @author junglas
 */
public class TestServiceBean implements SessionBean
{
  protected transient Log             m_log;
  protected SessionContext            m_ctx;

  /**
   * @ejb.interface-method
   * @ejb.transaction type="Required"
   **/
  public String testMethod1 ( String param )
    throws Exception
  {
    return TestService.getInstance().testMethod1Ctx(m_ctx, param);
  }
  
  /**
   * @ejb.interface-method
   */
  public String testMethod2 ( String param )
    throws Exception
  {
    return TestService.getInstance().testMethod2(param);
  }

  /**
   * @ejb.interface-method
   */
  public void setWaitDuration ( long waitDuration )
  {
    TestService.getInstance().setWaitDuration(waitDuration);
  }

  /**
   * @ejb.interface-method
   */
  public void setExceptionOn ( boolean exceptionOn )
  {
    TestService.getInstance().setExceptionOn(exceptionOn);
  }
  
  /**
   * @ejb.interface-method
   */
  public void setExceptionOn ( int exceptionOn )
  {
    TestService.getInstance().setExceptionOn(exceptionOn);
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
