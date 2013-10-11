package de.objectcode.canyon.worklist.spi.participant;

import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.worklist.IActivityInfo;


/**
 * @author    junglas
 * @created   3. November 2003
 */
public interface IResolverRepository
{
	public void beginTransaction() throws RepositoryException;
	
	public void endTransaction(boolean flush) throws RepositoryException;

  /**
   * Get a participant resolver for a certain activity.
   * 
   * @deprecated Use findResolver ( IActivityInfo ) instead
   * @author junglas
   */
  public IParticipantResolver findResolver ( Activity activity );
  
  public IParticipantResolver findResolver ( IActivityInfo activityInfo );
}
