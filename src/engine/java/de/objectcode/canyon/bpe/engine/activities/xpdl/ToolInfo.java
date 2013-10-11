package de.objectcode.canyon.bpe.engine.activities.xpdl;

import java.io.Serializable;

import de.objectcode.canyon.model.activity.Tool;
import de.objectcode.canyon.model.data.ActualParameter;

/**
 * @author    junglas
 * @created   15. Juli 2004
 */
public class ToolInfo implements Serializable
{
  final static  long               serialVersionUID      = -3882893221791467286L;

  private       String             m_id;
  private       String             m_name;
  private       String             m_description;
  private       ActualParameter[]  m_actualParameters;


  /**
   *Constructor for the ToolData object
   *
   * @param tool  Description of the Parameter
   */
  ToolInfo( Tool tool )
  {
    m_id = tool.getId();
    m_name = tool.getName();
    m_description = tool.getDescription();
    m_actualParameters = tool.getActualParameters();

  }


  /**
   * @return   Returns the actualParameters.
   */
  public ActualParameter[] getActualParameters()
  {
    return m_actualParameters;
  }


  /**
   * @return   Returns the description.
   */
  public String getDescription()
  {
    return m_description;
  }

  /**
   * @return   Returns the id.
   */
  public String getId()
  {
    return m_id;
  }


  /**
   * @return   Returns the name.
   */
  public String getName()
  {
    return m_name;
  }
  
  public String toString()
  {
    return "Tool(" + m_id + ", " + m_name + ")";
  }
}
