package de.objectcode.canyon.bpe.factory.xpdl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import de.objectcode.canyon.bpe.connector.SubProcessInitConnector;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Assign;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.activities.CompleteScope;
import de.objectcode.canyon.bpe.engine.activities.Empty;
import de.objectcode.canyon.bpe.engine.activities.Flow;
import de.objectcode.canyon.bpe.engine.activities.Invoke;
import de.objectcode.canyon.bpe.engine.activities.Link;
import de.objectcode.canyon.bpe.engine.activities.Receive;
import de.objectcode.canyon.bpe.engine.activities.Scope;
import de.objectcode.canyon.bpe.engine.activities.Sequence;
import de.objectcode.canyon.bpe.engine.activities.Wait;
import de.objectcode.canyon.bpe.engine.activities.While;
import de.objectcode.canyon.bpe.engine.activities.xpdl.BaseXPDLActivity;
import de.objectcode.canyon.bpe.engine.activities.xpdl.XPDLToolActivity;
import de.objectcode.canyon.bpe.engine.activities.xpdl.XPDLWorklistActivity;
import de.objectcode.canyon.bpe.engine.correlation.CorrelationPattern;
import de.objectcode.canyon.bpe.engine.correlation.CorrelationSet;
import de.objectcode.canyon.bpe.engine.evaluator.AndJoinCondition;
import de.objectcode.canyon.bpe.engine.evaluator.ComplexValueExpression;
import de.objectcode.canyon.bpe.engine.evaluator.ConstantExpression;
import de.objectcode.canyon.bpe.engine.evaluator.IAssignableExpression;
import de.objectcode.canyon.bpe.engine.evaluator.IExpression;
import de.objectcode.canyon.bpe.engine.evaluator.IJoinCondition;
import de.objectcode.canyon.bpe.engine.evaluator.OtherwiseCondition;
import de.objectcode.canyon.bpe.engine.evaluator.RuntimeConstantExpression;
import de.objectcode.canyon.bpe.engine.evaluator.ScriptExpressionFactory;
import de.objectcode.canyon.bpe.engine.evaluator.VariableExpression;
import de.objectcode.canyon.bpe.engine.evaluator.XorJoinCondition;
import de.objectcode.canyon.bpe.engine.handler.EscalationHandler;
import de.objectcode.canyon.bpe.engine.handler.FaultHandler;
import de.objectcode.canyon.bpe.engine.handler.OnAlarmHandler;
import de.objectcode.canyon.bpe.engine.variable.BasicVariable;
import de.objectcode.canyon.bpe.util.ExtendedAttributeHelper;
import de.objectcode.canyon.model.GraphConformance;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.activity.ActivitySet;
import de.objectcode.canyon.model.activity.BlockActivity;
import de.objectcode.canyon.model.activity.Deadline;
import de.objectcode.canyon.model.activity.ExecutionType;
import de.objectcode.canyon.model.activity.NoImplementation;
import de.objectcode.canyon.model.activity.SubFlow;
import de.objectcode.canyon.model.activity.Tool;
import de.objectcode.canyon.model.activity.ToolSet;
import de.objectcode.canyon.model.data.ActualParameter;
import de.objectcode.canyon.model.data.BasicType;
import de.objectcode.canyon.model.data.FormalParameter;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.participant.ParticipantType;
import de.objectcode.canyon.model.process.DurationUnit;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.model.transition.Condition;
import de.objectcode.canyon.model.transition.ConditionType;
import de.objectcode.canyon.model.transition.JoinType;
import de.objectcode.canyon.model.transition.Transition;
import de.objectcode.canyon.model.transition.TransitionRestriction;


/**
 * @author junglas
 * @created 15. Juli 2004
 */
public class ActivityBuilder {
  private BPEProcess m_bpeProcess;

  private WorkflowProcess m_workflowProcess;

  private Flow m_processFlow;

  private Map m_activityMap;

  private Map m_exceptionTransitions;

  /**
   * Constructor for the ActivityBuilder object
   *
   * @param workflowProcess
   *          Description of the Parameter
   * @param bpeProcess
   *          Description of the Parameter
   * @param processFlow
   *          Description of the Parameter
   */
  ActivityBuilder(WorkflowProcess workflowProcess, BPEProcess bpeProcess,
    Flow processFlow) {
    m_workflowProcess = workflowProcess;
    m_bpeProcess = bpeProcess;
    m_processFlow = processFlow;
    m_activityMap = new HashMap();
    m_exceptionTransitions = new HashMap();
  }

