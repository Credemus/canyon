package de.objectcode.canyon.bpe.connector;

import de.objectcode.canyon.bpe.engine.EngineException;

/**
 * @author junglas
 */
public class InvokationException extends EngineException {
  static final long serialVersionUID = -6380113528049426254L;

  public InvokationException(String message) {
    super(message);
  }

  public InvokationException(Throwable cause) {
    super(cause);
  }
}
