package de.objectcode.canyon.ejb;

import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.worklist.IWorklistEngine;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.Status;

/**
 * @ejb.bean generate="false"
 * @ejb.home generate="false"
 * @ejb.interface generate="false"
 *
 * @author    junglas
 * @created   16. Oktober 2003
 */
public class BaseServiceManagerBean extends BaseSessionBean
{
	static final long serialVersionUID = -137697717800823720L;
	
	private final static  String           SERVICE_MANAGER_COMP_NAME  = "java:comp/env/ServiceManager";
  protected transient   ServiceManager   m_serviceManager;
  protected transient   IWorklistEngine  m_worklistEngine;


  /**
   * Description of the Method
   */
  protected void obtainServiceManager()
  {
    m_log.debug( "obtainServiceManager" );

    try {
      InitialContext  ctx     = new InitialContext();

      ServiceManager  svcMgr  = ( ServiceManager ) ctx.lookup( SERVICE_MANAGER_COMP_NAME );

      m_serviceManager = svcMgr;
      m_worklistEngine = svcMgr.getWorklistEngine();
    }
    catch ( NamingException e ) {
      m_log.fatal( "Exception", e );
    }
  }


  /**
   * @exception EJBException     Description of the Exception
   * @exception RemoteException  Description of the Exception
   * @see                        javax.ejb.SessionBean#ejbActivate()
   */
  public void ejbActivate()
    throws EJBException, RemoteException
  {
    super.ejbActivate();

    obtainServiceManager();
  }


  /**
   * (non-Javadoc)
   *
   * @exception RepositoryException  Description of the Exception
   * @see                            javax.ejb.SessionSynchronization#afterBegin()
   */
  protected void beginTransaction()
    throws RepositoryException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug( "beginTransaction:" );
    }
    try {
      m_ctx.getUserTransaction().begin();
    }
    catch ( Exception e ) {
      m_log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    m_serviceManager.beginTransaction();
  }


  /**
   * (non-Javadoc)
   *
   * @see   javax.ejb.SessionSynchronization#beforeCompletion()
   */
  protected void rollbackTransaction()
  {
    try {
      int  status  = m_ctx.getUserTransaction().getStatus();

      if ( m_log.isDebugEnabled() ) {
        m_log.debug( "rollbackTransaction:" + status );
      }

      try {
        if ( status == Status.STATUS_ACTIVE || status == Status.STATUS_MARKED_ROLLBACK ) {
          m_serviceManager.beforeEndTransaction(false);
        }
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }

      try {
        if ( status == Status.STATUS_ACTIVE || status == Status.STATUS_MARKED_ROLLBACK ) {
          m_ctx.getUserTransaction().rollback();
        }
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }

      try {
        if ( status == Status.STATUS_ACTIVE || status == Status.STATUS_MARKED_ROLLBACK ) {
          m_serviceManager.afterEndTransaction(false);
	} else {
	  m_log.warn("Transaction not active or marked rollback in rollback:" + status);
	  try {
	    m_serviceManager.getLockManager().releaseAllLocks();
	  } catch (Throwable t) {
	    m_log.error("Exception", t);
	    throw new RepositoryException(t);
	  }
	}
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
    catch ( Exception e ) {
      m_log.error( "Exception", e );
    }
  }


  /**
   * Description of the Method
   */
  protected void commitTransaction()
  {
    try {
      int  status  = m_ctx.getUserTransaction().getStatus();

      if ( m_log.isDebugEnabled() ) {
        m_log.debug( "endTransaction:" + status );
      }

      try {
        if ( status == Status.STATUS_ACTIVE ) {
          m_serviceManager.beforeEndTransaction(true);
        }
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }

      try {
        if ( status == Status.STATUS_ACTIVE ) {
          m_ctx.getUserTransaction().commit();
        }
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }

      try {
        if ( status == Status.STATUS_ACTIVE ) {
          m_serviceManager.afterEndTransaction(true);
	} else {
	  m_log.warn("Transaction not active in commit:" + status);
	  try {
	    m_serviceManager.getLockManager().releaseAllLocks();
	  } catch (Throwable t) {
	    m_log.error("Exception", t);
	    throw new RepositoryException(t);
	  }
	}
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
    catch ( Exception e ) {
      m_log.error( "Exception", e );
    }
  }
}