  /**
   * Description of the Method
   *
   * @param activity
   *          Description of the Parameter
   * @exception EngineException
   *              Description of the Exception
   */
  public void build(Activity activity) throws EngineException {
    IJoinCondition joinCondition = null;

    if (activity.getTransitionRestrictions() != null) {
      TransitionRestriction[] transitionRestrictions = activity.getTransitionRestrictions();
      int i;

      for (i = 0; i < transitionRestrictions.length; i++) {
        if (transitionRestrictions[i].getJoin() != null) {
          if (transitionRestrictions[i].getJoin().getType() == JoinType.XOR) {
            joinCondition = new XorJoinCondition();
          } else if (transitionRestrictions[i].getJoin().getType() == JoinType.XOR) {
            joinCondition = new AndJoinCondition();
          }
        }
      }
    }

    de.objectcode.canyon.bpe.engine.activities.Activity createdActvity = null;

    if (activity.getBlockActivity() != null) {
      createdActvity = buildBlockActivity(activity, joinCondition, activity.getBlockActivity());
    } else if ((activity.getImplementation() == null) ||
        (activity.getImplementation() instanceof NoImplementation)) {
      createdActvity = buildNoImplementation(activity, joinCondition);
    } else if (activity.getImplementation() instanceof ToolSet) {
      createdActvity = buildToolSet(activity, joinCondition, (ToolSet) activity.getImplementation());
    } else if (activity.getImplementation() instanceof SubFlow) {
      createdActvity = buildSubFlow(activity, joinCondition, (SubFlow) activity.getImplementation());
    }
    m_activityMap.put(activity.getId(), createdActvity);
  }

  /**
   * Description of the Method
   *
   * @param activity
   *          Description of the Parameter
   * @param joinCondition
   *          Description of the Parameter
   * @param blockActivity
   *          Description of the Parameter
   * @return Description of the Return Value
   * @exception EngineException
   *              Description of the Exception
   */
  private de.objectcode.canyon.bpe.engine.activities.Activity buildBlockActivity(
    Activity activity, IJoinCondition joinCondition,
    BlockActivity blockActivity) throws EngineException {
    Flow blockActivityFlow = new Flow(activity.getId(), activity.getName(),
      m_bpeProcess);

    m_processFlow.addActivity(blockActivityFlow);
    blockActivityFlow.setNonBlocked(activity.getWorkflowProcess().getPackage().getGraphConformance() !=
      GraphConformance.FULL_BLOCKED);
    if (joinCondition != null) {
      blockActivityFlow.setJoinCondition(joinCondition);
    }

    ActivitySet activitySet = blockActivity.getActivitySet();

    Activity[] activities = activitySet.getActivities();
    ActivityBuilder activityBuilder = new ActivityBuilder(m_workflowProcess,
      m_bpeProcess, blockActivityFlow);
    int i;

    for (i = 0; i < activities.length; i++) {
      activityBuilder.build(activities[i]);
    }

    Transition[] transitions = activitySet.getTransitions();

    for (i = 0; i < transitions.length; i++) {
      activityBuilder.build(transitions[i]);
    }

    return blockActivityFlow;
  }

  /**
   * Description of the Method
   *
   * @param activity
   *          Description of the Parameter
   * @param joinCondition
   *          Description of the Parameter
   * @return Description of the Return Value
   * @exception EngineException
   *              Description of the Exception
   */
  private de.objectcode.canyon.bpe.engine.activities.Activity buildNoImplementation(
    Activity activity, IJoinCondition joinCondition) throws EngineException {
    if (activity.getPerformer() == null) {
      Empty empty = new Empty(activity.getName(), m_bpeProcess);
      if (joinCondition != null) {
        empty.setJoinCondition(joinCondition);
      }
      m_processFlow.addActivity(empty);
      return empty;
    } else {
      XPDLWorklistActivity xpdlActivity = new XPDLWorklistActivity(activity.getName(), m_bpeProcess, activity);
      de.objectcode.canyon.bpe.engine.activities.Activity bpeActivity = handleDeadlines(
        activity, xpdlActivity);

      if (joinCondition != null) {
        bpeActivity.setJoinCondition(joinCondition);
      }
      m_processFlow.addActivity(bpeActivity);

      return bpeActivity;
    }
  }

  protected boolean isSystemActivity(Activity activity) {
    if (activity.getPerformer() == null) {
      return true;
    }

    if (activity.getPerformer().equals("event-handler")) {
      return false; // event-handler gets workItems!
    }

    Participant[] participants = activity.getPerformerParticipants();
    if (participants == null) {
      return true;
    }

    for (int i = 0; i < participants.length; i++) {
      if (participants[i].getType() != ParticipantType.SYSTEM) {
        return false;
      }
    }
    return true;
  }

