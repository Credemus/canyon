package de.objectcode.canyon.bpe.factory;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;

/**
 * @author junglas
 */
public interface IProcessFactory {
  BPEProcess createProcess() throws EngineException;
}
