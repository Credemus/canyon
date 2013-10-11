package de.objectcode.canyon.bpe.engine.evaluator;

import de.objectcode.canyon.bpe.engine.EngineException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author    junglas
 * @created   17. Juni 2004
 */
public class ScriptExpressionFactory
{
  private static  Map  g_bsfScriptLanguages;


  /**
   * Description of the Method
   *
   * @param language             Description of the Parameter
   * @param expression           Description of the Parameter
   * @return                     Description of the Return Value
   * @exception EngineException  Description of the Exception
   */
  public static IExpression createExpression( String language, String expression )
    throws EngineException
  {
    String  bsfLanguage  = ( String ) g_bsfScriptLanguages.get( language );

    if ( bsfLanguage != null ) {
      return new BSFExpression( bsfLanguage, expression );
    } else if ( language == null || "text/xpath".equals( language ) ) {
      return new JXPathExpression( expression );
    }

    return null;
  }
  
  public static ICondition createCondition ( String language, String expression )
  throws EngineException
  {
    String  bsfLanguage  = ( String ) g_bsfScriptLanguages.get( language );

    if ( bsfLanguage != null ) {
      return new BSFCondition( bsfLanguage, expression );
    } else if ( language == null || "text/xpath".equals( language ) ) {
      return new JXPathCondition( expression );
    }

    return null;
  }
  
  static {
    g_bsfScriptLanguages = new HashMap();

    g_bsfScriptLanguages.put( "text/netrexx", "netrexx" );
    g_bsfScriptLanguages.put( "text/perl", "perl" );
    g_bsfScriptLanguages.put( "text/vbscript", "vbscript" );
    g_bsfScriptLanguages.put( "text/javaclass", "javaclass" );
    g_bsfScriptLanguages.put( "text/bml", "bml" );
    g_bsfScriptLanguages.put( "text/javascript", "javascript" );
    g_bsfScriptLanguages.put( "text/jscript", "jscript" );
    g_bsfScriptLanguages.put( "text/jpython", "jpython" );
    g_bsfScriptLanguages.put( "text/jython", "jython" );
    g_bsfScriptLanguages.put( "text/beanshell", "beanshell" );
  }
}
