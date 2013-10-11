package de.objectcode.canyon.spi.event;

import de.objectcode.canyon.api.worklist.BPEEvent;

public interface IBPEEventListener {
	
	public void handleBPEEvent(BPEEvent bpeEvent);
}
