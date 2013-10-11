package de.objectcode.canyon.spiImpl.event.async;

import java.util.EventListener;
import java.util.LinkedList;

import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.process.DataField;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.model.transition.Transition;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.event.IActivityInstanceListener;
import de.objectcode.canyon.spi.event.IAttributeInstanceListener;
import de.objectcode.canyon.spi.event.IPackageListener;
import de.objectcode.canyon.spi.event.IProcessDefinitionListener;
import de.objectcode.canyon.spi.event.IProcessInstanceListener;
import de.objectcode.canyon.spi.event.ITransitionListener;
import de.objectcode.canyon.spi.event.IWorkItemListener;
import de.objectcode.canyon.spi.event.IWorkflowEventBroker;
import de.objectcode.canyon.spi.instance.IActivityInstance;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.instance.IProcessInstance;
import de.objectcode.canyon.spiImpl.event.DefaultWorkflowEventBroker;
import de.objectcode.canyon.spiImpl.event.async.WorkflowEventQueue.WorkflowEventQueueEntry;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;


/**
 * This EventBroker queues Events on a per thread basis and delegates them to a default event broker on commit.
 * @author xylander
 *
 */
public class AsyncWorkflowEventBroker implements IWorkflowEventBroker {


	private static ThreadLocal gThreadLocal = new ThreadLocal();

	private WorkflowEventQueue createQueue() {
		WorkflowEventQueue queue = new WorkflowEventQueue(this);
		gThreadLocal.set(queue);
		return queue;
	}

	private WorkflowEventQueue getQueue() {
		WorkflowEventQueue queue = (WorkflowEventQueue) gThreadLocal.get();
		if (queue==null) {
			throw new RuntimeException("No queue bound");
		}
		return queue;
	}

	private void dropQueue() {
		WorkflowEventQueue queue = (WorkflowEventQueue) gThreadLocal.get();
		if (queue!=null) {
			gThreadLocal.set(null);
		}
	}

	public AsyncWorkflowEventBroker( ServiceManager serviceManager )
  {
  	fDefaultEventBroker = new DefaultWorkflowEventBroker(serviceManager);
  }

	public void beginTransaction() {
		createQueue();
	}

	public void commitTransaction() {
		LinkedList eventEntries =  getQueue().getEventEntries();
		while (eventEntries.size()>0) {
			WorkflowEventQueueEntry element = (WorkflowEventQueueEntry)eventEntries.removeFirst();
			element.getSupport().fireEvent(element.getEvent());
		}
	}

	public void rollbackTransaction() {
		dropQueue();
	}

	private DefaultWorkflowEventBroker fDefaultEventBroker;

  public void fireActivityInstanceStopped( IActivityInstance src,
      Activity defn )
  {
  	getQueue().addEvent(fDefaultEventBroker.getActInstListenerSupport().createActivityInstanceStopped( src, defn ),fDefaultEventBroker.getActInstListenerSupport());
  }


  protected final void fire( Object source, int id, Object definition )
  {
  	// fDefaultEventBroker.fire(source,id,definition);
  }


	public void addActivityInstanceListener(IActivityInstanceListener listener) {
		fDefaultEventBroker.addActivityInstanceListener(listener);
	}


	/**
	 * @see de.objectcode.canyon.spi.event.IWorkflowEventBroker#addAndDispose(java.util.EventListener)
	 */
	public void addAndDispose(EventListener listener) {
    fDefaultEventBroker.addAndDispose(listener);
  }

  public void addAttributeInstanceListener(IAttributeInstanceListener listener) {
		fDefaultEventBroker.addAttributeInstanceListener(listener);
	}


	public void addPackageListener(IPackageListener listener) {
		fDefaultEventBroker.addPackageListener(listener);
	}


	public void addProcessDefinitionListener(IProcessDefinitionListener listener) {
		fDefaultEventBroker.addProcessDefinitionListener(listener);
	}


