/* 
 * $Id: Developer-StyleGudie.txt,v 1.6 2004/08/09 06:56:14 joerg Exp $
 * 
 * Copyright (C) 2004 ObjectCode GmbH
 * All rights reserved. 
 */

package de.objectcode.canyon.spiImpl.tool;

import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.spi.tool.BPEContext;
import de.objectcode.canyon.spi.tool.IToolConnector;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.spi.tool.ReturnValue;

/**
 * 
 * 
 * @author xylander
 * @date 13.02.2005
 * @version $Revision: 1.6 $
 */
public abstract class AbstractToolConnector implements IToolConnector{

  /**
   * 
   */
  public AbstractToolConnector() {
    super();
  }

  /* (non-Javadoc)
   * @see de.objectcode.canyon.spi.tool.IToolConnector#invoke(de.objectcode.canyon.spi.tool.BPEContext, de.objectcode.canyon.spi.tool.Parameter[])
   */
  abstract public ReturnValue[] invoke(BPEContext context, Parameter[] parameters) throws Exception;

  /* (non-Javadoc)
   * @see de.objectcode.canyon.spi.tool.IToolConnector#init(de.objectcode.canyon.model.application.Application)
   */
  abstract public void init(Application application) throws Exception;

  
  protected String getExtendedStringAttribute(Application application, String attributeName, String defaultValue) {
    ExtendedAttribute attribute = application.getExtendedAttribute(attributeName);
    if (attribute == null)
      return defaultValue;
    else
      return attribute.getValue();
  }


  protected String getExtendedStringAttribute(Application application, String attributeName) {
    return getExtendedStringAttribute(application, attributeName, null);
  }

}


/*
 * $Log: Developer-StyleGudie.txt,v $
 * $Revision 1.6  2004/08/09 06:56:14  joerg
 * $no message
 * $
 */