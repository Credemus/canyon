package de.objectcode.canyon.persistent.async;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.TransactionLocal;
import de.objectcode.canyon.spi.async.IAsyncManager;

/**
 * @author    junglas
 * @created   19. Februar 2004
 */
public class AsyncManager implements IAsyncManager
{
  private final static  Log             log               = LogFactory.getLog( AsyncManager.class );

  private               SessionFactory  m_factory;
  private               ServiceManager  m_serviceManager;
  private               Set             m_inWork;


  /**
   *Constructor for the JMSAsyncManager object
   *
   * @param factory         Description of the Parameter
   * @param serviceManager  Description of the Parameter
   */
  public AsyncManager( ServiceManager serviceManager, SessionFactory factory )
  {
    m_serviceManager = serviceManager;
    m_factory = factory;
    m_inWork = new HashSet();
  }


  /**
   * Description of the Method
   *
   * @param request  Description of the Parameter
   */
  private void asyncRequest( AsyncRequest request )
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "Enqueuing request " + request );
    }

    try {
      Session        session   = ( Session ) TransactionLocal.get();

      PAsyncRequest  pRequest  = new PAsyncRequest( request );

      session.save( pRequest );
      session.flush();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RuntimeException( e );
    }
  }

  public void asyncCompleteBPEActivity ( String processInstanceId, String activityId )
  {
  	if ( log.isDebugEnabled() ){
  		log.debug("asyncCompleteBPEActivity: " + processInstanceId + " " + activityId );
  	}
  	asyncRequest( new AsyncCompleteBPEActivity(processInstanceId, activityId));
  }


  /**
   * Description of the Method
   *
   * @exception RepositoryException  Description of the Exception
   */
  public void checkMessages()
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "checkMessages" );
    }
		
    try {
			
      m_serviceManager.beginTransaction();

      Session   session  = ( Session ) TransactionLocal.get();

      Transaction trx = session.beginTransaction();

      Query     query    = session.createQuery( "select o from o in class " + PAsyncRequest.class.getName() + " order by o.id asc" );

      List      result   = query.list();

      if ( log.isDebugEnabled() ) {
        log.debug( "Found: " + result.size() );
      }

      trx.rollback();
      trx = null;

      Iterator  it       = result.iterator();

      while ( it.hasNext() ) {
        PAsyncRequest  request  = ( PAsyncRequest ) it.next();
        boolean        perform  = false;

        try {
          trx = session.beginTransaction();

          synchronized ( m_inWork ) {
            if ( !m_inWork.contains( new Long( request.getId() ) ) ) {
              m_inWork.add( new Long( request.getId() ) );
              perform = true;
            }
          }

          m_serviceManager.beginTransaction();

          if ( perform && request.getRequest() != null && request.getRequest() instanceof AsyncRequest ) {
            session.delete( request );

            AsyncRequest  asyncRequest  = ( AsyncRequest ) request.getRequest();

            asyncRequest.execute( m_serviceManager );
          }
          session.flush();
          trx.commit();
          trx = null;
        }
        finally {
          m_serviceManager.getLockManager().releaseAllLocks();
          synchronized ( m_inWork ) {
            m_inWork.remove( new Long( request.getId() ) );
          }
          if ( trx != null ) { 
            trx.rollback();
            trx = null;
          }
        }
      }
    }
    catch ( WMWorkflowException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
      	boolean flush = true;
        m_serviceManager.beforeEndTransaction(flush);
      }
      catch ( Exception e ) {
      }
      try {
        m_serviceManager.afterEndTransaction(true);
      }
      catch ( Exception e ) {
      }
    }
  }


  /**
   *  (non-Javadoc)
   *
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.instance.IInstanceRepository#beginTransaction()
   */
  public void beginTransaction()
    throws RepositoryException
  {
    try {
      if ( TransactionLocal.get() == null ) {
        TransactionLocal.set( m_factory.openSession() );
      }
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   *  (non-Javadoc)
   *
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.instance.IInstanceRepository#endTransaction()
   */
  public void endTransaction(boolean flush)
    throws RepositoryException
  {
    try {
      Session  session  = ( Session ) TransactionLocal.get();

      if ( session != null ) {
				if(flush){
					session.flush();    			
				}
        session.close();
        TransactionLocal.set( null );
      }
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }
}