	public void addProcessInstanceListener(IProcessInstanceListener listener) {
		fDefaultEventBroker.addProcessInstanceListener(listener);
	}


	public void addTransitionListener(ITransitionListener listener) {
		fDefaultEventBroker.addTransitionListener(listener);
	}

	public void addWorkItemListener(IWorkItemListener listener) {
		fDefaultEventBroker.addWorkItemListener(listener);
	}

	public void removeActivityInstanceListener(IActivityInstanceListener listener) {
		fDefaultEventBroker.removeActivityInstanceListener(listener);
	}


	public void removeAttributeInstanceListener(IAttributeInstanceListener listener) {
		fDefaultEventBroker.removeAttributeInstanceListener(listener);
	}


	public void removePackageListener(IPackageListener listener) {
		fDefaultEventBroker.removePackageListener(listener);
	}


	public void removeProcessDefinitionListener(IProcessDefinitionListener listener) {
		fDefaultEventBroker.removeProcessDefinitionListener(listener);
	}


	public void removeProcessInstanceListener(IProcessInstanceListener listener) {
		fDefaultEventBroker.removeProcessInstanceListener(listener);
	}


	public void removeTransitionListener(IWorkItemListener listener) {
		fDefaultEventBroker.removeTransitionListener(listener);
	}


	public void removeWorkItemListener(IWorkItemListener listener) {
		fDefaultEventBroker.removeWorkItemListener(listener);
	}


	public ServiceManager getServiceManager() {
		return fDefaultEventBroker.getServiceManager();
	}




