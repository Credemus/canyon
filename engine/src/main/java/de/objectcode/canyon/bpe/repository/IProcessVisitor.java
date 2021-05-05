package de.objectcode.canyon.bpe.repository;

import java.io.Serializable;

import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.spi.RepositoryException;

/**
 * @author junglas
 */
public interface IProcessVisitor {
  void visit(BPEProcess process, Serializable processSource) throws RepositoryException;
}
