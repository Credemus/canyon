package de.objectcode.canyon.worklist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMInvalidActivityInstanceException;
import org.wfmc.wapi.WMInvalidProcessDefinitionException;
import org.wfmc.wapi.WMInvalidProcessInstanceException;
import org.wfmc.wapi.WMWorkItemState;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.activities.xpdl.ActivityInfo;
import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.activity.CompletionStrategy;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.participant.ParticipantType;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.ObjectNotFoundException;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.filter.AndFilter;
import de.objectcode.canyon.spi.filter.CompareFilter;
import de.objectcode.canyon.spi.tool.IMessageHandler;
import de.objectcode.canyon.spi.tool.MessageEvent;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.worklist.spi.participant.IParticipantRepository;
import de.objectcode.canyon.worklist.spi.participant.IParticipantResolver;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;
import de.objectcode.canyon.worklist.spi.worklist.IWorklistRepository;

/**
 * @author junglas
 * @created 1. Dezember 2003
 */
public class WorklistEngine extends AbstractWorklistEngine {
  private final static Log log = LogFactory.getLog(WorklistEngine.class);

  final static String[] EMPTY_PARTICIPANTS = new String[0];

  private IWorkItemManager m_workItemManager;

  /**
   * Constructor for the WorklistEngine object
   * 
   * @param serviceManager
   *          Description of the Parameter
   */
  public WorklistEngine(ServiceManager serviceManager) {
    super(serviceManager);

    m_workItemManager = new WorkItemManager(this, serviceManager);
  }

  /**
   * @param manager
   */
  public void setWorkItemManager(IWorkItemManager manager) {
    m_workItemManager = manager;
  }

  /**
   * Sets the workItemState attribute of the WorklistEngine object
   * 
   * @param workItem
   *          The new workItemState value
   * @param workItemState
   *          The new workItemState value
   * @param throwWorkItemException
   *          The new workItemState value
   * @param forceTransitions
   *          The new workItemState value
   * @param startedDate
   *          The new workItemState value
   * @param dueDate
   *          The new workItemState value
   * @exception WMWorkflowException
   *              Description of the Exception
   */
  void setWorkItemState(IWorkItem workItem, int workItemState,
      boolean throwWorkItemException, boolean forceTransitions,
      Date startedDate, Date dueDate) throws WMWorkflowException {

    // Nothing to do if the state isn't actually changing.
    if (workItemState != workItem.getState()
        && workItemState != WMWorkItemState.DEFAULT_INT) {

      // Ensure that this is a valid transition, throwing an exception
      // if required.
      WMWorkItemState oldState = WMWorkItemState.fromInt(workItem.getState());
      int action = oldState.checkTransition(workItemState,
          throwWorkItemException && !forceTransitions);

      // If we're not re-using work items, ignore an illegal state change
      // even if the caller's trying to force it. For example, when
      // restarting an assigned loopback activity there'll be a new work
      // item in the open.notRunning state; the old work items must remain
      // in the closed state.
      if (action == WMWorkItemState.ILLEGAL_ACTION && forceTransitions) {
        // Report that the work item has started, even if this is not a
        // valid state transition according to the WfMC state machine.
        // For example, a WAPI call is not permitted the CLOSED -> OPEN
        // transition, but the engine does this internally when looping.
        action = workItemState == WMWorkItemState.OPEN_RUNNING_INT ? WMWorkItemState.START_ACTION
            : WMWorkItemState.FORCED_ACTION;
      }

      // If the transition is valid, apply the state change.
      if (action != WMWorkItemState.ILLEGAL_ACTION) {
        if (log.isDebugEnabled()) {
          log.debug("Setting work item '" + workItem.getWorkItemId()
              + "' state to " + WMWorkItemState.fromInt(workItemState));
        }

        // Update completed/due/started dates if starting/completing.
        switch (workItemState) {
        case WMWorkItemState.OPEN_NOTRUNNING_INT:
        	if (oldState==WMWorkItemState.OPEN_RUNNING) {
        		// Keep data during refuse
        		workItem.setCompletedDate(new Date());       		
        	} else {
        		workItem.setStartedDate(null);
        		workItem.setDueDate(null);
        		workItem.setCompletedDate(null);
        	}
          break;
        case WMWorkItemState.OPEN_RUNNING_INT:
          workItem.setStartedDate(startedDate);
          workItem.setDueDate(dueDate);
          workItem.setCompletedDate(null);
          break;
        case WMWorkItemState.CLOSED_COMPLETED_INT:
        case WMWorkItemState.CLOSED_TERMINATED_INT:
          // Work items should be marked as complete even when
          // they're being terminated - marking a manual-complete
          // activity complete results in its work items being
          // terminated.
          workItem.setCompletedDate(new Date());
          break;
        }

        // Set the work item state.
        workItem.setState(workItemState);

        // Notify work item listeners. Cannot notify forced actions,
        // because there is no corresponding WfMC-defined life cycle
        // event for the transition.
        if (action != WMWorkItemState.FORCED_ACTION) {
          m_serviceManager.getWorkflowEventBroker().fireWorkItemEvent(workItem,
              action, null);
        }
      }
    }
  }

