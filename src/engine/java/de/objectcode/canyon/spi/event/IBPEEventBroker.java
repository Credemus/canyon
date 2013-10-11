package de.objectcode.canyon.spi.event;

import de.objectcode.canyon.api.worklist.BPEEvent;
import de.objectcode.canyon.spi.ServiceManager;

public interface IBPEEventBroker {
	  public ServiceManager getServiceManager();
	
	  public void addBPEEventListener(IBPEEventListener bpeEventListener); 
	  
	  public void fireBPEEvent(BPEEvent bpeEvent);

	  public void beginTransaction();

	  public void commitTransaction();

	  public void rollbackTransaction();

}
