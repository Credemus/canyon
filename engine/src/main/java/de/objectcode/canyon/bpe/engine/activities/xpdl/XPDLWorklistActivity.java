package de.objectcode.canyon.bpe.engine.activities.xpdl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.wfmc.wapi.WMWorkflowException;
import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Scope;
import de.objectcode.canyon.bpe.engine.evaluator.IExpression;
import de.objectcode.canyon.bpe.engine.evaluator.ScriptExpressionFactory;
import de.objectcode.canyon.bpe.engine.handler.EscalationHandler;
import de.objectcode.canyon.bpe.engine.handler.OnAlarmHandler;
import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.bpe.util.DeadlineHelper;
import de.objectcode.canyon.bpe.util.ExtendedAttributeHelper;
import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.activity.Deadline;
import de.objectcode.canyon.model.activity.Tool;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.participant.ParticipantType;
import de.objectcode.canyon.model.process.Duration;
import de.objectcode.canyon.model.process.DurationUnit;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.worklist.spi.worklist.DefaultApplicationData;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;


/**
 * @author junglas
 * @created 15. Juli 2004
 */
public class XPDLWorklistActivity extends BaseXPDLActivity {
  static final long serialVersionUID = -4136548107324160768L;

  private static final Log log = LogFactory.getLog(XPDLWorklistActivity.class);

  private String m_scriptLanguage;

  private ApplicationInfo[] m_applicationData;

  private ToolInfo[] m_toolData;

  private int m_completionStrategy;

  private int m_assignStrategy;

  private Participant[] m_performers;

  private boolean m_dueDateIsDeadline = false;

  private boolean m_dueDateIsRelativeToActivityStart = false;

  private DurationUnit m_defaultDurationUnit = DurationUnit.DAY;

  private Deadline m_deadline;

  private Duration m_limit;

  private String m_activityNameExpression;

  private String m_processNameExpression;

  private String m_processName;

  private ExtendedAttribute[] m_extendedAttributes;

  private transient long m_dueDate;

  /**
   * Constructor for the XPDLWorklistActivity object
   *
   * @param name
   *          Description of the Parameter
   * @param scope
   *          Description of the Parameter
   * @param activity
   *          Description of the Parameter
   */
  public XPDLWorklistActivity(String name, Scope scope,
    de.objectcode.canyon.model.activity.Activity activity) {
    super(name, scope, activity);

    m_scriptLanguage = (activity.getWorkflowProcess().getPackage().getScript() != null)
      ? activity.getWorkflowProcess().getPackage().getScript().getType() : null;
    m_completionStrategy = activity.getCompletionStrategy().getValue();
    m_assignStrategy = activity.getAssignStrategy().getValue();
    m_performers = activity.getPerformerParticipants();
    m_dueDateIsDeadline = getDueDateIsDeadline(activity.getWorkflowProcess());
    if ((activity.getDeadlines() != null) && (activity.getDeadlines().length > 0)) {
      m_deadline = activity.getDeadlines()[0];
    }
    m_limit = getLimit(activity);
    m_applicationData = new ApplicationInfo[0];
    m_toolData = new ToolInfo[0];
    m_activityNameExpression = activity.getExtendedAttributeValue("canyon:ActivityName");
    m_processNameExpression = activity.getWorkflowProcess().getExtendedAttributeValue("canyon:ProcessName");

    // backward compatability
    if (m_processNameExpression == null) {
      m_processNameExpression = activity.getWorkflowProcess().getExtendedAttributeValue("flow_ProcessDescription");
    }
    m_processName = activity.getWorkflowProcess().getName();
    m_extendedAttributes = activity.getExtendedAttributes();
    m_defaultDurationUnit = getDefaultDurationUnit(activity.getWorkflowProcess());
    m_dueDateIsRelativeToActivityStart = getDueDateIsRelativeToActivityStart(activity);
  }

