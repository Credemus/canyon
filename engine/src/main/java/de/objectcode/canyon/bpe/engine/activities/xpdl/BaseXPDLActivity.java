package de.objectcode.canyon.bpe.engine.activities.xpdl;

import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.activities.Scope;

/**
 * @author junglas
 */
public abstract class BaseXPDLActivity extends Activity
{
  static final long serialVersionUID = 8163933814540755914L;
  
  protected String m_description;
  protected int m_priority;
  
  protected BaseXPDLActivity ( String name, Scope scope, de.objectcode.canyon.model.activity.Activity activity )
  {
    super ( activity.getId(), name, scope );

    m_description = activity.getDescription();
    m_priority = activity.getPriority();
    m_name = activity.getName();
  }
  
  protected BaseXPDLActivity ( String id, String name, Scope scope, de.objectcode.canyon.model.activity.Activity activity )
  {
    super ( id, name, scope );

    m_description = activity.getDescription();
    m_priority = activity.getPriority();
    m_name = activity.getName();
  }

  /**
   * @return Returns the description.
   */
  public String getDescription ( )
  {
    return m_description;
  }
  /**
   * @return Returns the priority.
   */
  public int getPriority ( )
  {
    return m_priority;
  }
}
