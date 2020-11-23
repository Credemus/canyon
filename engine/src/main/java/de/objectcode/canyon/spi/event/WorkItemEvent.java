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
import org.wfmc.wapi.WMWorkItemState;

import de.objectcode.canyon.CanyonRuntimeException;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.process.IProcessDefinitionID;
import de.objectcode.canyon.spi.process.IProcessRepository;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

/**
 * Delivers work item event notifications.
 * 
 * @author Adrian Price
 * @created 3. November 2003
 */
public final class WorkItemEvent extends WorkflowEvent {
	final static long serialVersionUID = -638304336588033226L;

	/**
	 * The work item was aborted.
	 */
	public final static int ABORTED = WMWorkItemState.ABORT_ACTION;

	/**
	 * The work item was assigned or reassigned.
	 */
	public final static int ASSIGNED = WMWorkItemState.ASSIGN_ACTION;

	/**
	 * The work item was completed.
	 */
	public final static int COMPLETED = WMWorkItemState.COMPLETE_ACTION;

	/**
	 * The work item was created.
	 */
	public final static int CREATED = WMWorkItemState.CREATE_ACTION;

	/**
	 * The work item was resumed.
	 */
	public final static int RESUMED = WMWorkItemState.RESUME_ACTION;

	/**
	 * The work item was started.
	 */
	public final static int STARTED = WMWorkItemState.START_ACTION;

	/**
	 * The work item was stopped.
	 */
	public final static int STOPPED = WMWorkItemState.STOP_ACTION;

	/**
	 * The work item was suspended.
	 */
	public final static int SUSPENDED = WMWorkItemState.SUSPEND_ACTION;

	/**
	 * The work item was terminated.
	 */
	public final static int TERMINATED = WMWorkItemState.TERMINATE_ACTION;

	/**
	 * The work item was deprived.
	 */
	public final static int DEPRIVED = 9; // NOT WFMC

	
	private final static WMAEventCode[] IF5_EVENT_CODES = {
			WMAEventCode.CHANGED_WORK_ITEM_STATE, WMAEventCode.ASSIGNED_WORK_ITEM,
			WMAEventCode.COMPLETED_WORK_ITEM, null,
			WMAEventCode.CHANGED_WORK_ITEM_STATE, WMAEventCode.STARTED_WORK_ITEM,
			WMAEventCode.CHANGED_WORK_ITEM_STATE,
			WMAEventCode.CHANGED_WORK_ITEM_STATE,
			WMAEventCode.CHANGED_WORK_ITEM_STATE,
			WMAEventCode.CHANGED_WORK_ITEM_STATE};

	private final static String[] EVENT_TYPES = { "WorkItemAborted",
			"WorkItemAssigned", "WorkItemCompleted", "WorkItemCreated",
			"WorkItemResumed", "WorkItemStarted", "WorkItemStopped",
			"WorkItemSuspended", "WorkItemTerminated", "WorkItemDeprived" };

	private transient Activity _definition;

	/**
	 * Constructor for the WorkItemEvent object
	 * 
	 * @param source
	 *          Description of the Parameter
	 * @param id
	 *          Description of the Parameter
	 * @param broker
	 *          Description of the Parameter
	 * @param definition
	 *          Description of the Parameter
	 * @throws RepositoryException 
	 */
	public WorkItemEvent(IWorkItem source, int id, IWorkflowEventBroker broker,
			Activity definition) throws RepositoryException {

		super(WorkItemData.createWorkItemData(source), id, IWorkItem.class, EVENT_TYPES[id],
				source.getWorkItemId(), broker);
		_definition = definition;
	}

	

	/**
	 * Gets the workItem attribute of the WorkItemEvent object
	 * 
	 * @return The workItem value
	 */
	public WorkItemData getWorkItem() {
		return (WorkItemData) source;
	}

	/**
	 * Gets the activityDefinition attribute of the WorkItemEvent object
	 * 
	 * @return The activityDefinition value
	 */
	public Activity getActivityDefinition() {
		if (_definition == null && _broker != null) {
			IProcessRepository processRepository = _broker.getServiceManager()
					.getProcessRepository();
			try {
				WorkItemData workItem = getWorkItem();
				WorkflowProcess workflow = processRepository
						.findWorkflowProcess(workItem.getProcessId());
				_definition = workflow.findActivity(workItem.getActivityId());
			} catch (RepositoryException e) {
				throw new CanyonRuntimeException(e);
			}
		}
		return _definition;
	}

	/**
	 * Gets the wMAEventCode attribute of the WorkItemEvent object
	 * 
	 * @return The wMAEventCode value
	 */
	public WMAEventCode getWMAEventCode() {
		return IF5_EVENT_CODES[_id];
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Return Value
	 */
	public String toString() {
		return EVENT_TYPES[_id] + "[source=" + source + ", definition="
				+ _definition + ']';
	}


}
