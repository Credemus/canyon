/*
 *  Copyright (c) 2003 Adrian Price.  All rights reserved.
 */
package de.objectcode.canyon.spi.instance;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.objectcode.canyon.CanyonRuntimeException;
import de.objectcode.canyon.engine.util.DataConverter;
import de.objectcode.canyon.spi.util.ServiceUtils;

/**
 * Abstract base class for system attributes
 *
 * @author    Adrian Price
 * @created   20. Oktober 2003
 */
public abstract class BaseSystemAttribute implements IAttributeInstance
{
  private final  PropertyDescriptor  _propDesc;


  /**
   *Constructor for the BaseSystemAttribute object
   *
   * @param propDesc  Description of the Parameter
   */
  protected BaseSystemAttribute( PropertyDescriptor propDesc )
  {
    _propDesc = propDesc;
  }


  /**
   * Sets the value attribute of the BaseSystemAttribute object
   *
   * @param type                            The new value value
   * @param value                           The new value value
   * @exception AttributeReadOnlyException  Description of the Exception
   */
  public void setValue( int type, Object value )
    throws AttributeReadOnlyException
  {

    Method  writeMethod  = _propDesc.getWriteMethod();
    if ( writeMethod == null ) {
      throw new AttributeReadOnlyException(
          "System attribute cannot be set directly: " + getName() );
    }

    // Convert the value to the appropriate type.
    if ( value != null ) {
      value = DataConverter.getInstance().convertValue( value,
          _propDesc.getPropertyType() );
    }

    // Store the data.
    try {
      writeMethod.invoke( getOwner(), new Object[]{value} );
    }
    catch ( IllegalAccessException e ) {
      throw new CanyonRuntimeException( e );
    }
    catch ( InvocationTargetException e ) {
      throw new CanyonRuntimeException( e );
    }
  }


  /**
   * Gets the owner attribute of the BaseSystemAttribute object
   *
   * @return   The owner value
   */
  public abstract IAttributedEntity getOwner();


  /**
   * Gets the ownerId attribute of the BaseSystemAttribute object
   *
   * @return   The ownerId value
   */
  public String getOwnerId()
  {
    return getOwner().getEntityId();
  }


  /**
   * Gets the name attribute of the BaseSystemAttribute object
   *
   * @return   The name value
   */
  public String getName()
  {
    return _propDesc.getName();
  }


  /**
   * Gets the type attribute of the BaseSystemAttribute object
   *
   * @return   The type value
   */
  public int getType()
  {
    // TODO: consider whether to optimize speed or memory usage.
    // This implementation ptimizes memory because it avoids the overhead of
    // an additional int type instance field.  Alternatively could optimize
    // performance by doing this lookup in the ctor and storing the result
    // in a private instance field.
    return ServiceUtils.typeForClass( _propDesc.getPropertyType() );
  }


  /**
   * Gets the value attribute of the BaseSystemAttribute object
   *
   * @return   The value value
   */
  public Object getValue()
  {
    try {
      return _propDesc.getReadMethod().invoke( getOwner(), null );
    }
    catch ( IllegalAccessException e ) {
      throw new CanyonRuntimeException( e );
    }
    catch ( InvocationTargetException e ) {
      throw new CanyonRuntimeException( e );
    }
  }
}
