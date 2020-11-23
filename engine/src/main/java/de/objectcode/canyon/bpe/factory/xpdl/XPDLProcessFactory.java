package de.objectcode.canyon.bpe.factory.xpdl;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.factory.IProcessFactory;
import de.objectcode.canyon.model.process.WorkflowProcess;

/**
 * @author junglas
 */
public class XPDLProcessFactory implements IProcessFactory
{
  private WorkflowProcess m_workflowProcess;
  
  public XPDLProcessFactory ( WorkflowProcess workflowProcess )
  {
    m_workflowProcess = workflowProcess;
  }
  
  /**
   * @see de.objectcode.canyon.bpe.factory.IProcessFactory#createProcess()
   */
  public BPEProcess createProcess ( ) throws EngineException
  {
    ProcessBuilder builder = new ProcessBuilder();
    
    builder.build(m_workflowProcess );
    
    return builder.getBPEProcess();
  }
}
