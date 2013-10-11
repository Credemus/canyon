package de.objectcode.canyon.spiImpl.event.async;

import java.util.LinkedList;
import java.util.List;

import de.objectcode.canyon.spi.event.ApplicationEvent;
import de.objectcode.canyon.spiImpl.event.AbstractListenerSupport;

public class WorkflowEventQueue {

	private LinkedList fEventEntries= new LinkedList();
	private AsyncWorkflowEventBroker fBroker;
	
	public WorkflowEventQueue(AsyncWorkflowEventBroker broker) {
		fBroker = broker;
	}

	public void addEvent(ApplicationEvent event, AbstractListenerSupport support) {
		fEventEntries.add(new WorkflowEventQueueEntry(event, support));
	}
	
	public static class WorkflowEventQueueEntry {
		AbstractListenerSupport fSupport;
		ApplicationEvent fEvent;
		public WorkflowEventQueueEntry(ApplicationEvent event, AbstractListenerSupport support) {
			fEvent = event;
			fSupport = support;
		}
		public ApplicationEvent getEvent() {
			return fEvent;
		}
		public AbstractListenerSupport getSupport() {
			return fSupport;
		}
	}

	public LinkedList getEventEntries() {
		return fEventEntries;
	}

	public void clear() {
		fEventEntries.clear();
	}
}
