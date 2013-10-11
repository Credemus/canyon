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
package de.objectcode.canyon.spi.evaluator;


import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.instance.IActivityInstance;
import de.objectcode.canyon.spi.instance.IProcessInstance;

/**
 * Exposes workflow context data to expression evaluators.
 *
 * @author    Adrian Price
 * @created   27. Oktober 2003
 */
public class EvaluatorContext
{
  private  WorkflowProcess    m_workflowProcess;
  private  IProcessInstance   m_processInstance;
  private  Activity           m_activity;
  private  IActivityInstance  m_activityInstance;


  /**
   *Constructor for the EvaluatorContext object
   *
   * @param workflowProcess   Description of the Parameter
   * @param processInstance   Description of the Parameter
   * @param activity          Description of the Parameter
   * @param activityInstance  Description of the Parameter
   */
  public EvaluatorContext( WorkflowProcess workflowProcess, IProcessInstance processInstance,
      Activity activity, IActivityInstance activityInstance )
  {
    m_activity = activity;
    m_activityInstance = activityInstance;
    m_workflowProcess = workflowProcess;
    m_processInstance = processInstance;
  }


  /**
   * Returns the activity definition that is currently being enacted.
   *
   * @return   The activity definition.
   */
  public Activity getActivity()
  {
    return m_activity;
  }


  /**
   * Returns the activity instance that is currently being enacted.
   *
   * @return   The activity instance.
   */
  public IActivityInstance getActivityInstance()
  {
    return m_activityInstance;
  }


  /**
   * Returns the process instance being enacted in this context.
   *
   * @return   The process instance.
   */
  public IProcessInstance getProcessInstance()
  {
    return m_processInstance;
  }


  /**
   * Returns the workflow definition that is currently being enacted.
   *
   * @return   The workflow process definition.
   */
  public WorkflowProcess getWorkflow()
  {
    return m_workflowProcess;
  }
}
