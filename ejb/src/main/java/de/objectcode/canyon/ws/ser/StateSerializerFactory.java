package de.objectcode.canyon.ws.ser;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.ser.BaseSerializerFactory;

/**
 * @author junglas
 */
public class StateSerializerFactory extends BaseSerializerFactory
{
	static final long serialVersionUID = -5655834898633702951L;
	
	public StateSerializerFactory( Class javaType, QName xmlType )
  {
    super( StateSerializer.class, xmlType, javaType );
  }
}