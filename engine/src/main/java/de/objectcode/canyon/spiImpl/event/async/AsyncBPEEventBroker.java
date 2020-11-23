package de.objectcode.canyon.spiImpl.event.async;

import java.util.List;
import java.util.EventListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.api.worklist.BPEEvent;
import de.objectcode.canyon.bpe.engine.event.EventHub;
import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.process.DataField;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.model.transition.Transition;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.event.IActivityInstanceListener;
import de.objectcode.canyon.spi.event.IAttributeInstanceListener;
import de.objectcode.canyon.spi.event.IBPEEventBroker;
import de.objectcode.canyon.spi.event.IBPEEventListener;
import de.objectcode.canyon.spi.event.IPackageListener;
import de.objectcode.canyon.spi.event.IProcessDefinitionListener;
import de.objectcode.canyon.spi.event.IProcessInstanceListener;
import de.objectcode.canyon.spi.event.ITransitionListener;
import de.objectcode.canyon.spi.event.IWorkItemListener;
import de.objectcode.canyon.spi.event.IWorkflowEventBroker;
import de.objectcode.canyon.spi.instance.IActivityInstance;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.instance.IProcessInstance;
import de.objectcode.canyon.spiImpl.event.DefaultBPEEventBroker;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;


/**
 * This EventBroker queues Events on a per thread basis and delegates them to a default event broker on commit.
 * @author xylander
 *
 */
public class AsyncBPEEventBroker implements IBPEEventBroker {

  private final static  Log  log  = LogFactory.getLog( AsyncBPEEventBroker.class );

	private static ThreadLocal gThreadLocal = new ThreadLocal();

	private BPEEventQueue createQueue() {
		BPEEventQueue queue = new BPEEventQueue(this);
		gThreadLocal.set(queue);
		return queue;
	}

	private BPEEventQueue getQueue() {
		BPEEventQueue queue = (BPEEventQueue) gThreadLocal.get();
		if (queue==null) {
			throw new RuntimeException("No queue bound");
		}
		return queue;
	}

	private void dropQueue() {
		BPEEventQueue queue = (BPEEventQueue) gThreadLocal.get();
		if (queue!=null) {
			gThreadLocal.set(null);
		}
	}

	public AsyncBPEEventBroker( ServiceManager serviceManager )
  {
  	fDefaultEventBroker = new DefaultBPEEventBroker(serviceManager);
  }

	public void beginTransaction() {
		createQueue();
	}

	public void commitTransaction() {
		long start = 0L;
		if (log.isInfoEnabled()) {
			start = System.currentTimeMillis();
		}
		List<BPEEvent> eventEntries =  getQueue().getEventEntries();
		while (eventEntries.size()>0) {
			BPEEvent event = (BPEEvent)eventEntries.remove(0);
			fDefaultEventBroker.fireBPEEvent(event);
		}
		if (log.isInfoEnabled()) {
			log.info("Split commitTransaction:"+(System.currentTimeMillis()-start));
		}

	}

	public void rollbackTransaction() {
		dropQueue();
	}

	private DefaultBPEEventBroker fDefaultEventBroker;

	public void addBPEEventListener(IBPEEventListener bpeEventListener) {
		fDefaultEventBroker.addBPEEventListener(bpeEventListener);		
	}

	public void fireBPEEvent(BPEEvent bpeEvent) {
		getQueue().addEvent(bpeEvent);
	}

	public ServiceManager getServiceManager() {
		return fDefaultEventBroker.getServiceManager();
	}


}
