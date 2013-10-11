package de.objectcode.canyon.bpe.engine.variable;

import de.objectcode.canyon.bpe.util.IDomSerializable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.dom4j.Element;

/**
 * @author    junglas
 * @created   16. Juni 2004
 */
public class ComplexType implements DynaClass, Serializable, IDomSerializable
{
  final static  long         serialVersionUID  = -3967558807653210587L;
  private       String       m_name;
  private       Map          m_properties;
  private       ComplexType  m_superType;


  /**
   *Constructor for the ComplexTypeDefinition object
   *
   * @param name  Description of the Parameter
   */
  public ComplexType( String name )
  {
    m_name = name;
    m_properties = new LinkedHashMap();
  }


  /**
   *Constructor for the ComplexType object
   *
   * @param name       Description of the Parameter
   * @param superType  Description of the Parameter
   */
  public ComplexType( String name, ComplexType superType )
  {
    m_name = name;
    m_properties = new LinkedHashMap( superType.m_properties );
    m_superType = superType;
  }


  /**
   * @return   The dynaProperties value
   * @see      org.apache.commons.beanutils.DynaClass#getDynaProperties()
   */
  public DynaProperty[] getDynaProperties()
  {
    DynaProperty[]  properties  = new DynaProperty[m_properties.size()];
    Iterator        it          = m_properties.values().iterator();
    int             i;

    for ( i = 0; it.hasNext(); i++ ) {
      PropertyDefinition  definition  = ( PropertyDefinition ) it.next();

      properties[i] = definition.getDynaProperty();
    }

    return properties;
  }


  /**
   * @param name  Description of the Parameter
   * @return      The dynaProperty value
   * @see         org.apache.commons.beanutils.DynaClass#getDynaProperty(java.lang.String)
   */
  public DynaProperty getDynaProperty( String name )
  {
    PropertyDefinition  definition  = ( PropertyDefinition ) m_properties.get( name );

    return definition.getDynaProperty();
  }

  public String[] getPropertyNames()
  {
    String[] names = new String[m_properties.size()];
    
    m_properties.keySet().toArray(names);
    
    return names;
  }
  
  public PropertyDefinition getPropertyDefinition ( String name )
  {
    return (PropertyDefinition)m_properties.get(name);
  }

  /**
   * @return   The name value
   * @see      org.apache.commons.beanutils.DynaClass#getName()
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * @return   The elementName value
   * @see      de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName()
  {
    return "complex-type";
  }


  /**
   * Adds a feature to the Property attribute of the ComplexTypeDefinition object
   *
   * @param name         The feature to be added to the Property attribute
   * @param clazz        The feature to be added to the Property attribute
   * @param label        The feature to be added to the Property attribute
   * @param description  The feature to be added to the Property attribute
   */
  public void addProperty( String name, String label, String description, Class clazz )
  {
    m_properties.put( name, new PropertyDefinition( name, label, description, clazz, null ) );
  }


  /**
   * Adds a feature to the Property attribute of the ComplexType object
   *
   * @param name          The feature to be added to the Property attribute
   * @param label         The feature to be added to the Property attribute
   * @param description   The feature to be added to the Property attribute
   * @param clazz         The feature to be added to the Property attribute
   * @param defaultValue  The feature to be added to the Property attribute
   */
  public void addProperty( String name, String label, String description, Class clazz, Object defaultValue )
  {
    m_properties.put( name, new PropertyDefinition( name, label, description, clazz, defaultValue ) );
  }


  /**
   * Adds a feature to the Property attribute of the ComplexType object
   *
   * @param name         The feature to be added to the Property attribute
   * @param label        The feature to be added to the Property attribute
   * @param description  The feature to be added to the Property attribute
   * @param complexType  The feature to be added to the Property attribute
   */
  public void addProperty( String name, String label, String description, ComplexType complexType )
  {
    m_properties.put( name, new PropertyDefinition( name, label, description, complexType ) );
  }


  /**
   * @return                            Description of the Return Value
   * @exception IllegalAccessException  Description of the Exception
   * @exception InstantiationException  Description of the Exception
   * @see                               org.apache.commons.beanutils.DynaClass#newInstance()
   */
  public DynaBean newInstance()
    throws IllegalAccessException, InstantiationException
  {
    return new ComplexValue( this );
  }


  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    element.addAttribute( "name", m_name );
    if ( m_superType != null ) {
      element.addAttribute( "super", m_superType.getName() );
    }

    Iterator  it  = m_properties.values().iterator();

    while ( it.hasNext() ) {
      PropertyDefinition  property  = ( PropertyDefinition ) it.next();

      property.toDom( element.addElement( property.getElementName() ) );
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   16. Juni 2004
   */
  public static class PropertyDefinition implements Serializable, IDomSerializable
  {
  	 static final long serialVersionUID = -6027270080903963150L;
  	 
  	 private  DynaProperty  m_dynaProperty;
    private  Object        m_defaultValue;
    private  ComplexType   m_complexType;
    private  String        m_label;
    private  String        m_description;


    /**
     *Constructor for the PropertyDefinition object
     *
     * @param name          Description of the Parameter
     * @param type          Description of the Parameter
     * @param label         Description of the Parameter
     * @param description   Description of the Parameter
     * @param defaultValue  Description of the Parameter
     */
    private PropertyDefinition( String name, String label, String description, Class type, Object defaultValue )
    {
      m_dynaProperty = new DynaProperty( name, type );
      m_defaultValue = defaultValue;
    }


    /**
     *Constructor for the PropertyDefinition object
     *
     * @param name         Description of the Parameter
     * @param complexType  Description of the Parameter
     * @param label        Description of the Parameter
     * @param description  Description of the Parameter
     */
    private PropertyDefinition( String name, String label, String description, ComplexType complexType )
    {
      m_dynaProperty = new DynaProperty( name, ComplexValue.class );
      m_complexType = complexType;
    }

    public String getName()
    {
      return m_dynaProperty.getName();
    }
    
    public String getLabel()
    {
      return m_label;
    }
    
    public Class getType ()
    {
      return m_dynaProperty.getType();
    }

    /**
     * @return   Returns the dynaProperty.
     */
    public DynaProperty getDynaProperty()
    {
      return m_dynaProperty;
    }


    /**
     * @return   Returns the complexType.
     */
    public ComplexType getComplexType()
    {
      return m_complexType;
    }


    /**
     * @return   The elementName value
     * @see      de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
     */
    public String getElementName()
    {
      return "property";
    }


    /**
     * @param element  Description of the Parameter
     * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
     */
    public void toDom( Element element )
    {
      element.addAttribute( "name", m_dynaProperty.getName() );
      if ( m_label != null ) {
        element.addAttribute( "label", m_label );
      }
      if ( m_description != null ) {
        element.addText( m_description );
      }
      element.addAttribute( "type", m_dynaProperty.getType().getName() );
      if ( m_complexType != null ) {
        element.addAttribute( "type", m_complexType.getName() );
      } else {
        element.addAttribute( "type", m_dynaProperty.getType().getName() );
      }
      if ( m_defaultValue != null ) {
        element.addAttribute( "default", m_defaultValue.toString() );
      }
    }
  }
}
