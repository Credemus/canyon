package de.objectcode.canyon.spi.process;

/**
 * @author junglas
 */
public interface IProcessDefinitionID 
{
  /**
   * Get the identifier of the workflow process.
   */
  String getId();
  
  /**
   * Get the version of the workflow process.
   */
  String getVersion();
}
