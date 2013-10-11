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
package de.objectcode.canyon.spi.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMAttribute;
import org.wfmc.wapi.WMInvalidProcessDefinitionException;
import org.wfmc.wapi.WMInvalidToolException;

import de.objectcode.canyon.CanyonRuntimeException;
import de.objectcode.canyon.FormalParameterNotFoundException;
import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.model.activity.Tool;
import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.BasicType;
import de.objectcode.canyon.model.data.DataType;
import de.objectcode.canyon.model.data.DeclaredType;
import de.objectcode.canyon.model.data.ExternalReference;
import de.objectcode.canyon.model.data.FormalParameter;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.model.data.SchemaType;
import de.objectcode.canyon.model.data.TypeDeclaration;
import de.objectcode.canyon.model.process.DataField;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.ObjectNotFoundException;

/**
 * Utility class for searching activity/workflow/package for settings and
 * definitions.
 *
 * @author    Anthony Eden
 * @author    Adrian Price
 * @created   20. Oktober 2003
 */
public class ServiceUtils
{
  private final static  Log          log                                = LogFactory.getLog( ServiceUtils.class );
  private final static  int          DEFAULT_PRIORITY                   = 5;
  private final static  Map          _typeMap                           = new HashMap();
  private final static  Object[][]   _typeInfo                          = {
      {String.class, new Integer( WMAttribute.STRING_TYPE )},
      {double.class, new Integer( WMAttribute.FLOAT_TYPE )},
      {Double.class, new Integer( WMAttribute.FLOAT_TYPE )},
      {float.class, new Integer( WMAttribute.FLOAT_TYPE )},
      {Float.class, new Integer( WMAttribute.FLOAT_TYPE )},
      {int.class, new Integer( WMAttribute.INTEGER_TYPE )},
      {Integer.class, new Integer( WMAttribute.INTEGER_TYPE )},
      {Date.class, new Integer( WMAttribute.DATETIME_TYPE )},
      {boolean.class, new Integer( WMAttribute.BOOLEAN_TYPE )},
      {Boolean.class, new Integer( WMAttribute.BOOLEAN_TYPE )}
      };
  private final static  Class[]      _classForType                      = {
      String.class,
  // WMAttribute.STRING_TYPE
      Double.class,
  // WMAttribute.FLOAT_TYPE
      Integer.class,
  // WMAttribute.INTEGER_TYPE
      Object.class,
  // WMAttribute.REFERENCE_TYPE
      Date.class,
  // WMAttribute.DATETIME_TYPE
      Boolean.class,
  // WMAttribute.BOOLEAN_TYPE
      String.class,
  // WMAttribute.PERFORMER_TYPE;
      Object.class,
  // WMAttribute.DECLARED_TYPE;
      String.class,
  // WMAttribute.SCHEMA_TYPE;
      Object.class,
  // WMAttribute.EXTERNAL_REFERENCE_TYPE;
      Object.class,
  // WMAttribute.RECORD_TYPE;
      Object.class,
  // WMAttribute.UNION_TYPE;
      Object.class,
  // WMAttribute.ENUMERATION_TYPE;
      Object.class,
  // WMAttribute.ARRAY_TYPE;
      Object.class
  // WMAttribute.LIST_TYPE;
      };
  private final static  Comparator   _dataFieldComparator;
  private final static  Comparator   _propertyDescriptorNameComparator;
  private static        DataField[]  _systemDataFields;


  /**
   *Constructor for the Util object
   */
  private ServiceUtils() { }


  /**
   * Gets the interfaceInheritancePath attribute of the Util class
   *
   * @param ifClass       Description of the Parameter
   * @param superIfClass  Description of the Parameter
   * @return              The interfaceInheritancePath value
   */
  private static Class[] getInterfaceInheritancePath( Class ifClass,
      Class superIfClass )
  {

    // TODO: Do this properly!!!
    Class[]  classes;
    if ( superIfClass == null ) {
      classes = new Class[]{ifClass};
    } else {
      classes = new Class[]{ifClass, superIfClass};
    }

    return classes;
  }


  /**
   * Gets the propertyNames attribute of the ServiceUtils class
   *
   * @param propDescs  Description of the Parameter
   * @return           The propertyNames value
   */
  public static String[] getPropertyNames( PropertyDescriptor[] propDescs )
  {
    String[]  propNames  = new String[propDescs.length];
    for ( int i = 0; i < propDescs.length; i++ ) {
      propNames[i] = propDescs[i].getName();
    }
    return propNames;
  }


