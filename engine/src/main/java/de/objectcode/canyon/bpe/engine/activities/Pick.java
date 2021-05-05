package de.objectcode.canyon.bpe.engine.activities;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.correlation.CorrelationDefinition;
import de.objectcode.canyon.bpe.engine.correlation.CorrelationSet;
import de.objectcode.canyon.bpe.engine.correlation.IMessageReceiver;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.evaluator.IAssignableExpression;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.bpe.util.IDomSerializable;

/**
 * @author junglas
 * @created 16. Juni 2004
 */
public class Pick extends Activity implements IActivityContainer {
  static final long serialVersionUID = -5746536574452726475L;

  private final static Log log = LogFactory.getLog(Pick.class);

  private List<OnMessage> m_onMessages;


  /**
   * Constructor for the Pick object
   *
   * @param name  Description of the Parameter
   * @param scope Description of the Parameter
   */
  public Pick(String name, Scope scope) {
    super(name, scope);

    m_onMessages = new ArrayList<OnMessage>();
  }


  /**
   * Gets the elementName attribute of the Pick object
   *
   * @return The elementName value
   */
  public String getElementName() {
    return "pick";
  }


  /**
   * Adds a feature to the OnMessage attribute of the Pick object
   *
   * @param activity           The feature to be added to the OnMessage attribute
   * @param messageOperation   The feature to be added to the OnMessage attribute
   * @param messageContentType The feature to be added to the OnMessage attribute
   * @return Description of the Return Value
   */
  public OnMessage addOnMessage(String messageOperation, ComplexType messageContentType, Activity activity) {
    activity.setParentActivity(this);

    OnMessage onMessage = new OnMessage(messageOperation, messageContentType, activity);
    m_onMessages.add(onMessage);

    return onMessage;
  }


  /**
   * Adds a feature to the OnMessage attribute of the Pick object
   *
   * @param messageOperation   The feature to be added to the OnMessage attribute
   * @param correlationSet     The feature to be added to the OnMessage attribute
   * @param activity           The feature to be added to the OnMessage attribute
   * @param messageContentType The feature to be added to the OnMessage attribute
   * @return Description of the Return Value
   */
  public OnMessage addOnMessage(String messageOperation, ComplexType messageContentType, CorrelationSet correlationSet, Activity activity) {
    activity.setParentActivity(this);

    OnMessage onMessage = new OnMessage(messageOperation, messageContentType, activity);
    m_onMessages.add(onMessage);

    onMessage.addCorrelation(correlationSet, false);

    return onMessage;
  }


  /**
   * @see de.objectcode.canyon.bpe.engine.activities.IActivityContainer#isNonBlocked()
   */
  public boolean isNonBlocked() {
    return false;
  }

  /**
   * @param childActivity Description of the Parameter
   * @throws EngineException Description of the Exception
   * @see de.objectcode.canyon.bpe.engine.activities.IActivityContainer#childAborted(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public void childAborted(Activity childActivity)
          throws EngineException {
    if (m_state != ActivityState.RUNNING)
      return;
    complete();
  }


  /**
   * @param childActivity Description of the Parameter
   * @throws EngineException Description of the Exception
   * @see de.objectcode.canyon.bpe.engine.activities.IActivityContainer#childCompleted(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public void childCompleted(Activity childActivity)
          throws EngineException {
    if (m_state != ActivityState.RUNNING)
      return;
    complete();
  }


  /**
   * Description of the Method
   *
   * @param childActivity Description of the Parameter
   * @throws EngineException Description of the Exception
   */
  public void childSkiped(Activity childActivity)
          throws EngineException {
  }


  /**
   * @param element Description of the Parameter
   * @see de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom(Element element) {
    super.toDom(element);

    for (OnMessage onMessage : m_onMessages) {
      onMessage.toDom(element.addElement(onMessage.getElementName()));
    }
  }


  /**
   * @param in Description of the Parameter
   * @throws IOException Description of the Exception
   */
  public void hydrate(HydrationContext context, ObjectInput in)
          throws IOException {
    super.hydrate(context, in);

    for (OnMessage onMessage : m_onMessages) {
      onMessage.getActivity().hydrate(context, in);
    }
  }


  /**
   * @param out Description of the Parameter
   * @throws IOException Description of the Exception
   */
  public void dehydrate(HydrationContext context, ObjectOutput out)
          throws IOException {
    super.dehydrate(context, out);

    for (OnMessage onMessage : m_onMessages) {
      onMessage.getActivity().dehydrate(context, out);
    }
  }


