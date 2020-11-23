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
import org.wfmc.wapi.WMProcessDefinitionState;

import de.objectcode.canyon.model.process.WorkflowProcess;

/**
 * Delivers process definition event notifications.
 *
 * @author    Adrian Price
 * @created   3. November 2003
 */
public final class ProcessDefinitionEvent extends WorkflowEvent
{
  final static          long            serialVersionUID  = 6206079798767638899L;
  /**
   * The process definition was created.
   */
  public final static   int             CREATED           = WMProcessDefinitionState.CREATE_ACTION;
  /**
   * The process definition was deleted.
   */
  public final static   int             DELETED           = WMProcessDefinitionState.DELETE_ACTION;
  /**
   * The process definition was disabled.
   */
  public final static   int             DISABLED          = WMProcessDefinitionState.DISABLE_ACTION;
  /**
   * The process definition was enabled.
   */
  public final static   int             ENABLED           = WMProcessDefinitionState.ENABLE_ACTION;
  /**
   * The process definition was updated.
   */
  public final static   int             UPDATED           = WMProcessDefinitionState.UPDATE_ACTION;
  private final static  WMAEventCode[]  IF5_EVENT_CODES   = {
      null,
      null,
      WMAEventCode.CHANGED_PROCESS_DEFINITION_STATE,
      WMAEventCode.CHANGED_PROCESS_DEFINITION_STATE,
      null
      };
  private final static  String[]        EVENT_TYPES       = {
      "ProcessDefinitionCreated",
      "ProcessDefinitionDeleted",
      "ProcessDefinitionDisabled",
      "ProcessDefinitionEnabled",
      "ProcessDefinitionUpdated"
      };


  /**
   * Constructs a new process definition event.
   *
   * @param source
   * @param id
   * @param broker
   */
  public ProcessDefinitionEvent( WorkflowProcess source, int id,
      IWorkflowEventBroker broker )
  {

    super( source, id, WorkflowProcess.class, EVENT_TYPES[id],
        source.getId(), broker );
  }


  /**
   * Returns the source workflow process definition.
   *
   * @return   The workflow process that fired the event.
   */
  public WorkflowProcess getProcessDefinition()
  {
    return ( WorkflowProcess ) source;
  }


  /**
   * Gets the wMAEventCode attribute of the ProcessDefinitionEvent object
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
    return EVENT_TYPES[_id] + "[source=" + source + +']';
  }
}
