package de.objectcode.canyon.persistent.participant;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.TransactionLocal;
import de.objectcode.canyon.worklist.IActivityInfo;
import de.objectcode.canyon.worklist.spi.participant.IParticipantResolver;
import de.objectcode.canyon.worklist.spi.participant.IResolverRepository;

/**
 * @author    junglas
 * @created   3. November 2003
 */
public class ResolverRepository implements IResolverRepository
{
  private final static  Log                   log                = LogFactory.getLog( ResolverRepository.class );
  private               IParticipantResolver  m_defaultResolver;
  private               IParticipantResolver  m_assignAllResolver;
  
  private               SessionFactory        m_factory;


  /**
   *Constructor for the ResolverRepository object
   *
   * @param factory         Description of the Parameter
   * @param serviceManager  Description of the Parameter
   */
  public ResolverRepository( ServiceManager serviceManager, SessionFactory factory )
  {
    m_factory = factory;
    IResolveStrategy  resolveStrategy  = new LeastWorkItemsStrategy( factory );
    m_defaultResolver = new StickyResolver( serviceManager, factory, resolveStrategy );
    m_assignAllResolver = new AssignAllResolver(serviceManager );
  }


  /**
   * @param activity  Description of the Parameter
   * @return          Description of the Return Value
   * @see             de.objectcode.canyon.spi.participant.IResolverRepository#findResolver(org.obe.activity.Activity)
   */
  public IParticipantResolver findResolver( Activity activity )
  {
    return m_defaultResolver;
  }

  public IParticipantResolver findResolver( IActivityInfo activityInfo )
  {
    switch ( activityInfo.getAssignStrategy() ) {
    	case 1:
    	  return m_assignAllResolver;
    }
    return m_defaultResolver;
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
