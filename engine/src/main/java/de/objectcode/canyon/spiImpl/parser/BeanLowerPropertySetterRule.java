package de.objectcode.canyon.spiImpl.parser;

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.digester.Rule;

/**
 * @author    junglas
 * @created   24. November 2003
 */
public class BeanLowerPropertySetterRule extends Rule
{
  protected  String  m_bodyText  = null;


  /**
   * Description of the Method
   *
   * @param namespace      Description of the Parameter
   * @param name           Description of the Parameter
   * @param text           Description of the Parameter
   * @exception Exception  Description of the Exception
   */
  public void body( String namespace, String name, String text )
    throws Exception
  {

    // log some debugging information
    if ( digester.getLogger().isDebugEnabled() ) {
      digester.getLogger().debug( "[BeanPropertySetterRule]{" +
          digester.getMatch() + "} Called with text '" + text + "'" );
    }

    m_bodyText = text.trim();
  }


  /**
   * Description of the Method
   *
   * @param namespace      Description of the Parameter
   * @param name           Description of the Parameter
   * @exception Exception  Description of the Exception
   */
  public void end( String namespace, String name )
    throws Exception
  {
    if ( name == null || name.length() == 0 ) {
      return;
    }

    if ( name.length() == 1 || Character.isLowerCase(name.charAt(1)))
      name = Character.toLowerCase( name.charAt( 0 ) ) + name.substring( 1 );

    // Get a reference to the top object
    Object              top        = digester.peek();

    // log some debugging information
    if ( digester.getLogger().isDebugEnabled() ) {
      digester.getLogger().debug( "[BeanPropertySetterRule]{" + digester.getMatch() +
          "} Set " + top.getClass().getName() + " property " +
          name + " with text " + m_bodyText );
    }

    /*
     *  this is a standard JavaBean
     */
    PropertyDescriptor  desc       =
        PropertyUtils.getPropertyDescriptor( top, name );
    if ( desc == null ) {
      throw new NoSuchMethodException
          ( "Bean has no property named " + name );
    }

    Converter           converter  = Converter.getConverter( desc.getPropertyType() );

    if ( converter == null ) {
      throw new IllegalArgumentException( "No converter for class " + desc.getPropertyType() );
    }

    // Set the property (with conversion as necessary)
    BeanUtils.setProperty( top, name, converter.fromString( m_bodyText ) );

  }

}
