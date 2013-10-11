package de.objectcode.canyon.model.data;

import de.objectcode.canyon.model.BaseElement;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class TypeDeclaration extends BaseElement
{
	static final long serialVersionUID = 9202709499379161064L;
	
	private  DataType  m_dataType;


  /**
   * @param type
   */
  public void setDataType( DataType type )
  {
    m_dataType = type;
  }


  /**
   * @return
   */
  public DataType getDataType()
  {
    return m_dataType;
  }

}
