package de.objectcode.canyon.persistent.participant;

import java.util.HashSet;
import java.util.Set;

import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.TransactionLocal;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.instance.IProcessInstance;
import de.objectcode.canyon.worklist.IActivityInfo;
import de.objectcode.canyon.worklist.spi.participant.IParticipantResolver;

/**
 * @author junglas
 * @created 3. November 2003
 */
public class StickyResolver implements IParticipantResolver {
  private final static Log log = LogFactory.getLog(StickyResolver.class);

  private ServiceManager m_serviceManager;

  private SessionFactory m_factory;

  private IResolveStrategy m_resolveStrategy;

  /**
   * Constructor for the StickyRoundRobinResolver object
   * 
   * @param factory
   *          Description of the Parameter
   * @param serviceManager
   *          Description of the Parameter
   * @param resolveStrategy
   *          Description of the Parameter
   */
  StickyResolver(ServiceManager serviceManager, SessionFactory factory,
      IResolveStrategy resolveStrategy) {
    m_factory = factory;
    m_serviceManager = serviceManager;
    m_resolveStrategy = resolveStrategy;
  }


  /**
   * Description of the Method
   * 
   * @param participant
   *          Description of the Parameter
   * @param activityInfo
   *          Description of the Parameter
   * @return Description of the Return Value
   * @exception RepositoryException
   *              Description of the Exception
   */
  public String[] resolveParticipants(Participant participant, String clientId,
      IActivityInfo activityInfo, Set ignoreParticipants) throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("resolveParticipants: " + participant + " " + activityInfo);
    }

    try {
      Session session = (Session) TransactionLocal.get();

      String piid = getRootProcessInstanceId(activityInfo);

      PProcessInstanceToParticipantId procInstToUserId = new PProcessInstanceToParticipantId(
          piid, participant.getId());
      PProcessInstanceToParticipant procInstToUser = null;
      
      try {
        procInstToUser = (PProcessInstanceToParticipant) session
            .load(PProcessInstanceToParticipant.class, procInstToUserId);

        // membership may have changed (e.g. absence), so recheck
        if (m_serviceManager.getParticipantRepository().isMember(
        		procInstToUser.getResolvedParticipantId(), clientId, participant)) {
        	return new String[] { procInstToUser.getResolvedParticipantId() };
        }
      } catch (net.sf.hibernate.ObjectNotFoundException e) {
        log.debug("ObjectNotFoundException: " + e);
      }

      try {
        if (m_serviceManager.getParticipantRepository().isMember(
            activityInfo.getProcessStarter(), clientId, participant)) {
          return new String[] { activityInfo.getProcessStarter() };
        }
      } catch (Exception e) {
        log.debug("Exception: " + e);
      }

      String[] members = m_serviceManager.getParticipantRepository()
          .findMembers(participant, clientId);

      String resolvedId = m_resolveStrategy.resolveParticipant(members);

      if (procInstToUser==null)	
      	session.save(new PProcessInstanceToParticipant(procInstToUserId,
          resolvedId));
      else {
      	procInstToUser.setResolvedParticipantId(resolvedId);
      	session.update(procInstToUser);
      }
      session.flush();

      return new String[] { resolvedId };
    } catch (Exception e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.objectcode.canyon.worklist.spi.participant.IParticipantResolver#resolveDelegatableParticipants(de.objectcode.canyon.model.participant.Participant,
   *      de.objectcode.canyon.spi.instance.IProcessInstance)
   */
  /**
   * Stickyness is handled with respect to the root process instance
   * @param activityInfo
   * @return
   */
  public static  String getRootProcessInstanceId(IActivityInfo activityInfo) {
    String piid_path = activityInfo.getProcessInstanceIdPath();
    if (piid_path==null)
      return activityInfo.getProcessInstanceId();
    else {
      int index = piid_path.indexOf("_");
      if (index == -1)
        return piid_path;
      else
        return piid_path.substring(0,index);
    }
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see de.objectcode.canyon.worklist.spi.participant.IParticipantResolver#markAccepted(java.lang.String,
   *      java.lang.String, de.objectcode.canyon.worklist.IActivityInfo)
   */
  public void markAccepted(String participantId, String clientId, String performerId,
      IActivityInfo activityInfo) throws RepositoryException {
    // nothing to do stickyness is taken care of during resolve
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