package de.objectcode.canyon.persistent.async;

import java.io.Serializable;

import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.spi.ServiceManager;

/**
 * @author    junglas
 * @created   29. Oktober 2003
 */
public abstract class AsyncRequest implements Serializable
{
  /**
   * Description of the Method
   *
   * @param serviceManager           Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public abstract void execute( ServiceManager serviceManager )
    throws WMWorkflowException;

}
