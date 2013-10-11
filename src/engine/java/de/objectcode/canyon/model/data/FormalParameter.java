package de.objectcode.canyon.model.data;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class FormalParameter implements Serializable
{
	static final long serialVersionUID = -3924971944917431078L;
	
	private  String         m_id;
  private  String         m_name;
  private  String         m_description;
  private  int            m_index;
  private  DataType       m_dataType;
  private  ParameterMode  m_mode         = ParameterMode.IN;


  /**
   * @param type
   */
  public void setDataType( DataType type )
  {
    m_dataType = type;
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
  public void setId( String string )
  {
    m_id = string;
  }


  /**
   * @param i
   */
  public void setIndex( int i )
  {
    m_index = i;
  }


  /**
   * @param mode
   */
  public void setMode( ParameterMode mode )
  {
    m_mode = mode;
  }


  /**
   * @param string
   */
  public void setName( String string )
  {
    m_name = string;
  }


  /**
   * @return
   */
  public DataType getDataType()
  {
    return m_dataType;
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
  public String getId()
  {
    return m_id;
  }


  /**
   * @return
   */
  public int getIndex()
  {
    return m_index;
  }


  /**
   * @return
   */
  public ParameterMode getMode()
  {
    return m_mode;
  }


  /**
   * @return
   */
  public String getName()
  {
    return m_name;
  }

}
