/*
 *  --
 *  Copyright (C) 2002-2003 Aetrion LLC.
 *  All rights reserved.
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions, and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions, and the disclaimer that follows
 *  these conditions in the documentation and/or other materials
 *  provided with the distribution.
 *  3. The names "OBE" and "Open Business Engine" must not be used to
 *  endorse or promote products derived from this software without prior
 *  written permission.  For written permission, please contact
 *  obe@aetrion.com.
 *  4. Products derived from this software may not be called "OBE" or
 *  "Open Business Engine", nor may "OBE" or "Open Business Engine"
 *  appear in their name, without prior written permission from
 *  Aetrion LLC (obe@aetrion.com).
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT,
 *  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 *  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 *  STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 *  IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 *  For more information on OBE, please see
 *  <http://www.openbusinessengine.org/>.
 */
package de.objectcode.canyon.spiImpl.evaluator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.BasicVariables;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.CompiledExpression;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Variables;

import de.objectcode.canyon.CanyonRuntimeException;
import de.objectcode.canyon.model.transition.Condition;
import de.objectcode.canyon.model.transition.ConditionType;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.evaluator.EvaluatorContext;
import de.objectcode.canyon.spi.evaluator.EvaluatorException;
import de.objectcode.canyon.spi.evaluator.IEvaluator;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.instance.IProcessInstance;

/**
 * Invokes JXPath to evaluate an XPath expression against the workflow data.
 *
 * @author    Adrian Price
 * @created   27. Oktober 2003
 */
public final class JXPathEvaluator implements IEvaluator
{
  private final static  Map              compiledExpressions  = new HashMap();
  private static        FunctionLibrary  funcLib;


  /**
   *Constructor for the JXPathEvaluator object
   */
  public JXPathEvaluator() { }

  /**
   * Gets the jXPathContext attribute of the JXPathEvaluator object
   *
   * @param ctx                      Description of the Parameter
   * @return                         The jXPathContext value
   * @exception RepositoryException  Description of the Exception
   */
  private JXPathContext getJXPathContext( final EvaluatorContext ctx )
    throws RepositoryException
  {

    // Check the cache first.
    JXPathContext  jxpc  = JXPathContext.newContext( ctx );

    Variables      vars  =
      new BasicVariables()
      {
        final  Map  _data;


        public Object getVariable( String s )
        {
          return ( ( IAttributeInstance ) _data.get( s ) ).getValue();
        }


        public boolean isDeclaredVariable( String s )
        {
          return _data.containsKey( s );
        }
        {
          try {
            // Provide access to the workflow instance data.
            IProcessInstance  pi  = ctx.getProcessInstance();
            _data = pi == null
                ? Collections.EMPTY_MAP
                : pi.getAttributeInstances();
          }
          catch ( RepositoryException e ) {
            throw new CanyonRuntimeException( e );
          }
        }
      };
    jxpc.setVariables( vars );

    // Provide access to system functions.
    if ( funcLib == null ) {
      funcLib = new FunctionLibrary();
      
      funcLib.addFunctions(new ClassFunctions(RuntimeFunctions.class, "obe"));
      funcLib.addFunctions(new ClassFunctions(WfMCFunctions.class, "wfmc"));
    }
    jxpc.setFunctions( funcLib );
    return jxpc;
  }


  /**
   * Gets the compiledExpression attribute of the JXPathEvaluator object
   *
   * @param jxpc  Description of the Parameter
   * @param expr  Description of the Parameter
   * @return      The compiledExpression value
   */
  private CompiledExpression getCompiledExpression( JXPathContext jxpc,
      String expr )
  {

    CompiledExpression  cexpr  = ( CompiledExpression )
        compiledExpressions.get( expr );
    if ( cexpr == null ) {
      cexpr = JXPathContext.compile( expr );
      synchronized ( compiledExpressions ) {
        compiledExpressions.put( expr, cexpr );
      }
    }
    return cexpr;
  }


  /**
   * Description of the Method
   *
   * @param cond                    Description of the Parameter
   * @param ctx                     Description of the Parameter
   * @return                        Description of the Return Value
   * @exception EvaluatorException  Description of the Exception
   */
  public boolean evaluateCondition( Condition cond, EvaluatorContext ctx )
    throws EvaluatorException
  {

    boolean  ret;
    if ( cond.getType().equals( ConditionType.CONDITION ) ) {
      String  text  = cond.getValue().trim();
      if ( text.length() > 0 ) {
        Object  result  = evaluateExpression( text, ctx );
        ret = Boolean.TRUE.equals( result );
      } else {
        // If there's no condition, the result is true.
        ret = true;
      }
    } else {
      ret = cond.getType().equals( ConditionType.OTHERWISE );
    }
    return ret;
  }


  /**
   * Description of the Method
   *
   * @param expr                    Description of the Parameter
   * @param ctx                     Description of the Parameter
   * @return                        Description of the Return Value
   * @exception EvaluatorException  Description of the Exception
   */
  public Object evaluateExpression( Object expr, EvaluatorContext ctx )
    throws EvaluatorException
  {

    try {
      JXPathContext       jxpc;

      jxpc = getJXPathContext( ctx );

      // Evaluate the expression.
      CompiledExpression  cexpr  = getCompiledExpression( jxpc,
          ( String ) expr );
      return cexpr.getValue( jxpc );
    }
    catch ( RepositoryException e ) {
      throw new EvaluatorException( e );
    }
  }
}
