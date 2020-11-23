package de.objectcode.canyon.bpe.engine.correlation;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.ExtensibleElement;
import de.objectcode.canyon.bpe.engine.evaluator.IAssignableExpression;

/**
 * @author    junglas
 * @created   8. Juni 2004
 */
public class CorrelationSet extends ExtensibleElement
{
  final static  long           serialVersionUID  = 5120229960498256799L;
  
  protected     String         m_name;
  protected     IAssignableExpression[]  m_expressions;


  /**
   *Constructor for the CorrelationSet object
   *
   * @param name         Description of the Parameter
   * @param expressions  Description of the Parameter
   */
  public CorrelationSet( String name, IAssignableExpression[] expressions )
  {
    m_name = name;
    m_expressions = expressions;
  }


  /**
   * @return   Returns the expressions.
   */
  public IAssignableExpression[] getExpressions()
  {
    return m_expressions;
  }


  /**
   * @return   Returns the name.
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * Gets the elementName attribute of the CorrelationSet object
   *
   * @return   The elementName value
   */
  public String getElementName()
  {
    return "correlation-set";
  }


  /**
   * Gets the correlation attribute of the CorrelationSet object
   *
   * @param message  Description of the Parameter
   * @return         The correlation value
   */
  public Correlation initiateCorrelation( Message message ) throws EngineException
  {
    int       i;
    Object[]  values  = new Object[m_expressions.length];

    for ( i = 0; i < m_expressions.length; i++ ) {
      values[i] = m_expressions[i].eval( message );
    }

    return new Correlation( this, values );
  }

  /**
   * Description of the Method
   *
   * @param element  Description of the Parameter
   */
  public void toDom( Element element )
  {
    int  i;

    element.addAttribute( "name", m_name );

    for ( i = 0; i < m_expressions.length; i++ ) {
      m_expressions[i].toDom( element.addElement( m_expressions[i].getElementName() ) );
    }
  }
}
