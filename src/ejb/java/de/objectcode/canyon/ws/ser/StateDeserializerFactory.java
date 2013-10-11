package de.objectcode.canyon.ws.ser;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.ser.BaseDeserializerFactory;

/**
 * @author junglas
 */
public class StateDeserializerFactory extends BaseDeserializerFactory
{
	static final long serialVersionUID = 1273917984204280980L;
	
	public StateDeserializerFactory( Class javaType, QName xmlType )
  {
    super( StateDeserializer.class, xmlType, javaType ); // Can't share
                                                        // deserializers
  }
}
