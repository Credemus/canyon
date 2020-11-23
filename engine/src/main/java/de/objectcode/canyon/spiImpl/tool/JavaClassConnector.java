/*
 *  --
 *  Copyright (C) 2002-2003 Aetrion LLC.
 *  All rights reserved.
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions, and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions, and the disclaimer that follows
 *  these conditions in the documentation and/or other materials
 *  provided with the distribution.
 *  3. The names "OBE" and "Open Business Engine" must not be used to
 *  endorse or promote products derived from this software without prior
 *  written permission.  For written permission, please contact
 *  obe@aetrion.com.
 *  4. Products derived from this software may not be called "OBE" or
 *  "Open Business Engine", nor may "OBE" or "Open Business Engine"
 *  appear in their name, without prior written permission from
 *  Aetrion LLC (obe@aetrion.com).
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT,
 *  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 *  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 *  STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 *  IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 *  For more information on OBE, please see
 *  <http://www.openbusinessengine.org/>.
 */
package de.objectcode.canyon.spiImpl.tool;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.BasicType;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.tool.BPEContext;
import de.objectcode.canyon.spi.tool.IBPEContextAware;
import de.objectcode.canyon.spi.tool.IToolConnector;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.spi.tool.ReturnValue;

/**
 * An application connector that invokes the main() method of a Java class.
 * A custom class loader can be provided.
 *
 * @author    Anthony Eden
 * @author    Adrian Price
 * @created   28. Oktober 2003
 */
public class JavaClassConnector implements IToolConnector
{
  private final static  Log     log           = LogFactory.getLog( JavaClassConnector.class );

  private               String  m_className;
  private               String  m_methodName;


  /**
   * Construct a ClassApplicationConnector which invokes the main() method
   *of the given class.  The class must be loadable through the same
   *class loader which is used to load this connector.
   *
   * @param className   The fully-qualified class name
   * @param methodName  Description of the Parameter
   */

  public JavaClassConnector( String className, String methodName )
  {
    m_className = className;
    m_methodName = methodName;
  }

  public JavaClassConnector()
  {
  }

  /**
   * Sets the className attribute of the JavaClassConnector object
   *
   * @param className  The new className value
   */
  public void setClassName( String className )
  {
    if ( className == null ) {
      throw new IllegalArgumentException( "Class name may not be null" );
    }

    this.m_className = className;
  }



  /**
   * Gets the className attribute of the JavaClassConnector object
   *
   * @return   The className value
   */
  public String getClassName()
  {
    return m_className;
  }


  /**
   * Invoke the application.
   *
   * @param parameters  The parameters
   * @return            Result values
   * @throws Exception  Any Exception
   */

  public ReturnValue[] invoke( BPEContext context, Parameter[] parameters )
    throws Exception
  {
    if ( log.isDebugEnabled() ) {
      StringBuffer buffer = new StringBuffer("Invoke");
      int i;
      
      buffer.append(" java class '").append(m_className).append("'");
      buffer.append(" method '").append(m_methodName).append("'");
      buffer.append(" parameters [");
      for ( i = 0; i < parameters.length; i++ ) {
        if ( i > 0 )
        buffer.append(", ");
        buffer.append(parameters[i]);
      }
      buffer.append("]");
      
      log.debug(buffer.toString());
    }

    Class        clazz               = Class.forName( m_className );

    List         parameterTypeList   = new ArrayList();
    List         parameterValueList  = new ArrayList();
    int          i;

    for ( i = 0; i < parameters.length; i++ ) {
      if ( parameters[i].mode == ParameterMode.IN ) {
        if (parameters[i].value==null)
          throw new IllegalArgumentException("Parameter '" + parameters[i].formalName+"' not set in JavaClassConnector for class '" + m_className+"'! Context="+context);
        
        if ( parameters[i].dataType instanceof BasicType ) {
          BasicType  type  = ( BasicType ) parameters[i].dataType;

          switch ( type.getValue() ) {
            case BasicType.BOOLEAN_INT:
              parameterTypeList.add( Boolean.TYPE );
              parameterValueList.add( new Boolean( parameters[i].value.toString() ) );
              break;
            case BasicType.FLOAT_INT:
              parameterTypeList.add( Float.TYPE );
              parameterValueList.add( new Float( parameters[i].value.toString() ) );
              break;
            case BasicType.INTEGER_INT:
              parameterTypeList.add( Integer.TYPE );
              parameterValueList.add( new Integer( ( int ) Float.parseFloat( parameters[i].value.toString() ) ) );
              break;
            case BasicType.STRING_INT:
              parameterTypeList.add( String.class );
              parameterValueList.add( parameters[i].value.toString() );
              break;
          }
        }
      }
    }

    if ( log.isDebugEnabled() ) {
      log.debug( "Invoke java class '" + m_className + "' method '" + m_methodName + "' " + parameterTypeList + " " + parameterValueList);
    }
    
    Class[]      parameterTypes      = new Class[parameterTypeList.size()];

    parameterTypeList.toArray( parameterTypes );

    Object[]     parameterValues     = new Object[parameterValueList.size()];

    parameterValueList.toArray( parameterValues );

    Method       method              = clazz.getMethod( m_methodName, parameterTypes );

    Object       javaObject          = clazz.newInstance();
    
    if (javaObject instanceof IBPEContextAware)
      ((IBPEContextAware) javaObject).setBPEContext(context);

    Object       result              = method.invoke( javaObject, parameterValues );

    if ( log.isDebugEnabled() ) {
      log.debug( "result: " + result );
    }

    List         values              = new ArrayList();
    for ( i = 0; i < parameters.length; i++ ) {
      if ( parameters[i].mode == ParameterMode.OUT ) {
        values.add( new ReturnValue( parameters[i].actualName, result ) );
      }
    }

    ReturnValue  ret[]                 = new ReturnValue[values.size()];

    values.toArray( ret );

    return ret;
  }


  /* (non-Javadoc)
   * @see de.objectcode.canyon.spi.tool.IToolConnector#init(de.objectcode.canyon.model.application.Application)
   */
  public void init(Application application) throws Exception {
  	if (m_className==null && m_methodName==null) {
      m_className = application.getExtendedAttributeValue("canyon:javaClass");
      m_methodName = application.getExtendedAttributeValue("canyon:javaMethod");;
  	} 
  }
}
