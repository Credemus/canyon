package de.objectcode.canyon.bpe.repository;

import de.objectcode.canyon.spi.RepositoryException;

/**
 * @author junglas
 */
public interface IProcessInstanceVisitor {
  void visit(ProcessInstance processInstance) throws RepositoryException;
}
