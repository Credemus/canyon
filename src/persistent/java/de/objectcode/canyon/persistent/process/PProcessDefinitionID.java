package de.objectcode.canyon.persistent.process;

import java.io.Serializable;

import de.objectcode.canyon.spi.process.IProcessDefinitionID;

/**
 * @author    junglas
 * @created   4. Dezember 2003
 */
public class PProcessDefinitionID implements Serializable, IProcessDefinitionID
{
	static final long serialVersionUID = 2194990048285637031L;
	
	private  String  m_id;
  private  String  m_version;


  /**
   *Constructor for the PPackageID object
   */
  public PProcessDefinitionID() { }


  /**
   *Constructor for the PPackageID object
   *
   * @param version    Description of the Parameter
   * @param id         Description of the Parameter
   */
  public PProcessDefinitionID( String id, String version )
  {
    m_id = id;
    m_version = version;
  }


  /**
   * @param string
   */
  public void setId( String string )
  {
    m_id = string;
  }


  /**
   * @param string
   */
  public void setVersion( String string )
  {
    m_version = string;
  }


  /**
   * @hibernate.property column="PROCESSDEFINITIONID" type="string" length="64"
   *
   * @return
   */
  public String getId()
  {
    return m_id;
  }


  /**
   * @hibernate.property column="PROCESSDEFINITIONVERSION" type="string" length="64"
   *
   * @return
   */
  public String getVersion()
  {
    return m_version;
  }


  /**
   * @param obj  Description of the Parameter
   * @return     Description of the Return Value
   * @see        java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals( Object obj )
  {
    if ( !( obj instanceof PProcessDefinitionID ) ) {
      return false;
    }

    PProcessDefinitionID  castObj  = ( PProcessDefinitionID ) obj;

    return ( ( m_id == null && castObj.getId() == null ) ||
        ( m_id != null && m_id.equals( castObj.getId() ) ) ) &&
        ( ( m_version == null && castObj.getVersion() == null ) ||
        ( m_version != null && m_version.equals( castObj.getVersion() ) ) );
  }


  /**
   * @return   Description of the Return Value
   * @see      java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    int  code  = 0;

    if ( m_id != null ) {
      code ^= m_id.hashCode();
    }

    if ( m_version != null ) {
      code ^= m_version.hashCode();
    }

    return code;
  }


  /**
   * @return   Description of the Return Value
   * @see      java.lang.Object#toString()
   */
  public String toString()
  {
    return "PProcessDefinitionID [ " + m_id + ", " + m_version + "]";
  }

}
