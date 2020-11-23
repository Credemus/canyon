package de.objectcode.canyon.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class PackageHeader implements Serializable
{
	static final long serialVersionUID = -5716317102619577026L;
	
	private  String  m_XPDLVersion;
  private  String  m_vendor;
  private  Date    m_created;
  private  String  m_description;
  private  String  m_documentation;
  private  String  m_priorityUnit;
  private  String  m_costUnit;


  /**
   * @param string
   */
  public void setCostUnit( String string )
  {
    m_costUnit = string;
  }


  /**
   * @param date
   */
  public void setCreated( Date date )
  {
    m_created = date;
  }


  /**
   * @param string
   */
  public void setDescription( String string )
  {
    m_description = string;
  }


  /**
   * @param string
   */
  public void setDocumentation( String string )
  {
    m_documentation = string;
  }


  /**
   * @param string
   */
  public void setPriorityUnit( String string )
  {
    m_priorityUnit = string;
  }


  /**
   * @param string
   */
  public void setVendor( String string )
  {
    m_vendor = string;
  }


  /**
   * @param string
   */
  public void setXPDLVersion( String string )
  {
    m_XPDLVersion = string;
  }


  /**
   * @return
   */
  public String getCostUnit()
  {
    return m_costUnit;
  }


  /**
   * @return
   */
  public Date getCreated()
  {
    return m_created;
  }


  /**
   * @return
   */
  public String getDescription()
  {
    return m_description;
  }


  /**
   * @return
   */
  public String getDocumentation()
  {
    return m_documentation;
  }


  /**
   * @return
   */
  public String getPriorityUnit()
  {
    return m_priorityUnit;
  }


  /**
   * @return
   */
  public String getVendor()
  {
    return m_vendor;
  }


  /**
   * @return
   */
  public String getXPDLVersion()
  {
    return m_XPDLVersion;
  }

}