  /**
   * Gets the ParameterMode for the named parameter in the given application.
   *
   * @param app                                The Application
   * @param paramName                          The parameter name
   * @return                                   The ParameterMode
   * @throws FormalParameterNotFoundException  If the named parameter could
   * be found.
   */
  public static ParameterMode findParameterMode( Application app,
      String paramName )
    throws FormalParameterNotFoundException
  {

    if ( log.isDebugEnabled() ) {
      log.debug( "findParameterMode(" + app + "," + paramName + ")" );
    }
    FormalParameter  formalParameters[]  = app.getFormalParameters();
    int              i;

    for ( i = 0; i < formalParameters.length; i++ ) {
      if ( formalParameters[i].getId().equals( paramName ) ) {
        if ( log.isDebugEnabled() ) {
          log.debug( "Parameter mode: " + formalParameters[i].getMode() );
        }
        return formalParameters[i].getMode();
      }
    }
    throw new FormalParameterNotFoundException( paramName + " not found" );
  }


  /**
   * Finds the definition of the specified tool in a workflow or package.
   *
   * @param tool                     The tool for which the definition is required.
   * @param wp                       The WorkflowProcess to search.
   * @return                         The tool definition.
   * @throws WMInvalidToolException  if the tool definition could not be
   * located.
   */
  public static Application findToolDefinition( WorkflowProcess wp,
      Tool tool )
    throws WMInvalidToolException
  {
    String       toolId   = tool.getId();
    Application  toolDef;

    if ( log.isDebugEnabled() ) {
      log.debug( "Searching workflow '" + wp.getId() + "' for tool '" +
          toolId + '\'' );
    }
    toolDef = wp.getApplication( toolId );
    if ( toolDef != null ) {
      if ( log.isDebugEnabled() ) {
        log.debug( "Tool '" + toolId + " found in workflow '" +
            wp.getId() + '\'' );
      }
    } else {
      WorkflowPackage  pkg  = wp.getPackage();
      if ( log.isDebugEnabled() ) {
        log.debug( "Searching package '" + pkg.getId() + "' for tool '" +
            tool.getId() + '\'' );
      }
      toolDef = pkg.getApplication( toolId );
      if ( toolDef != null ) {
        if ( log.isDebugEnabled() ) {
          log.debug( "Tool '" + toolId + " found in package '" +
              pkg.getId() + '\'' );
        }
      }
      if ( toolDef == null ) {
        throw new WMInvalidToolException( toolId );
      }
    }

    return toolDef;
  }


  /**
   * Finds a type declaration within a package.
   *
   * @param pkg                       The package to search.
   * @param declaredTypeId            The ID of the declared type.
   * @return                          The type declaration.
   * @throws ObjectNotFoundException  if the type declaration could not be
   * found.
   */
  public static TypeDeclaration findTypeDeclaration( WorkflowPackage pkg,
      String declaredTypeId )
    throws ObjectNotFoundException
  {
    TypeDeclaration typeDeclaration = pkg.getTypeDeclaration(declaredTypeId);
    
    if ( typeDeclaration == null )
      throw new ObjectNotFoundException( declaredTypeId );
    
    return typeDeclaration;
  }


  /**
   * Finds a workflow definition within a package.
   *
   * @param pkg                                   The package to search.
   * @param processDefinitionId                   The ID of the process definition required.
   * @return                                      The process definition.
   * @throws WMInvalidProcessDefinitionException  if the process definition
   * could not be found in the package.
   */
  public static WorkflowProcess findWorkflowProcess( WorkflowPackage pkg,
      String processDefinitionId )
    throws WMInvalidProcessDefinitionException
  {

    if ( log.isDebugEnabled() ) {
      log.debug( "Searching package '" + pkg.getId() + "' for workflow '" +
          processDefinitionId + '\'' );
    }

    WorkflowProcess workflow = pkg.getWorklowProcess(processDefinitionId);

    if ( workflow == null ) {
      throw new WMInvalidProcessDefinitionException( processDefinitionId );
    }
    return workflow;
  }


