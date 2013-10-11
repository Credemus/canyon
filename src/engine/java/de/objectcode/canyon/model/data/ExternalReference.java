package de.objectcode.canyon.model.data;

/**
 * @author    junglas
 * @created   21. November 2003
 */
public class ExternalReference extends DataType
{
	static final long serialVersionUID = 2677143216685205534L;
	
	private  String  m_location;
  private  String  m_xref;
  private  String  m_namespace;


  /**
   * @param string
   */
  public void setLocation( String string )
  {
    m_location = string;
  }


  /**
   * @param string
   */
  public void setNamespace( String string )
  {
    m_namespace = string;
  }


  /**
   * @param string
   */
  public void setXref( String string )
  {
    m_xref = string;
  }


  /**
   * @return
   */
  public String getLocation()
  {
    return m_location;
  }


  /**
   * @return
   */
  public String getNamespace()
  {
    return m_namespace;
  }


  /**
   * @return
   */
  public String getXref()
  {
    return m_xref;
  }

  public Class getValueClass()
  {
    return Object.class;
  }
}