  /**
   * Description of the Method
   *
   * @param activity
   *          Description of the Parameter
   * @param joinCondition
   *          Description of the Parameter
   * @param toolSet
   *          Description of the Parameter
   * @return Description of the Return Value
   * @exception EngineException
   *              Description of the Exception
   */
  private de.objectcode.canyon.bpe.engine.activities.Activity buildToolSet(
    Activity activity, IJoinCondition joinCondition, ToolSet toolSet) throws EngineException {
    if (isSystemActivity(activity)) {
      Sequence sequence = new Sequence(activity.getName(), m_bpeProcess);

      Tool[] tools = toolSet.getTools();

      if (tools.length == 1) {
        XPDLToolActivity xpdlActivity = new XPDLToolActivity(
          activity.getName(), m_bpeProcess, tools[0], 0);
        de.objectcode.canyon.bpe.engine.activities.Activity bpeActivity = handleDeadlines(
          activity, xpdlActivity);

        bpeActivity = handleFault(activity, bpeActivity);
        if (joinCondition != null) {
          bpeActivity.setJoinCondition(joinCondition);
        }

        m_processFlow.addActivity(bpeActivity);

        return bpeActivity;
      } else {
        int i;
        boolean isEmpty = true;

        for (i = 0; i < tools.length; i++) {
          XPDLToolActivity xpdlActivity = new XPDLToolActivity(activity.getName(), m_bpeProcess, tools[i], i);
          de.objectcode.canyon.bpe.engine.activities.Activity bpeActivity = handleDeadlines(
            activity, xpdlActivity);

          sequence.addActivity(bpeActivity);
          isEmpty = false;
        }

        de.objectcode.canyon.bpe.engine.activities.Activity result = handleFault(activity, sequence);

        if (isEmpty) {
          Empty empty = new Empty(activity.getName(), m_bpeProcess);
          if (joinCondition != null) {
            empty.setJoinCondition(joinCondition);
          }
          m_processFlow.addActivity(empty);
          return empty;
        } else {
          if (joinCondition != null) {
            result.setJoinCondition(joinCondition);
          }
          m_processFlow.addActivity(result);
          return result;
        }
      }

    } else {
      // buildNoImpl takes care of case with no Tools
      Tool[] tools = toolSet.getTools();
      XPDLWorklistActivity xpdlActivity = new XPDLWorklistActivity(activity.getName(), m_bpeProcess, tools);
      de.objectcode.canyon.bpe.engine.activities.Activity bpeActivity = handleDeadlines(
        activity, xpdlActivity);

      if (joinCondition != null) {
        bpeActivity.setJoinCondition(joinCondition);
      }
      m_processFlow.addActivity(bpeActivity);
      return bpeActivity;
    }
  }

  /**
   * Description of the Method
   *
   * @param activity
   *          Description of the Parameter
   * @param joinCondition
   *          Description of the Parameter
   * @param subFlow
   *          Description of the Parameter
   * @return Description of the Return Value
   * @exception EngineException
   *              Description of the Exception
   */
  private de.objectcode.canyon.bpe.engine.activities.Activity buildSubFlow(
    Activity activity, IJoinCondition joinCondition, SubFlow subFlow) throws EngineException {
    Sequence sequence = new Sequence(activity.getName(), m_bpeProcess);

    if (joinCondition != null) {
      sequence.setJoinCondition(joinCondition);
    }

    m_processFlow.addActivity(sequence);

    Invoke invoke = new Invoke(activity.getName(), m_bpeProcess, "worklist");

    CorrelationSet correlationSet = buildSubFlowRequest(activity, subFlow,
      invoke);

    sequence.addActivity(invoke);

    // TODO check for formal out parameters!
    if (subFlow.getExecution() != ExecutionType.ASYNCHRONOUS) {
      Receive receive = new Receive(activity.getName(), m_bpeProcess,
        subFlow.getWorkflowProcess().getId() + "-finish",
        m_bpeProcess.getType(subFlow.getWorkflowProcess().getId() +
          "-response"));

      buildSubFlowResponse(activity, correlationSet, subFlow, receive);

      sequence.addActivity(receive);
    }

    return sequence;
  }