  /**
   * Description of the Method
   *
   * @param pkg                          Description of the Parameter
   * @param dataType                     Description of the Parameter
   * @return                             Description of the Return Value
   * @exception ObjectNotFoundException  Description of the Exception
   */
  public static Class classForDataType( WorkflowPackage pkg, DataType dataType )
    throws ObjectNotFoundException
  {
    Class  clazz  = Object.class;
    if ( dataType instanceof DeclaredType ) {
      TypeDeclaration  decl  = findTypeDeclaration( pkg,
          ( ( DeclaredType ) dataType ).getId() );
      dataType = decl.getDataType();
    }
    if ( dataType instanceof BasicType ) {
      BasicType  basicType  = ( BasicType ) dataType;
      switch ( basicType.getValue() ) {
        case BasicType.BOOLEAN_INT:
          clazz = Boolean.class;
          break;
        case BasicType.DATETIME_INT:
          clazz = Date.class;
          break;
        case BasicType.FLOAT_INT:
          clazz = Double.class;
          break;
        case BasicType.INTEGER_INT:
          clazz = Integer.class;
          break;
        case BasicType.PERFORMER_INT:
          clazz = String.class;
          break;
        case BasicType.REFERENCE_INT:
          clazz = Object.class;
          break;
        case BasicType.STRING_INT:
          clazz = String.class;
          break;
      }
    } else if ( dataType instanceof ExternalReference ) {
      // See whether the location attribute refers to a Java class.
      ExternalReference  extRef  = ( ExternalReference ) dataType;
      try {
        clazz = Class.forName( extRef.getLocation() );
      }
      catch ( ClassNotFoundException e ) {
      }
    } else if ( dataType instanceof SchemaType ) {
      // SchemaType is partially supported, but at present we can't
      // specify an actual Java class for a specific schema.  For now,
      // treat XML documents of all types as strings.
      clazz = String.class;
    }
    // ArrayType, EnumerationType, ListType, RecordType and UnionType are
    // all deprecated, just ignore them.

    return clazz;
  }


  /**
   * Description of the Method
   *
   * @param attributeType  Description of the Parameter
   * @return               Description of the Return Value
   */
  public static Class classForType( int attributeType )
  {
    return _classForType[attributeType];
  }


  /**
   * Description of the Method
   *
   * @param propertyType  Description of the Parameter
   * @return              Description of the Return Value
   */
  public static int typeForClass( Class propertyType )
  {
    Integer  type  = ( ( Integer ) _typeMap.get( propertyType ) );
    return type != null ? type.intValue() : WMAttribute.REFERENCE_TYPE;
  }


  /**
   * Introspects properties of the specified class(es) into a map.
   *
   * @param beanClass
   * @param stopClass
   * @return           Array of <code>PropertyDescriptor</code>.
   */
  public static PropertyDescriptor[] introspect( Class beanClass,
      Class stopClass )
  {

    try {
      PropertyDescriptor[]  propDescs;
      if ( !beanClass.isInterface() || stopClass == null ) {
        propDescs = Introspector.getBeanInfo( beanClass, stopClass )
            .getPropertyDescriptors();
      } else {
        List     props    = new ArrayList();
        Class[]  classes  = getInterfaceInheritancePath( beanClass,
            stopClass );
        for ( int i = 0; i < classes.length; i++ ) {
          // Get the properties for the class.
          // N.B. This has been re-written to introspect both classes
          // separately because Introspector doesn't recognize a
          // superinterface as it does a superclass (because
          // Class.getSuperclass() returns null for an interface).
          // Thus passing superinterface as stopClass doesn't work.
          propDescs = Introspector.getBeanInfo( classes[i] )
              .getPropertyDescriptors();

          // Filter to include only primitive and serializable types.
          for ( int j = 0; j < propDescs.length; j++ ) {
            PropertyDescriptor  propDesc      = propDescs[j];
            Class               propertyType  = propDesc.getPropertyType();
            if ( propertyType.isPrimitive() ||
                Serializable.class.isAssignableFrom( propertyType ) ) {

              props.add( propDesc );
            }
          }
        }
        propDescs = ( PropertyDescriptor[] ) props.toArray(
            new PropertyDescriptor[props.size()] );
      }

      // Sort on property name.
      Arrays.sort( propDescs, _propertyDescriptorNameComparator );

      return propDescs;
    }
    catch ( IntrospectionException e ) {
      throw new CanyonRuntimeException( e );
    }
  }

  static {
    for ( int i = 0; i < _typeInfo.length; i++ ) {
      Object[]  typeInfo  = _typeInfo[i];
      _typeMap.put( typeInfo[0], typeInfo[1] );
    }
    _dataFieldComparator =
      new Comparator()
      {
        public int compare( Object o1, Object o2 )
        {
          return ( ( DataField ) o1 ).getId().compareTo(
              ( ( DataField ) o1 ).getId() );
        }
      };
    _propertyDescriptorNameComparator =
      new Comparator()
      {
        public int compare( Object o1, Object o2 )
        {
          return ( ( PropertyDescriptor ) o1 ).getName().compareTo(
              ( ( PropertyDescriptor ) o2 ).getName() );
        }
      };
  }

}
