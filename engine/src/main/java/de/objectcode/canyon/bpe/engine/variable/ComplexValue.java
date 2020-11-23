package de.objectcode.canyon.bpe.engine.variable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;

/**
 * @author    junglas
 * @created   16. Juni 2004
 */
public class ComplexValue implements DynaBean, Serializable
{
  final static  long         serialVersionUID  = -3397654071303290834L;
  private       ComplexType  m_typeDefinition;
  private       Map          m_values;


  /**
   *Constructor for the ComplexType object
   *
   * @param typeDefinition  Description of the Parameter
   */
  public ComplexValue( ComplexType typeDefinition )
  {
    m_typeDefinition = typeDefinition;
    m_values = new HashMap();
  }

  public ComplexType getType()
  {
    return m_typeDefinition;
  }

  /**
   * @param name   Description of the Parameter
   * @param index  Description of the Parameter
   * @param val    Description of the Parameter
   * @see          org.apache.commons.beanutils.DynaBean#set(java.lang.String, int, java.lang.Object)
   */
  public void set( String name, int index, Object val )
  {
    Object  value  = m_values.get( name );

    if ( value != null ) {
      if ( value instanceof List ) {
        ( ( List ) value ).set( index, val );
      } else if ( value instanceof Object[] ) {
        ( ( Object[] ) value )[index] = val;
      }
    }
  }


  /**
   * @param name   Description of the Parameter
   * @param value  Description of the Parameter
   * @see          org.apache.commons.beanutils.DynaBean#set(java.lang.String, java.lang.Object)
   */
  public void set( String name, Object value )
  {
    m_values.put( name, value );
  }


  /**
   * (non-Javadoc)
   *
   * @param name  Description of the Parameter
   * @param key   Description of the Parameter
   * @param val   Description of the Parameter
   * @see         org.apache.commons.beanutils.DynaBean#set(java.lang.String, java.lang.String, java.lang.Object)
   */
  public void set( String name, String key, Object val )
  {
    Object  value  = m_values.get( name );

    if ( value != null && value instanceof Map ) {
      ( ( Map ) value ).put( key, val );
    }
  }


  /**
   * @param name  Description of the Parameter
   * @return      Description of the Return Value
   * @see         org.apache.commons.beanutils.DynaBean#get(java.lang.String)
   */
  public Object get( String name )
  {
    return m_values.get( name );
  }


  /**
   * @param name   Description of the Parameter
   * @param index  Description of the Parameter
   * @return       Description of the Return Value
   * @see          org.apache.commons.beanutils.DynaBean#get(java.lang.String, int)
   */
  public Object get( String name, int index )
  {
    Object  value  = m_values.get( name );

    if ( value != null ) {
      if ( value instanceof List ) {
        return ( ( List ) value ).get( index );
      } else if ( value instanceof Object[] ) {
        return ( ( Object[] ) value )[index];
      }
    }
    return null;
  }


  /**
   * @param name  Description of the Parameter
   * @param key   Description of the Parameter
   * @return      Description of the Return Value
   * @see         org.apache.commons.beanutils.DynaBean#get(java.lang.String, java.lang.String)
   */
  public Object get( String name, String key )
  {
    Object  value  = m_values.get( name );

    if ( value != null && value instanceof Map ) {
      return ( ( Map ) value ).get( key );
    }
    return null;
  }


  /**
   * @return   The dynaClass value
   * @see      org.apache.commons.beanutils.DynaBean#getDynaClass()
   */
  public DynaClass getDynaClass()
  {
    return m_typeDefinition;
  }


  /**
   * @param name  Description of the Parameter
   * @param key   Description of the Parameter
   * @see         org.apache.commons.beanutils.DynaBean#remove(java.lang.String, java.lang.String)
   */
  public void remove( String name, String key )
  {
    Object  value  = m_values.get( name );

    if ( value != null && value instanceof Map ) {
      ( ( Map ) value ).remove( key );
    }
  }


  /**
   * @param name  Description of the Parameter
   * @param key   Description of the Parameter
   * @return      Description of the Return Value
   * @see         org.apache.commons.beanutils.DynaBean#contains(java.lang.String, java.lang.String)
   */
  public boolean contains( String name, String key )
  {
    Object  value  = m_values.get( name );

    if ( value != null && value instanceof Map ) {
      return ( ( Map ) value ).containsKey( key );
    }
    return false;
  }
  
  public String toString()
  {
    return "ComplexValue(" + m_typeDefinition.getName() + ", " + m_values + ")";
  }
}
