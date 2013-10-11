package de.objectcode.canyon.bpe.engine.evaluator;

import java.io.Serializable;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.variable.IVariable;

import org.dom4j.Element;

/**
 * @author    junglas
 * @created   14. Juni 2004
 */
public class VariableExpression implements Serializable, IAssignableExpression
{
  static final long serialVersionUID = -6786172339587419916L;
  protected  String  m_variableName;


  /**
   *Constructor for the VariableExpression object
   *
   * @param variableName  Description of the Parameter
   */
  public VariableExpression( String variableName )
  {
    m_variableName = variableName;
  }


  /**
   * @return   The elementName value
   * @see      de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName()
  {
    return "variable-expression";
  }


  /**
   * @param activity             Description of the Parameter
   * @param value                Description of the Parameter
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.evaluator.IExpression#assign(de.objectcode.canyon.bpe.engine.activities.Activity, java.lang.Object)
   */
  public void assign( Activity activity, Object value )
    throws EngineException
  {
    IVariable  variable  = activity.getVariable( m_variableName );

    if ( variable == null ) {
      throw new EngineException( "Variable '" + m_variableName + "' not found in activity '" + activity.getId() + "'" );
    }
    variable.setValue( value );
  }


  /**
   * @param message              Description of the Parameter
   * @param value                Description of the Parameter
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.evaluator.IAssignableExpression#assign(de.objectcode.canyon.bpe.engine.correlation.Message, java.lang.Object)
   */
  public void assign( Message message, Object value )
    throws EngineException
  {
    message.getContent().set( m_variableName, value );
  }


  /**
   * @param activity             Description of the Parameter
   * @return                     Description of the Return Value
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.evaluator.IExpression#eval(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public Object eval( Activity activity )
    throws EngineException
  {
    IVariable  variable  = activity.getVariable( m_variableName );

    if ( variable == null ) {
      throw new EngineException( "Variable '" + m_variableName + "' not found in activity '" + activity.getId() + "'" );
    }

    return variable.getValue();
  }


  /**
   * @param message  Description of the Parameter
   * @return         Description of the Return Value
   * @see            de.objectcode.canyon.bpe.engine.evaluator.IExpression#eval(de.objectcode.canyon.bpe.engine.correlation.IMessage)
   */
  public Object eval( Message message )
  {
    return message.getContent().get( m_variableName );
  }


  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    element.addAttribute( "variableName", m_variableName );
  }
}
