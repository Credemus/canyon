package de.objectcode.canyon.bpe.engine.evaluator;

import java.io.Serializable;

import org.apache.bsf.BSFManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.variable.IVariable;

/**
 * @author    junglas
 * @created   22. Juni 2004
 */
public class BSFCondition implements Serializable,ICondition
{
  static final long serialVersionUID = -1037041054522338792L;
  
  private final static Log log = LogFactory.getLog(BSFCondition.class);
  
  private  String  m_language;
  private  String  m_expression;


  /**
   *Constructor for the BSFCondition object
   *
   * @param language             Description of the Parameter
   * @param expression           Description of the Parameter
   * @exception EngineException  Description of the Exception
   */
  public BSFCondition( String language, String expression )
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
    return "bsf-condition";
  }


  /**
   * @param activity  Description of the Parameter
   * @return          Description of the Return Value
   * @see             de.objectcode.canyon.bpe.engine.evaluator.ICondition#eval(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public boolean eval( Activity activity )
  	throws EngineException
  {
    try {
      if (m_expression == null || m_expression.trim().length() == 0)
        return true; // empty condition is true
      
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
      
      return Boolean.TRUE.equals(bsfMgr.eval(m_language, null, 0,0, m_expression));
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
}
