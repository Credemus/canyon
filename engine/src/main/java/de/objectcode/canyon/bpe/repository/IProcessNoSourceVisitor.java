package de.objectcode.canyon.bpe.repository;

import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.spi.RepositoryException;

public interface IProcessNoSourceVisitor {
  void visit(BPEProcess process) throws RepositoryException;
}
