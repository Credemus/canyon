package de.objectcode.canyon.persistent.instance;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.persistent.process.PProcessDefinitionID;
import de.objectcode.canyon.spi.ObjectAlreadyExistsException;
import de.objectcode.canyon.spi.ObjectNotFoundException;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.instance.BaseSystemAttribute;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.instance.IAttributedEntity;
import de.objectcode.canyon.spi.process.IProcessDefinitionID;

/**
 * @hibernate.class table="PATTRIBUTEDENTITIES"
 *
 * @author    junglas
 * @created   20. Oktober 2003
 */
public class PAttributedEntity implements IAttributedEntity
{
  private final static  Log                   log                    = LogFactory.getLog( PAttributedEntity.class );

  private final         Map                   m_sysAttrs             = new HashMap();

  protected             long                  m_entityOid;
  protected             Set                   m_attributesSet;
  protected             IProcessDefinitionID  m_processDefinitionId;
  protected transient   Map                   m_attributeMap;


  /**
   *Constructor for the PAttributedEntity object
   */
  public PAttributedEntity() { }


  /**
   *Constructor for the PAttributedEntity object
   *
   * @param propDescs  Description of the Parameter
   */
  protected PAttributedEntity( PropertyDescriptor[] propDescs )
  {
    for ( int i = 0; i < propDescs.length; i++ ) {
      PropertyDescriptor  propDesc  = propDescs[i];
      m_sysAttrs.put( propDesc.getName(), new SystemAttribute( propDesc ) );
    }
  }


  /**
   *Constructor for the PAttributedEntity object
   *
   * @param propDescs            Description of the Parameter
   * @param processDefinitionId  Description of the Parameter
   */
  protected PAttributedEntity( PropertyDescriptor[] propDescs, IProcessDefinitionID processDefinitionId )
  {
    this( propDescs );

    m_processDefinitionId = new PProcessDefinitionID( processDefinitionId.getId(), processDefinitionId.getVersion() );
    m_attributesSet = new HashSet();

  }


  /**
   * @param l
   */
  public void setEntityOid( long l )
  {
    m_entityOid = l;
  }


  /**
   * @param set  The new attributes value
   */
  public void setAttributesSet( Set set )
  {
    m_attributesSet = set;
  }


  /**
   * @param id  The new processDefinitionId value
   */
  public void setProcessDefinitionId( IProcessDefinitionID id )
  {
    m_processDefinitionId = id;
  }



  /**
   * @hibernate.component class="de.objectcode.canyon.persistent.process.PProcessDefinitionID"
   *
   * @return
   */
  public IProcessDefinitionID getProcessDefinitionId()
  {
    return m_processDefinitionId;
  }


  /**
   * @hibernate.id generator-class="native" column="ENTITYID" type="long" unsaved-value="0"
   *
   * @return
   */
  public long getEntityOid()
  {
    return m_entityOid;
  }


  /**
   * @hibernate.set cascade="all" lazy="true"
   * @hibernate.collection-key column="OWNERID"
   * @hibernate.collection-one-to-many class="de.objectcode.canyon.persistent.instance.PAttributeInstance"
   *
   * @return
   */
  public Set getAttributesSet()
  {
    return m_attributesSet;
  }


  /**
   * Gets the entityId attribute of the HibAttributedEntity object
   *
   * @return   The entityId value
   */
  public String getEntityId()
  {
    return String.valueOf( m_entityOid );
  }


  /**
   * Gets the attributeInstances attribute of the HibAttributedEntity object
   *
   * @return                         The attributeInstances value
   * @exception RepositoryException  Description of the Exception
   */
  public Map getAttributeInstances()
    throws RepositoryException
  {
    if ( m_attributeMap != null ) {
      return m_attributeMap;
    }

    TreeMap   attributeMap  = new TreeMap();

    Iterator  it            = m_attributesSet.iterator();

    while ( it.hasNext() ) {
      PAttributeInstance  attr  = ( PAttributeInstance ) it.next();

      attributeMap.put( attr.getName(), attr );
    }
    attributeMap.putAll( m_sysAttrs );

    m_attributeMap = attributeMap;

    return attributeMap;
  }


  /**
   * Gets the attributeInstance attribute of the HibAttributedEntity object
   *
   * @param attrName                 Description of the Parameter
   * @return                         The attributeInstance value
   * @exception RepositoryException  Description of the Exception
   */
  public IAttributeInstance getAttributeInstance( String attrName )
    throws RepositoryException
  {
    Map                 attributeMap  = getAttributeInstances();

    IAttributeInstance  attr          = ( IAttributeInstance ) attributeMap.get( attrName );

    if ( attr != null ) {
      return attr;
    }
    throw new ObjectNotFoundException( attrName );
  }


  /**
   * Adds a feature to the AttributeInstance attribute of the HibAttributedEntity object
   *
   * @param attrName                 The feature to be added to the AttributeInstance attribute
   * @param attrType                 The feature to be added to the AttributeInstance attribute
   * @param attrValue                The feature to be added to the AttributeInstance attribute
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public IAttributeInstance addAttributeInstance( String attrName,
      int attrType, Object attrValue )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "addAttributeInstance: " + m_entityOid + " " + attrName + " " + attrType + " " + attrValue );
    }

    // Can't override system attributes.
    if ( m_sysAttrs.containsKey( attrName ) ) {
      throw new ObjectAlreadyExistsException( attrName );
    }

    Map attributes = getAttributeInstances();

    PAttributeInstance attr = (PAttributeInstance)attributes.get(attrName);
    
    if ( attr == null ) {
      attr  = new PAttributeInstance( attrName, attrType, attrValue );

      m_attributesSet.add( attr );
    } else {
      attr.setValue(attrType, attrValue);
    }
    
    if ( m_attributeMap != null ) {
      m_attributeMap.put( attrName, attr );
    }

    return attr;
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String toString()
  {
    StringBuffer  buffer  = new StringBuffer( "PAttributedEntity[" );

    buffer.append( "oid=" ).append( m_entityOid );

    buffer.append( "set={" );
    Iterator      it      = m_attributesSet.iterator();

    while ( it.hasNext() ) {
      PAttributeInstance  attr  = ( PAttributeInstance ) it.next();

      buffer.append( attr.getName() ).append(" = ").append(attr.getValue()).append( "," );
    }
    buffer.append( "}]" );

    return buffer.toString();
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   24. Juni 2003
   */
  private class SystemAttribute extends BaseSystemAttribute
  {
    /**
     *Constructor for the SystemAttribute object
     *
     * @param propDesc  Description of the Parameter
     */
    public SystemAttribute( PropertyDescriptor propDesc )
    {
      super( propDesc );
    }


    /**
     * Gets the owner attribute of the SystemAttribute object
     *
     * @return   The owner value
     */
    public IAttributedEntity getOwner()
    {
      return PAttributedEntity.this;
    }
  }
}
