package de.objectcode.canyon.worklist.spi.participant;

import java.util.Set;

import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.worklist.IActivityInfo;

/**
 * An IParticipantResolver resolves an abstract participant to concrete human(s), program(s),
 * resource(s), machine(s) etc.
 *
 * @author    junglas
 * @created   3. November 2003
 */
public interface IParticipantResolver
{

  public String[] resolveParticipants( Participant participant, String clientId,
                                       IActivityInfo activityInfo, Set ignoreParticipants )
                                     throws RepositoryException;

  /**
   * This method is called by the WorklistEngine, when a user accepts a workItem
   * @param participantId
   * @param clientId
   * @param activityInfo
   * @throws RepositoryException
   */
  public void markAccepted(String participantId, String clientId, String performerId, IActivityInfo activityInfo) throws RepositoryException;

  public Set markRejected(String participantId, String clientId, String performerId, IActivityInfo activityInfo) throws RepositoryException;
  
  public void markDelegated(String participantId, String clientId, String performerId, IActivityInfo activityInfo, String delegateParticipantId) throws RepositoryException;

}
