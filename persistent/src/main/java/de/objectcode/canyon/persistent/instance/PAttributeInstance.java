package de.objectcode.canyon.persistent.instance;

import java.util.Date;

import org.wfmc.wapi.WMAttribute;

import de.objectcode.canyon.engine.util.DataConverter;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.instance.IAttributedEntity;

/**
 * @hibernate.class table="PATTRIBUTEINSTANCES"
 *
 * @author    junglas
 * @created   20. Oktober 2003
 */
public class PAttributeInstance implements IAttributeInstance
{
  private  long               m_attributeInstanceId;
  private  String             m_name;
  private  int                m_type;
  private  String             m_stringValue;
  private  Boolean            m_booleanValue;
  private  Integer            m_intValue;
  private  Double             m_doubleValue;
  private  Date               m_dateValue;
  private  PObjectValue       m_objectValue;
  private  IAttributedEntity  m_owner;


  /**
   *Constructor for the PAttributeInstance object
   */
  public PAttributeInstance() { }


  /**
   *Constructor for the PAttributeInstance object
   *
   * @param attrName   Description of the Parameter
   * @param attrType   Description of the Parameter
   * @param attrValue  Description of the Parameter
   */
  public PAttributeInstance( String attrName, int attrType, Object attrValue )
  {
    m_name = attrName;
    m_type = attrType;

    setValue( attrType, attrValue );
  }


  /**
   * Sets the value attribute of the HibAttributeInstance object
   *
   * @param type   The new value value
   * @param value  The new value value
   */
  public void setValue( int type, Object value )
  {
    if ( type >= 0 ) {
      setType( type );
    }
    switch ( getType() ) {
      case WMAttribute.BOOLEAN_TYPE:
        setBooleanValue( ( Boolean ) DataConverter.getInstance().convertValue( value,
            Boolean.class ) );
        break;
      case WMAttribute.FLOAT_TYPE:
        setDoubleValue( ( Double ) DataConverter.getInstance().convertValue( value,
            Double.class ) );
        break;
      case WMAttribute.INTEGER_TYPE:
        setIntValue( ( Integer ) DataConverter.getInstance().convertValue( value,
            Integer.class ) );
        break;
      case WMAttribute.DATETIME_TYPE:
        setDateValue( ( Date ) DataConverter.getInstance().convertValue( value,
            Date.class ) );
        break;
      case WMAttribute.EXTERNAL_REFERENCE_TYPE:
      case WMAttribute.DECLARED_TYPE:
      case WMAttribute.SCHEMA_TYPE:
      case WMAttribute.RECORD_TYPE:
      case WMAttribute.UNION_TYPE:
      case WMAttribute.ARRAY_TYPE:
      case WMAttribute.LIST_TYPE:
        if ( getObjectValue() != null )
          getObjectValue().setValue(value);
        else
          setObjectValue( new PObjectValue(value) );
        break;
      default:
        setStringValue( ( String ) value );
    }
  }


  /**
   * @param boolean1
   */
  public void setBooleanValue( Boolean boolean1 )
  {
    m_booleanValue = boolean1;
  }


  /**
   * @param date
   */
  public void setDateValue( Date date )
  {
    m_dateValue = date;
  }


  /**
   * @param double1
   */
  public void setDoubleValue( Double double1 )
  {
    m_doubleValue = double1;
  }


  /**
   * @param integer
   */
  public void setIntValue( Integer integer )
  {
    m_intValue = integer;
  }


  /**
   * @param string
   */
  public void setName( String string )
  {
    m_name = string;
  }


  /**
   * @param object
   */
  public void setObjectValue( PObjectValue object )
  {
    m_objectValue = object;
  }


  /**
   * @param string
   */
  public void setStringValue( String string )
  {
    m_stringValue = string;
  }


  /**
   * @param i
   */
  public void setType( int i )
  {
    m_type = i;
  }


  /**
   * @param l
   */
  public void setAttributeInstanceId( long l )
  {
    m_attributeInstanceId = l;
  }


  /**
   * @param entity
   */
  public void setOwner( IAttributedEntity entity )
  {
    m_owner = entity;
  }


  /**
   * @hibernate.property column="BOOLVALUE" type="boolean" not-null="false"
   *
   * @return
   */
  public Boolean getBooleanValue()
  {
    return m_booleanValue;
  }


  /**
   * @hibernate.property column="DATAVALUE" type="timestamp" not-null="false"
   *
   * @return
   */
  public Date getDateValue()
  {
    return m_dateValue;
  }


  /**
   * @hibernate.property column="DBLVALUE" type="double" not-null="false"
   *
   * @return
   */
  public Double getDoubleValue()
  {
    return m_doubleValue;
  }


  /**
   * @hibernate.property column="INTVALUE" type="integer" not-null="false"
   *
   * @return
   */
  public Integer getIntValue()
  {
    return m_intValue;
  }


  /**
   * @hibernate.property column="STRVALUE" type="string" length="254" not-null="false"
   *
   * @return
   */
  public String getStringValue()
  {
    return m_stringValue;
  }


  /**
   * @hibernate.id generator-class="native" column="ATTRIBUTEINSTANCEID" type="long" unsaved-value="0"
   *
   * @return
   */
  public long getAttributeInstanceId()
  {
    return m_attributeInstanceId;
  }


  /**
   * @hibernate.many-to-one not-null="false" class="de.objectcode.canyon.persistent.instance.PObjectValue" column="BLOBID" cascade="all"
   *
   * @return
   */
  public PObjectValue getObjectValue()
  {
    return m_objectValue;
  }


  /**
   * @hibernate.property column="TYPE" type="integer" length="2" not-null="true"
   *
   * @return
   */
  public int getType()
  {
    return m_type;
  }


  /**
   * Gets the value attribute of the HibAttributeInstance object
   *
   * @return   The value value
   */
  public Object getValue()
  {
    Object  value;
    switch ( getType() ) {
      case WMAttribute.BOOLEAN_TYPE:
        value = getBooleanValue();
        break;
      case WMAttribute.FLOAT_TYPE:
        value = getDoubleValue();
        break;
      case WMAttribute.INTEGER_TYPE:
        value = getIntValue();
        break;
      case WMAttribute.DATETIME_TYPE:
        value = getDateValue();
        break;
      case WMAttribute.EXTERNAL_REFERENCE_TYPE:
      case WMAttribute.DECLARED_TYPE:
      case WMAttribute.SCHEMA_TYPE:
      case WMAttribute.RECORD_TYPE:
      case WMAttribute.UNION_TYPE:
      case WMAttribute.ARRAY_TYPE:
      case WMAttribute.LIST_TYPE:
        value = getObjectValue().getValue();
        break;
      default:
        value = getStringValue();
    }
    return value;
  }


  /**
   * @hibernate.property column="NAME" type="string" length="64" not-null="true"
   *
   * @return
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * @hibernate.many-to-one column="OWNERID" class="de.objectcode.canyon.persistent.instance.PAttributedEntity"
   *
   *
   * @return
   */
  public IAttributedEntity getOwner()
  {
    return m_owner;
  }

  public String toString()
	{
  	StringBuffer buffer =new StringBuffer("PAttributeInstance@");

  	buffer.append(Integer.toHexString(hashCode())).append("[");
  	buffer.append("name=").append(m_name);
  	buffer.append(", type=").append(m_type);
  	buffer.append(", value=").append(getValue());
  	buffer.append("]");
  	
  	return buffer.toString();
  }
}
