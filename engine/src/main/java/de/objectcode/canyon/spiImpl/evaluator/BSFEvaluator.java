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

import de.objectcode.canyon.model.transition.Condition;
import de.objectcode.canyon.model.transition.ConditionType;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.evaluator.EvaluatorContext;
import de.objectcode.canyon.spi.evaluator.EvaluatorException;
import de.objectcode.canyon.spi.evaluator.IEvaluator;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.instance.IProcessInstance;

import java.util.Iterator;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Invokes the BSF to evaluate an expression in an arbitrary scripting language.
 *
 * @author    Adrian Price
 * @created   3. Februar 2004
 */
public final class BSFEvaluator implements IEvaluator
{
  private final static  Log     log           = LogFactory.getLog( BSFEvaluator.class );

  private final static  String  DISPLAY_NAME  = "BSF Evaluator";
  private final static  String  DESCRIPTION   =
      "An evaluator for expressions in BSF-supported scripting languages.";

  private               String  _lang;


  /**
   *Constructor for the BSFEvaluator object
   *
   * @param lang  Description of the Parameter
   */
  public BSFEvaluator( String lang )
  {
    if ( !BSFManager.isLanguageRegistered( lang ) ) {
      throw new IllegalArgumentException( lang );
    }
    _lang = lang;
  }


  /**
   * Description of the Method
   *
   * @param cond                    Description of the Parameter
   * @param ctx                     Description of the Parameter
   * @return                        Description of the Return Value
   * @exception EvaluatorException  Description of the Exception
   */
  public final boolean evaluateCondition( Condition cond, EvaluatorContext ctx )
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
  public Object evaluateExpression( Object expr, final EvaluatorContext ctx )
    throws EvaluatorException
  {

    try {
      BSFManager        bsfMgr  = new BSFManager();

      IProcessInstance  pi      = ctx.getProcessInstance();

      if ( pi != null ) {
        if ( log.isDebugEnabled() ) {
          log.debug( "Using attributes from process instance '" + pi.getProcessInstanceId() );
        }

        Iterator      it          = pi.getAttributeInstances().values().iterator();
        StringBuffer  logMessage  = null;

        if ( log.isDebugEnabled() ) {
          logMessage = new StringBuffer( "declare:" );
        }

        while ( it.hasNext() ) {
          IAttributeInstance  attr   = ( IAttributeInstance ) it.next();
          Object              value  = attr.getValue();

          if ( value != null ) {
            if ( log.isDebugEnabled() ) {
              logMessage.append( " " + attr.getName() + " = " + value + " (" + value.getClass() + ")" );
            }
            bsfMgr.declareBean( attr.getName(), value, value.getClass() );
          }
        }

        if ( log.isDebugEnabled() ) {
          log.debug( logMessage );
        }
      }

      if ( log.isDebugEnabled() ) {
        log.debug( "Evaluate (" + _lang + "): " + expr );
      }

      // TODO: optimize performance by precompilation, pooling, caching...
      // TODO: might be better to call declareBean instead.
      Object            ret     = bsfMgr.eval( _lang, null, 0, 0, expr );

      if ( log.isDebugEnabled() ) {
        log.debug( "Result: " + ret );
      }

      return ret;
    }
    catch ( RepositoryException e ) {
      throw new EvaluatorException( e );
    }
    catch ( BSFException e ) {
      throw new EvaluatorException( e );
    }
  }
}
