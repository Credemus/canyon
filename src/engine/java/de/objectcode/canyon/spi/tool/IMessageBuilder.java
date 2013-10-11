/*
 * Created on 10.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.objectcode.canyon.spi.tool;

import javax.jms.Message;

/**
 * @author xylander
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IMessageBuilder {
  public void build(BPEContext context, Parameter[] parameters,  Message msg);
}
