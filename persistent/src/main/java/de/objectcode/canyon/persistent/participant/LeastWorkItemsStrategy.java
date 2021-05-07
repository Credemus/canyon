package de.objectcode.canyon.persistent.participant;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.persistent.instance.PWorkItem;
import de.objectcode.canyon.spi.RepositoryException;

/**
 * @author    junglas
 * @created   3. November 2003
 */
public class LeastWorkItemsStrategy implements IResolveStrategy
{
  private final static  Log             log        = LogFactory.getLog( LeastWorkItemsStrategy.class );

  private               SessionFactory  m_factory;


  /**
   *Constructor for the LeastWorkItemsStrategy object
   *
   * @param factory  Description of the Parameter
   */
  LeastWorkItemsStrategy( SessionFactory factory )
  {
    m_factory = factory;
  }


  /**
   * Description of the Method
   *
   * @param userIds                  Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public String resolveParticipant( String[] userIds )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      StringBuilder buffer  = new StringBuilder( "resolveParticipant: [" );
      int           i;

      for ( i = 0; i < userIds.length; i++ ) {
        if ( i > 0 ) {
          buffer.append( ", " );
        }
        buffer.append( userIds[i] );
      }
      buffer.append( "]" );

      log.debug( buffer.toString() );
    }

    int      i;
    int      workItemCount  = Integer.MAX_VALUE;
    String   userId         = null;
    Session  session        = null;

    try {
      session = m_factory.openSession();

      for ( i = 0; i < userIds.length; i++ ) {
        Query  query   = session.createQuery( "select count(*) from o in class " + PWorkItem.class.getName() + " where o.participant = ?" );

        query.setString( 0, userIds[i] );

        int    result  = (Integer) query.iterate().next();

        if ( result < workItemCount ) {
          userId = userIds[i];
          workItemCount = result;
        }
      }
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        if ( session != null ) {
          session.close();
        }
      }
      catch ( Exception ignored) {
      }
    }

    return userId;
  }

}
