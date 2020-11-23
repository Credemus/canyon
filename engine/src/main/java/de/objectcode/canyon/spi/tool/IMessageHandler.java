/*
 * Created on 10.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.objectcode.canyon.spi.tool;

import java.util.Map;

import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;

/**
 * @author xylander
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IMessageHandler {

  public void handle(MessageEvent event);
  
}
