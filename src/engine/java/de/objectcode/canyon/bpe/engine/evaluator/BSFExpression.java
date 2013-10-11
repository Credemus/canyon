package de.objectcode.canyon.bpe.engine.evaluator;

import java.io.Serializable;

import org.apache.bsf.BSFManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.variable.ComplexValue;
import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.spi.calendar.IBusinessCalendar;

/**
 * @author    junglas
 * @created   17. Juni 2004
 */
public class BSFExpression implements Serializable, IExpression
{
  static final long serialVersionUID = 8679531109974513294L;
  
  private final static  Log     log           = LogFactory.getLog( BSFExpression.class );

  private               String  m_language;
  private               String  m_expression;


  /**
   *Constructor for the BSFExpression object
   *
   * @param language             Description of the Parameter
   * @param expression           Description of the Parameter
   * @exception EngineException  Description of the Exception
   */
  public BSFExpression( String language, String expression )
    throws EngineException
  {
    if ( !BSFManager.isLanguageRegistered( language ) ) {
      throw new EngineException( "BSFManger does not have language: '" + m_language + "'" );
    }

    m_language = language;
    m_expression = expression;
  }


  /**
   * @return   The elementName value
   * @see      de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName()
  {
    return "bsf-expression";
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
    try {
      BSFManager   bsfMgr     = new BSFManager();

      IVariable[]  variables  = activity.getVariables();
      int          i;

      for ( i = 0; i < variables.length; i++ ) {
        // BSF uses Hashtable an cannot handle null values
        if ( variables[i].getValue() != null ) {
          bsfMgr.declareBean( variables[i].getName(), variables[i].getValue(), variables[i].getValueClass() );
        }
      }
      
      bsfMgr.declareBean("canyon_context", new BSFContext(activity), BSFContext.class);
      
      return bsfMgr.eval(m_language, null, 0,0, m_expression);
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
      throw new EngineException( e );
    }
  }


  /**
   * @param message  Description of the Parameter
   * @return         Description of the Return Value
   * @see            de.objectcode.canyon.bpe.engine.evaluator.IExpression#eval(de.objectcode.canyon.bpe.engine.correlation.Message)
   */
  public Object eval( Message message ) throws EngineException
  {
    try {
      BSFManager   bsfMgr     = new BSFManager();

      bsfMgr.declareBean("operation", message.getOperation(), String.class);
      bsfMgr.declareBean("content", message.getContent(), ComplexValue.class);
      
      return bsfMgr.eval(m_language, null, 0,0, m_expression);
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
      throw new EngineException( e );
    }
  }


  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    element.addAttribute( "language", m_language );
    element.addAttribute( "expression", m_expression );
  }
  
  public String toString()
  {
    return "BSFExpression('" + m_expression + "', " + m_language + ")";
  }
}
