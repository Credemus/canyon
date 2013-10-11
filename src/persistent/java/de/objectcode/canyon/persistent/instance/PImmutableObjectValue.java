/*
 *  Created on 17.12.2003
 */
package de.objectcode.canyon.persistent.instance;

/**
 * @hibernate.class table="POBJECTVALUES" proxy="de.objectcode.canyon.persistent.instance.PImmutableObjectValue" mutable="false"
 * 
 * @author    junglas
 * @created   17. Dezember 2003
 */
public class PImmutableObjectValue
{
  private  long    m_blobId;
  private  Object  m_value;

  public PImmutableObjectValue()
  {
  }
  
  public PImmutableObjectValue ( Object value )
  {
    m_value = value;
  }

  /**
   * @param l
   */
  public void setBlobId( long l )
  {
    m_blobId = l;
  }




  /**
   * @hibernate.id generator-class="native" column="BLOBID" type="long" unsaved-value="0"
   *
   *
   * @return
   */
  public long getBlobId()
  {
    return m_blobId;
  }


  /**
   * @hibernate.property column="OBJVALUE" type="de.objectcode.canyon.persistent.util.ImmutableSerializableType" not-null="false"
   *
   *
   * @return
   */
  public Object getValue()
  {
    return m_value;
  }

  /**
   * @param object
   */
  public void setValue( Object object )
  {
    m_value = object;
  }
  
}
