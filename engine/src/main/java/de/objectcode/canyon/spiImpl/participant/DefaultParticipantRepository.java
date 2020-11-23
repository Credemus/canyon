package de.objectcode.canyon.spiImpl.participant;

import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.participant.ParticipantType;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.instance.IProcessInstance;
import de.objectcode.canyon.worklist.spi.participant.IParticipantRepository;

/**
 * @author    junglas
 * @created   23. Oktober 2003
 */
public class DefaultParticipantRepository implements IParticipantRepository
{

  /**
   * Gets the member attribute of the DefaultParticipantRepository object
   *
   * @param participantId            Description of the Parameter
   * @param group                    Description of the Parameter
   * @return                         The member value
   * @exception RepositoryException  Description of the Exception
   */
  public boolean isMember( String participantId, String clientId, Participant group )
    throws RepositoryException
  {
    return participantId.equals( group.getId() );
  }


  /**
   * @param participantId    Description of the Parameter
   * @param processInstance  Description of the Parameter
   * @return                 Description of the Return Value
   * @see                    de.objectcode.canyon.spi.participant.IParticipantRepository#resolveParticipants(java.lang.String, de.objectcode.canyon.spi.instance.IProcessInstance)
   */
  public String[] resolveParticipants(
      String participantId,
      IProcessInstance processInstance )
  {
    return new String[]{participantId};
  }


  /**
   * @param participantId  Description of the Parameter
   * @return               Description of the Return Value
   * @see                  de.objectcode.canyon.spi.participant.IParticipantRepository#findParticipant(java.lang.String)
   */
  public ParticipantType findParticipant( String participantId )
  {
    if ( "system".equals( participantId ) ) {
      return ParticipantType.SYSTEM;
    }
    return ParticipantType.HUMAN;
  }


  /**
   * Description of the Method
   *
   * @param participant              Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public String[] findMembers( Participant participant, String clientId )
    throws RepositoryException
  {
    return new String[]{participant.getId()};
  }

	/** (non-Javadoc)
	 * @see de.objectcode.canyon.worklist.spi.participant.IParticipantRepository#beginTransaction()
	 */
	public void beginTransaction() throws RepositoryException {
	}

	/** (non-Javadoc)
	 * @see de.objectcode.canyon.worklist.spi.participant.IParticipantRepository#endTransaction()
	 */
	public void endTransaction(boolean flush) throws RepositoryException {
	}

}
