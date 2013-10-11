package de.objectcode.canyon.wetdock.repository.participant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.participant.ParticipantType;
import de.objectcode.canyon.spi.ObjectNotFoundException;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.wetdock.api.user.UserData;
import de.objectcode.canyon.wetdock.user.IUserManager;
import de.objectcode.canyon.worklist.spi.participant.IParticipantRepository;

/**
 * @author xylander
 */
public class ParticipantRepository implements IParticipantRepository {

  private final static  Log                   log                     = LogFactory.getLog( ParticipantRepository.class );
  
  private IUserManager m_userManager;
  
  public ParticipantRepository(IUserManager userManager) {   
    if(log.isInfoEnabled()) {
      log.info("Creating ParticipantRepository");      
    }
    
    m_userManager = userManager;
  }
  

/**
 * Note: Gets all UserIds containing the Role of aParticipant
 */
  public String[] findMembers(Participant aParticipant, String clientId) throws RepositoryException {

    if(log.isDebugEnabled()) {
      log.debug("findMembers for participant '" + aParticipant.getId() + "' client '" + clientId + "'");       
    }

    UserData[] users = m_userManager.findUsersInRole(clientId, aParticipant.getId());

    if(log.isDebugEnabled()) {
      log.debug("findMembers user found " + users.length);       
    }
    
    String[] userIds = new String[users.length];
    
    for(int i=0; i<users.length; i++){
      userIds[i] = users[i].getUserId();

      if(log.isDebugEnabled()) {
        log.debug("findMembers user " + users[i].getUserId());             
      }      
    }
 
    return userIds;
  }

  /**
   * @see de.objectcode.canyon.spi.participant.IParticipantRepository#findParticipant(java.lang.String)
   */
  public ParticipantType findParticipant(String participantId )
    throws RepositoryException {
    
    if ( m_userManager.getUser(participantId) != null )
      return ParticipantType.HUMAN;
    
    if ( m_userManager.getRole(participantId) != null  )
      return ParticipantType.ROLE;

    throw new ObjectNotFoundException( participantId );
  }

  /**
   * @see de.objectcode.canyon.spi.participant.IParticipantRepository#isMember(java.lang.String, org.obe.participant.Participant)
   */
  public boolean isMember(String aUserId, String clientId, Participant aParticipant)
    throws RepositoryException {
            
      if(log.isDebugEnabled()) {
        log.debug("isMember for user '" 
          + aUserId + "' and participant '" + aParticipant.getId() + "' client '" + clientId + "'");       
      }    
      
      String userId = aUserId;

      UserData[] users = m_userManager.findUsersInRole(clientId, aParticipant.getId());
     
      for(int i=0; i<users.length; i++){
        if(userId.equalsIgnoreCase(users[i].getUserId())) {
          return true;        
        }            
      }

    return false;
  }
  
  /**
   * @see de.objectcode.canyon.worklist.spi.participant.IParticipantRepository#beginTransaction()
   */
  public void beginTransaction() throws RepositoryException {
//    if(gLOGGER.isDebugEnabled()) {
//      gLOGGER.debug("begin transaction");       
//    }
   }

  /**
   * @see de.objectcode.canyon.worklist.spi.participant.IParticipantRepository#endTransaction()
   */
  public void endTransaction(boolean flush) throws RepositoryException  {  
//    if(gLOGGER.isDebugEnabled()) {
//     gLOGGER.debug("end transaction");  
//    }   
  }
}
