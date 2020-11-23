package de.objectcode.canyon.model.data;

/**
 * @author    junglas
 * @created   26. November 2003
 */
public class DeclaredType extends DataType
{
	static final long serialVersionUID = -6579528438735580273L;
	
	private  String  m_id;


  /**
   * @param string
   */
  public void setId( String string )
  {
    m_id = string;
  }


  /**
   * @return
   */
  public String getId()
  {
    return m_id;
  }

  public Class getValueClass()
  {
    return Object.class;
  }
}
