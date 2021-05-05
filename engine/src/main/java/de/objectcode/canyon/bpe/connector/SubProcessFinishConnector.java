package de.objectcode.canyon.bpe.connector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.correlation.Message;

/**
 * @author junglas
 * @created 22. Juli 2004
 */
public class SubProcessFinishConnector implements IConnector {
  static final long serialVersionUID = -1772170408442938740L;

  private final static Log log = LogFactory.getLog(SubProcessInitConnector.class);


  /**
   * @return The elementName value
   * @see de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName() {
    return "sub-process-finish-connector";
  }


  public Message invoke(BPEProcess bpeProcess, Message message)
          throws InvokationException {
    try {
      bpeProcess.getBPEEngine().handleMessage((String) message.getContent().get("parentProcessId"), message);

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