  /**
   * @return
   */
  public IWorkItemManager getWorkItemManager() {
    return m_workItemManager;
  }

  /**
   * Sets the workItemState attribute of the WorklistEngine object
   * 
   * @param processInstanceId
   *          Description of the Parameter
   * @param activityInstanceId
   *          Description of the Parameter
   * @exception WMWorkflowException
   *              Description of the Exception
   */
  public void terminateWorkItems(String processInstanceId,
      String activityInstanceId) throws WMWorkflowException {
    try {
      IWorkItem workItems[] = m_serviceManager.getWorklistRepository()
          .findWorkItems(processInstanceId, activityInstanceId);
      int i;

      for (i = 0; i < workItems.length; i++) {
        setWorkItemState(workItems[i], WMWorkItemState.CLOSED_TERMINATED_INT,
            false, true, null, null);
      }
    } catch (RepositoryException e) {
      log.error("Exception", e);
      throw new WMWorkflowException(e);
    }
  }

  private String[] getEventTypes(IApplicationData appData) {
    ExtendedAttribute[] eas = appData.getExtendedAttributes();
    if (eas != null) {
      int i = 0;
      while (i < eas.length) {
        if (eas[i].getName().equals("canyon:eventType")
            && eas[i].getValue() != null) {
          return ((String) eas[i].getValue()).split(";");
        }
        i++;
      }
    }
    return new String[] { appData.getId() };
  }

