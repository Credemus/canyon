package de.objectcode.canyon.api.worklist;

import java.io.Serializable;


/**
 * @author    junglas
 * @created   10. Dezember 2003
 */
public class ProcessData implements Serializable
{
	static final long serialVersionUID = -4672021667776855268L;
	
	private String m_id;
  private String m_name;
  private String m_version;
  private String m_packageId;
  private String m_packageName;
  private int m_state;
  private Parameter[] m_parameters;
  private Parameter[] m_extendedAttributes;

  /**
   *Constructor for the ProcessData object
   */
  public ProcessData()
  {
  }

  /**
   *Constructor for the ProcessData object
   *
   * @param id           Description of the Parameter
   * @param parameters   Description of the Parameter
   * @param name         Description of the Parameter
   * @param version      Description of the Parameter
   * @param packageId    Description of the Parameter
   * @param packageName  Description of the Parameter
   * @param state        Description of the Parameter
   */
  public ProcessData(String id, String name, String version, String packageId,
    String packageName, int state, Parameter[] parameters)
  {
    m_id = id;
    m_name = name;
    m_version = version;
    m_packageId = packageId;
    m_packageName = packageName;
    m_state = state;
    m_parameters = parameters;
    m_extendedAttributes = new Parameter[0];
  }

  /**
   *Constructor for the ProcessData object
   *
   * @param id           Description of the Parameter
   * @param parameters   Description of the Parameter
   * @param name         Description of the Parameter
   * @param version      Description of the Parameter
   * @param packageId    Description of the Parameter
   * @param packageName  Description of the Parameter
   * @param state        Description of the Parameter
   */
  public ProcessData(String id, String name, String version, String packageId,
    String packageName, int state, Parameter[] parameters,
    Parameter[] extendedAttributes)
  {
    m_id = id;
    m_name = name;
    m_version = version;
    m_packageId = packageId;
    m_packageName = packageName;
    m_state = state;
    m_parameters = parameters;
    m_extendedAttributes = extendedAttributes;
  }

  /**
   * @param string
   */
  public void setId(String string)
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

  /**
   * @return
   */
  public Parameter[] getParameters()
  {
    return m_parameters;
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
  public String getVersion()
  {
    return m_version;
  }

  /**
   * @return   Returns the packageId.
   */
  public String getPackageId()
  {
    return m_packageId;
  }

  /**
   * @return   Returns the packageName.
   */
  public String getPackageName()
  {
    return m_packageName;
  }

  /**
   * @return   Returns the state.
   */
  public int getState()
  {
    return m_state;
  }

  /**
   * @return
   */
  public Parameter[] getExtendedAttributes()
  {
    return m_extendedAttributes;
  }

  /**
   * @param parameters
   */
  public void setExtendedAttributes(Parameter[] parameters)
  {
    m_extendedAttributes = parameters;
  }
  
  public void setParameterValue ( String parameterId, Object value) throws IllegalAccessException
  {
    int i;
    
    for ( i = 0; i < m_parameters.length; i++ ) {
      if ( parameterId.equals(m_parameters[i].getName())) {
        m_parameters[i].setValue(value);
        
        return;
      }
    }
  }

  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   5. Dezember 2003
   */
  public static class Parameter implements Serializable, IParameter
  {
  	 static final long serialVersionUID = 2032684319460410147L;
  	 
  	 private String m_name;
    private int m_type;
    private Object m_value;
    private String m_description = "";

    /**
     *Constructor for the Parameter object
     */
    public Parameter()
    {
    }

    /**
     *Constructor for the Parameter object
     *
     * @param name   Description of the Parameter
     * @param type   Description of the Parameter
     * @param value  Description of the Parameter
     */
    public Parameter(String name, int type, Object value)
    {
      m_name = name;
      m_type = type;
      m_value = value;
    }

	public Parameter(String name, int type, String description, Object value)
	{
	  m_name = name;
	  m_type = type;
	  m_value = value;
	  m_description = description;
	}

    /**
     * @param object
     * @exception IllegalAccessException  Description of the Exception
     */
    public void setValue(Object object) throws IllegalAccessException
    {
      m_value = object;
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
    public Object getValue()
    {
      return m_value;
    }

    /**
     * @return
     */
    public int getType()
    {
      return m_type;
    }

	// WARNING: When starting a process we hand over Parameters to the process bean
	// from this point of view the mode is out not in! Do not change this.
	public int getMode() {
		return IParameter.MODE_OUT_INT;
	}
	
	/**
	 * @return
	 */
	public String getDescription() {
		return m_description;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		m_description = string;
	}

  }
}
