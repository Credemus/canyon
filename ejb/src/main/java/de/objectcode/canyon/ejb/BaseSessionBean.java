package de.objectcode.canyon.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @ejb.bean generate="false"
 * @ejb.home generate="false"
 * @ejb.interface generate="false"
 *
 * @author    Bodo Junglas
 * @created   21.02.2003
 * @version   $Id: BaseSessionBean.java,v 1.2 2003/11/19 09:03:11 junglas Exp $
 */
public class BaseSessionBean implements SessionBean
{
	static final long serialVersionUID = 1113072178513686984L;
	
	protected transient  Log             m_log;
  protected            SessionContext  m_ctx;


  /**
   * @param ctx                  The new sessionContext value
   * @exception EJBException     Description of the Exception
   * @exception RemoteException  Description of the Exception
   * @see                        javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
   */
  public void setSessionContext( SessionContext ctx )
    throws EJBException, RemoteException
  {
    m_ctx = ctx;
    if ( m_log == null ) {
      m_log = LogFactory.getLog( getClass() );
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
    if ( m_log == null ) {
      m_log = LogFactory.getLog( getClass() );
    }
    m_log.debug("Activate");
  }


  /**
   * @exception EJBException     Description of the Exception
   * @exception RemoteException  Description of the Exception
   * @see                        javax.ejb.SessionBean#ejbPassivate()
   */
  public void ejbPassivate()
    throws EJBException, RemoteException
  {
    m_log.debug("Passivate");
  }


  /**
   * @exception EJBException     Description of the Exception
   * @exception RemoteException  Description of the Exception
   * @see                        javax.ejb.SessionBean#ejbRemove()
   */
  public void ejbRemove()
  {
    m_log.debug("Remove"); 
  }
}
