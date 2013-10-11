package de.objectcode.canyon.spiImpl.parser;

import java.beans.PropertyDescriptor;
import java.util.HashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

/**
 * @author    junglas
 * @created   24. November 2003
 */
public class SetLowerPropertiesRule extends Rule
{
  /**
   * @param namespace      Description of the Parameter
   * @param elementName    Description of the Parameter
   * @param attributes     Description of the Parameter
   * @exception Exception  Description of the Exception
   * @see                  org.apache.commons.digester.Rule#begin(java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void begin( String namespace, String elementName, Attributes attributes )
    throws Exception
  {
    // Build a set of attribute names and corresponding values
    HashMap             values      = new HashMap();

    for ( int i = 0; i < attributes.getLength(); i++ ) {
      String  name   = attributes.getLocalName( i );
      if ( "".equals( name ) ) {
        name = attributes.getQName( i );
      }
      String  value  = attributes.getValue( i );

      if ( digester.getLogger().isDebugEnabled() ) {
        digester.getLogger().debug( "[SetLowerPropertiesRule]{" + digester.getMatch() +
            "} Setting property '" + name + "' to '" +
            value + "'" );
      }
      if ( name != null && name.length() > 0 ) {
        if ( name.length() == 1 || Character.isLowerCase( name.charAt( 1 ) ) ) {
          name = Character.toLowerCase( name.charAt( 0 ) ) + name.substring( 1 );
        }

        values.put( name, value );
      }
    }
    // Populate the corresponding properties of the top object
    Object              top         = digester.peek();
    PropertyDescriptor  descriptor[]  = PropertyUtils.getPropertyDescriptors( top );
    int                 i;

    for ( i = 0; i < descriptor.length; i++ ) {
      if ( values.containsKey( descriptor[i].getName() ) ) {
        Converter  converter  = Converter.getConverter( descriptor[i].getPropertyType() );

        if ( converter == null ) {
          throw new IllegalArgumentException( "No converter for class " + descriptor[i].getPropertyType() );
        }

        BeanUtils.setProperty( top, descriptor[i].getName(), converter.fromString( ( String ) values.get( descriptor[i].getName() ) ) );
      }
    }
  }

}
