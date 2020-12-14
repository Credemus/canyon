package de.objectcode.canyon.persistent.participant;

import java.util.HashSet;
import java.util.Set;

import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.TransactionLocal;
import de.objectcode.canyon.worklist.IActivityInfo;
import de.objectcode.canyon.worklist.spi.participant.IParticipantResolver;

/**
 * @author    junglas
 * @created   12. August 2004
 */
public class AssignAllResolver implements IParticipantResolver
{
  private final static  Log             log               = LogFactory.getLog( AssignAllResolver.class );
  private               ServiceManager  m_serviceManager;


  /**
   *Constructor for the StickyRoundRobinResolver object
   *
   * @param serviceManager   Description of the Parameter
   */
  AssignAllResolver( ServiceManager serviceManager )
  {
    m_serviceManager = serviceManager;
  }




  /**
   * Description of the Method
   *
   * @param participant              Description of the Parameter
   * @param activityInfo             Description of the Parameter
   * @param clientId                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public String[] resolveParticipants( Participant participant, String clientId, IActivityInfo activityInfo, Set ignoreParticipants )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "resolveParticipants: " + participant + " " +
          activityInfo );
    }

    String[]  members  = m_serviceManager.getParticipantRepository()
        .findMembers( participant, clientId );

    return members;
  }




  /* (non-Javadoc)
   * @see de.objectcode.canyon.worklist.spi.participant.IParticipantResolver#markAccepted(java.lang.String, java.lang.String, de.objectcode.canyon.worklist.IActivityInfo)
   */
  public void markAccepted(String participantId, String clientId,
      String performerId, IActivityInfo activityInfo)
      throws RepositoryException {
    try {
      Session session = (Session) TransactionLocal.get();

      String piid = StickyResolver.getRootProcessInstanceId(activityInfo);
      PProcessInstanceToParticipantId procInstToUserId = new PProcessInstanceToParticipantId(
          piid, performerId);

      try {
        PProcessInstanceToParticipant procInstToUser = (PProcessInstanceToParticipant) session
            .load(PProcessInstanceToParticipant.class, procInstToUserId);
        return; // There is already a mapping
      } catch (net.sf.hibernate.ObjectNotFoundException e) {
      }

      session.save(new PProcessInstanceToParticipant(procInstToUserId,
          participantId));
      session.flush();
    } catch (Exception e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }




  /* (non-Javadoc)
   * @see de.objectcode.canyon.worklist.spi.participant.IParticipantResolver#markRejected(java.lang.String, java.lang.String, java.lang.String, de.objectcode.canyon.worklist.IActivityInfo)
   */
  public Set markRejected(String participantId, String clientId, String performerId, IActivityInfo activityInfo) throws RepositoryException {
  	HashSet ignoreParticipants = new HashSet();
  	// ignore old owner
  	ignoreParticipants.add(participantId);
  	return ignoreParticipants;    
  }


  /* (non-Javadoc)
   * @see de.objectcode.canyon.worklist.spi.participant.IParticipantResolver#markDelegated(java.lang.String, java.lang.String, java.lang.String, de.objectcode.canyon.worklist.IActivityInfo, java.lang.String)
   */
  public void markDelegated(String participantId, String clientId, String performerId, IActivityInfo activityInfo, String delegateParticipantId) throws RepositoryException {
    // TODO Auto-generated method stub
    
  }

}
