package de.objectcode.canyon.bpe.repository;

import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.spi.RepositoryException;

import java.io.Serializable;

public interface IProcessNoSourceVisitor {
  public void visit (BPEProcess process ) throws RepositoryException;
}
