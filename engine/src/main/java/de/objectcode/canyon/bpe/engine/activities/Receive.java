package de.objectcode.canyon.bpe.engine.activities;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.correlation.CorrelationDefinition;
import de.objectcode.canyon.bpe.engine.correlation.CorrelationSet;
import de.objectcode.canyon.bpe.engine.correlation.IMessageReceiver;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.evaluator.IAssignableExpression;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author junglas
 * @created 8. Juni 2004
 */
public class Receive extends Activity implements IMessageReceiver {
  private final static Log log = LogFactory.getLog(Receive.class);

  final static long serialVersionUID = 2746530880832717997L;

  protected List<CorrelationDefinition> m_correlations;
  protected String m_messageOperation;
  protected ComplexType m_messageContentType;
  protected IAssignableExpression m_inputExpression;
  protected boolean m_createInstance;


  public Receive(String name, Scope scope, String messageOperation, ComplexType messageContentType) {
    this(name, scope, messageOperation, messageContentType, false);
  }

  /**
   * Constructor for the Receive object
   *
   * @param name  Description of the Parameter
   * @param scope Description of the Parameter
   */
  public Receive(String name, Scope scope, String messageOperation, ComplexType messageContentType, boolean createInstance) {
    super(name, scope);

    m_correlations = new ArrayList<CorrelationDefinition>();
    m_createInstance = createInstance;
    m_messageOperation = messageOperation;
    m_messageContentType = messageContentType;

    m_scope.getProcess().addMessageReceiver(this);
  }


  /**
   * @param inputExpression The inputExpression to set.
   */
  public void setInputExpression(IAssignableExpression inputExpression) {
    m_inputExpression = inputExpression;
  }

  /**
   * @return Returns the messageType.
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
   * Gets the active attribute of the Receive object
   *
   * @return The active value
   */
  public boolean isActive() {
    return m_state == ActivityState.RUNNING;
  }


  /**
   * @return Returns the createInstance.
   */
  public boolean isCreateInstance() {
    return m_createInstance;
  }


  /**
   * Gets the elementName attribute of the Receive object
   *
   * @return The elementName value
   */
  public String getElementName() {
    return "receive";
  }


  /**
   * Gets the correlations attribute of the Receive object
   *
   * @return The correlations value
   */
  public CorrelationSet[] getCorrelationSets() {
    List<CorrelationSet> correlationSets = new ArrayList<CorrelationSet>();

    for(CorrelationDefinition correlationDef : m_correlations) {
      if (!correlationDef.isInitiate())
        correlationSets.add(correlationDef.getCorrelationSet());
    }

    CorrelationSet[] ret = new CorrelationSet[correlationSets.size()];

    correlationSets.toArray(ret);

    return ret;
  }


  /**
   * Adds a feature to the Correlation attribute of the Receive object
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

    if (m_inputExpression != null) {
      m_inputExpression.assign(this, message.getContent());
    }

    if (!m_correlations.isEmpty()) {

      for (CorrelationDefinition correlationDef : m_correlations) {
        if (correlationDef.isInitiate()) {
          m_scope.addCorrelation(correlationDef.getCorrelationSet().initiateCorrelation(message));
        }
      }
    }

    complete();

    return true;
  }


  /**
   * @param element Description of the Parameter
   * @see de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom(Element element) {
    super.toDom(element);

    element.addAttribute("messageOperation", m_messageOperation);
    element.addAttribute("createInstance", Boolean.toString(m_createInstance));

    if (m_inputExpression != null) {
      Element inputElement = element.addElement("input");

      m_inputExpression.toDom(inputElement.addElement(m_inputExpression.getElementName()));
    }

    if (!m_correlations.isEmpty()) {
      Element correlationsElement = element.addElement("correlations");

      for (CorrelationDefinition correlationDef : m_correlations) {
        correlationDef.toDom(correlationsElement.addElement(correlationDef.getElementName()));
      }
    }
  }
}
