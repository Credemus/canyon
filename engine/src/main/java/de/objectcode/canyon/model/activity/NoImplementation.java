package de.objectcode.canyon.model.activity;

import de.objectcode.canyon.model.ValidationErrors;

/**
 * @author    junglas
 * @created   26. November 2003
 */
public class NoImplementation extends Implementation
{
	static final long serialVersionUID = -5078995327164185996L;
	
  /**
   * @return   Description of the Return Value
   * @see      de.objectcode.canyon.model.IValidatable#validate()
   */
  public ValidationErrors validate()
  {
    return new ValidationErrors();
  }

}
