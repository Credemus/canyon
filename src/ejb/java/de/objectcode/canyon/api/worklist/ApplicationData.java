package de.objectcode.canyon.api.worklist;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author    junglas
 * @created   5. Dezember 2003
 */
public class ApplicationData implements Serializable
{
	static final long serialVersionUID = -773664994678301132L;

	public final static  int     IN            = 0;
  public final static  int     OUT           = 1;
  public final static  int     INOUT         = 2;

  private 						 String  m_id;	
  private              String  m_name;
  private			   String  m_description;
  private              Parameter[]     m_parameters;
  private              Map		       m_parameterMap;
  private			   Attribute[]    m_extendedAttributes;


  /**
   *Constructor for the ApplicationData object
   */
  public ApplicationData() { }

  public ApplicationData (String id, String name, String description, List parameters, List extendedAttributes ) 
  {
  	m_id = id;
    m_name = name;
    m_description = description;
    m_parameters = (Parameter[]) parameters.toArray(new Parameter[parameters.size()]);
    m_parameterMap = new HashMap();
    for (int i = 0; i < m_parameters.length; i++) {
    	m_parameterMap.put(m_parameters[i].getName(), m_parameters[i]);
    }
	m_extendedAttributes = (Attribute[]) extendedAttributes.toArray(new Attribute[extendedAttributes.size()]);
  }
  
  /**
   * @return
   */
  public String getName()
  {
    return m_name;
  }

  public String getDescription()
  {
	return m_description;
  }

  /**
   * Gets the parameter attribute of the ApplicationData object
   *
   * @param name  Description of the Parameter
   * @return      The parameter value
   */
  public Parameter getParameter( String name )
  {
    return ( Parameter ) m_parameterMap.get( name );
  }


  /**
   * Gets the parameters attribute of the ApplicationData object
   *
   * @return   The parameters value
   */
  public Parameter[] getParameters()
  {
  	return m_parameters;
  }

  public Attribute[] getExtendedAttributes()
  {
  	return m_extendedAttributes;
  }

  public String toString() {
  	StringBuffer buffer = new StringBuffer("ApplicationData@");
	buffer.append(Integer.toHexString(super.hashCode())).append(" [");
	Parameter[] params = getParameters();
	for (int i=0; i < params.length; i++) {
		buffer.append(" "); 
		buffer.append(params[i].getName());
		buffer.append("=");
		buffer.append(params[i].getValue());
		buffer.append("[");
		buffer.append(params[i].getDescription());
		buffer.append("]");
	}
	buffer.append(" ");
	Attribute[] attrs = getExtendedAttributes();
	for (int i=0; i < attrs.length; i++) {
		buffer.append(" "); 
		buffer.append(attrs[i].getName());
		buffer.append("=");
		buffer.append(attrs[i].getValue());
	}
	buffer.append("]");
    return buffer.toString();
  }
  
  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   5. Dezember 2003
   */
  public static class Parameter implements Serializable, IParameter
  {
  	static final long serialVersionUID = 4479370332326975917L;
  	
  	private  String  m_name;
    private int m_type;
    private  Object  m_value;
    private  int     m_mode;
    private String m_description = "";

    public Parameter() {
    }
    
    public Parameter ( String name, int mode, int type, Object value ) 
    {
      m_name = name;
      m_mode = mode;
      m_type = type;
      m_value = value;
    }

	public Parameter ( String name, int mode, int type, String description, Object value ) 
	{
	  m_name = name;
	  m_mode = mode;
	  m_type = type;
	  m_value = value;
	  m_description = description;
	}

    /**
     * @param object
     * @exception IllegalAccessException  Description of the Exception
     */
    public void setValue( Object object )
      throws IllegalAccessException
    {
      if ( m_mode == IN ) {
        throw new IllegalAccessException( "Parameter " + m_name + " is read only (IN)" );
      }
      m_value = object;
    }


    /**
     * @return
     */
    public int getMode()
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
    public int getType() {
      return m_type;
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

  public static class Attribute implements Serializable
    {
  	static final long serialVersionUID = 1068295491556108993L;
  	
  	private  String  m_name;
	private  Object  m_value;

	public Attribute() {
	}
    
	public Attribute ( String name, Object value ) 
	{
	  m_name = name;
	  m_value = value;
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


  }

		/**
		 * @return
		 */
		public String getId() {
			return m_id;
		}


}
