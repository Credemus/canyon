package de.objectcode.canyon.jmx.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.spi.ServiceManager;

/**
 * @author junglas
 */
public class BaseAdmin {
	private static final Log log = LogFactory.getLog(BaseAdmin.class);

	protected               ServiceManager  m_svcMgr;
	protected               String          m_jndiName  = "java:/canyon/ServiceManager";
	
	/**
	 * (non-Javadoc)
	 *
	 * @exception EJBException     Description of the Exception
	 * @exception RemoteException  Description of the Exception
	 * @see                        javax.ejb.SessionSynchronization#afterBegin()
	 */
	protected void beginTransaction()
	{
		if ( log.isDebugEnabled() ) {
			log.debug( "beginTransaction:" );
		}
		try {
			m_svcMgr.beginTransaction();
		}
		catch ( Exception e ) {
			log.error("Exception", e);
		}
	}


	/**
	 * (non-Javadoc)
	 *
	 * @exception EJBException     Description of the Exception
	 * @exception RemoteException  Description of the Exception
	 * @see                        javax.ejb.SessionSynchronization#beforeCompletion()
	 */
	protected void endTransaction()
	{
		if ( log.isDebugEnabled() ) {
			log.debug( "endTransaction:" );
		}
		try {
			boolean flush = true;
			m_svcMgr.beforeEndTransaction(flush);
			m_svcMgr.afterEndTransaction(true);
		}
		catch ( Exception e ) {
			log.error("Exception", e);
		}
	}
	
}
