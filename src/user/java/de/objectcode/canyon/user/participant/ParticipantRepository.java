package de.objectcode.canyon.user.participant;

import java.util.Iterator;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.participant.ParticipantType;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.TransactionLocal;
import de.objectcode.canyon.worklist.spi.participant.IParticipantRepository;

/**
 * @author    junglas
 * @created   9. Dezember 2003
 */
public class ParticipantRepository implements IParticipantRepository
{
  private final static  Log             log        = LogFactory.getLog( ParticipantRepository.class );

  private               SessionFactory  m_factory;


  /**
   *Constructor for the ParticipantRepository object
   *
   * @param factory  Description of the Parameter
   */
  public ParticipantRepository( SessionFactory factory )
  {
    m_factory = factory;
  }


  /**
   * @param participantId            Description of the Parameter
   * @param group                    Description of the Parameter
   * @return                         The member value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.worklist.spi.participant.IParticipantRepository#isMember(java.lang.String, de.objectcode.canyon.model.participant.Participant)
   */
  public boolean isMember( String participantId, String clientId, Participant group )
    throws RepositoryException
  {
    // TODO Auto-generated method stub
    return false;
  }


  /**
   * @param participant              Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.worklist.spi.participant.IParticipantRepository#findMembers(de.objectcode.canyon.model.participant.Participant)
   */
  public String[] findMembers( Participant participant, String clientId )
    throws RepositoryException
  {
    Session  session  = null;
    try {
      session = m_factory.openSession();

      PRole     role  = ( PRole ) session.load( PRole.class, participant.getId() );

      String[]  ret   = new String[role.getUsers().size()];
      Iterator  it    = role.getUsers().iterator();
      int       i;

      for ( i = 0; it.hasNext(); i++ ) {
        PUser  user  = ( PUser ) it.next();

        ret[i] = user.getUserId();
      }

      return ret;
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        if ( session != null ) {
          session.close();
        }
      }
      catch ( Exception e ) {
      }
    }
  }


  /**
   * @param participantId            Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.worklist.spi.participant.IParticipantRepository#findParticipant(java.lang.String)
   */
  public ParticipantType findParticipant( String participantId )
    throws RepositoryException
  {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   *  (non-Javadoc)
   *  @see de.objectcode.canyon.spi.instance.IInstanceRepository#beginTransaction()
   */
  public void beginTransaction()
	throws RepositoryException
	{
  	try {
  		if (TransactionLocal.get() == null)
  			TransactionLocal.set(m_factory.openSession());
  	}
  	catch ( HibernateException e ) {
  		log.error( "Exception", e );
  		throw new RepositoryException( e );
  	}
  }


  /**
   *  (non-Javadoc)
   *  @see de.objectcode.canyon.spi.instance.IInstanceRepository#endTransaction()
   */
  public void endTransaction(boolean flush)
	throws RepositoryException
	{
  	try {
  		Session session = (Session)TransactionLocal.get();
  		
  		if (session != null ) {
				if(flush){
					session.flush();    			
				}
  			session.close();
  			TransactionLocal.set(null);
  		}
  	}
  	catch ( HibernateException e ) {
  		log.error( "Exception", e );
  		throw new RepositoryException( e );
  	}
  }
}
