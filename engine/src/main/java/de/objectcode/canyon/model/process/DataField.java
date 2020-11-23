package de.objectcode.canyon.model.process;

import de.objectcode.canyon.model.BaseElement;
import de.objectcode.canyon.model.data.DataType;

/**
 * @author    junglas
 * @created   21. November 2003
 */
public class DataField extends BaseElement
{
	static final long serialVersionUID = -8422793160748726155L;
	
	private  DataType  m_dataType;
  private  String    m_initialValue;
  private  int       m_length;
  private  boolean   m_isArray;

  public DataField () 
  {
  }

  public DataField ( String id, String name, DataType dataType )
  {
    super(id,name);
    
    m_dataType = dataType;
  }

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
  public void setInitialValue( String string )
  {
    m_initialValue = string;
  }


  /**
   * @param b
   */
  public void setIsArray( boolean b )
  {
    m_isArray = b;
  }


  /**
   * @param i
   */
  public void setLength( int i )
  {
    m_length = i;
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
  public String getInitialValue()
  {
    return m_initialValue;
  }


  /**
   * @return
   */
  public boolean getIsArray()
  {
    return m_isArray;
  }


  /**
   * @return
   */
  public int getLength()
  {
    return m_length;
  }

}
