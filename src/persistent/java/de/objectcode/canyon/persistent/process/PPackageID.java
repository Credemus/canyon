package de.objectcode.canyon.persistent.process;

import java.io.Serializable;

import de.objectcode.canyon.spi.process.IPackageID;

/**
 * @author    junglas
 * @created   16. Oktober 2003
 */
public class PPackageID implements Serializable, IPackageID
{
	static final long serialVersionUID = -7581690352855710793L;
	
	private  String  m_id;
  private  String  m_version;


  /**
   *Constructor for the PPackageID object
   */
  public PPackageID() { }


  /**
   *Constructor for the PPackageID object
   *
   * @param packageId  Description of the Parameter
   * @param version    Description of the Parameter
   */
  public PPackageID( String id, String version )
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
   * @hibernate.property column="PACKAGEID" type="string" length="64"
   *
   * @return
   */
  public String getId()
  {
    return m_id;
  }


  /**
   * @hibernate.property column="PACKAGEVERSION" type="string" length="64"
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
    if ( !( obj instanceof PPackageID ) ) {
      return false;
    }

    PPackageID  castObj  = ( PPackageID ) obj;

    
    return ((m_id == null && castObj.getId() == null) || 
            (m_id != null && m_id.equals(castObj.getId()))) &&
           ((m_version == null && castObj.getVersion() == null) ||
            (m_version != null && m_version.equals(castObj.getVersion())));
  }


  /**
   * @return   Description of the Return Value
   * @see      java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    int code = 0;
    
    if ( m_id != null ) 
      code ^= m_id.hashCode();
    
    if ( m_version != null )
      code ^= m_version.hashCode();
    
    return code;
  }


  /**
   * @return   Description of the Return Value
   * @see      java.lang.Object#toString()
   */
  public String toString()
  {
    return "PPackageID [ " + m_id + ", " + m_version + "]";
  }
}
