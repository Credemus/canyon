package de.objectcode.canyon.worklist.spi.worklist;

import java.io.Serializable;

import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.tool.Parameter;


/**
 * Description of the Class
 *
 * @author    junglas
 * @created   5. Dezember 2003
 */
public class DefaultApplicationData implements IApplicationData, Serializable
{
	static final long serialVersionUID = 3283580590757005501L;
	
	private String m_id;
  private String m_name;
  private Parameter[] m_parameters;
  private String m_description;
  private ExtendedAttribute[] m_extendedAttributes;

  /**
   *Constructor for the ApplicationData object
   *
   * @param name        Description of the Parameter
   * @param parameters  Description of the Parameter
   */
  public DefaultApplicationData(String id, String name, String description,
    Parameter[] parameters, ExtendedAttribute[] extendedAttributes)
  {
    m_id = id;
    m_name = name;
    m_parameters = parameters;
    m_description = description;
    m_extendedAttributes = extendedAttributes;
  }

  /**
   * @return
   */
  public String getName()
  {
    return m_name;
  }

  /**
   * @return
   */
  public Parameter[] getParameters()
  {
    return m_parameters;
  }

  /**
   * @see de.objectcode.canyon.worklist.spi.worklist.IApplicationData#getId()
   */
  public String getId()
  {
    return m_id;
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
  public ExtendedAttribute[] getExtendedAttributes()
  {
    return m_extendedAttributes;
  }
  
  
  /**
   * @see de.objectcode.canyon.worklist.spi.worklist.IApplicationData#setParameterValue(java.lang.String, java.lang.Object)
   */
  public void setParameterValue (String formalName, Object value)
  {
    int i;
    
    for ( i = 0; i < m_parameters.length; i++ ) {	
      if ( m_parameters[i].formalName.equals(formalName) && m_parameters[i].mode != ParameterMode.IN ) {
        m_parameters[i] = new Parameter(formalName, m_parameters[i].actualName, m_parameters[i].dataType,
            m_parameters[i].mode, m_parameters[i].description, value);
        
      }
    }
  }
}
