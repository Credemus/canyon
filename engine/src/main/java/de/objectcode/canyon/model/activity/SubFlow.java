package de.objectcode.canyon.model.activity;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.canyon.model.ValidationErrors;
import de.objectcode.canyon.model.data.ActualParameter;
import de.objectcode.canyon.model.process.WorkflowProcess;

/**
 * @author    junglas
 * @created   26. November 2003
 */
public class SubFlow extends Implementation
{
	static final long serialVersionUID = 7662719333112398877L;
	
	private  WorkflowProcess  m_workflowProcess;
  private  String           m_id;
  private  ExecutionType    m_execution;
  private  List             m_actualParameters;


  /**
   *Constructor for the SubFlow object
   */
  public SubFlow()
  {
    m_actualParameters = new ArrayList();
  }


  /**
   * @param type
   */
  public void setExecution( ExecutionType type )
  {
    m_execution = type;
  }


  /**
   * @param string
   */
  public void setId( String string )
  {
    m_id = string;
  }


  /**
   * @param process
   */
  public void setWorkflowProcess( WorkflowProcess process )
  {
    m_workflowProcess = process;
  }


  /**
   * Gets the actualParameters attribute of the Tool object
   *
   * @return   The actualParameters value
   */
  public ActualParameter[] getActualParameters()
  {
    ActualParameter  ret[]  = new ActualParameter[m_actualParameters.size()];

    m_actualParameters.toArray( ret );

    return ret;
  }


  /**
   * @return
   */
  public ExecutionType getExecution()
  {
    return m_execution;
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
  public WorkflowProcess getWorkflowProcess()
  {
    return m_workflowProcess;
  }


  /**
   * Adds a feature to the ActualParameter attribute of the SubFlow object
   *
   * @param actualParameter  The feature to be added to the ActualParameter attribute
   */
  public void addActualParameter( ActualParameter actualParameter )
  {
    m_actualParameters.add( actualParameter );
  }


  /**
   * @return   Description of the Return Value
   * @see      de.objectcode.canyon.model.IValidatable#validate()
   */
  public ValidationErrors validate()
  {
    ValidationErrors  errors  = new ValidationErrors();

    m_workflowProcess = m_activity.getContainer().getWorkflowProcess().getPackage().getWorklowProcess(m_id);

    if ( m_workflowProcess == null ) {
      errors.addMessage( "subFlow.undefined.process", new Object[]{m_activity.getId(), m_id} );
    }

    return errors;
  }

}