  /**
   * Description of the Class
   *
   * @author junglas
   * @created 16. Juni 2004
   */
  public class OnMessage implements IMessageReceiver, IDomSerializable, Serializable {
    static final long serialVersionUID = -889092738916115684L;

    private String m_messageOperation;
    private ComplexType m_messageContentType;
    private Activity m_activity;
    protected List<CorrelationDefinition> m_correlations;
    private IAssignableExpression m_inputExpression;


    /**
     * Constructor for the OnMessage object
     *
     * @param messageOperation   Description of the Parameter
     * @param activity           Description of the Parameter
     * @param messageContentType Description of the Parameter
     */
    private OnMessage(String messageOperation, ComplexType messageContentType, Activity activity) {
      m_messageOperation = messageOperation;
      m_messageContentType = messageContentType;
      m_activity = activity;
      m_correlations = new ArrayList<CorrelationDefinition>();
      m_scope.getProcess().addMessageReceiver(this);
    }


    /**
     * @param inputExpression The inputExpression to set.
     */
    public void setInputExpression(IAssignableExpression inputExpression) {
      m_inputExpression = inputExpression;
    }


    /**
     * @return The correlations value
     */
    public CorrelationSet[] getCorrelationSets() {
      List<CorrelationSet> correlationSets = new ArrayList<CorrelationSet>();

      for(CorrelationDefinition correlationDef : m_correlations) {
        if (!correlationDef.isInitiate()) {
          correlationSets.add(correlationDef.getCorrelationSet());
        }
      }

      CorrelationSet[] ret = new CorrelationSet[correlationSets.size()];

      correlationSets.toArray(ret);

      return ret;
    }


    /**
     * @return The messageType value
     */
    public String getMessageOperation() {
      return m_messageOperation;
    }


    /**
     * @return Returns the messageContentType.
     */
    public ComplexType getMessageContentType() {
      return m_messageContentType;
    }


    /**
     * Gets the active attribute of the OnMessage object
     *
     * @return The active value
     */
    public boolean isActive() {
      return m_state == ActivityState.RUNNING;
    }


    /**
     * Gets the createInstance attribute of the OnMessage object
     *
     * @return The createInstance value
     */
    public boolean isCreateInstance() {
      return false;
    }


    /**
     * @return Returns the activity.
     */
    public Activity getActivity() {
      return m_activity;
    }


    /**
     * @return The elementName value
     * @see de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
     */
    public String getElementName() {
      return "onMessage";
    }


    /**
     * Adds a feature to the Correlation attribute of the OnMessage object
     *
     * @param correlationSet The feature to be added to the Correlation attribute
     * @param initiate       The feature to be added to the Correlation attribute
     */
    public void addCorrelation(CorrelationSet correlationSet, boolean initiate) {
      m_correlations.add(new CorrelationDefinition(correlationSet, initiate));
      m_scope.addCorrelationSet(correlationSet);
    }


    /**
     * @param message Description of the Parameter
     * @return Description of the Return Value
     * @throws EngineException Description of the Exception
     */
    public boolean onMessage(Message message)
            throws EngineException {
      if (log.isDebugEnabled()) {
        log.debug("onMessage: " + message);
      }

      for (OnMessage onMessage : m_onMessages) {
        if (onMessage != this) {
          onMessage.getActivity().deactivate();
        }
      }

      if (m_inputExpression != null) {
        m_inputExpression.assign(Pick.this, message.getContent());
      }

      if (!m_correlations.isEmpty()) {
        for (CorrelationDefinition correlationDef : m_correlations) {
          if (correlationDef.isInitiate()) {
            m_scope.addCorrelation(correlationDef.getCorrelationSet().initiateCorrelation(message));
          }
        }
      }

      m_activity.activate();

      return true;
    }


    /**
     * @param element Description of the Parameter
     * @see de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
     */
    public void toDom(Element element) {
      element.addAttribute("messageType", m_messageOperation);

      if (m_inputExpression != null) {
        Element inputElement = element.addElement("input");

        m_inputExpression.toDom(inputElement.addElement(m_inputExpression.getElementName()));
      }

      if (!m_correlations.isEmpty()) {
        int i;
        Element correlationsElement = element.addElement("correlations");

        for (CorrelationDefinition correlationDef : m_correlations) {
          correlationDef.toDom(correlationsElement.addElement(correlationDef.getElementName()));
        }
      }

      m_activity.toDom(element.addElement(m_activity.getElementName()));
    }
  }
}