  private String getEventDescriptor(IApplicationData appData) {
    StringBuffer buffy = new StringBuffer("[eventTypes={");
    String[] eventTypes = getEventTypes(appData);
    for (int j = 0; j < eventTypes.length; j++) {
      if (j != 0)
        buffy.append(",");
      String eventType = eventTypes[j];
      buffy.append("'").append(eventType).append("'");
    }
    buffy.append("},filters={");
    Parameter[] parameters = appData.getParameters();
    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      if (parameter.mode == ParameterMode.IN) {
        buffy.append(parameter.getName()).append("='").append(
            parameter.getValue()).append("',");
      }
    }
    buffy.append("}]");
    return buffy.toString();
  }

  /**
   * @param activityInfo
   *          Description of the Parameter
   * @param applicationData
   *          Description of the Parameter
   * @return Description of the Return Value
   * @exception WMWorkflowException
   *              Description of the Exception
   * @see de.objectcode.canyon.worklist.IWorklistEngine#startWorkItems(java.lang.String,
   *      java.lang.String,
   *      de.objectcode.canyon.worklist.spi.worklist.IApplicationData)
   */
  public String[] startWorkItems(String engineId, String clientId,
      IActivityInfo activityInfo, IApplicationData[] applicationData,
      Set ignoreParticipants) throws WMWorkflowException {
    if (log.isDebugEnabled()) {
      log.debug("Creating/updating work items for activity instance '"
          + activityInfo.getActivityInstanceId() + "' processInstance '"
          + activityInfo.getProcessInstanceId() + "'");
    }

    Participant[] performerDefs = activityInfo.getPerformers();

    // In OBE, performer can be a comma-separated list of performer names.
    if (performerDefs != null) {
      Set participantSet = new TreeSet();
      // (For lexical userID ordering)
      String[] participants;

      // Resolve the performers into actual participants, keeping track of
      // the resolved participant sets so that we can eliminate unwanted
      // work items afterwards.
      Map resolvedParticipants = new HashMap();
      IParticipantRepository participantRepository = m_serviceManager
          .getParticipantRepository();
      IParticipantResolver resolver = m_serviceManager.getResolverRepository()
          .findResolver(activityInfo);

      ArrayList<String> assignedPerformers = new ArrayList<String>();
      for (int i = 0; i < performerDefs.length; i++) {
        Participant performerDef = performerDefs[i];

        String performerId = performerDef.getId();

        switch (performerDef.getType().getValue()) {
        case ParticipantType.HUMAN_INT:
        case ParticipantType.ORGANIZATIONAL_UNIT_INT:
        case ParticipantType.SYSTEM_INT:
          // These are concrete participant types, and work items
          // are assigned to them directly.
          participants = new String[] { performerId };
          break;
        case ParticipantType.RESOURCE_INT:
        case ParticipantType.RESOURCE_SET_INT:
        case ParticipantType.ROLE_INT:
          // These participant types are abstract actors that must
          // be resolved by the ParticipantRepository at runtime
          // to concrete humans / programs / machines etc.
          try {

            participants = resolver.resolveParticipants(performerDef, clientId,
                activityInfo, ignoreParticipants);
          } catch (RepositoryException e) {
            log.error("Exception", e);
            throw new WMWorkflowException(e);
          }
          break;
        default:
          // Cannot happen, just to keep compiler happy.
          participants = EMPTY_PARTICIPANTS;
          break;
        }
        resolvedParticipants.put(performerId, participants);

        // Make sure there's a work item for each participant.
        for (int j = 0, n = participants.length; j < n; j++) {

          IWorkItem workItem = null;

          String participantId = participants[j];

          // be sure, a participant only gets one workitem per activity
          if (!participantSet.contains(participantId)) {
            participantSet.add(participantId);

            if (workItem == null) {
              try {
                workItem = m_serviceManager.getWorklistRepository()
                    .createWorkItem(engineId, clientId, activityInfo,
                        WMWorkItemState.OPEN_NOTRUNNING_INT, performerId,
                        participantId, applicationData);
                if (participantId.equals("event-handler")) {
                  IApplicationData appData = getEventApplication(workItem);
                  if (appData != null) {
                    if (log.isInfoEnabled()) {
                      log.info("EventHandler ist waiting for event "
                          + getEventDescriptor(appData));
                    }
                  }
                }

                // Start if only one participant or if all have to complete
                if (participants.length == 1
                    || workItem.getCompletionStrategy() == CompletionStrategy.ALL_INT) {
                  resolver.markAccepted(workItem.getParticipant(), workItem
                      .getClientId(), workItem.getPerformer(), activityInfo);
                  setWorkItemState(workItem, WMWorkItemState.OPEN_RUNNING_INT,
                      true, true, new Date(), activityInfo.getDueDate());
                }
              } catch (RepositoryException e) {
                log.error("Exception", e);
                throw new WMWorkflowException(e);
              }
              workItem.setName(activityInfo.getName());

              workItem.setPriority(activityInfo.getPriority());

              // Notify work item listeners.
              m_serviceManager.getWorkflowEventBroker().fireWorkItemCreated(
                  workItem);
            }
          }
        }
      }
      participants = (String[]) participantSet
          .toArray(new String[participantSet.size()]);
    	m_serviceManager.getBpeEngine().getEventHub().fireManualActivityStarted(clientId, activityInfo,participants);

      return participants;
    }

    return null;
  }

  /**
   * Description of the Method
   * 
   * @param workItem
   *          Description of the Parameter
   * @exception WMWorkflowException
   *              Description of the Exception
   */
  void checkCompleted(IWorkItem workItem) throws WMWorkflowException {
    if (log.isDebugEnabled()) {
      log.debug("Check completed: " + workItem.getWorkItemId());
    }

    try {
      if (workItem.getCompletionStrategy() == CompletionStrategy.ALL_INT) {
        boolean hasOpen = false;
        int completedState = WMWorkItemState.CLOSED_TERMINATED_INT;
        IWorkItem workItems[] = m_serviceManager.getWorklistRepository()
            .findWorkItems(workItem.getProcessInstanceId(),
                workItem.getActivityInstanceId());
        int i;

        for (i = 0; i < workItems.length; i++) {
          IWorkItem wi = workItems[i];
          WMWorkItemState state = WMWorkItemState.fromInt(wi.getState());
          if (state.isOpen()) {
            hasOpen = true;
            break;
          } else if (state == WMWorkItemState.CLOSED_ABORTED) {
            completedState = WMWorkItemState.CLOSED_ABORTED_INT;
          } else if (state == WMWorkItemState.CLOSED_COMPLETED) {
            completedState = WMWorkItemState.CLOSED_COMPLETED_INT;
          }
        }

        if (!hasOpen) {
          switch (completedState) {
          case WMWorkItemState.CLOSED_COMPLETED_INT:
            fireWorkItemCompleted(workItem);
            break;
          case WMWorkItemState.CLOSED_ABORTED_INT:
            fireWorkItemAborted(workItem);
            break;
          case WMWorkItemState.CLOSED_TERMINATED_INT:
            fireWorkItemTerminated(workItem);
            break;
          }
        }
      } else {
        switch (workItem.getState()) {
        case WMWorkItemState.CLOSED_COMPLETED_INT:
          fireWorkItemCompleted(workItem);
          break;
        case WMWorkItemState.CLOSED_ABORTED_INT:
          fireWorkItemAborted(workItem);
          break;
        case WMWorkItemState.CLOSED_TERMINATED_INT:
          fireWorkItemTerminated(workItem);
          break;
        }
      }

    } catch (RepositoryException e) {
      log.error("Exception", e);
      throw new WMWorkflowException(e);
    }
  }

  /**
   * Description of the Method
   * 
   * @param workItem
   *          Description of the Parameter
   * @exception WMWorkflowException
   *              Description of the Exception
   */
  void checkStart(IWorkItem workItem) throws WMWorkflowException {
    if (log.isDebugEnabled()) {
      log.debug("Check start workitem: " + workItem.getWorkItemId());
    }

		long start = 0L;
		if (log.isInfoEnabled()) {
			start = System.currentTimeMillis();
		}
    
    try {
      if (workItem.getCompletionStrategy() != CompletionStrategy.ALL_INT) {
        boolean hasOpen = false;
        int completedState = WMWorkItemState.CLOSED_TERMINATED_INT;
        IWorkItem workItems[] = m_serviceManager.getWorklistRepository()
            .findWorkItems(workItem.getProcessInstanceId(),
                workItem.getActivityInstanceId());
        int i;

        for (i = 0; i < workItems.length; i++) {
          IWorkItem wi = workItems[i];
          WMWorkItemState state = WMWorkItemState.fromInt(wi.getState());
          if (state.isOpen()) {
            if (!wi.getWorkItemId().equals(workItem.getWorkItemId())) {
              setWorkItemState(wi, WMWorkItemState.CLOSED_TERMINATED_INT, true,
                  false, null, null);
              if (log.isDebugEnabled()) {
                log.debug("set workitem " + workItem.getWorkItemId()
                    + " to state" + WMWorkItemState.CLOSED_TERMINATED_TAG);
              }
              fireWorkItemTerminated(wi);
            }
          }
        }

        IActivityInfo activityInfo = createActivityInfo(workItem);
        String processInstanceID = activityInfo.getProcessInstanceId();
        IParticipantResolver resolver = m_serviceManager
            .getResolverRepository().findResolver(activityInfo);
        resolver.markAccepted(workItem.getParticipant(),
            workItem.getClientId(), workItem.getPerformer(), activityInfo);
      }
			if (log.isInfoEnabled()) {
				long stop = System.currentTimeMillis();
				log.info("Split checkStart [" +workItem.getWorkItemId()+"]:" + (stop-start) + " ms");
			}
      
    } catch (RepositoryException e) {
      log.error("Exception", e);
      throw new WMWorkflowException(e);
    }
  }

  public String[] findParticipants(IWorkItem workItem)
      throws WMInvalidActivityInstanceException,
      WMInvalidProcessDefinitionException, RepositoryException, WMInvalidProcessInstanceException {
    IActivityInfo activityInfo = createActivityInfo(workItem);
    String processInstanceID = activityInfo.getProcessInstanceId();
    IParticipantResolver resolver = m_serviceManager.getResolverRepository()
        .findResolver(activityInfo);
      Set ignoreSet = new HashSet();
      ignoreSet.add(workItem.getParticipant());
      // be careful with stickyness
      return resolver.resolveParticipants(activityInfo.getPerformers()[0], workItem.getClientId(),
          activityInfo, ignoreSet);
  }

  /**
   * Terminates a refused workitem (open.running - open.notrunning) if strategy
   * is not ALL and all other workitems of activity are not open.
   * 
   * 
   * @param workItem
   * @param refuse
   *          Description of the Parameter
   * @throws WMWorkflowException
   */
  void checkRefuse(IWorkItem workItem, boolean refuse)
      throws WMWorkflowException {
    if (log.isDebugEnabled()) {
      log.debug("Check refuse workitem: " + workItem.getWorkItemId()
          + " refuse:" + String.valueOf(refuse));
    }
    if (!refuse) {
      return;
    }

    boolean terminate = true;
    try {
      if (workItem.getCompletionStrategy() != CompletionStrategy.ALL_INT) {

        IWorkItem workItems[] = m_serviceManager.getWorklistRepository()
            .findWorkItems(workItem.getProcessInstanceId(),
                workItem.getActivityInstanceId());

        int i;
        for (i = 0; i < workItems.length; i++) {
          IWorkItem wi = workItems[i];
          WMWorkItemState state = WMWorkItemState.fromInt(wi.getState());
          if (state.isOpen()) {
            if (!wi.getWorkItemId().equals(workItem.getWorkItemId())) {
              terminate = false;
              if (log.isDebugEnabled()) {
                log
                    .debug("can not terminate refused workitem because of open workitem: "
                        + wi.getWorkItemId());
              }
              break;
            }
          }
        }
        if (terminate) {
          startNewWorkItems(workItem);
        }
      }
    } catch (RepositoryException e) {
      log.error("Exception", e);
      throw new WMWorkflowException(e);
    }
  }

  void checkReassign(IWorkItem workItem, String targetParticipant)
      throws WMWorkflowException {
    if (log.isDebugEnabled()) {
      log.debug("Check reassign workitem: " + workItem.getWorkItemId()
          + " targetParticipant:" + targetParticipant);
    }
    try {
      IActivityInfo activityInfo = createActivityInfo(workItem);
      String processInstanceID = activityInfo.getProcessInstanceId();
      IParticipantResolver resolver = m_serviceManager.getResolverRepository()
          .findResolver(activityInfo);

      resolver.markDelegated(workItem.getParticipant(), workItem.getClientId(),
          workItem.getPerformer(), activityInfo, targetParticipant);
      
    } catch (RepositoryException e) {
      log.error("Exception", e);
      throw new WMWorkflowException(e);
    }

  }

  public IActivityInfo createActivityInfo(IWorkItem workItem)
      throws WMInvalidActivityInstanceException,
      WMInvalidProcessDefinitionException, WMInvalidProcessInstanceException {
    WorkflowProcess workflowProcess = null;
    BPEProcess processInstance = null;
    try {
      workflowProcess = (WorkflowProcess) m_serviceManager.getBpeEngine()
          .getProcessRepository().getProcessSource(
              workItem.getProcessDefinitionId().getId(), workItem.getProcessDefinitionId().getVersion());
      // this is ugly and breaks encapsulation. but we need the variables here
    } catch (RepositoryException e) {
      throw new WMInvalidProcessDefinitionException(workItem
          .getProcessDefinitionId().getId());
    }
    try {
			processInstance = m_serviceManager.getBpeEngine().getProcessInstance(workItem.getProcessInstanceId());
		} catch (RepositoryException e1) {
      throw new WMInvalidProcessInstanceException(workItem
          .getProcessDefinitionId().getId());
		}

    Activity activity = workflowProcess.getActivity(workItem
        .getActivityDefinitionId());

    // Filter out the performer
    Participant[] performers = activity.getPerformerParticipants();
    Participant performer = null;
    for (int i = 0; i < performers.length; i++) {
      if (workItem.getPerformer().equals(performers[i].getId())) {
        performer = performers[i];
        break;
      }
    }
    
  	// DynaRole
    if (performer==null) {
    	try {
				performer = workflowProcess.findParticipant(workItem.getPerformer());
			} catch (ObjectNotFoundException e) {
	      performer = new Participant();
	      performer.setId(workItem.getPerformer());
	      performer.setName(workItem.getPerformer());
	      performer.setDescription(workItem.getPerformer());
	      performer.setType(ParticipantType.ROLE); // Assuming Role here
			}
    }
    performers = new  Participant[] { performer };
        
    ActivityInfo activityInfo = new ActivityInfo(activity.getName(), activity
        .getId(), activity.getId(), workItem.getProcessDefinitionId().getId(),
        workItem.getProcessDefinitionId().getVersion(), workItem
            .getProcessInstanceId(), workItem.getParentProcessInstanceIdPath(),
        workItem.getDueDate(), workItem.getPriority(), activity
            .getCompletionStrategy().getValue(), activity.getAssignStrategy()
            .getValue(), performers, null,
        processInstance.getVariables(),activity.getExtendedAttributes(),workItem.getProcessName(), false);

    return activityInfo;
  }

  /**
   * @param workItem
   * @throws WMWorkflowException
   * @throws RepositoryException
   */
  private void startNewWorkItems(IWorkItem workItem)
      throws WMWorkflowException, RepositoryException {


    IActivityInfo activityInfo = createActivityInfo(workItem);
    String processInstanceID = activityInfo.getProcessInstanceId();
    IParticipantResolver resolver = m_serviceManager.getResolverRepository()
        .findResolver(activityInfo);

    Set ignoreParticipants = resolver.markRejected(workItem.getParticipant(), workItem.getClientId(),
        workItem.getPerformer(), activityInfo);
    //teminate the refused when no error occured
    teminateWorkItem(workItem);

    // resolve new, old application data will be transferred
    String[] participants = startWorkItems(workItem.getEngineId(), workItem
        .getClientId(), activityInfo, workItem.getApplicationData(),
        ignoreParticipants);
  }

  /**
   * @param workItem
   * @throws WMWorkflowException
   */
  private void teminateWorkItem(IWorkItem workItem) throws WMWorkflowException {
    setWorkItemState(workItem, WMWorkItemState.CLOSED_TERMINATED_INT, true,
        false, null, null);
    if (log.isDebugEnabled()) {
      log.debug("set workitem " + workItem.getWorkItemId() + " to state"
          + WMWorkItemState.CLOSED_TERMINATED_TAG);
    }
    fireWorkItemTerminated(workItem);
  }

  private boolean match(IApplicationData appData, String eventType) {
    // Std list does not support contains, so transform to set!
    Set eventTypes = new HashSet(Arrays.asList(getEventTypes(appData)));
    if (eventTypes.contains(eventType))
      return true;
    else
      return false;
  }

  private IApplicationData getEventApplication(IWorkItem workItem)
      throws RepositoryException {
    StringBuffer buffy = new StringBuffer("[");
    IApplicationData[] appDatas = workItem.getApplicationData();
    if (appDatas == null || appDatas.length == 0) {
      log
          .warn("Activity for internal participant 'event-handler' has no application attached. Will wait forever!");
      return null;
    }
    if (appDatas.length > 1) {
      log
          .warn("Activity for internal participant 'event-handler' has more than one application attached. Only first one will be used!");
    }
    return appDatas[0];
  }

  /**
   * Description of the Method
   * 
   * @param eventType
   *          Description of the Parameter
   * @param eventParameters
   *          Description of the Parameter
   * @return Description of the Return Value
   * @exception WMWorkflowException
   *              Description of the Exception
   */
  public String[] findWorkItemsForEvent(MessageEvent event)
      throws WMWorkflowException {
    String eventType = event.getEventType();
    Map eventParameters = event.getEventParams();

    if (log.isDebugEnabled()) {
      log.debug("findWorkItemsForEvent(" + eventType + " " + eventParameters
          + ')');
    }

    ArrayList result = new ArrayList();
    try {
      IWorklistRepository worklistRepository = m_serviceManager
          .getWorklistRepository();

      AndFilter filter = new AndFilter();
      filter.addFilter(new CompareFilter("clientId", CompareFilter.EQ, event
          .getClientId()));
      filter.addFilter(new CompareFilter("participant", CompareFilter.EQ,
          "event-handler"));
      filter.addFilter(new CompareFilter("state", CompareFilter.LE,
          WMWorkItemState.OPEN_RUNNING_INT));

      if (event.getProcessId()!=null) {
        filter.addFilter(new CompareFilter("processDefinitionId.id", CompareFilter.EQ,
        		event.getProcessId()));      	
      }
      IWorkItem workItems[] = worklistRepository.findWorkItems(filter);
      int i;

      for (i = 0; i < workItems.length; i++) {
        IApplicationData[] appDataSet = workItems[i].getApplicationData();
        // todo what if more than one tool matches?
        for (int k = 0; k < appDataSet.length; k++) {
          IApplicationData appData = appDataSet[k];

          if (match(appData, eventType)) {
            Parameter params[] = appData.getParameters();
            int j;

            for (j = 0; j < params.length; j++) {
              if (params[j].mode != ParameterMode.OUT) {
                Object value = eventParameters.get(params[j].formalName);

                if (value == null || !value.equals(params[j].value)) {
                  break;
                }
              }
            }

            if (j == params.length) {
              if (log.isDebugEnabled()) {
                log.debug("found matching workitem: " + workItems[i]);
              }

              result.add(workItems[i].getWorkItemId());
            }
          }
        }
      }
      String[] resultArray = new String[result.size()];
      result.toArray(resultArray);
      return resultArray;
    } catch (RepositoryException e) {
      throw new WMWorkflowException(e);
    }
  }

  /**
   * Description of the Method
   * 
   * @param workItemId
   *          Description of the Parameter
   * @param eventType
   *          Description of the Parameter
   * @param eventParameters
   *          Description of the Parameter
   * @return Description of the Return Value
   * @exception WMWorkflowException
   *              Description of the Exception
   */
  public boolean handleEvent(String workItemId, MessageEvent event)
      throws WMWorkflowException {
    String eventType = event.getEventType();
    Map eventParameters = event.getEventParams();
    if (log.isInfoEnabled()) {
      log.info("handleEvent(" + workItemId + " " + eventType + " "
          + eventParameters + ')');
    }

    try {
      IWorklistRepository worklistRepository = m_serviceManager
          .getWorklistRepository();

      IWorkItem workItem = worklistRepository.findWorkItem(workItemId);

      if (workItem != null) {
        m_serviceManager.getLockManager().lock(workItem.getProcessInstanceId());
        IApplicationData[] appDataSet = workItem.getApplicationData();
        // todo what if more than one matches: here first one wins
        for (int k = 0; k < appDataSet.length; k++) {
          IApplicationData appData = appDataSet[k];

          if (match(appData, eventType)) {
            Parameter params[] = appData.getParameters();
            int j;

            for (j = 0; j < params.length; j++) {
              if (params[j].mode != ParameterMode.OUT) {
                Object value = eventParameters.get(params[j].formalName);

                if (value == null || !value.equals(params[j].value)) {
                  break;
                }
              }
            }

            if (j == params.length) {
              for (j = 0; j < params.length; j++) {
                if (params[j].mode != ParameterMode.IN) {
                  if (params[j].formalName.equals("_canyon_eventType")) {
                    params[j] = new Parameter(params[j].formalName,
                        params[j].actualName, params[j].dataType,
                        params[j].mode, params[j].description, eventType);
                  } else {
                    params[j] = new Parameter(params[j].formalName,
                        params[j].actualName, params[j].dataType,
                        params[j].mode, params[j].description, eventParameters
                            .get(params[j].formalName));
                  }
                }
              }

              callEventHandler(appData, event);
              setWorkItemState(workItem, WMWorkItemState.CLOSED_COMPLETED_INT,
                  true, true, workItem.getStartedDate(), workItem.getDueDate());
              checkCompleted(workItem);
              return true;
            }
          }
        }
      }
    } catch (RepositoryException e) {
      throw new WMWorkflowException(e);
    }

    return false;
  }

  /**
   * @param appData
   * @param eventType
   * @param eventParameters
   */
  private void callEventHandler(IApplicationData appData, MessageEvent event)
      throws WMWorkflowException {
    Map attrMap = new HashMap();
    ExtendedAttribute[] attrs = appData.getExtendedAttributes();
    for (int i = 0; i < attrs.length; i++) {
      ExtendedAttribute attribute = attrs[i];
      attrMap.put(attribute.getName(), attribute.getValue());
    }
    String className = (String) attrMap.get("canyon:messageHandlerClass");
    if (className != null) {
      try {
        Class clazz = Class.forName(className);
        IMessageHandler messageHandler = (IMessageHandler) clazz.newInstance();
        messageHandler.handle(event);
      } catch (Exception e) {
        throw new WMWorkflowException(e);
      }
    }
  }

}