  /**
   * Description of the Method
   *
   * @param activity
   *          Description of the Parameter
   * @param subFlow
   *          Description of the Parameter
   * @param invoke
   *          Description of the Parameter
   * @return Description of the Return Value
   * @exception EngineException
   *              Description of the Exception
   */
  private CorrelationSet buildSubFlowRequest(Activity activity,
    SubFlow subFlow, Invoke invoke) throws EngineException {
    invoke.setConnector(new SubProcessInitConnector());
    invoke.setOutMessageType(m_bpeProcess.getType("subflow-request"));
    invoke.setMessageOperation(subFlow.getWorkflowProcess().getId() + "-init");

    ComplexValueExpression outputExpression = new ComplexValueExpression(
      m_bpeProcess.getType("subflow-request"));

    outputExpression.addExpression("parentProcessId",
      new RuntimeConstantExpression(
        RuntimeConstantExpression.PROCESSINSTANCEID));
    outputExpression.addExpression("parentProcessIdPath",
      new RuntimeConstantExpression(
        RuntimeConstantExpression.PROCESSINSTANCEIDPATH));
    outputExpression.addExpression("parentActivityId",
      new RuntimeConstantExpression(RuntimeConstantExpression.ACTIVITYID));

    ActualParameter[] actualParameters = subFlow.getActualParameters();
    FormalParameter[] formalParameters = subFlow.getWorkflowProcess().getFormalParameters();
    int i;

    for (i = 0; i < actualParameters.length; i++) {
      if (formalParameters[i].getMode() != ParameterMode.OUT) {
        IExpression expression;

        if (m_workflowProcess.getPackage().getScript() != null) {
          expression = ScriptExpressionFactory.createExpression(
            m_workflowProcess.getPackage().getScript().getType(),
            actualParameters[i].getText());
        } else {
          expression = ScriptExpressionFactory.createExpression("text/xpath",
            actualParameters[i].getText());
        }

        outputExpression.addExpression(formalParameters[i].getId(), expression);
      }
    }

    invoke.setOutputExpression(outputExpression);

    CorrelationSet correlationSet = new CorrelationSet("SubFlow" +
      activity.getId(),
      new IAssignableExpression[] {
        new VariableExpression("parentProcessId"),
        new VariableExpression("parentActivityId")
      });

    invoke.addCorrelation(correlationSet, true, CorrelationPattern.OUT);

    return correlationSet;
  }


  /**
   * Description of the Method
   *
   * @param activity
   *          Description of the Parameter
   * @param correlationSet
   *          Description of the Parameter
   * @param receive
   *          Description of the Parameter
   * @param subFlow
   *          Description of the Parameter
   * @exception EngineException
   *              Description of the Exception
   */
  private void buildSubFlowResponse(Activity activity,
    CorrelationSet correlationSet, SubFlow subFlow, Receive receive) throws EngineException {
    receive.addCorrelation(correlationSet, false);

    ComplexValueExpression inputExpression = new ComplexValueExpression(
      m_bpeProcess.getType("subflow-response"));

    ActualParameter[] actualParameters = subFlow.getActualParameters();
    FormalParameter[] formalParameters = subFlow.getWorkflowProcess().getFormalParameters();
    int i;
    boolean hasInput = false;

    for (i = 0; i < actualParameters.length; i++) {
      if (formalParameters[i].getMode() != ParameterMode.IN) {
        IExpression expression = new VariableExpression(actualParameters[i].getText());

        inputExpression.addExpression(formalParameters[i].getId(), expression);
        hasInput = true;
      }
    }

    if (hasInput) {
      receive.setInputExpression(inputExpression);
    }
  }

  /**
   * Description of the Method
   *
   * @param transition
   *          Description of the Parameter
   * @exception EngineException
   *              Description of the Exception
   */
  public void build(Transition transition) throws EngineException {
    if (m_exceptionTransitions.containsKey(transition.getId())) {
      de.objectcode.canyon.bpe.engine.activities.Activity from = (de.objectcode.canyon.bpe.engine.activities.Activity)
        m_exceptionTransitions.get(transition.getId());
      de.objectcode.canyon.bpe.engine.activities.Activity to = (de.objectcode.canyon.bpe.engine.activities.Activity)
        m_activityMap.get(transition.getTo());

      Link link = from.addLink(transition.getId(), to);
    } else {
      de.objectcode.canyon.bpe.engine.activities.Activity from = (de.objectcode.canyon.bpe.engine.activities.Activity)
        m_activityMap.get(transition.getFrom());
      de.objectcode.canyon.bpe.engine.activities.Activity to = (de.objectcode.canyon.bpe.engine.activities.Activity)
        m_activityMap.get(transition.getTo());

      Link link = from.addLink(transition.getId(), to);

      Condition condition = transition.getCondition();

      if (condition != null) {
        if (condition.getType() == ConditionType.CONDITION) {
          if (m_workflowProcess.getPackage().getScript() != null) {
            link.setTransitionCondition(ScriptExpressionFactory.createCondition(
                m_workflowProcess.getPackage().getScript().getType(), condition.getValue()));
          } else {
            link.setTransitionCondition(ScriptExpressionFactory.createCondition("text/xpath", condition.getValue()));
          }
        } else if (condition.getType() == ConditionType.OTHERWISE) {
          OtherwiseCondition otherwise = new OtherwiseCondition();

          Map otherTransitions = transition.getFromActivity().getOutboundTransitions();
          Iterator it = otherTransitions.values().iterator();

          while (it.hasNext()) {
            Transition other = (Transition) it.next();

            if ((other != transition) && (other.getCondition() != null) &&
                (other.getCondition().getType() == ConditionType.CONDITION)) {
              if (m_workflowProcess.getPackage().getScript() != null) {
                otherwise.addCondition(ScriptExpressionFactory.createCondition(
                    m_workflowProcess.getPackage().getScript().getType(), other.getCondition().getValue()));
              } else {
                otherwise.addCondition(ScriptExpressionFactory.createCondition(
                    "text/xpath", other.getCondition().getValue()));
              }
            }
          }

          link.setTransitionCondition(otherwise);
        }
      }
    }
  }

