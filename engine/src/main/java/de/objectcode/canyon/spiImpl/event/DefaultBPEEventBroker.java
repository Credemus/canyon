package de.objectcode.canyon.spiImpl.event;

import java.util.ArrayList;
import java.util.Iterator;

import de.objectcode.canyon.api.worklist.BPEEvent;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.event.IBPEEventBroker;
import de.objectcode.canyon.spi.event.IBPEEventListener;

public class DefaultBPEEventBroker implements IBPEEventBroker {
	
	private ArrayList<IBPEEventListener> m_eventListeners = new ArrayList<IBPEEventListener>();
  private ServiceManager							 m_serviceManager;
  
  
  public DefaultBPEEventBroker(ServiceManager serviceManager) {
  	m_serviceManager  = serviceManager;
  }
  
	public void addBPEEventListener(IBPEEventListener bpeEventListener) {
		synchronized (m_eventListeners) {
			m_eventListeners.add(bpeEventListener);			
		}
	}

	public void beginTransaction() {
		// TODO Auto-generated method stub

	}

	public void commitTransaction() {
		// TODO Auto-generated method stub

	}

	public void fireBPEEvent(BPEEvent bpeEvent) {
		for (IBPEEventListener listener : m_eventListeners) {
			listener.handleBPEEvent(bpeEvent);
		}
	}

	public ServiceManager getServiceManager() {
		return m_serviceManager;
	}

	public void rollbackTransaction() {
		// TODO Auto-generated method stub

	}

}
