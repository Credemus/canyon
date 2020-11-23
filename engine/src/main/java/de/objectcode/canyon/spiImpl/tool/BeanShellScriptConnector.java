/* 
 * $Id: Developer-StyleGudie.txt,v 1.6 2004/08/09 06:56:14 joerg Exp $
 * 
 * Copyright (C) 2004 ObjectCode GmbH
 * All rights reserved. 
 */

package de.objectcode.canyon.spiImpl.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bsh.Interpreter;
import bsh.Primitive;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.evaluator.ScriptExpressionFactory;
import de.objectcode.canyon.bpe.util.ExtendedAttributeHelper;
import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.FormalParameter;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.tool.BPEContext;
import de.objectcode.canyon.spi.tool.IToolConnector;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.spi.tool.ReturnValue;

/**
 * 
 * 
 * @author xylander
 * @date 01.06.2005
 * @version $Revision: 1.6 $
 */
public class BeanShellScriptConnector implements IToolConnector{
	private final static Log log = LogFactory.getLog(BeanShellScriptConnector.class);

  private String m_script;
  
  /* (non-Javadoc)
   * @see de.objectcode.canyon.spi.tool.IToolConnector#invoke(de.objectcode.canyon.spi.tool.BPEContext, de.objectcode.canyon.spi.tool.Parameter[])
   */
  public ReturnValue[] invoke(BPEContext context, Parameter[] parameters) throws Exception {
    try {
      Interpreter interpreter = new Interpreter(); 

      int          i;

      List outsOrInOuts = new ArrayList();
      for ( i = 0; i < parameters.length; i++ ) {
        if ( parameters[i].mode == ParameterMode.OUT || parameters[i].mode == ParameterMode.INOUT) {
          outsOrInOuts.add(parameters[i]);
        } 
          if (parameters[i].value==null && (parameters[i].mode == ParameterMode.IN || parameters[i].mode == ParameterMode.INOUT))
            throw new IllegalArgumentException("Parameter '" + parameters[i].formalName+"' not set in ScriptConnector! Context="+context);
        if ( parameters[i].getValue() != null ) {
          interpreter.set(parameters[i].getName(), parameters[i].getValue());
        } else {          
          interpreter.set(parameters[i].getName(), Primitive.NULL);
        }
      }
      interpreter.eval(m_script);
      
      ReturnValue[] result = new ReturnValue[outsOrInOuts.size()];
      for (i = 0; i < result.length; i++) {
        Parameter parameter = ((Parameter) outsOrInOuts.get(i)); 
        String name = parameter.formalName;
        Object value = interpreter.get(name);
        if (value == null)
          throw new IllegalStateException("Script does not declare out variable " + name);
        result[i] = new ReturnValue(parameter.actualName,value);
      }
      
      return result;
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
      throw new EngineException( e );
    }
  }

  /* (non-Javadoc)
   * @see de.objectcode.canyon.spi.tool.IToolConnector#init(de.objectcode.canyon.model.application.Application)
   */
  public void init(Application application) throws Exception {
    ExtendedAttributeHelper eah = new ExtendedAttributeHelper(application.getExtendedAttributes());
    m_script = eah.getMandatoryValue("script");
  }
}


/*
 * $Log: Developer-StyleGudie.txt,v $
 * $Revision 1.6  2004/08/09 06:56:14  joerg
 * $no message
 * $
 */