  private boolean getDueDateIsRelativeToActivityStart(
    de.objectcode.canyon.model.activity.Activity activity) {
    ExtendedAttributeHelper eah = new ExtendedAttributeHelper(activity);
    return eah.getOptionalBooleanValue(
      ExtendedAttributeHelper.DUE_DATE_IS_RELATIVE_TO_ACTIVITY_START, false);
  }

  private Duration getLimit(
    de.objectcode.canyon.model.activity.Activity activity) {
    Duration result = activity.getLimit();
    if (result == null) {
      result = activity.getWorkflowProcess().getProcessHeader().getLimit();
    }
    return result;
  }

  private DurationUnit getDefaultDurationUnit(WorkflowProcess workflowProcess) {
    DurationUnit durationUnit = workflowProcess.getProcessHeader().getDurationUnit();
    if (durationUnit == null) {
      durationUnit = DurationUnit.DAY;
    }
    return durationUnit;
  }

  private boolean getDueDateIsDeadline(WorkflowProcess workflowProcess) {
    ExtendedAttribute dueDateIsDeadLineAttribute = workflowProcess.getExtendedAttribute("canyon:DueDateIsDeadline");
    if (dueDateIsDeadLineAttribute == null) {
      return false;
    } else {
      return "true".equalsIgnoreCase(dueDateIsDeadLineAttribute.getValue());
    }
  }

  /**
   * Constructor for the XPDLWorklistActivity object
   *
   * @param name
   *          Description of the Parameter
   * @param scope
   *          Description of the Parameter
   * @param tool
   *          Description of the Parameter
   */
  public XPDLWorklistActivity(String name, Scope scope, Tool[] tools) {
    // this method is always called with at least one tool!
    super(name, scope, tools[0].getActivity());

    de.objectcode.canyon.model.activity.Activity activity = tools[0].getActivity();
    m_scriptLanguage = (activity.getWorkflowProcess().getPackage().getScript() != null)
      ? activity.getWorkflowProcess().getPackage().getScript().getType() : null;
    m_completionStrategy = activity.getCompletionStrategy().getValue();
    m_assignStrategy = activity.getAssignStrategy().getValue();
    m_extendedAttributes = activity.getExtendedAttributes();
    m_performers = activity.getPerformerParticipants();
    m_dueDateIsDeadline = getDueDateIsDeadline(activity.getWorkflowProcess());
    if ((activity.getDeadlines() != null) && (activity.getDeadlines().length > 0)) {
      m_deadline = activity.getDeadlines()[0];
    }
    m_limit = getLimit(activity);
    m_toolData = new ToolInfo[tools.length];
    m_applicationData = new ApplicationInfo[tools.length];
    m_activityNameExpression = tools[0].getActivity().getExtendedAttributeValue("canyon:ActivityName");
    m_processNameExpression = activity.getWorkflowProcess().getExtendedAttributeValue("canyon:ProcessName");

    // backward compatability
    if (m_processNameExpression == null) {
      m_processNameExpression = activity.getWorkflowProcess().getExtendedAttributeValue("flow_ProcessDescription");
    }
    m_processName = activity.getWorkflowProcess().getName();
    for (int i = 0; i < tools.length; i++) {
      m_toolData[i] = new ToolInfo(tools[i]);
      m_applicationData[i] = new ApplicationInfo(tools[i]);
    }
  }

