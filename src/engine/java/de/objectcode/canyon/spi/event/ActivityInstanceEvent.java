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
import org.wfmc.wapi.WMActivityInstanceState;

import de.objectcode.canyon.CanyonRuntimeException;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.instance.IActivityInstance;
import de.objectcode.canyon.spi.process.IProcessRepository;


/**
 * Delivers activity instance event notifications.
 *
 * @author    Anthony Eden
 * @author    Adrian Price
 * @created   3. November 2003
 */
public final class ActivityInstanceEvent extends WorkflowEvent
{
  final static          long            serialVersionUID  = -3860391828783448630L;
  /**
   * The activity instance was aborted.
   */
  public final static   int             ABORTED           = WMActivityInstanceState.ABORT_ACTION;
  /**
   * The activity instance was completed.
   */
  public final static   int             COMPLETED         = WMActivityInstanceState.COMPLETE_ACTION;
  /**
   * The activity instance was created.
   */
  public final static   int             CREATED           = WMActivityInstanceState.CREATE_ACTION;
  /**
   * The activity instance was resumed.
   */
  public final static   int             RESUMED           = WMActivityInstanceState.RESUME_ACTION;
  /**
   * The activity instance was started.
   */
  public final static   int             STARTED           = WMActivityInstanceState.START_ACTION;
  /**
   * The activity instance was started.
   */
  public final static   int             STOPPED           = WMActivityInstanceState.STOP_ACTION;
  /**
   * The activity instance was suspended.
   */
  public final static   int             SUSPENDED         = WMActivityInstanceState.SUSPEND_ACTION;
  /**
   * The activity instance was terminated.
   */
  public final static   int             TERMINATED        = WMActivityInstanceState.TERMINATE_ACTION;
  private final static  WMAEventCode[]  IF5_EVENT_CODES   = {
      WMAEventCode.ABORTED_ACTIVITY_INSTANCE,
      WMAEventCode.COMPLETED_ACTIVITY_INSTANCE,
      null,
      WMAEventCode.CHANGED_ACTIVITY_INSTANCE_STATE,
      WMAEventCode.CHANGED_ACTIVITY_INSTANCE_STATE,
      WMAEventCode.CHANGED_ACTIVITY_INSTANCE_STATE,
      WMAEventCode.CHANGED_ACTIVITY_INSTANCE_STATE,
      WMAEventCode.TERMINATED_ACTIVITY_INSTANCE
      };
  private final static  String[]        EVENT_TYPES       = {
      "ActivityInstanceAborted",
      "ActivityInstanceCompleted",
      "ActivityInstanceCreated",
      "ActivityInstanceResumed",
      "ActivityInstanceStarted",
      "ActivityInstanceStopped",
      "ActivityInstanceSuspended",
      "ActivityInstanceTerminated"
      };

  private transient     Activity        _definition;


  /**
   * Constructs a new activity instance event.
   *
   * @param source
   * @param id
   * @param broker
   * @param definition
   */
  public ActivityInstanceEvent( IActivityInstance source, int id,
      IWorkflowEventBroker broker, Activity definition )
  {

    super( source, id, IActivityInstance.class, EVENT_TYPES[id],
        source.getActivityInstanceId(), broker );
    _definition = definition;
  }


  /**
   * Returns the source activity instance.
   *
   * @return   The activity instance that fired the event.
   */
  public IActivityInstance getActivityInstance()
  {
    return ( IActivityInstance ) source;
  }


  /**
   * Returns the definition of the source activity.
   *
   * @return   The activity definition from the workflow process definition.
   */
  public Activity getActivityDefinition()
  {
    if ( _definition == null && _broker != null ) {
      IProcessRepository  processRepository  =
          _broker.getServiceManager().getProcessRepository();
      try {
        WorkflowProcess  workflow  =
            processRepository.findWorkflowProcess(
            getActivityInstance().getProcessDefinitionId() );
        _definition = workflow.findActivity( getActivityInstance().getActivityDefinitionId() );
      }
      catch ( RepositoryException e ) {
        throw new CanyonRuntimeException( e );
      }
    }
    return _definition;
  }


  /**
   * Gets the wMAEventCode attribute of the ActivityInstanceEvent object
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
