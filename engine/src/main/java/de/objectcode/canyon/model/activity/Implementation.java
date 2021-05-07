package de.objectcode.canyon.model.activity;

import java.io.Serializable;

import de.objectcode.canyon.model.IValidatable;

/**
 * @author junglas
 * @created 26. November 2003
 */
public abstract class Implementation implements Serializable, IValidatable {
  static final long serialVersionUID = 5030103501423852554L;

  protected Activity m_activity;


  /**
   * @param activity
   */
  public void setActivity(Activity activity) {
    m_activity = activity;
  }


  /**
   * @return
   */
  public Activity getActivity() {
    return m_activity;
  }

}
