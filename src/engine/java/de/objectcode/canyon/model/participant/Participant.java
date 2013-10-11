package de.objectcode.canyon.model.participant;

import de.objectcode.canyon.model.BaseElement;
import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.IValidatable;
import de.objectcode.canyon.model.ValidationErrors;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class Participant extends BaseElement implements IValidatable
{
	static final long serialVersionUID = 3992624517974299517L;
	
	private final static  String           OBE_EXPRESSION  = "obe:Expression";

  private               ParticipantType  m_type;
  private               String           m_expression;


  /**
   * @param type
   */
  public void setType( ParticipantType type )
  {
    m_type = type;
  }


  /**
   * @return
   */
  public ParticipantType getType()
  {
    return m_type;
  }


  /**
   * @return   Returns the expression.
   */
  public String getExpression()
  {
    return m_expression;
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public ValidationErrors validate()
  {
    if ( m_extendedAttributes.containsKey( OBE_EXPRESSION ) ) {
      ExtendedAttribute  attr  = ( ExtendedAttribute ) m_extendedAttributes.get( OBE_EXPRESSION );

      m_expression = attr.getValue();

      m_extendedAttributes.remove( OBE_EXPRESSION );
    }

    return new ValidationErrors();
  }
}