  /**
   * @return The elementName value
   * @see de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName() {
    return "xpdl-worklist-activity";
  }

  public long computeDueDate() throws EngineException {
    long dueDate = 0L;
    if (m_limit != null) {
      if (m_limit.isStatic()) {
        dueDate = DeadlineHelper.computeDeadline(m_scope, m_scope.getProcess().getStartedDate().getTime(), m_limit,
          m_defaultDurationUnit);
      } else {
        IExpression expr = ScriptExpressionFactory.createExpression(
          m_scriptLanguage, m_limit.getDeadlineConditionExpr());
        dueDate = DeadlineHelper.computeDeadline(m_scope, expr, m_defaultDurationUnit);
      }
    }

    if ((m_limit == null) && m_dueDateIsDeadline && (m_deadline != null)) {
      if (m_deadline.getDeadlineCondition().isDynamic()) {
        IExpression expr = ScriptExpressionFactory.createExpression(
          m_scriptLanguage, m_deadline.getDeadlineCondition().getDeadlineConditionExpr());
        dueDate = DeadlineHelper.computeDeadline(m_scope, expr, m_defaultDurationUnit);

      } else {
        dueDate = DeadlineHelper.computeDeadline(m_scope, System.currentTimeMillis(), m_deadline.getDeadlineCondition(),
          m_defaultDurationUnit);
      }
    }
    return dueDate;
  }

  /**
   * @exception EngineException
   *              Description of the Exception
   * @see de.objectcode.canyon.bpe.engine.activities.Activity#start()
   */
  public void start() throws EngineException {
    super.start();
    if (log.isInfoEnabled()) {
      log.info("starting XPDLWorklistActivity id='" + m_id + "', name='" +
        m_name + "', piid='" + getProcess().getProcessInstanceId() + "'");
    }

    EscalationHandler eh = getEscalationHandler();
    Participant[] performers = null;
    if ((eh == null) || (eh.getRetryPerformer() == null)) {
      performers = computePerformers();
    } else {
      if (log.isInfoEnabled()) {
        log.info(getDiagnosticInfo() + ": Using retryPerformer='" + eh.getRetryPerformer() + "'");
      }

      // TODO first go through computed performers. in case of match use it. else assume human
      performers = new Participant[1];
      performers[0] = new Participant();
      performers[0].setId((eh.getRetryPerformer()));
      performers[0].setName(eh.getRetryPerformer());
      performers[0].setDescription(eh.getRetryPerformer());
      performers[0].setType(ParticipantType.HUMAN);
    }

    long dueDate = 0L;
    if (eh == null) {
      dueDate = computeDueDate();
    } else {
      if (eh.getRetryDueDate() != null) {
        dueDate = eh.getRetryDueDate().getTime();
      } else {
        dueDate = eh.getAlarmTime();
      }
    }

    String activityName = m_name;
    if (m_activityNameExpression != null) {
      IExpression expr = ScriptExpressionFactory.createExpression(
        m_scriptLanguage, m_activityNameExpression);
      activityName = (String) expr.eval(this);
    }

    String processName = m_processName + "(" +
      m_scope.getProcess().getProcessInstanceId() + ")";
    if (m_processNameExpression != null) {
      IExpression expr = ScriptExpressionFactory.createExpression(
        m_scriptLanguage, m_processNameExpression);
      processName = (String) expr.eval(this);
    }

    boolean escalationRetry = false;
    if (eh != null) {
      escalationRetry = eh.isRetrying();
    }

    ActivityInfo activityInfo = new ActivityInfo(activityName, m_id, m_id,
      m_scope.getProcess().getId(), m_scope.getProcess().getVersion(),
      m_scope.getProcess().getProcessInstanceId(), m_scope.getProcess().getParentProcessInstanceIdPath(),
      ((dueDate != 0L) ? new Date(
          dueDate) : null), m_priority, m_completionStrategy,
      m_assignStrategy, performers, m_scope.getProcess().getStartedBy(),
      m_scope.getProcess().getVariables(), m_extendedAttributes, processName, escalationRetry);
    IApplicationData[] applicationData = new IApplicationData[m_toolData.length];
    for (int j = 0; j < applicationData.length; j++) {
      Parameter[] parameters = m_applicationData[j].createParameters(
        m_toolData[j].getActualParameters(), m_scriptLanguage, this);

      applicationData[j] = new DefaultApplicationData(m_applicationData[j].getId(), m_applicationData[j].getName(),
        m_applicationData[j].getDescription(), parameters, m_applicationData[j].getExtendedAttributes());
    }

    try {
      m_scope.getEngine().getWorklistEngine().startWorkItems(
        BPEEngine.ENGINE_ID, getProcess().getClientId(), activityInfo,
        applicationData, null);
    } catch (WMWorkflowException e) {
      log.error("Exception in activity '" + getId() + "' of pid='" +
        getProcess().getProcessInstanceId() + "'", e);
      throw new EngineException(e);
    }
  }

