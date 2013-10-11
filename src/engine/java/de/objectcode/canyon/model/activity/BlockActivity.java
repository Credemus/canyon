package de.objectcode.canyon.model.activity;

import java.io.Serializable;

import de.objectcode.canyon.model.IValidatable;
import de.objectcode.canyon.model.ValidationErrors;

/**
 * @author    junglas
 * @created   21. November 2003
 */
public class BlockActivity implements Serializable, IValidatable
{
	static final long serialVersionUID = 8495754456177856402L;
	
	private  Activity  m_activity;
  private  String    m_blockId;
  private ActivitySet m_activitySet;


  /**
   * @param string
   */
  public void setBlockId( String string )
  {
    m_blockId = string;
  }


  /**
   * @param activity
   */
  public void setActivity( Activity activity )
  {
    m_activity = activity;
  }


  /**
   * @return
   */
  public String getBlockId()
  {
    return m_blockId;
  }


  /**
   * @return
   */
  public Activity getActivity()
  {
    return m_activity;
  }


  /**
   * @return   Description of the Return Value
   * @see      de.objectcode.canyon.model.IValidatable#validate()
   */
  public ValidationErrors validate()
  {
    ValidationErrors errors = new ValidationErrors();
    
    m_activitySet = m_activity.getContainer().getWorkflowProcess().getActivitySet(m_blockId);
    
    if ( m_activitySet == null ) {
      errors.addMessage("blockActivity.undefined.activitySet", new Object[] {m_activity.getId(), m_blockId});
    }
    
    return errors;
  }

  /**
   * @return
   */
  public ActivitySet getActivitySet() {
    return m_activitySet;
  }

}
