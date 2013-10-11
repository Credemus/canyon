package de.objectcode.canyon.worklist.spi.participant;

import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.participant.ParticipantType;
import de.objectcode.canyon.spi.RepositoryException;

/**
 * @author    junglas
 * @created   30. Oktober 2003
 */
public interface IParticipantRepository
{
	public void beginTransaction() throws RepositoryException;
	
	public void endTransaction(boolean flush) throws RepositoryException;
	
  /**
   * Find a participant in the repository and return its type.
   *
   * @param participantId            Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public ParticipantType findParticipant( String participantId )
    throws RepositoryException;


  /**
   * Find all members of a role or organizational unit.
   *
   * @param participant              Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public String[] findMembers( Participant participant, String clientId )
    throws RepositoryException;

  /**
   * Check if a participant is member of a role or organizational unit.
   * 
   * @author junglas
   */
  public boolean isMember ( String participantId, String clientId, Participant group )
    throws RepositoryException;
}
