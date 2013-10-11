package de.objectcode.canyon.worklist;

import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.engine.util.WAPIHelper;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.ObjectNotFoundException;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.filter.AndFilter;
import de.objectcode.canyon.spi.filter.CompareFilter;
import de.objectcode.canyon.spi.filter.IFilter;
import de.objectcode.canyon.spi.filter.InFilter;
import de.objectcode.canyon.spi.filter.LikeFilter;
import de.objectcode.canyon.spi.instance.AttributeReadOnlyException;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.instance.IProcessInstance;
import de.objectcode.canyon.spi.process.IProcessRepository;
import de.objectcode.canyon.worklist.spi.participant.IParticipantRepository;
import de.objectcode.canyon.worklist.spi.participant.IParticipantResolver;
import de.objectcode.canyon.worklist.spi.participant.IResolverRepository;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;
import de.objectcode.canyon.worklist.spi.worklist.IWorklistRepository;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMAttribute;
import org.wfmc.wapi.WMFilter;
import org.wfmc.wapi.WMInvalidAttributeException;
import org.wfmc.wapi.WMInvalidProcessDefinitionException;
import org.wfmc.wapi.WMInvalidProcessInstanceException;
import org.wfmc.wapi.WMInvalidSourceUserException;
import org.wfmc.wapi.WMInvalidTargetUserException;
import org.wfmc.wapi.WMInvalidWorkItemException;
import org.wfmc.wapi.WMUnsupportedOperationException;
import org.wfmc.wapi.WMWorkItem;
import org.wfmc.wapi.WMWorkItemState;
import org.wfmc.wapi.WMWorkflowException;

/**
 * @author junglas
 * @created 30. Oktober 2003
 */
public class WorkItemManager implements IWorkItemManager {
	private final static Log log = LogFactory.getLog(WorkItemManager.class);

	private ServiceManager m_serviceManager;

	private WorklistEngine m_worklistEngine;

	/**
	 * Constructor for the ActivityManager object
	 * 
	 * @param serviceManager
	 *          Description of the Parameter
	 * @param worklistEngine
	 *          Description of the Parameter
	 */
	public WorkItemManager(WorklistEngine worklistEngine,
			ServiceManager serviceManager) {
		m_serviceManager = serviceManager;
		m_worklistEngine = worklistEngine;
	}

