package de.objectcode.canyon.bpe.connector;

import java.io.Serializable;

import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.util.IDomSerializable;

/**
 * @author junglas
 */
public interface IConnector extends IDomSerializable, Serializable
{
  public Message invoke ( BPEProcess bpeProcess, Message message ) throws InvokationException;
}
