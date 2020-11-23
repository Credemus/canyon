package de.objectcode.canyon.spiImpl.event;

import java.util.EventListener;

import de.objectcode.canyon.api.worklist.BPEEvent;
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
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

/**
 * @author    junglas
 * @created   21. Oktober 2003
 */
public class DefaultWorkflowEventBroker implements IWorkflowEventBroker
{
  private final  ActivityInstanceListenerSupport   actInstListenerSupport;
  private final  AttributeInstanceListenerSupport  attrInstListenerSupport;
  private final  PackageListenerSupport            pkgListenerSupport;
  private final  ProcessDefinitionListenerSupport  procDefListenerSupport;
  private final  ProcessInstanceListenerSupport    procInstListenerSupport;
  private final  TransitionListenerSupport         transListenerSupport;
  private final  WorkItemListenerSupport           workItemListenerSupport;
  private        ServiceManager                    m_serviceManager;


  /**
   *Constructor for the DefaultWorkflowEventBroker object
   *
   * @param serviceManager  Description of the Parameter
   */
  public DefaultWorkflowEventBroker( ServiceManager serviceManager )
  {
    m_serviceManager = serviceManager;
    actInstListenerSupport = new ActivityInstanceListenerSupport( this );
    attrInstListenerSupport = new AttributeInstanceListenerSupport( this );
    pkgListenerSupport = new PackageListenerSupport( this );
    procDefListenerSupport = new ProcessDefinitionListenerSupport( this );
    procInstListenerSupport = new ProcessInstanceListenerSupport( this );
    workItemListenerSupport = new WorkItemListenerSupport( this );
    transListenerSupport = new TransitionListenerSupport( this );
  }


  /**
   * @return
   */
  public ServiceManager getServiceManager()
  {
    return m_serviceManager;
  }


  /**
   * Subscribes to activity instance events.
   *
   * @param listener  The activity instance event listener to add.
   */
  public void addActivityInstanceListener( IActivityInstanceListener listener )
  {

    actInstListenerSupport.addListener( listener );
  }



  /**
   * @see de.objectcode.canyon.spi.event.IWorkflowEventBroker#addAndDispose(java.util.EventListener)
   */
  public void addAndDispose(final EventListener listener) {
    if (listener instanceof IActivityInstanceListener) {
      addActivityInstanceListener((IActivityInstanceListener) listener);
    }
    if (listener instanceof IAttributeInstanceListener) {
      addAttributeInstanceListener((IAttributeInstanceListener) listener);
    }
    if (listener instanceof IPackageListener) {
      addPackageListener((IPackageListener) listener);
    }
    if (listener instanceof IProcessDefinitionListener) {
      addProcessDefinitionListener((IProcessDefinitionListener) listener);
    }
    if (listener instanceof IProcessInstanceListener) {
      addProcessInstanceListener((IProcessInstanceListener) listener);
    }
    if (listener instanceof ITransitionListener) {
      addTransitionListener((ITransitionListener) listener);
    }
    if (listener instanceof IWorkItemListener) {
      addWorkItemListener((IWorkItemListener) listener);
    }
  }


  /**
   * Subscribes to attribute instance events.
   *
   * @param listener  The attribute instance event listener to add.
   */
  public void addAttributeInstanceListener( IAttributeInstanceListener listener )
  {

    attrInstListenerSupport.addListener( listener );
  }



  /**
   * Subscribes to package events.
   *
   * @param listener  The package event listener to add.
   */
  public void addPackageListener( IPackageListener listener )
  {
    pkgListenerSupport.addListener( listener );
  }



  /**
   * Subscribes to process definition events.
   *
   * @param listener  The process definition event listener to add.
   */
  public void addProcessDefinitionListener( IProcessDefinitionListener listener )
  {

    procDefListenerSupport.addListener( listener );
  }




  /**
   * Subscribes to process instance events.
   *
   * @param listener  The process instance event listener to add.
   */
  public void addProcessInstanceListener( IProcessInstanceListener listener )
  {

    procInstListenerSupport.addListener( listener );
  }



  /**
   * Subscribes to transition events.
   *
   * @param listener  The transition event listener to add.
   */
  public void addTransitionListener( ITransitionListener listener )
  {
    transListenerSupport.addListener( listener );
  }