  private Participant[] computePerformers() throws EngineException {
    Participant[] performers = new Participant[m_performers.length];
    int i;

    for (i = 0; i < m_performers.length; i++) {
      if (m_performers[i].getExpression() == null) {
        performers[i] = m_performers[i];
      } else {
        IExpression expression = ScriptExpressionFactory.createExpression(
          m_scriptLanguage, m_performers[i].getExpression());

        performers[i] = new Participant();
        performers[i].setId((String) expression.eval(this));
        performers[i].setName(m_performers[i].getName());
        performers[i].setDescription(m_performers[i].getDescription());
        performers[i].setType(m_performers[i].getType());
      }
    }
    return performers;
  }

  protected void terminateWorkItems() throws EngineException {
    try {
      m_scope.getEngine().getWorklistEngine().terminateWorkItems(
        getProcess().getProcessInstanceId(), getId());
    } catch (WMWorkflowException e) {
      log.error("Exception", e);
      throw new EngineException(e);
    }
  }

  /**
   * Description of the Method
   *
   * @exception EngineException
   *              Description of the Exception
   */
  public void abort() throws EngineException {
    super.abort();
    terminateWorkItems();
  }

  public void terminate() throws EngineException {
    super.terminate();
    terminateWorkItems();
  }

  public EscalationHandler getEscalationHandler() {
    Scope scope = getScope();
    if (scope != null) {
      List l = scope.getAlarmHandlers();
      if (l != null) {
        Iterator i = l.iterator();
        while (i.hasNext()) {
          OnAlarmHandler oah = (OnAlarmHandler) i.next();
          if (oah instanceof EscalationHandler) {
            return (EscalationHandler) oah;
          }
        }
      }
    }
    return null;
  }

  /**
   * Description of the Method
   *
   * @param workItem
   *          Description of the Parameter
   */
  public void workItemAborted(IWorkItem workItem) {
    if (log.isInfoEnabled()) {
      log.info("WorkItemAborted user='" + workItem.getParticipant() +
        "', clientId='" + workItem.getClientId() + "', pid='" +
        workItem.getProcessDefinitionId() + "', piid='" +
        workItem.getProcessInstanceId() + "', activityId='" +
        workItem.getActivityDefinitionId() + "', workItemId='" +
        workItem.getWorkItemId() + "'");
    }
    try {
      complete();
    } catch (EngineException e) {
      log.error("Exception", e);
    }
  }

  /**
   * Description of the Method
   *
   * @param workItem
   *          Description of the Parameter
   */
  public void workItemCompleted(IWorkItem workItem) {
    if (log.isInfoEnabled()) {
      log.info("WorkItemCompleted user='" + workItem.getParticipant() +
        "', clientId='" + workItem.getClientId() + "', pid='" +
        workItem.getProcessDefinitionId() + "', piid='" +
        workItem.getProcessInstanceId() + "', activityId='" +
        workItem.getActivityDefinitionId() + "', workItemId='" +
        workItem.getWorkItemId() + "'");
    }
    try {
      IApplicationData[] applicationDataSet = workItem.getApplicationData();
      for (int k = 0; k < applicationDataSet.length; k++) {
        IApplicationData applicationData = applicationDataSet[k];

        Parameter[] parameters = applicationData.getParameters();
        int i;

        for (i = 0; i < parameters.length; i++) {
          if (parameters[i].mode != ParameterMode.IN) {
            IVariable variable = getVariable(parameters[i].actualName);

            if (variable != null) {
              variable.setValue(parameters[i].value);
            }
          }
        }
      }
      complete();
    } catch (RepositoryException e) {
      log.error("Exception", e);
    } catch (EngineException e) {
      log.error("Exception", e);
    }
  }

  /**
   * @param element
   *          Description of the Parameter
   * @see de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom(Element element) {
    super.toDom(element);
  }

  public void activate() throws EngineException {
    super.activate();
  }

}
