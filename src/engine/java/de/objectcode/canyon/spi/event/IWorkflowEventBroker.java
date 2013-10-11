/*--

 Copyright (C) 2002-2003 Aetrion LLC.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.

 3. The names "OBE" and "Open Business Engine" must not be used to
  endorse or promote products derived from this software without prior
  written permission.  For written permission, please contact
  obe@aetrion.com.

 4. Products derived from this software may not be called "OBE" or
  "Open Business Engine", nor may "OBE" or "Open Business Engine"
  appear in their name, without prior written permission from
  Aetrion LLC (obe@aetrion.com).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.

 For more information on OBE, please see
 <http://www.openbusinessengine.org/>.

 */
package de.objectcode.canyon.spi.event;


import java.util.EventListener;

import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.process.DataField;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.model.transition.Transition;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.instance.IActivityInstance;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.instance.IProcessInstance;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

/**
 * @author junglas
 */
public interface IWorkflowEventBroker {
  public ServiceManager getServiceManager();



  /**
   * Take the given concrete EventListener, check with instanceof the concrete
   * type (multiple are possible) and call the improved add method for the given
   * instance.
   *
   * @param listener
   *          The listner to add and dispose
   */
  public void addAndDispose(EventListener listener);

  /**
   * Subscribes to activity instance events.
   *
   * @param listener The activity instance event listener to add.
   * @param mask Bitmask to specify which events to notify.
   */
  public void addActivityInstanceListener(IActivityInstanceListener listener);

  /**
   * Subscribes to attribute instance events.
   *
   * @param listener The attribute instance event listener to add.
   * @param mask Bitmask to specify which events to notify.
   */
  public void addAttributeInstanceListener(IAttributeInstanceListener listener);

  /**
   * Subscribes to package events.
   *
   * @param listener The package event listener to add.
   * @param mask Bitmask to specify which events to notify.
   */
  public void addPackageListener(IPackageListener listener);

  /**
   * Subscribes to process definition events.
   *
   * @param listener The process definition event listener to add.
   * @param mask Bitmask to specify which events to notify.
   */
  public void addProcessDefinitionListener(IProcessDefinitionListener listener);


  /**
   * Subscribes to process instance events.
   *
   * @param listener The process instance event listener to add.
   * @param mask Bitmask to specify which events to notify.
   */
  public void addProcessInstanceListener(IProcessInstanceListener listener);


  /**
   * Subscribes to transition events.
   *
   * @param listener The transition event listener to add.
   * @param mask Bitmask to specify which events to notify.
   */
  public void addTransitionListener(ITransitionListener listener);


  /**
   * Subscribes to work item events.
   *
   * @param listener The work item event listener to add.
   * @param mask Bitmask to specify which events to notify.
   */
  public void addWorkItemListener(IWorkItemListener listener);


  /**
   * Internal use only - do not call.
   */
  public void fireActivityInstanceEvent(IActivityInstance src, int id,
      Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireActivityInstanceAborted(IActivityInstance src,
      Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireActivityInstanceCompleted(IActivityInstance src,
      Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireActivityInstanceCreated(IActivityInstance src,
      Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireActivityInstanceResumed(IActivityInstance src,
      Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireActivityInstanceStarted(IActivityInstance src,
      Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireActivityInstanceStopped(IActivityInstance src,
      Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireActivityInstanceSuspended(IActivityInstance src,
      Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireActivityInstanceTerminated(IActivityInstance src,
      Activity defn);
  /**
   * Internal use only - do not call.
   */
  public void fireAttributeInstanceCreated(IAttributeInstance src,
      DataField defn);

  /**
   * Internal use only - do not call.
   */
  public void fireAttributeInstanceDeleted(IAttributeInstance src,
      DataField defn);

  /**
   * Internal use only - do not call.
   */
  public void fireAttributeInstanceUpdated(IAttributeInstance src,
      DataField defn);

  /**
   * Internal use only - do not call.
   */
  public void firePackageCreated(WorkflowPackage src);

  /**
   * Internal use only - do not call.
   */
  public void firePackageDeleted(WorkflowPackage src);

  /**
   * Internal use only - do not call.
   */
  public void firePackageUpdated(WorkflowPackage src);

  /**
   * Internal use only - do not call.
   */
  public void fireProcessDefinitionCreated(WorkflowProcess src);

  /**
   * Internal use only - do not call.
   */
  public void fireProcessDefinitionDeleted(WorkflowProcess src);

  /**
   * Internal use only - do not call.
   */
  public void fireProcessDefinitionDisabled(WorkflowProcess src);

  /**
   * Internal use only - do not call.
   */
  public void fireProcessDefinitionEnabled(WorkflowProcess src);

  /**
   * Internal use only - do not call.
   */
  public void fireProcessDefinitionUpdated(WorkflowProcess src);

  /**
   * Internal use only - do not call.
   */
  public void fireProcessInstanceEvent(IProcessInstance src, int id,
      WorkflowProcess defn);

  /**
   * Internal use only - do not call.
   */
  public void fireProcessInstanceAborted(IProcessInstance src,
      WorkflowProcess defn);

  /**
   * Internal use only - do not call.
   */
  public void fireProcessInstanceCompleted(IProcessInstance src,
      WorkflowProcess defn);

  /**
   * Internal use only - do not call.
   */
  public void fireProcessInstanceCreated(IProcessInstance src,
      WorkflowProcess defn);

  /**
   * Internal use only - do not call.
   */
  public void fireProcessInstanceDeleted(IProcessInstance src,
      WorkflowProcess defn);

  /**
   * Internal use only - do not call.
   */
  public void fireProcessInstanceResumed(IProcessInstance src,
      WorkflowProcess defn);

  /**
   * Internal use only - do not call.
   */
  public void fireProcessInstanceStarted(IProcessInstance src,
      WorkflowProcess defn);
  /**
   * Internal use only - do not call.
   */
  public void fireProcessInstanceSuspended(IProcessInstance src,
      WorkflowProcess defn);
  /**
   * Internal use only - do not call.
   */
  public void fireProcessInstanceTerminated(IProcessInstance src,
      WorkflowProcess defn);

  /**
   * Internal use only - do not call.
   */
  public void fireTransitionEvent(IActivityInstance activityInstance, int id,
      Transition defn);

  /**
   * Internal use only - do not call.
   */
  public void fireTransitionFired(IActivityInstance activityInstance,
      Transition defn);

  /**
   * Internal use only - do not call.
   */
  public void fireWorkItemEvent(IWorkItem src, int id, Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireWorkItemAborted(IWorkItem src, Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireWorkItemAssigned(IWorkItem src, Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireWorkItemDeprived(IWorkItem src, Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireWorkItemCompleted(IWorkItem src, Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireWorkItemCreated(IWorkItem src );

  /**
   * Internal use only - do not call.
   */
  public void fireWorkItemResumed(IWorkItem src, Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireWorkItemStarted(IWorkItem src, Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireWorkItemStopped(IWorkItem src, Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireWorkItemSuspended(IWorkItem src, Activity defn);

  /**
   * Internal use only - do not call.
   */
  public void fireWorkItemTerminated(IWorkItem src, Activity defn);


  public void beginTransaction();

  public void commitTransaction();

  public void rollbackTransaction();

}