  /**
   * Description of the Method
   *
   * @param activity
   *          Description of the Parameter
   * @param xpdlActivty
   *          Description of the Parameter
   * @return Description of the Return Value
   */
  private de.objectcode.canyon.bpe.engine.activities.Activity handleDeadlines(
    Activity activity, BaseXPDLActivity xpdlActivty) throws EngineException {
    DurationUnit defaultDurationUnit = activity.getWorkflowProcess().getProcessHeader().getDurationUnit();
    String defaultExceptionId = null;
    Iterator it = activity.getOutboundTransitions().values().iterator();

    while (it.hasNext()) {
      Transition transition = (Transition) it.next();

      if ((transition.getCondition() != null) &&
          (transition.getCondition().getType() == ConditionType.DEFAULTEXCEPTION)) {
        defaultExceptionId = transition.getId();
      }
    }

    Deadline[] deadlines = activity.getDeadlines();

    if (((deadlines == null) || (deadlines.length == 0)) &&
        (activity.getLimit() == null) && (defaultExceptionId == null)) {
      return xpdlActivty;
    } else {
      int i;
      boolean hasAsync = false;
      ExecutionType limitExecution = ExecutionType.SYNCHRONOUS;

      for (i = 0; i < deadlines.length; i++) {
        if (deadlines[i].getExecutionType() == ExecutionType.ASYNCHRONOUS) {
          hasAsync = true;
          break;
        }
      }
      if (activity.getLimit() != null) {
        if (activity.getExtendedAttribute("canyon:LimitExceededExecution") != null) {
          limitExecution = ExecutionType.fromString(activity.getExtendedAttribute(
            "canyon:LimitExceededExecution").getValue());
        } else if (activity.getWorkflowProcess().getExtendedAttribute(
              "canyon:LimitExceededExecution") != null) {
          limitExecution = ExecutionType.fromString(activity.getWorkflowProcess().getExtendedAttribute(
            "canyon:LimitExceededExecution").getValue());
        }

        if (limitExecution == ExecutionType.ASYNCHRONOUS) {
          hasAsync = true;
        }
      }

      Scope scope = new Scope(activity.getName(), m_bpeProcess);
      Flow flow = null;

      if (hasAsync) {
        flow = new Flow(activity.getName(), scope);

        scope.setActivity(flow);

        flow.addActivity(xpdlActivty);

        CompleteScope completeScope = new CompleteScope(activity.getName() +
          " complete", scope);

        flow.addActivity(completeScope);
        xpdlActivty.addLink(activity.getName() + " complete", completeScope);
      } else {
        scope.setActivity(xpdlActivty);
      }

      for (i = 0; i < deadlines.length; i++) {
        if (deadlines[i].getDeadlineCondition().isDynamic()) {
          IExpression expr;

          if (activity.getWorkflowProcess().getPackage().getScript() != null) {
            expr = ScriptExpressionFactory.createExpression(activity.getWorkflowProcess().getPackage().getScript()
              .getType(),
              deadlines[i].getDeadlineCondition().getDeadlineConditionExpr());
          } else {
            expr = ScriptExpressionFactory.createExpression("text/xpath",
              deadlines[i].getDeadlineCondition().getDeadlineConditionExpr());
          }

          if (deadlines[i].getExecutionType() == ExecutionType.ASYNCHRONOUS) {
            Wait wait = new Wait(activity.getName(), scope);

            wait.setExpression(expr, defaultDurationUnit);

            flow.addActivity(wait);

            m_exceptionTransitions.put(deadlines[i].getTransition().getId(),
              wait);
          } else {
            OnAlarmHandler alarmHandler = new OnAlarmHandler(expr, defaultDurationUnit);
            Empty empty = new Empty(activity.getName(), scope);

            alarmHandler.setActivity(empty);

            scope.addEventHandler(alarmHandler);

            m_exceptionTransitions.put(deadlines[i].getTransition().getId(),
              empty);
          }
        } else {
          if (deadlines[i].getExecutionType() == ExecutionType.ASYNCHRONOUS) {
            Wait wait = new Wait(activity.getName(), scope);

            wait.setForDuration(deadlines[i].getDeadlineCondition(), defaultDurationUnit);

            flow.addActivity(wait);

            m_exceptionTransitions.put(deadlines[i].getTransition().getId(),
              wait);
          } else {
            OnAlarmHandler alarmHandler = new OnAlarmHandler(deadlines[i].getDeadlineCondition(), defaultDurationUnit);
            Empty empty = new Empty(activity.getName(), scope);

            alarmHandler.setActivity(empty);

            scope.addEventHandler(alarmHandler);

            m_exceptionTransitions.put(deadlines[i].getTransition().getId(),
              empty);
          }
        }
      }

      if (activity.getLimit() != null) {
        String escalationProcessId = null;

        if (activity.getExtendedAttribute("canyon:LimitExceeded") != null) {
          escalationProcessId = activity.getExtendedAttribute(
            "canyon:LimitExceeded").getValue();
        } else if (activity.getWorkflowProcess().getExtendedAttribute(
              "canyon:LimitExceeded") != null) {
          escalationProcessId = activity.getWorkflowProcess().getExtendedAttribute("canyon:LimitExceeded").getValue();
        }

        if ((escalationProcessId != null) &&
            (escalationProcessId.trim().length() > 0)) {
          Invoke invoke = new Invoke("Limit-" + activity.getId(), scope,
            escalationProcessId + "-init");

          invoke.setConnector(new SubProcessInitConnector());
          invoke.setOutMessageType(m_bpeProcess.getType("subflow-request"));

          if (limitExecution == ExecutionType.ASYNCHRONOUS) {
            Wait wait = new Wait(activity.getName(), scope);

            wait.setForDuration(activity.getLimit(), defaultDurationUnit);

            flow.addActivity(wait);
            flow.addActivity(invoke);


            wait.addLink("Limit-" + activity.getId() + "-exceed", invoke);
          } else {
            OnAlarmHandler alarmHandler = new OnAlarmHandler(activity.getLimit(), defaultDurationUnit);

            alarmHandler.setActivity(invoke);

            scope.addEventHandler(alarmHandler);
          }
        } else {
          ExtendedAttributeHelper eah = new ExtendedAttributeHelper(activity);
          if (eah.hasAttributeValue("canyon:escalationHandler")) {
            // EscalationHandlers are only used for manual activities
            if (xpdlActivty instanceof XPDLWorklistActivity) {
              scope.setActivity(handleSyncEscalation(eah, activity, (XPDLWorklistActivity) xpdlActivty));
            }
          }
        }
      }

      if (defaultExceptionId != null) {
        FaultHandler faultHandler = new FaultHandler(defaultExceptionId);
        Empty empty = new Empty(activity.getName() + "-fault", scope);

        scope.addFaultHandler(faultHandler);
        faultHandler.setActivity(empty);

        m_exceptionTransitions.put(defaultExceptionId, empty);
      }
      return scope;
    }
  }


