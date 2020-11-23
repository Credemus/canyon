package de.objectcode.canyon.model.activity;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.canyon.model.BaseElement;
import de.objectcode.canyon.model.IValidatable;
import de.objectcode.canyon.model.ValidationErrors;
import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.ActualParameter;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class Tool extends BaseElement implements IValidatable
{
	static final long serialVersionUID = -644895738457555289L;
	
	private  Activity     m_activity;
  private  ToolType     m_type;
  private  List         m_actualParameters;
  private  Application  m_application;


  /**
   *Constructor for the Tool object
   */
  public Tool()
  {
    m_actualParameters = new ArrayList();
  }


  /**
   * @param type
   */
  public void setType( ToolType type )
  {
    m_type = type;
  }


  /**
   * @param activity
   */
  public void setActivity( Activity activity )
  {
    m_activity = activity;
  }


  /**
   * @param application
   */
  public void setApplication( Application application )
  {
    m_application = application;
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
  public ToolType getType()
  {
    return m_type;
  }


  /**
   * @return
   */
  public Activity getActivity()
  {
    return m_activity;
  }


  /**
   * @return
   */
  public Application getApplication()
  {
    return m_application;
  }


  /**
   * Adds a feature to the ActualParameter attribute of the Tool object
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

    m_application = m_activity.getContainer().getWorkflowProcess().getApplication(m_id);
    
    if ( m_application == null ) {
      m_application = m_activity.getContainer().getWorkflowProcess().getPackage().getApplication(m_id);
    }
    
    if ( m_application == null )
      errors.addMessage("tool.undefined.application", new Object[] {m_activity.getId(), m_id});

    return errors;
  }

}
