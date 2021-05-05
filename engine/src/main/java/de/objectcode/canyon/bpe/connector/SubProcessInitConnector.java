package de.objectcode.canyon.bpe.connector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.BPERuntimeContext;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.correlation.Message;

/**
 * @author junglas
 * @created 22. Juni 2004
 */
public class SubProcessInitConnector implements IConnector {
  static final long serialVersionUID = 2368920307354959774L;

  private final static Log log = LogFactory.getLog(SubProcessInitConnector.class);


  /**
   * @return The elementName value
   * @see de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName() {
    return "sub-process-init-connector";
  }


  public Message invoke(BPEProcess bpeProcess, Message message)
          throws InvokationException {
    if (log.isDebugEnabled()) {
      log.debug("invoke: " + bpeProcess.getId() + " " + bpeProcess.getProcessInstanceId() + " " + message);
    }

    try {
      BPERuntimeContext context = new BPERuntimeContext(bpeProcess.getStartedBy(), bpeProcess.getClientId());
      bpeProcess.getBPEEngine().handleMessage(context, message);

      return null;
    } catch (Exception e) {
      log.error("Exception", e);
      throw new InvokationException(e);
    }
  }


  /**
   * @param element Description of the Parameter
   * @see de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom(Element element) {
  }
}