  protected de.objectcode.canyon.bpe.engine.activities.Activity handleFault(Activity activity,
    de.objectcode.canyon.bpe.engine.activities.Activity faultableActivity) throws EngineException {
    ExtendedAttributeHelper eah = new ExtendedAttributeHelper(activity);
    if (eah.hasAttributeValue("canyon:faultHandler")) {
      String faultProcessId = eah.getMandatoryValue("canyon:faultHandler");

      // the faultContextId is the name of a data field which describes the context
      String faultContextId = eah.getOptionalValue("canyon:faultHandler:faultContextId");

      String faultVariableName = "FV" + activity.getId() + "_faultState";
      String faultCounterVariableName = "FC" + activity.getId() + "_faultCounter";
      Scope variableScope = new Scope(activity.getName() + " faultVariableScope", m_bpeProcess);
      variableScope.addVariable(new BasicVariable(faultVariableName, BasicType.STRING, "START"));

      Sequence seq = new Sequence(activity.getName() + " faultSequence", variableScope);
      Assign init = new Assign(activity.getName() + " faultInit", m_bpeProcess);
      init.addCopy(new ConstantExpression("START"), new VariableExpression(faultVariableName));

      While whle = new While(activity.getName() + " faultWhile", m_bpeProcess);
      whle.setCondition(ScriptExpressionFactory.createCondition("text/beanshell",
          faultVariableName + ".equals(\"START\") || " + faultVariableName + ".equals(\"RETRY\")"));

      Scope scope = new Scope(activity.getName() + " faultScope", m_bpeProcess);
      Sequence innerSeq = new Sequence(activity.getName() + " innerFaultSequence", scope);
      innerSeq.addActivity(faultableActivity);

      Assign exit = new Assign(activity.getName() + " faultExit", m_bpeProcess);
      exit.addCopy(new ConstantExpression("STOP"), new VariableExpression(faultVariableName));
      innerSeq.addActivity(exit);
      scope.setActivity(innerSeq);

      FaultHandler faultHandler = new FaultHandler();
      Sequence invokeSeq = buildFaultFlowInvokeSequence(faultProcessId, faultContextId, faultVariableName, activity,
        faultHandler, scope);
      faultHandler.setActivity(invokeSeq);
      scope.addFaultHandler(faultHandler);

      whle.addActivity(scope);
      seq.addActivity(init);
      seq.addActivity(whle);
      variableScope.setActivity(seq);
      return variableScope;
    } else {
      return faultableActivity;
    }
  }