	/**
	 * Gets the workItem attribute of the WorkItemManager object
	 * 
	 * @param workItemId
	 *          Description of the Parameter
	 * @return The workItem value
	 * @exception WMWorkflowException
	 *              Description of the Exception
	 */
	public IWorkItem getWorkItem(String workItemId) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("getWorkItem(" + workItemId + ", " + ')');
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();
			IWorkItem workItem = worklistRepository.findWorkItem(workItemId);
			return workItem;
		} catch (ObjectNotFoundException e) {
			throw new WMInvalidWorkItemException(workItemId);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	/**
	 * Get the specified work item.
	 * 
	 * @param workItemId
	 *          The work item id
	 * @return The work item
	 * @throws WMWorkflowException
	 *           Workflow client exception
	 */
	public WMWorkItem findWorkItem(String workItemId) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("findWorkItem(" + workItemId + ", " + ')');
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();
			IWorkItem workItem = worklistRepository.findWorkItem(workItemId);
			return WAPIHelper.fromWorkItem(workItem);
		} catch (ObjectNotFoundException e) {
			throw new WMInvalidWorkItemException(workItemId);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	/**
	 * Set the specified work item attribute value.
	 * 
	 * @param workItemId
	 *          The work item id
	 * @param attributeName
	 *          The attribute name
	 * @param attributeType
	 *          The attribute type
	 * @param attributeValue
	 *          The attribute value
	 * @throws WMWorkflowException
	 *           Workflow client exception
	 */
	public void assignWorkItemAttribute(String workItemId, String attributeName,
			int attributeType, Object attributeValue) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("assignWorkItemAttribute(" + ", " + workItemId + ", "
					+ attributeName + ", " + attributeValue + ')');
		}

		try {
			// Find the work item attribute.
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();
			IWorkItem workItem = worklistRepository.findWorkItem(workItemId);
			IAttributeInstance attr = workItem.getAttributeInstance(attributeName);

			// Update the attribute instance value.
			attr.setValue(attributeType, attributeValue);

			// Notify attribute instance listeners.
			m_serviceManager.getWorkflowEventBroker().fireAttributeInstanceUpdated(
					attr, null);
		} catch (ObjectNotFoundException e) {
			throw new WMInvalidWorkItemException(workItemId);
		} catch (AttributeReadOnlyException e) {
			throw new WMWorkflowException(e);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param workItemId
	 *          Description of the Parameter
	 * @param applicationData
	 *          Description of the Parameter
	 * @exception WMWorkflowException
	 *              Description of the Exception
	 */
	public void assignApplicationData(String workItemId,
			IApplicationData[] applicationData) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("assignApplicationData(" + ", " + workItemId + ", "
					+ applicationData + ')');
		}

		try {
			// Find the work item attribute.
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();
			IWorkItem workItem = worklistRepository.findWorkItem(workItemId);

			workItem.setApplicationData(applicationData);
		} catch (ObjectNotFoundException e) {
			throw new WMInvalidWorkItemException(workItemId);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	/**
	 * Get the specified work item attribute value.
	 * 
	 * @param workItemId
	 *          The work item id
	 * @param attributeName
	 *          The attribute name
	 * @return The attribute
	 * @throws WMWorkflowException
	 *           Workflow client exception
	 */
	public WMAttribute findWorkItemAttributeValue(String workItemId,
			String attributeName) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("findWorkItemAttributeValue(" + ", " + workItemId + ", "
					+ attributeName + ')');
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();
			IWorkItem workItem;
			try {
				workItem = worklistRepository.findWorkItem(workItemId);
			} catch (ObjectNotFoundException e) {
				throw new WMInvalidWorkItemException(workItemId);
			}
			IAttributeInstance attr;
			try {
				attr = workItem.getAttributeInstance(attributeName);
			} catch (ObjectNotFoundException e) {
				throw new WMInvalidAttributeException(attributeName);
			}
			return WAPIHelper.fromAttributeInstance(attr);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	/**
	 * Retrieve a list of activity instance attributes.
	 * 
	 * @param filter
	 *          The filter or null
	 * @param countFlag
	 *          True to return count value
	 * @param workItemId
	 *          Description of the Parameter
	 * @return The query handle
	 * @throws WMWorkflowException
	 *           Workflow client exception
	 */
	public WMAttribute[] findWorkItemAttributes(String workItemId,
			WMFilter filter, boolean countFlag) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("findWorkItemInstanceAttributes(" + workItemId + ", " + filter
					+ ", " + countFlag + ')');
		}

		if (filter != null) {
			throw new WMUnsupportedOperationException("Filters are not supported");
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();
			IWorkItem workItem = worklistRepository.findWorkItem(workItemId);

			if (countFlag) {
				return new WMAttribute[workItem.getAttributeInstances().size()];
			} else {
				IAttributeInstance[] attributeInstances = new IAttributeInstance[workItem
						.getAttributeInstances().size()];

				workItem.getAttributeInstances().values().toArray(attributeInstances);

				return WAPIHelper.fromAttributeInstances(attributeInstances);
			}
		} catch (ObjectNotFoundException e) {
			throw new WMInvalidProcessInstanceException(workItemId);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	/**
	 * @param processDefinitionId
	 * @param activityDefinitionId
	 * @param overdue
	 * @param performer
	 * @param participants
	 * @param states
	 * @param activityName
	 * @return
	 * @throws WMWorkflowException
	 */
	public IWorkItem[] listWorkItems(String processDefinitionId,
	      String activityDefinitionId, Boolean overdue, String performer,
	      String[] participants, int[] states, String activityName, String processName, String processInstanceIdPath)
	      throws WMWorkflowException {
	  return listWorkItems(processDefinitionId,
	      activityDefinitionId, overdue,performer,
	      participants, states, activityName, processName, processInstanceIdPath, Integer.MAX_VALUE);
	}	
	
	/**
	 * @param processDefinitionId
	 * @param activityDefinitionId
	 * @param overdue
	 * @param performer
	 * @param participants
	 * @param states
	 * @param activityName
	 * @param maxResultSize
	 * @return
	 * @throws WMWorkflowException
	 */
public IWorkItem[] listWorkItems(String processDefinitionId,
      String activityDefinitionId, Boolean overdue, String performer,
      String[] participants, int[] states, String activityName, String processName, String processInstanceIdPath, int maxResultSize)
      throws WMWorkflowException {

    if (log.isDebugEnabled()) {
      StringBuffer buf = new StringBuffer("listWorkItems(");
      buf.append("processDefinitionId='" + processDefinitionId + "', ");
      buf.append("activityDefinitionId='" + activityDefinitionId + "', ");
      buf.append("overdue='" + overdue + "', ");
      buf.append("performer='" + performer + "', ");
      buf.append("participants=[");
      int i;
      if (participants != null) {
        for (i = 0; i < participants.length; i++) {
          if (i > 0) {
            buf.append(",");
          }
          buf.append(participants[i]);
        }
      }
      buf.append("], states=[");
      if (states != null) {
        for (i = 0; i < states.length; i++) {
          if (i > 0) {
            buf.append(",");
          }
          buf.append(WMWorkItemState.fromInt(states[i]).stringValue());
        }
      }
      buf.append("]");
      buf.append("activityName='" + activityName + "', ");
      buf.append("processName='" + processName + "', ");
      buf.append("processInstanceIdPath='" + processInstanceIdPath + "', ");
      log.debug(buf.toString());
    }

    try {
      IWorklistRepository worklistRepository = m_serviceManager
          .getWorklistRepository();

      AndFilter filter = new AndFilter();
      if (processDefinitionId != null) {
        filter.addFilter(new CompareFilter("processDefinitionId.id",
            CompareFilter.EQ, processDefinitionId));
      }
      // TODO ruth testen
      if (activityDefinitionId != null) {
        filter.addFilter(new CompareFilter("activityDefinitionId",
            CompareFilter.EQ, activityDefinitionId));
      }
      if (overdue != null) {
        Date now = new Date(System.currentTimeMillis());
        filter.addFilter(new CompareFilter("dueDate", CompareFilter.LT, now));
      }
      if (performer != null) {
        filter.addFilter(new CompareFilter("performer", CompareFilter.EQ,
            performer));
      }
      if (participants != null && participants.length > 0) {
        if (participants.length == 1) {
          filter.addFilter(new CompareFilter("participant", CompareFilter.EQ,
              participants[0]));
        } else {
          filter.addFilter(new InFilter("participant", participants));
        }
      }
      if (states != null && states.length > 0) {
        if (states.length == 1) {
          filter.addFilter(new CompareFilter("state", CompareFilter.EQ,
              states[0]));
        } else {
          filter.addFilter(new InFilter("state", states));
        }
      }
      if (activityName != null) {
        filter.addFilter(new LikeFilter("name", activityName));
      }

      if (processName != null) {
        filter.addFilter(new LikeFilter("processName", processName));
      }
      
      // Due to backward compatability we must distribute a single query
			// attribute
      // on two columns
      if (processInstanceIdPath!=null) {
        filter.addFilter(new LikeFilter("parentProcessInstanceIdPath", processInstanceIdPath));      			
//      	int underScoreIndex = processInstanceIdPath.lastIndexOf("_");
//      	int percentIndex = processInstanceIdPath.lastIndexOf("%");
//      	if (underScoreIndex!=-1) {
//      			if (percentIndex<underScoreIndex) {
//	        		if (underScoreIndex<processInstanceIdPath.length()-1) {
//	        			String lastPID = processInstanceIdPath.substring(underScoreIndex+1); // does not contain _
//	        			filter.addFilter(new CompareFilter("processInstanceId", CompareFilter.EQ,lastPID));
//	        			String parentProcessInstanceIdPath = processInstanceIdPath.substring(0,underScoreIndex);
//	              filter.addFilter(new LikeFilter("parentProcessInstanceIdPath", parentProcessInstanceIdPath));
//	        		} else {
//	              filter.addFilter(new LikeFilter("parentProcessInstanceIdPath", processInstanceIdPath));      			
//	        		}
//      			} else {
//	        		if (percentIndex<processInstanceIdPath.length()-1) {
//	        			String lastPart = processInstanceIdPath.substring(percentIndex); // contains %
//	        			filter.addFilter(new CompareFilter("processInstanceId", CompareFilter.EQ,lastPart));
//	        			String firstPart = processInstanceIdPath.substring(0,percentIndex+1); // contains %
//	              filter.addFilter(new LikeFilter("parentProcessInstanceIdPath", firstPart));
//	        		} else {
//	              filter.addFilter(new LikeFilter("parentProcessInstanceIdPath", processInstanceIdPath));      			
//	        		}
//      				
//      			}
//      	} else {
//      		// No underscore found
//      		if (percentIndex!=-1) {
//      			// percent is last char
//      			if (percentIndex==processInstanceIdPath.length()-1) {
//        			OrFilter orFilter = new OrFilter();
//        			orFilter.addFilter(new LikeFilter("parentProcessInstanceIdPath", processInstanceIdPath));
//        			orFilter.addFilter(new LikeFilter("processInstanceId", processInstanceIdPath));
//        			filter.addFilter(orFilter);
//      			} else {
//      				
//      			}
//      		} else {
//      			// just a single pid
//      			filter.addFilter(new CompareFilter("processInstanceId", CompareFilter.EQ,processInstanceIdPath));      			
//      		}
//      	}
//      	if (percentIndex<underScoreIndex) {
//      	} else {
//      		if (percentIndex!=-1) {
//      			OrFilter orFilter = new OrFilter();
//      			orFilter.add()
//      		} else {
//      			
//      		}
//      	}
      }

      return worklistRepository.findWorkItems(filter,maxResultSize);
    } catch (RepositoryException e) {
      throw new WMWorkflowException(e);
    }
  }
	/**
	 * Retrieve a list of work items.
	 * 
	 * @param filter
	 *          A Filter specification.
	 * @param countFlag
	 *          Description of the Parameter
	 * @return An array of matching work items.
	 * @exception WMWorkflowException
	 *              Description of the Exception
	 */
	public WMWorkItem[] findWorkItems(WMFilter filter, boolean countFlag)
			throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("listWorkItems(" + filter + ", " + countFlag + ')');
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();
			if (countFlag) {
				return new WMWorkItem[worklistRepository.countWorkItems(WAPIHelper
						.fromWMFilter(filter))];
			} else {
				IWorkItem[] workList = worklistRepository.findWorkItems(WAPIHelper
						.fromWMFilter(filter));
				return WAPIHelper.fromWorkItems(workList);
			}
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param participant
	 *          Description of the Parameter
	 * @param onlyOpen
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception WMWorkflowException
	 *              Description of the Exception
	 */
	public int countWorkItems(String participant, boolean onlyOpen)
			throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("countWorkItems(" + participant + " " + onlyOpen + ')');
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();

			AndFilter filter = new AndFilter();
			filter.addFilter(new CompareFilter("participant", CompareFilter.EQ,
					participant));

			if (onlyOpen) {
				filter.addFilter(new CompareFilter("state", CompareFilter.LE,
						WMWorkItemState.OPEN_RUNNING_INT));
			}
			return worklistRepository.countWorkItems(filter);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param participant
	 *          Description of the Parameter
	 * @param onlyOpen
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception WMWorkflowException
	 *              Description of the Exception
	 */
	public IWorkItem[] listWorkItems(String participant, boolean onlyOpen)
			throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("listWorkItems(" + participant + " " + onlyOpen + ')');
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();

			AndFilter filter = new AndFilter();
			filter.addFilter(new CompareFilter("participant", CompareFilter.EQ,
					participant));

			if (onlyOpen) {
				filter.addFilter(new CompareFilter("state", CompareFilter.LE,
						WMWorkItemState.OPEN_RUNNING_INT));
			}

			return worklistRepository.findWorkItems(filter);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param participant
	 *          Description of the Parameter
	 * @param clientId
	 *          Description of the Parameter
	 * @param onlyOpen
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception WMWorkflowException
	 *              Description of the Exception
	 */
	public IWorkItem[] listWorkItems(String participant, String clientId,
			boolean onlyOpen) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("listWorkItems(" + participant + " " + onlyOpen + ')');
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();

			AndFilter filter = new AndFilter();
			filter.addFilter(new CompareFilter("participant", CompareFilter.EQ,
					participant));
			filter
					.addFilter(new CompareFilter("clientId", CompareFilter.EQ, clientId));

			if (onlyOpen) {
				filter.addFilter(new CompareFilter("state", CompareFilter.LE,
						WMWorkItemState.OPEN_RUNNING_INT));
			}

			return worklistRepository.findWorkItems(filter);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}

	}

	/**
	 * Description of the Method
	 * 
	 * @param onlyOpen
	 *          Description of the Parameter
	 * @param processInstanceId
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception WMWorkflowException
	 *              Description of the Exception
	 */
	public IWorkItem[] listWorkItemsForProcessInstance(String processInstanceId,
			boolean onlyOpen) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("listWorkItemsForProcessInstance(" + processInstanceId + " "
					+ onlyOpen + ')');
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();

			AndFilter filter = new AndFilter();
			filter.addFilter(new CompareFilter("processInstanceId", CompareFilter.EQ,
					processInstanceId));

			if (onlyOpen) {
				filter.addFilter(new CompareFilter("state", CompareFilter.LE,
						WMWorkItemState.OPEN_RUNNING_INT));
			}

			return worklistRepository.findWorkItems(filter);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param workItemId
	 *          Description of the Parameter
	 * @param filter
	 *          Description of the Parameter
	 * @param countFlag
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 * @exception WMWorkflowException
	 *              Description of the Exception
	 */
	public WMWorkItemState[] findWorkItemStates(String workItemId,
			WMFilter filter, boolean countFlag) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("listWorkItemStates(" + workItemId + ", " + filter + ", "
					+ countFlag + ')');
		}

		if (filter != null) {
			throw new WMUnsupportedOperationException("Filters are not supported");
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();
			IWorkItem workItem = worklistRepository.findWorkItem(workItemId);
			WMWorkItemState state = WMWorkItemState.fromInt(workItem.getState());
			WMWorkItemState[] states = (WMWorkItemState[]) state.getStates();
			if (countFlag) {
				Arrays.fill(states, null);
			}
			return states;
		} catch (ObjectNotFoundException e) {
			throw new WMInvalidWorkItemException(workItemId);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	/**
	 * Reassign the specified work item to another user.
	 * 
	 * @param sourceUser
	 *          The current user
	 * @param targetUser
	 *          The new user
	 * @param workItemId
	 *          The work item id
	 * @throws WMWorkflowException
	 *           Workflow client exception
	 */
	public void reassignWorkItem(String sourceUser, String targetUser,
			String workItemId) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("reassignWorkItem(" + sourceUser + ", " + targetUser + ", "
					+ workItemId + ", " + ')');
		}

		try {
			// Find the work item.
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();
			IParticipantRepository participantRepository = m_serviceManager
					.getParticipantRepository();
			IWorkItem workItem = worklistRepository.findWorkItem(workItemId);

			// Validate the source user.
			String currentUser = workItem.getParticipant();
			if (currentUser != null && !currentUser.equals(sourceUser)
					|| currentUser == null && sourceUser != null) {

				throw new WMInvalidSourceUserException(sourceUser);
			}
			// TODO: check abstract participant types against repository.
			// TODO: check concrete participant types against the workflow.
			try {
				participantRepository.findParticipant(sourceUser);
			} catch (ObjectNotFoundException e) {
				throw new WMInvalidSourceUserException(sourceUser);
			}

			// Validate the target user.
			try {
				participantRepository.findParticipant(targetUser);
			} catch (ObjectNotFoundException e) {
				throw new WMInvalidTargetUserException(targetUser);
			}

			// Reassign the work item (if necessary).
			if (currentUser != null && !currentUser.equals(targetUser)
					|| currentUser == null && targetUser != null) {

				m_worklistEngine.checkReassign(workItem, targetUser);
				// we assume here, that the broker makes a copy of the state immediately
				m_serviceManager.getWorkflowEventBroker().fireWorkItemDeprived(
						workItem, null);

				workItem.setParticipant(targetUser);

				// Notify work item listeners.
				m_serviceManager.getWorkflowEventBroker().fireWorkItemAssigned(
						workItem, null);
			}
		} catch (ObjectNotFoundException e) {
			throw new WMInvalidWorkItemException(workItemId);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param workItemId
	 *          Description of the Parameter
	 * @param newState
	 *          Description of the Parameter
	 * @exception WMWorkflowException
	 *              Description of the Exception
	 */
	public void changeWorkItemState(String workItemId, WMWorkItemState newState)
			throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("changeWorkItemState(" + workItemId + ", " + newState + ')');
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();

			IWorkItem workItem;
			try {
				// Find the work item.
				workItem = worklistRepository.findWorkItem(workItemId);
			} catch (ObjectNotFoundException e) {
				throw new WMInvalidWorkItemException(workItemId);
			}

			m_serviceManager.getLockManager().lock(workItem.getProcessInstanceId());

			// If there's no state change, there's nothing to do.
			if (newState.intValue() == workItem.getState()) {
				return;
			}

			// check
			boolean refuse = false;
			if (workItem.getState() == WMWorkItemState.OPEN_RUNNING_INT
					&& newState.intValue() == WMWorkItemState.OPEN_NOTRUNNING_INT) {
				refuse = true;
			}

			java.util.Date startedDate = workItem.getStartedDate();
			if (startedDate == null)
				startedDate = new java.util.Date();

			m_worklistEngine.setWorkItemState(workItem, newState.intValue(), true,
					false, startedDate, workItem.getDueDate());

			switch (newState.intValue()) {
			case WMWorkItemState.OPEN_RUNNING_INT:
				m_worklistEngine.checkStart(workItem);
				m_worklistEngine.fireWorkItemStarted(workItem);
				break;
			case WMWorkItemState.OPEN_NOTRUNNING_INT:
				m_worklistEngine.checkRefuse(workItem, refuse);
				break;
			case WMWorkItemState.CLOSED_ABORTED_INT:
			case WMWorkItemState.CLOSED_TERMINATED_INT:
			case WMWorkItemState.CLOSED_COMPLETED_INT:
				m_worklistEngine.checkCompleted(workItem);
				break;
			}
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	/**
	 * @param workItemId
	 *          Description of the Parameter
	 * @exception WMWorkflowException
	 *              Description of the Exception
	 * @see de.objectcode.canyon.engine.IWorkflowEngine#completeWorkItem(java.lang.String)
	 */
	public void completeWorkItem(String workItemId) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("completeWorkItem(" + workItemId + ')');
		}

		changeWorkItemState(workItemId, WMWorkItemState.CLOSED_COMPLETED);
	}

	/**
	 * @param workItemId
	 * @param processInstanceId
	 * @return
	 * @throws WMWorkflowException
	 */
	public String[] findParticipants(String workItemId)
			throws WMWorkflowException {
		try {
			IWorkItem workItem = getWorkItem(workItemId);
			return m_worklistEngine.findParticipants(workItem);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	public IWorkItem[] listWorkItems(String userName, String clientId, boolean onlyOpen, int offset, int length, IFilter addFilter, String[] sortAttrs, boolean[] sortAscending) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("listWorkItems(" + userName + " " + onlyOpen + " " + addFilter + ')');
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();

			AndFilter filter = createFilter(userName, clientId, onlyOpen, addFilter);
			
			return worklistRepository.findWorkItems(filter, offset, length, sortAttrs, sortAscending);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

	private AndFilter createFilter(String userName, String clientId, boolean onlyOpen, IFilter addFilter) {
		AndFilter filter = new AndFilter();
		if (userName != null)
			filter.addFilter(new CompareFilter("participant", CompareFilter.EQ,
					userName));
		if (clientId != null)
			filter
					.addFilter(new CompareFilter("clientId", CompareFilter.EQ, clientId));

		if (onlyOpen) {
			filter.addFilter(new CompareFilter("state", CompareFilter.LE,
					WMWorkItemState.OPEN_RUNNING_INT));
		}
		
		
		if (addFilter!=null) {
			filter.addFilter(addFilter);
		}
		
		return filter;
	}
	
	
	public int indexOf(WorkItemData workItemData, String userName, String clientId, boolean onlyOpen, IFilter addFilter, String[] sortAttrs, boolean[] sortAscending) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("listWorkItems(" + userName + " " + onlyOpen + " " + addFilter + ')');
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();

			AndFilter filter = createFilter(userName, clientId, onlyOpen, addFilter);
			
			return worklistRepository.indexOf(workItemData, filter, sortAttrs, sortAscending);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}
	
	

	public int countWorkItems(String userName, String clientId, boolean onlyOpen, IFilter addFilter) throws WMWorkflowException {
		if (log.isDebugEnabled()) {
			log.debug("countWorkItems(" + userName + " " + clientId + " " + onlyOpen + " " + addFilter + ')');
		}

		try {
			IWorklistRepository worklistRepository = m_serviceManager
					.getWorklistRepository();

			AndFilter filter = createFilter(userName, clientId, onlyOpen, addFilter);

			return worklistRepository.countWorkItems(filter);
		} catch (RepositoryException e) {
			throw new WMWorkflowException(e);
		}
	}

}