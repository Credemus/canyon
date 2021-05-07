package de.objectcode.canyon.persistent.participant;

import de.objectcode.canyon.spi.RepositoryException;

/**
 * @author    junglas
 * @created   3. November 2003
 */
public interface IResolveStrategy
{
  /**
   * Picks one participant out of a list of participants.
   * 
   * @param participantIds
   * @return
   * @throws RepositoryException
   */
  String resolveParticipant(String[] participantIds) throws RepositoryException;
}
