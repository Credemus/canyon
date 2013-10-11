package de.objectcode.canyon.model.transition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author    junglas
 * @created   26. November 2003
 */
public class Condition implements Serializable
{
	static final long serialVersionUID = 8669423982500467378L;
	
  private  ConditionType  m_type        = ConditionType.CONDITION;
  private  List           m_xpressions;
  private  String         m_value;


  /**
   *Constructor for the Condition object
   */
  public Condition()
  {
    m_xpressions = new ArrayList();
  }


  /**
   * @param type
   */
  public void setType( ConditionType type )
  {
    m_type = type;
  }


  /**
   * @param string
   */
  public void setValue( String string )
  {
    m_value = string;
  }


  /**
   * Gets the xpressions attribute of the Condition object
   *
   * @return   The xpressions value
   */
  public Xpression[] getXpressions()
  {
    Xpression  ret[]  = new Xpression[m_xpressions.size()];

    m_xpressions.toArray( ret );

    return ret;
  }


  /**
   * @return
   */
  public ConditionType getType()
  {
    return m_type;
  }


  /**
   * @return
   */
  public String getValue()
  {
    return m_value;
  }


  /**
   * Adds a feature to the Xpression attribute of the Condition object
   *
   * @param xpression  The feature to be added to the Xpression attribute
   */
  public void addXpression( Xpression xpression )
  {
    m_xpressions.add( xpression );
  }

}
