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
package de.objectcode.canyon.spi.event;

import org.wfmc.audit.WMAEventCode;
import org.wfmc.wapi.WMProcessInstanceState;

import de.objectcode.canyon.CanyonRuntimeException;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.instance.IProcessInstance;
import de.objectcode.canyon.spi.process.IProcessRepository;

/**
 * Delivers process instance event notifications.
 *
 * @author    Adrian Price
 * @created   3. November 2003
 * @see       ProcessInstanceListener
 */
public final class ProcessInstanceEvent extends WorkflowEvent
{
  final static          long             serialVersionUID  = -5575325141776012183L;
  /**
   * The process instance was aborted.
   */
  public final static   int              ABORTED           = WMProcessInstanceState.ABORT_ACTION;
  /**
   * The process instance was completed.
   */
  public final static   int              COMPLETED         = WMProcessInstanceState.COMPLETE_ACTION;
  /**
   * The process instance was created.
   */
  public final static   int              CREATED           = WMProcessInstanceState.CREATE_ACTION;
  /**
   * The process instance was deleted.
   */
  public final static   int              DELETED           = WMProcessInstanceState.DELETE_ACTION;
  /**
   * The process instance was resumed.
   */
  public final static   int              RESUMED           = WMProcessInstanceState.RESUME_ACTION;
  /**
   * The process instance was started.
   */
  public final static   int              STARTED           = WMProcessInstanceState.START_ACTION;
  /**
   * The process instance was suspended.
   */
  public final static   int              SUSPENDED         = WMProcessInstanceState.SUSPEND_ACTION;
  /**
   * The process instance was terminated.
   */
  public final static   int              TERMINATED        = WMProcessInstanceState.TERMINATE_ACTION;
  private final static  WMAEventCode[]   IF5_EVENT_CODES   = {
      WMAEventCode.ABORTED_PROCESS_INSTANCE,
      WMAEventCode.COMPLETED_PROCESS_INSTANCE,
      WMAEventCode.CREATED_PROCESS_INSTANCE,
      null,
      WMAEventCode.CHANGED_PROCESS_INSTANCE_STATE,
      WMAEventCode.STARTED_PROCESS_INSTANCE,
      WMAEventCode.CHANGED_PROCESS_INSTANCE_STATE,
      WMAEventCode.TERMINATED_PROCESS_INSTANCE
      };
  private final static  String[]         EVENT_TYPES       = {
      "ProcessInstanceAborted",
      "ProcessInstanceCompleted",
      "ProcessInstanceCreated",
      "ProcessInstanceDeleted",
      "ProcessInstanceResumed",
      "ProcessInstanceStarted",
      "ProcessInstanceSuspended",
      "ProcessInstanceTerminated"
      };

  private transient     WorkflowProcess  _definition;


  /**
   * Constructs a new process instance event.
   *
   * @param source
   * @param id
   * @param broker
   * @param definition
   */
  public ProcessInstanceEvent( IProcessInstance source,
      int id, IWorkflowEventBroker broker, WorkflowProcess definition )
  {

    super( source, id, IProcessInstance.class, EVENT_TYPES[id],
        source.getProcessInstanceId(), broker );
    _definition = definition;
  }


  /**
   * Returns the source process instance.
   *
   * @return   The process instance that fired the event.
   */
  public IProcessInstance getProcessInstance()
  {
    return ( IProcessInstance ) source;
  }


  /**
   * Returns the definition of the source process instance.
   *
   * @return   The workflow process definition.
   */
  public WorkflowProcess getProcessDefinition()
  {
    if ( _definition == null && _broker != null ) {
      IProcessRepository  processRepository  =
          _broker.getServiceManager().getProcessRepository();
      try {
        _definition = processRepository.findWorkflowProcess(
            getProcessInstance().getProcessDefinitionId() );
      }
      catch ( RepositoryException e ) {
        throw new CanyonRuntimeException( e );
      }
    }
    return _definition;
  }


  /**
   * Gets the wMAEventCode attribute of the ProcessInstanceEvent object
   *
   * @return   The wMAEventCode value
   */
  public WMAEventCode getWMAEventCode()
  {
    return IF5_EVENT_CODES[_id];
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String toString()
  {
    return EVENT_TYPES[_id] + "[source=" + source + ", definition=" +
        _definition + ']';
  }
}
