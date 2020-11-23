package de.objectcode.canyon.model.activity;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.canyon.model.ValidationErrors;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class ToolSet extends Implementation
{
	static final long serialVersionUID = 7207765721775382154L;
	
	private  List  m_tools;


  /**
   *Constructor for the ToolSet object
   */
  public ToolSet()
  {
    m_tools = new ArrayList();
  }


  /**
   * Gets the tools attribute of the ToolSet object
   *
   * @return   The tools value
   */
  public Tool[] getTools()
  {
    Tool  ret[]  = new Tool[m_tools.size()];

    m_tools.toArray( ret );

    return ret;
  }


  /**
   * Adds a feature to the Tool attribute of the ToolSet object
   *
   * @param tool  The feature to be added to the Tool attribute
   */
  public void addTool( Tool tool )
  {
    m_tools.add( tool );
  }


  /**
   * @return   Description of the Return Value
   * @see      de.objectcode.canyon.model.IValidatable#validate()
   */
  public ValidationErrors validate()
  {
    ValidationErrors  errors  = new ValidationErrors();

    errors.check( m_tools );

    return errors;
  }

}