  protected de.objectcode.canyon.bpe.engine.activities.Activity handleSyncEscalation(ExtendedAttributeHelper eah,
    Activity activity, XPDLWorklistActivity escalatableActivity) throws EngineException {
    String escalationProcessId = eah.getMandatoryValue("canyon:escalationHandler");

    // the faultContextId is the name of a data field which describes the context
    String escalationContextId = eah.getOptionalValue("canyon:escalationHandler:escalationContextId");

    Scope variableScope = new Scope(activity.getName() + " escalationVariableScope", m_bpeProcess);
    variableScope.addVariable(new BasicVariable(EscalationHandler.STATE_VARIABLE_NAME, BasicType.STRING, "START"));
    variableScope.addVariable(new BasicVariable(EscalationHandler.RETRY_PERFORMER_VARIABLE_NAME, BasicType.STRING,
        null));
    variableScope.addVariable(new BasicVariable(EscalationHandler.RETRY_DUE_DATE_VARIABLE_NAME, BasicType.DATETIME,
        null));

    Sequence seq = new Sequence(activity.getName() + " escalationSequence", variableScope);
    Assign init = new Assign(activity.getName() + " escalationInit", m_bpeProcess);
    init.addCopy(new ConstantExpression("START"), new VariableExpression(EscalationHandler.STATE_VARIABLE_NAME));

    While whle = new While(activity.getName() + " escalationWhile", m_bpeProcess);
    whle.setCondition(ScriptExpressionFactory.createCondition("text/beanshell",
        EscalationHandler.STATE_VARIABLE_NAME + ".equals(\"START\") || " + EscalationHandler.STATE_VARIABLE_NAME +
        ".equals(\"RETRY\")"));

    Scope scope = new Scope(activity.getName() + " escalationScope", m_bpeProcess);
    Sequence innerSeq = new Sequence(activity.getName() + " innerEscalationSequence", scope);
    innerSeq.addActivity(escalatableActivity);

    Assign exit = new Assign(activity.getName() + " escalationExit", m_bpeProcess);
    exit.addCopy(new ConstantExpression("STOP"), new VariableExpression(EscalationHandler.STATE_VARIABLE_NAME));
    innerSeq.addActivity(exit);
    scope.setActivity(innerSeq);

    EscalationHandler escalationHandler = new EscalationHandler(activity, escalatableActivity);
    Sequence invokeSeq = buildEscalationFlowInvokeSequence(escalationProcessId, escalationContextId,
      activity, escalationHandler, scope);
    escalationHandler.setActivity(invokeSeq);
    scope.addEventHandler(escalationHandler);

    whle.addActivity(scope);
    seq.addActivity(init);
    seq.addActivity(whle);
    variableScope.setActivity(seq);
    return variableScope;
  }

  public Sequence buildEscalationFlowInvokeSequence(String escalationProcessId, String escalationContextId,
    Activity activity, EscalationHandler escalationHandler, Scope scope) {
    Sequence invokeSeq = new Sequence(activity.getName() + " escalationInvokeSequence", scope);
    Invoke exceptionInvoke = buildEscalationFlowInvoker(escalationProcessId, escalationContextId, escalationHandler,
      scope);
    invokeSeq.addActivity(exceptionInvoke);

    CorrelationSet correlationSet = new CorrelationSet("SubFlow" +
      activity.getId(),
      new IAssignableExpression[] {
        new VariableExpression("parentProcessId"),
        new VariableExpression("parentActivityId")
      });

    exceptionInvoke.addCorrelation(correlationSet, true, CorrelationPattern.OUT);

    Receive receive = new Receive(activity.getName(), scope, escalationProcessId +
      "-finish", m_bpeProcess.getType(escalationProcessId +
        "-response"));

    receive.addCorrelation(correlationSet, false);

    ComplexValueExpression inputExpression = new ComplexValueExpression(
      m_bpeProcess.getType("subflow-response"));
    inputExpression.addExpression("escalationState", new VariableExpression(EscalationHandler.STATE_VARIABLE_NAME));
    inputExpression.addExpression("escalationRetryDueDate",
      new VariableExpression(EscalationHandler.RETRY_DUE_DATE_VARIABLE_NAME));
    inputExpression.addExpression("escalationRetryPerformer",
      new VariableExpression(EscalationHandler.RETRY_PERFORMER_VARIABLE_NAME));
    receive.setInputExpression(inputExpression);
    invokeSeq.addActivity(receive);
    return invokeSeq;
  }