	public void fireActivityInstanceEvent(IActivityInstance src, int id, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getActInstListenerSupport().createEvent( src, id, defn ),fDefaultEventBroker.getActInstListenerSupport());
	}


	public void fireActivityInstanceAborted(IActivityInstance src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getActInstListenerSupport().createActivityInstanceAborted( src, defn ),fDefaultEventBroker.getActInstListenerSupport());
	}


	public void fireActivityInstanceCompleted(IActivityInstance src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getActInstListenerSupport().createActivityInstanceCompleted( src, defn ),fDefaultEventBroker.getActInstListenerSupport());
	}


	public void fireActivityInstanceCreated(IActivityInstance src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getActInstListenerSupport().createActivityInstanceCreated( src, defn ),fDefaultEventBroker.getActInstListenerSupport());
	}


	public void fireActivityInstanceResumed(IActivityInstance src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getActInstListenerSupport().createActivityInstanceResumed( src, defn ),fDefaultEventBroker.getActInstListenerSupport());
	}


	public void fireActivityInstanceStarted(IActivityInstance src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getActInstListenerSupport().createActivityInstanceStarted( src, defn ),fDefaultEventBroker.getActInstListenerSupport());
	}


	public void fireActivityInstanceSuspended(IActivityInstance src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getActInstListenerSupport().createActivityInstanceSuspended( src, defn ),fDefaultEventBroker.getActInstListenerSupport());
	}


	public void fireActivityInstanceTerminated(IActivityInstance src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getActInstListenerSupport().createActivityInstanceTerminated( src, defn ),fDefaultEventBroker.getActInstListenerSupport());
	}


	public void fireAttributeInstanceCreated(IAttributeInstance src, DataField defn) {
  	getQueue().addEvent(fDefaultEventBroker.getAttrInstListenerSupport().createAttributeInstanceCreated( src, defn ),fDefaultEventBroker.getAttrInstListenerSupport());
	}


	public void fireAttributeInstanceDeleted(IAttributeInstance src, DataField defn) {
  	getQueue().addEvent(fDefaultEventBroker.getAttrInstListenerSupport().createAttributeInstanceDeleted( src, defn ),fDefaultEventBroker.getAttrInstListenerSupport());
	}


	public void fireAttributeInstanceUpdated(IAttributeInstance src, DataField defn) {
  	getQueue().addEvent(fDefaultEventBroker.getAttrInstListenerSupport().createAttributeInstanceUpdated( src, defn ),fDefaultEventBroker.getAttrInstListenerSupport());
	}


	public void firePackageCreated(WorkflowPackage src) {
  	getQueue().addEvent(fDefaultEventBroker.getPkgListenerSupport().createPackageCreated( src ),fDefaultEventBroker.getPkgListenerSupport());
	}


	public void firePackageDeleted(WorkflowPackage src) {
  	getQueue().addEvent(fDefaultEventBroker.getPkgListenerSupport().createPackageDeleted( src ),fDefaultEventBroker.getPkgListenerSupport());
	}


	public void firePackageUpdated(WorkflowPackage src) {
  	getQueue().addEvent(fDefaultEventBroker.getPkgListenerSupport().createPackageUpdated( src ),fDefaultEventBroker.getPkgListenerSupport());
	}


	public void fireProcessDefinitionCreated(WorkflowProcess src) {
  	getQueue().addEvent(fDefaultEventBroker.getProcDefListenerSupport().createProcessDefinitionCreated( src ),fDefaultEventBroker.getProcDefListenerSupport());
	}


	public void fireProcessDefinitionDeleted(WorkflowProcess src) {
  	getQueue().addEvent(fDefaultEventBroker.getProcDefListenerSupport().createProcessDefinitionDeleted( src ),fDefaultEventBroker.getProcDefListenerSupport());
	}


	public void fireProcessDefinitionDisabled(WorkflowProcess src) {
  	getQueue().addEvent(fDefaultEventBroker.getProcDefListenerSupport().createProcessDefinitionDisabled( src ),fDefaultEventBroker.getProcDefListenerSupport());
	}


	public void fireProcessDefinitionEnabled(WorkflowProcess src) {
  	getQueue().addEvent(fDefaultEventBroker.getProcDefListenerSupport().createProcessDefinitionEnabled( src ),fDefaultEventBroker.getProcDefListenerSupport());
	}


	public void fireProcessDefinitionUpdated(WorkflowProcess src) {
  	getQueue().addEvent(fDefaultEventBroker.getProcDefListenerSupport().createProcessDefinitionUpdated( src ),fDefaultEventBroker.getProcDefListenerSupport());
	}


	public void fireProcessInstanceEvent(IProcessInstance src, int id, WorkflowProcess defn) {
  	getQueue().addEvent(fDefaultEventBroker.getProcInstListenerSupport().createEvent( src, id, defn ),fDefaultEventBroker.getProcInstListenerSupport());
	}


	public void fireProcessInstanceAborted(IProcessInstance src, WorkflowProcess defn) {
  	getQueue().addEvent(fDefaultEventBroker.getProcInstListenerSupport().createProcessInstanceAborted( src, defn ),fDefaultEventBroker.getProcInstListenerSupport());
	}


	public void fireProcessInstanceCompleted(IProcessInstance src, WorkflowProcess defn) {
  	getQueue().addEvent(fDefaultEventBroker.getProcInstListenerSupport().createProcessInstanceCompleted( src, defn ),fDefaultEventBroker.getProcInstListenerSupport());
	}


	public void fireProcessInstanceCreated(IProcessInstance src, WorkflowProcess defn) {
  	getQueue().addEvent(fDefaultEventBroker.getProcInstListenerSupport().createProcessInstanceCreated( src, defn ),fDefaultEventBroker.getProcInstListenerSupport());
	}


	public void fireProcessInstanceDeleted(IProcessInstance src, WorkflowProcess defn) {
  	getQueue().addEvent(fDefaultEventBroker.getProcInstListenerSupport().createProcessInstanceDeleted( src, defn ),fDefaultEventBroker.getProcInstListenerSupport());
	}


	public void fireProcessInstanceResumed(IProcessInstance src, WorkflowProcess defn) {
  	getQueue().addEvent(fDefaultEventBroker.getProcInstListenerSupport().createProcessInstanceResumed( src, defn ),fDefaultEventBroker.getProcInstListenerSupport());
	}


	public void fireProcessInstanceStarted(IProcessInstance src, WorkflowProcess defn) {
  	getQueue().addEvent(fDefaultEventBroker.getProcInstListenerSupport().createProcessInstanceStarted( src, defn ),fDefaultEventBroker.getProcInstListenerSupport());
	}


	public void fireProcessInstanceSuspended(IProcessInstance src, WorkflowProcess defn) {
  	getQueue().addEvent(fDefaultEventBroker.getProcInstListenerSupport().createProcessInstanceResumed( src, defn ),fDefaultEventBroker.getProcInstListenerSupport());
	}


	public void fireProcessInstanceTerminated(IProcessInstance src, WorkflowProcess defn) {
  	getQueue().addEvent(fDefaultEventBroker.getProcInstListenerSupport().createProcessInstanceTerminated( src, defn ),fDefaultEventBroker.getProcInstListenerSupport());
	}


	public void fireTransitionEvent(IActivityInstance activityInstance, int id, Transition defn) {
  	getQueue().addEvent(fDefaultEventBroker.getTransListenerSupport().createEvent( activityInstance, id, defn ),fDefaultEventBroker.getTransListenerSupport());
	}


	public void fireTransitionFired(IActivityInstance activityInstance, Transition defn) {
  	getQueue().addEvent(fDefaultEventBroker.getTransListenerSupport().createTransitionFired( activityInstance, defn ),fDefaultEventBroker.getTransListenerSupport());
	}


	public void fireWorkItemEvent(IWorkItem src, int id, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getWorkItemListenerSupport().createEvent( src, id, defn ),fDefaultEventBroker.getWorkItemListenerSupport());
	}


	public void fireWorkItemAborted(IWorkItem src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getWorkItemListenerSupport().createWorkItemAborted( src, defn ),fDefaultEventBroker.getWorkItemListenerSupport());
	}


	public void fireWorkItemAssigned(IWorkItem src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getWorkItemListenerSupport().createWorkItemAssigned( src, defn ),fDefaultEventBroker.getWorkItemListenerSupport());
	}


	public void fireWorkItemCompleted(IWorkItem src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getWorkItemListenerSupport().createWorkItemCompleted( src, defn ),fDefaultEventBroker.getWorkItemListenerSupport());
	}


	public void fireWorkItemCreated(IWorkItem src) {
  	getQueue().addEvent(fDefaultEventBroker.getWorkItemListenerSupport().createWorkItemCreated( src ),fDefaultEventBroker.getWorkItemListenerSupport());
	}


	public void fireWorkItemResumed(IWorkItem src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getWorkItemListenerSupport().createWorkItemResumed( src, defn ),fDefaultEventBroker.getWorkItemListenerSupport());
	}


	public void fireWorkItemStarted(IWorkItem src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getWorkItemListenerSupport().createWorkItemStarted( src, defn ),fDefaultEventBroker.getWorkItemListenerSupport());
	}


	public void fireWorkItemStopped(IWorkItem src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getWorkItemListenerSupport().createWorkItemStopped( src, defn ),fDefaultEventBroker.getWorkItemListenerSupport());
	}


	public void fireWorkItemSuspended(IWorkItem src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getWorkItemListenerSupport().createWorkItemSuspended( src, defn ),fDefaultEventBroker.getWorkItemListenerSupport());
	}


	public void fireWorkItemTerminated(IWorkItem src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getWorkItemListenerSupport().createWorkItemTerminated( src, defn ),fDefaultEventBroker.getWorkItemListenerSupport());
	}

	public void fireWorkItemDeprived(IWorkItem src, Activity defn) {
  	getQueue().addEvent(fDefaultEventBroker.getWorkItemListenerSupport().createWorkItemDeprived( src, defn ),fDefaultEventBroker.getWorkItemListenerSupport());
	}


}
