package de.objectcode.canyon.ws.ser;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.ser.SimpleDeserializer;

/**
 * @author junglas
 */
public class StateDeserializer extends SimpleDeserializer
{
  private Method               fromStringMethod = null;

  private static final Class[] STRING_CLASS     = new Class[] { java.lang.String.class };

  public StateDeserializer( Class javaType, QName xmlType )
  {
    super( javaType, xmlType );
  }

  public Object makeValue( String source )
      throws Exception
  {
    // Invoke the fromString static method to get the Enumeration value
    if ( isNil )
      return null;
    if ( fromStringMethod == null ) {
      try {
        fromStringMethod = javaType.getMethod("fromTag", STRING_CLASS);
      } catch (Exception e) {
        throw new IntrospectionException( e.toString() );
      }
    }
    return fromStringMethod.invoke( null, new Object[] { source } );
  }
}
