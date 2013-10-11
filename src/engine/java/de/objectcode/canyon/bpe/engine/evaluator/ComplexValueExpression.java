package de.objectcode.canyon.bpe.engine.evaluator;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.engine.variable.ComplexValue;
import de.objectcode.canyon.bpe.util.IDomSerializable;

/**
 * @author    junglas
 * @created   17. Juni 2004
 */
public class ComplexValueExpression implements IAssignableExpression, Serializable, IDomSerializable
{

  static final long serialVersionUID = 6663677231885969706L;
  
  private  ComplexType  m_complexType;
  private  Map          m_expressions;


  /**
   *Constructor for the ComplexValueExpression object
   *
   * @param complexType  Description of the Parameter
   */
  public ComplexValueExpression( ComplexType complexType )
  {
    m_complexType = complexType;
    m_expressions = new LinkedHashMap();
  }


  /**
   * @return   The elementName value
   * @see      de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName()
  {
    return "complex-type-expression";
  }


  /**
   * Adds a feature to the Expression attribute of the ComplexValueExpression object
   *
   * @param propertyName  The feature to be added to the Expression attribute
   * @param expression    The feature to be added to the Expression attribute
   */
  public void addExpression( String propertyName, IExpression expression )
  {
    m_expressions.put( propertyName, expression );
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
    ComplexValue  value  = new ComplexValue( m_complexType );

    Iterator      it     = m_expressions.keySet().iterator();

    while ( it.hasNext() ) {
      String       propertyName  = ( String ) it.next();
      IExpression  expression    = ( IExpression ) m_expressions.get( propertyName );

      value.set( propertyName, expression.eval( activity ) );
    }

    return value;
  }


  /**
   * @param message  Description of the Parameter
   * @return         Description of the Return Value
   * @see            de.objectcode.canyon.bpe.engine.evaluator.IExpression#eval(de.objectcode.canyon.bpe.engine.correlation.Message)
   */
  public Object eval( Message message ) throws EngineException
  {
    ComplexValue  value  = new ComplexValue( m_complexType );

    Iterator      it     = m_expressions.keySet().iterator();

    while ( it.hasNext() ) {
      String       propertyName  = ( String ) it.next();
      IExpression  expression    = ( IExpression ) m_expressions.get( propertyName );

      value.set( propertyName, expression.eval( message ) );
    }

    return value;
  }


  /**
   * @see de.objectcode.canyon.bpe.engine.evaluator.IAssignableExpression#assign(de.objectcode.canyon.bpe.engine.activities.Activity, java.lang.Object)
   */
  public void assign (Activity activity, Object value) throws EngineException
  {
    if ( value instanceof ComplexValue ) {
      ComplexValue complexValue = (ComplexValue)value;
      Iterator      it     = m_expressions.keySet().iterator();

      while ( it.hasNext() ) {
        String       propertyName  = ( String ) it.next();
        IExpression  expression    = ( IExpression ) m_expressions.get( propertyName );
        
        if ( expression instanceof IAssignableExpression ) {
          ((IAssignableExpression)expression).assign(activity, complexValue.get(propertyName));
        }
      }
    }
  }
  
  /**
   * @see de.objectcode.canyon.bpe.engine.evaluator.IAssignableExpression#assign(de.objectcode.canyon.bpe.engine.correlation.Message, java.lang.Object)
   */
  public void assign (Message message, Object value) throws EngineException
  {
    if ( value instanceof ComplexValue ) {
      ComplexValue complexValue = (ComplexValue)value;
      Iterator      it     = m_expressions.keySet().iterator();

      while ( it.hasNext() ) {
        String       propertyName  = ( String ) it.next();
        IExpression  expression    = ( IExpression ) m_expressions.get( propertyName );
        
        if ( expression instanceof IAssignableExpression ) {
          ((IAssignableExpression)expression).assign(message, complexValue.get(propertyName));
        }
      }
    }
  }
  
  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    element.addAttribute( "complexType", m_complexType.getName() );
    Iterator  it  = m_expressions.keySet().iterator();

    while ( it.hasNext() ) {
      String       propertyName       = ( String ) it.next();
      IExpression  expression         = ( IExpression ) m_expressions.get( propertyName );
      Element      expressionElement  = element.addElement( expression.getElementName() );

      expression.toDom( expressionElement );
      expressionElement.addAttribute( "toProperty", propertyName );
    }
  }
}
