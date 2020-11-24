package de.objectcode.canyon.api.worklist;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author    junglas
 * @created   18. Februar 2004
 */
public class ProcessInstanceData implements Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 161879906855118015L;
	private  String  m_id;
  private  String  m_processDefinitionId;
  private  String  m_processDefinitionVersion;
  private  String  m_name;
  private  int     m_state;
  private  Map     m_workflowRelevantData;
  private  String  m_parentProcessInstanceIdPath;
  private  Date	   m_startedDate;
  private  String  m_startedBy;


  /**
   *Constructor for the ProcessInstanceData object
   */
  public ProcessInstanceData() { }


  /**
   *Constructor for the ProcessInstanceData object
   *
   * @param id                        Description of the Parameter
   * @param workflowRelevantData      Description of the Parameter
   * @param state                     Description of the Parameter
   * @param processDefinitionId       Description of the Parameter
   * @param processDefinitionVersion  Description of the Parameter
   * @param name                      Description of the Parameter
   */
  public ProcessInstanceData( String id, String parentProcessInstanceIdPath, String processDefinitionId, String processDefinitionVersion,
      String name, int state, Map workflowRelevantData, Date startedDate, String startedBy )
  {
    m_id = id;
    m_parentProcessInstanceIdPath = parentProcessInstanceIdPath;
    m_name = name;
    m_processDefinitionId = processDefinitionId;
    m_processDefinitionVersion = processDefinitionVersion;
    m_state = state;
    m_workflowRelevantData = workflowRelevantData;
    m_startedDate = startedDate;
    m_startedBy = startedBy;
  }


  /**
   * Gets the id attribute of the ProcessInstanceData object
   *
   * @return   The id value
   */
  public String getId()
  {
    return m_id;
  }


  /**
   * Gets the attributes attribute of the ProcessInstanceData object
   *
   * @return   The attributes value
   */
  public Attribute[] getAttributes()
  {
    Attribute[]  ret  = new Attribute[m_workflowRelevantData.size()];

    m_workflowRelevantData.values().toArray( ret );

    return ret;
  }


  /**
   * @return   Returns the processDefinitionId.
   */
  public String getProcessDefinitionId()
  {
    return m_processDefinitionId;
  }


  /**
   * @return   Returns the processDefinitionVersion.
   */
  public String getProcessDefinitionVersion()
  {
    return m_processDefinitionVersion;
  }


  /**
   * @return   Returns the state.
   */
  public int getState()
  {
    return m_state;
  }


  /**
   * @return   Returns the name.
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   18. Februar 2004
   */
  public static class Attribute implements Serializable
  {
  	static final long serialVersionUID = -6817874812457251343L;
  	
  	private  String  m_name;
    private  Object  m_value;


    /**
     *Constructor for the Attribute object
     */
    public Attribute() { }


    /**
     *Constructor for the Attribute object
     *
     * @param name   Description of the Parameter
     * @param value  Description of the Parameter
     */
    public Attribute( String name, Object value )
    {
      m_name = name;
      m_value = value;
    }


    /**
     * @return   Returns the name.
     */
    public String getName()
    {
      return m_name;
    }


    /**
     * @return   Returns the value.
     */
    public Object getValue()
    {
      return m_value;
    }

  }


	public String getParentProcessInstanceIdPath() {
		return m_parentProcessInstanceIdPath;
	}


	public String getStartedBy() {
		return m_startedBy;
	}


	public Date getStartedDate() {
		return m_startedDate;
	}
}
