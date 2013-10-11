package de.objectcode.canyon.spiImpl.event.async;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.canyon.api.worklist.BPEEvent;

public class BPEEventQueue {

	private List<BPEEvent> fEventEntries= new ArrayList<BPEEvent>();
	private AsyncBPEEventBroker fBroker;
	
	public BPEEventQueue(AsyncBPEEventBroker broker) {
		fBroker = broker;
	}

	public void addEvent(BPEEvent event) {
		fEventEntries.add(event);
	}
	
	public List<BPEEvent> getEventEntries() {
		return fEventEntries;
	}

	public void clear() {
		fEventEntries.clear();
	}
}