  public Sequence buildFaultFlowInvokeSequence(String faultProcessId, String faultContextId, String faultVariableName,
    Activity activity, FaultHandler faultHandler, Scope scope) {
    Sequence invokeSeq = new Sequence(activity.getName() + " faultInvokeSequence", scope);
    Invoke exceptionInvoke = buildFaultFlowInvoker(faultProcessId, faultContextId, faultHandler, scope);
    invokeSeq.addActivity(exceptionInvoke);

    CorrelationSet correlationSet = new CorrelationSet("SubFlow" +
      activity.getId(),
      new IAssignableExpression[] {
        new VariableExpression("parentProcessId"),
        new VariableExpression("parentActivityId")
      });

    exceptionInvoke.addCorrelation(correlationSet, true, CorrelationPattern.OUT);

    Receive receive = new Receive(activity.getName(), scope, faultProcessId +
      "-finish", m_bpeProcess.getType(faultProcessId +
        "-response"));

    receive.addCorrelation(correlationSet, false);

    ComplexValueExpression inputExpression = new ComplexValueExpression(
      m_bpeProcess.getType("subflow-response"));
    inputExpression.addExpression("faultState", new VariableExpression(faultVariableName));
    receive.setInputExpression(inputExpression);
    invokeSeq.addActivity(receive);
    return invokeSeq;
  }

  public Invoke buildEscalationFlowInvoker(String escalationProcessId, String escalationContextId,
    EscalationHandler escalationHandler, Scope scope) {
    Invoke exceptionInvoke = new Invoke("Escalation-" + escalationProcessId, scope,
      escalationProcessId + "-init");

    exceptionInvoke.setConnector(new SubProcessInitConnector());
    exceptionInvoke.setOutMessageType(m_bpeProcess.getType("subflow-request"));

    exceptionInvoke.setMessageOperation(escalationProcessId + "-init");

    ComplexValueExpression outputExpression = new ComplexValueExpression(
      m_bpeProcess.getType("subflow-request"));

    outputExpression.addExpression("parentProcessId",
      new RuntimeConstantExpression(
        RuntimeConstantExpression.PROCESSINSTANCEID));
    outputExpression.addExpression("parentProcessIdPath",
      new RuntimeConstantExpression(
        RuntimeConstantExpression.PROCESSINSTANCEIDPATH));
    outputExpression.addExpression("parentActivityId",
      new RuntimeConstantExpression(RuntimeConstantExpression.ACTIVITYID));

    String[] variableNames = escalationHandler.getEscalationVariableParameterNames();
    String[] parameterNames = escalationHandler.getEscalationProcessParameterNames();
    for (int i = 0; i < variableNames.length; i++) {
      outputExpression.addExpression(parameterNames[i], new VariableExpression(variableNames[i]));
    }

    //    outputExpression.addExpression("escalationType", new VariableExpression(escalationHandler.getTypeVariableName()));
    //    outputExpression.addExpression("escalationMessage", new VariableExpression(escalationHandler.getMessageVariableName()));
    //    outputExpression.addExpression("escalationContext", new VariableExpression(escalationHandler.getStackTraceVariableName()));
    if (escalationContextId != null) {
      outputExpression.addExpression("escalationContextId", new VariableExpression(escalationContextId));
    }

    exceptionInvoke.setOutputExpression(outputExpression);

    return exceptionInvoke;
  }

  public Invoke buildFaultFlowInvoker(String faultProcessId, String faultContextId, FaultHandler faultHandler,
    Scope scope) {
    Invoke exceptionInvoke = new Invoke("Exception-" + faultProcessId, scope,
      faultProcessId + "-init");

    exceptionInvoke.setConnector(new SubProcessInitConnector());
    exceptionInvoke.setOutMessageType(m_bpeProcess.getType("subflow-request"));

    exceptionInvoke.setMessageOperation(faultProcessId + "-init");

    ComplexValueExpression outputExpression = new ComplexValueExpression(
      m_bpeProcess.getType("subflow-request"));

    outputExpression.addExpression("parentProcessId",
      new RuntimeConstantExpression(
        RuntimeConstantExpression.PROCESSINSTANCEID));
    outputExpression.addExpression("parentProcessIdPath",
      new RuntimeConstantExpression(
        RuntimeConstantExpression.PROCESSINSTANCEIDPATH));
    outputExpression.addExpression("parentActivityId",
      new RuntimeConstantExpression(RuntimeConstantExpression.ACTIVITYID));

    String[] variableNames = faultHandler.getFaultVariableParameterNames();
    String[] parameterNames = faultHandler.getFaultProcessParameterNames();
    for (int i = 0; i < variableNames.length; i++) {
      outputExpression.addExpression(parameterNames[i], new VariableExpression(variableNames[i]));
    }

    //    outputExpression.addExpression("faultType", new VariableExpression(faultHandler.getTypeVariableName()));
    //    outputExpression.addExpression("faultMessage", new VariableExpression(faultHandler.getMessageVariableName()));
    //    outputExpression.addExpression("faultContext", new VariableExpression(faultHandler.getStackTraceVariableName()));
    if (faultContextId != null) {
      outputExpression.addExpression("faultContextId", new VariableExpression(faultContextId));
    }

    exceptionInvoke.setOutputExpression(outputExpression);

    return exceptionInvoke;
  }

}
