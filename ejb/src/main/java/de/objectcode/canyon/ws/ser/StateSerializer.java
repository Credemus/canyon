package de.objectcode.canyon.ws.ser;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.encoding.ser.SimpleSerializer;
import org.apache.axis.wsdl.fromJava.Types;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.wfmc.wapi.WMObjectState;
import org.xml.sax.Attributes;

/**
 * @author junglas
 */
public class StateSerializer extends SimpleSerializer
{
	static final long serialVersionUID = 8937822835053149107L;
	
	private final static Log log = LogFactory.getLog( StateSerializer.class );

  public StateSerializer( Class javaType, QName xmlType )
  {
    super( javaType, xmlType );
  }

  public void serialize( QName name, Attributes attributes, Object value,
      SerializationContext context )
      throws IOException
  {
    context.startElement( name, attributes );
    context.writeString( getValueAsString( value, context ) );
    context.endElement();
  }

  public String getValueAsString( Object value, SerializationContext context )
  {
    try {
      return ((WMObjectState) value).stringValue();
    } catch (Exception e) {
      log.error( "Exception", e );
    }
    return null;
  }

  public Element writeSchema( Class javaType, Types types )
      throws Exception
  {
    // Get the base type of the enum class
    java.lang.reflect.Method m = javaType.getMethod( "states", (Class[])null );

    // Create simpleType, restriction elements
    Element simpleType = types.createElement( "simpleType" );

    simpleType.setAttribute( "name", xmlType.getLocalPart() );

    Element restriction = types.createElement( "restriction" );

    simpleType.appendChild( restriction );

    String baseType = types.writeType( String.class, null );

    restriction.setAttribute( "base", baseType );

    WMObjectState states[] = (WMObjectState[]) m.invoke( null, (Object[])null );

    for ( int i = 0; i < states.length; i++ ) {
      Element enumeration = types.createElement( "enumeration" );

      enumeration.setAttribute( "value", states[i].stringValue() );
      restriction.appendChild(enumeration);
    }

    return simpleType;
  }

}