  /**
   * Subscribes to work item events.
   *
   * @param listener  The work item event listener to add.
   */
  public void addWorkItemListener( IWorkItemListener listener )
  {
    workItemListenerSupport.addListener( listener );
  }



  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param id    Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireActivityInstanceEvent( IActivityInstance src, int id,
      Activity defn )
  {

    actInstListenerSupport.fireActivityInstanceEvent( src, id, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireActivityInstanceAborted( IActivityInstance src,
      Activity defn )
  {

    actInstListenerSupport.fireActivityInstanceAborted( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireActivityInstanceCompleted( IActivityInstance src,
      Activity defn )
  {

    actInstListenerSupport.fireActivityInstanceCompleted( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireActivityInstanceCreated( IActivityInstance src,
      Activity defn )
  {

    actInstListenerSupport.fireActivityInstanceCreated( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireActivityInstanceResumed( IActivityInstance src,
      Activity defn )
  {

    actInstListenerSupport.fireActivityInstanceResumed( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireActivityInstanceStarted( IActivityInstance src,
      Activity defn )
  {

    actInstListenerSupport.fireActivityInstanceStarted( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireActivityInstanceStopped( IActivityInstance src,
      Activity defn )
  {

    actInstListenerSupport.fireActivityInstanceStopped( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireActivityInstanceSuspended( IActivityInstance src,
      Activity defn )
  {

    actInstListenerSupport.fireActivityInstanceSuspended( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireActivityInstanceTerminated( IActivityInstance src,
      Activity defn )
  {

    actInstListenerSupport.fireActivityInstanceTerminated( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireAttributeInstanceCreated( IAttributeInstance src,
      DataField defn )
  {

    attrInstListenerSupport.fireAttributeInstanceCreated( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireAttributeInstanceDeleted( IAttributeInstance src,
      DataField defn )
  {

    attrInstListenerSupport.fireAttributeInstanceDeleted( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireAttributeInstanceUpdated( IAttributeInstance src,
      DataField defn )
  {

    attrInstListenerSupport.fireAttributeInstanceUpdated( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src  Description of the Parameter
   */
  public void firePackageCreated( WorkflowPackage src )
  {
    pkgListenerSupport.firePackageCreated( src );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src  Description of the Parameter
   */
  public void firePackageDeleted( WorkflowPackage src )
  {
    pkgListenerSupport.firePackageDeleted( src );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src  Description of the Parameter
   */
  public void firePackageUpdated( WorkflowPackage src )
  {
    pkgListenerSupport.firePackageUpdated( src );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src  Description of the Parameter
   */
  public void fireProcessDefinitionCreated( WorkflowProcess src )
  {
    procDefListenerSupport.fireProcessDefinitionCreated( src );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src  Description of the Parameter
   */
  public void fireProcessDefinitionDeleted( WorkflowProcess src )
  {
    procDefListenerSupport.fireProcessDefinitionDeleted( src );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src  Description of the Parameter
   */
  public void fireProcessDefinitionDisabled( WorkflowProcess src )
  {
    procDefListenerSupport.fireProcessDefinitionDisabled( src );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src  Description of the Parameter
   */
  public void fireProcessDefinitionEnabled( WorkflowProcess src )
  {
    procDefListenerSupport.fireProcessDefinitionEnabled( src );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src  Description of the Parameter
   */
  public void fireProcessDefinitionUpdated( WorkflowProcess src )
  {
    procDefListenerSupport.fireProcessDefinitionUpdated( src );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param id    Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireProcessInstanceEvent( IProcessInstance src, int id,
      WorkflowProcess defn )
  {

    procInstListenerSupport.fireProcessInstanceEvent( src, id, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireProcessInstanceAborted( IProcessInstance src,
      WorkflowProcess defn )
  {

    procInstListenerSupport.fireProcessInstanceAborted( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireProcessInstanceCompleted( IProcessInstance src,
      WorkflowProcess defn )
  {

    procInstListenerSupport.fireProcessInstanceCompleted( src,
        defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireProcessInstanceCreated( IProcessInstance src,
      WorkflowProcess defn )
  {

    procInstListenerSupport.fireProcessInstanceCreated( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireProcessInstanceDeleted( IProcessInstance src,
      WorkflowProcess defn )
  {

    procInstListenerSupport.fireProcessInstanceDeleted( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireProcessInstanceResumed( IProcessInstance src,
      WorkflowProcess defn )
  {

    procInstListenerSupport.fireProcessInstanceResumed( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireProcessInstanceStarted( IProcessInstance src,
      WorkflowProcess defn )
  {

    procInstListenerSupport.fireProcessInstanceStarted( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireProcessInstanceSuspended( IProcessInstance src,
      WorkflowProcess defn )
  {

    procInstListenerSupport.fireProcessInstanceSuspended( src,
        defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireProcessInstanceTerminated( IProcessInstance src,
      WorkflowProcess defn )
  {

    procInstListenerSupport.fireProcessInstanceTerminated( src,
        defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param activityInstance  Description of the Parameter
   * @param id                Description of the Parameter
   * @param defn              Description of the Parameter
   */
  public void fireTransitionEvent( IActivityInstance activityInstance, int id,
      Transition defn )
  {

    transListenerSupport.fireTransitionEvent( activityInstance, id, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param activityInstance  Description of the Parameter
   * @param defn              Description of the Parameter
   */
  public void fireTransitionFired( IActivityInstance activityInstance,
      Transition defn )
  {

    transListenerSupport.fireTransitionFired( activityInstance, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param id    Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemEvent( IWorkItem src, int id, Activity defn )
  {
    workItemListenerSupport.fireWorkItemEvent( src, id, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemAborted( IWorkItem src, Activity defn )
  {
    workItemListenerSupport.fireWorkItemAborted( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemAssigned( IWorkItem src, Activity defn )
  {
    workItemListenerSupport.fireWorkItemAssigned( src, defn );
    m_serviceManager.getBpeEngine().getEventHub().fireManualActivityStateChanged(src,BPEEvent.Type.MANUAL_ACTIVITY_DELEGATED);
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemCompleted( IWorkItem src, Activity defn )
  {
    workItemListenerSupport.fireWorkItemCompleted( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemCreated( IWorkItem src )
  {
    workItemListenerSupport.fireWorkItemCreated( src );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemResumed( IWorkItem src, Activity defn )
  {
    workItemListenerSupport.fireWorkItemResumed( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemStarted( IWorkItem src, Activity defn )
  {
    workItemListenerSupport.fireWorkItemStarted( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemStopped( IWorkItem src, Activity defn )
  {
    workItemListenerSupport.fireWorkItemStopped( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemSuspended( IWorkItem src, Activity defn )
  {
    workItemListenerSupport.fireWorkItemSuspended( src, defn );
  }


  /**
   * Internal use only - do not call.
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemTerminated( IWorkItem src, Activity defn )
  {
    workItemListenerSupport.fireWorkItemTerminated( src, defn );
  }

  public void fireWorkItemDeprived( IWorkItem src, Activity defn )
  {
    workItemListenerSupport.fireWorkItemDeprived( src, defn );
  }

  /**
   * Unsubscribes from activity instance events.
   *
   * @param listener  The activity instance event listener to remove.
   */
  public void removeActivityInstanceListener(
      IActivityInstanceListener listener )
  {

    actInstListenerSupport.removeListener( listener );
  }



  /**
   * Unsubscribes from attribute instance events.
   *
   * @param listener  The attribute instance event listener to remove.
   */
  public void removeAttributeInstanceListener(
      IAttributeInstanceListener listener )
  {

    attrInstListenerSupport.removeListener( listener );
  }



  /**
   * Unsubscribes from package events.
   *
   * @param listener  The package event listener to remove.
   */
  public void removePackageListener( IPackageListener listener )
  {
    pkgListenerSupport.removeListener( listener );
  }




  /**
   * Unsubscribes from process definition events.
   *
   * @param listener  The process definition event listener to remove.
   */
  public void removeProcessDefinitionListener(
      IProcessDefinitionListener listener )
  {

    procDefListenerSupport.removeListener( listener );
  }



  /**
   * Unsubscribes from process instance events.
   *
   * @param listener  The process instance event listener to remove.
   */
  public void removeProcessInstanceListener(
      IProcessInstanceListener listener )
  {

    procInstListenerSupport.removeListener( listener );
  }



  /**
   * Unsubscribes from transition events.
   *
   * @param listener  The transition event listener to remove.
   */
  public void removeTransitionListener( IWorkItemListener listener )
  {
    transListenerSupport.removeListener( listener );
  }

  /**
   * Unsubscribes from work item events.
   *
   * @param listener  The work item event listener to remove.
   */
  public void removeWorkItemListener( IWorkItemListener listener )
  {
    workItemListenerSupport.removeListener( listener );
  }


	public void beginTransaction() {
		// TODO Auto-generated method stub

	}


	public void commitTransaction() {
		// TODO Auto-generated method stub

	}


	public void rollbackTransaction() {
		// TODO Auto-generated method stub

	}


	public ActivityInstanceListenerSupport getActInstListenerSupport() {
		return actInstListenerSupport;
	}


	public AttributeInstanceListenerSupport getAttrInstListenerSupport() {
		return attrInstListenerSupport;
	}


	public PackageListenerSupport getPkgListenerSupport() {
		return pkgListenerSupport;
	}


	public ProcessDefinitionListenerSupport getProcDefListenerSupport() {
		return procDefListenerSupport;
	}


	public ProcessInstanceListenerSupport getProcInstListenerSupport() {
		return procInstListenerSupport;
	}


	public TransitionListenerSupport getTransListenerSupport() {
		return transListenerSupport;
	}


	public WorkItemListenerSupport getWorkItemListenerSupport() {
		return workItemListenerSupport;
	}
}
