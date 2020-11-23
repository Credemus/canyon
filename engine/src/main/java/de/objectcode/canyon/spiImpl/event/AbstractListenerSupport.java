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
package de.objectcode.canyon.spiImpl.event;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.CanyonRuntimeException;
import de.objectcode.canyon.spi.event.ApplicationEvent;
import de.objectcode.canyon.spi.event.IWorkflowEventBroker;
import de.objectcode.canyon.spi.event.WorkflowEvent;

/**
 * Base class for implementing listener support classes. The listener interfaces
 * themselves must follow a specific design pattern: they should declare a set
 * of <code>int &lt;event-name&gt;_EVENT_MASK</code> constants that match the
 * events declared by the corresponding {@link WorkflowEvent} subclass, with
 * values equal to two raised to the power of the corresponding event id.
 * 
 * @author Adrian Price
 * @created 3. November 2003
 * @see WorkflowEvent
 */
public abstract class AbstractListenerSupport {
	private final static Log log = LogFactory
			.getLog(AbstractListenerSupport.class);

	private final static String[] EMPTY_KEYS = {};

	protected final IWorkflowEventBroker _eventBroker;

	private final Constructor _eventCtor;

	private final int _eventCtorArgCount;

	private final Integer[] _eventIds;

	private final Method[] _notifMethods;

	// We store the event subscriptions in a single object so that we can
	// update the registrations atomically.
	private EventListener[] m_listeners;
	
	private final Map _notifMethodMap; 

	private static String capitalize(String s) {
		if (s.length()>0) {
			StringBuffer sb = new StringBuffer(s.substring(0,1).toUpperCase());
			if (s.length()>1)
				sb.append(s.substring(1));
			return sb.toString();
		}	else
			return s;
	}
	
	/**
	 * Create a new listener support instance.
	 * 
	 * @param eventClass
	 *          The class of the {@link WorkflowEvent} sub-class to pass to the
	 *          listeners in the fire method. The event class must have a single,
	 *          public constructor that takes the following arguments:
	 *          <code>(java.lang.Object source, int id[, org.obe.WFElement definition])
	 * </code>.
	 *          The <code>source</code> and <code>definition</code> arguments
	 *          are sub-classes of <code>Object</code> and
	 *          <code>WFElement</code> respectively. The <code>definition</code>
	 *          argument is optional.
	 * @param listenerClass
	 *          The class of the listeners to be managed by this listener support
	 *          object.
	 * @param notifMethods
	 *          Names of notification methods, ordered by their corresponding
	 *          {@link WorkflowEvent#getId event ID}.
	 * @param eventBroker
	 *          Description of the Parameter
	 */
	protected AbstractListenerSupport(IWorkflowEventBroker eventBroker,
			Class eventClass, Class listenerClass, String[] notifMethods) {

		_eventBroker = eventBroker;
		_eventCtor = eventClass.getConstructors()[0];
		_eventCtorArgCount = _eventCtor.getParameterTypes().length;
		m_listeners = new EventListener[0];

		// Set up parallel arrays of event id, event mask, and notification
		// method. These will be used in the fire() method.
		int count = notifMethods.length;
		_eventIds = new Integer[count];
		_notifMethods = new Method[count];
		_notifMethodMap = new HashMap();
		Class[] notifMethodArgs = { eventClass };
		for (int i = 0; i < count; i++) {
			_eventIds[i] = new Integer(i);
			try {
				_notifMethods[i] = listenerClass.getMethod(notifMethods[i],
						notifMethodArgs);
				_notifMethodMap.put(capitalize(notifMethods[i]),_notifMethods[i]);
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalArgumentException(notifMethods[i]);
			}
		}
	}

	protected Method getNotificationMethod(String eventType) {
		Method m = (Method) _notifMethodMap.get(eventType);
		if (m==null)
			throw new RuntimeException("Illegal event type '" + eventType + "'");
		return m;
	}
	
	// This method is synchronized because it updates listener registrations.
	/**
	 * Adds a feature to the Listener attribute of the AbstractListenerSupport
	 * object
	 * 
	 * @param listener
	 *          The feature to be added to the Listener attribute
	 */
	public final synchronized void addListener(EventListener listener) {

		if (log.isDebugEnabled()) {
			log.debug("addListener(" + listener + ')');
		}

		// Cannot register the same listener twice.
		if (findIndex(listener) != -1) {
			throw new IllegalArgumentException(
					"Event listener is already registered: " + listener);
		}
		int count = m_listeners.length;
		EventListener newListeners[] = new EventListener[count + 1];
		System.arraycopy(m_listeners, 0, newListeners, 0, count);
		newListeners[count] = listener;
		m_listeners = newListeners;
	}

	// This method is synchronized because it updates listener registrations.
	/**
	 * Description of the Method
	 * 
	 * @param listener
	 *          Description of the Parameter
	 */
	public final synchronized void removeListener(EventListener listener) {
		if (log.isDebugEnabled()) {
			log.debug("removeListener(" + listener + ')');
		}

		int index = findIndex(listener);
		if (index == -1) {
			throw new IllegalArgumentException("Event listener is not registered: "
					+ listener);
		}

		// Resize the subscriptions arrays.
		int count = m_listeners.length;
		EventListener newListeners[] = new EventListener[count - 1];

		// Copy the lower portion below the index that was removed.
		if (index > 0) {
			System.arraycopy(m_listeners, 0, newListeners, 0, index);
		}

		// Copy the upper portion above the index that was removed.
		if (index + 1 < count) {
			int remainder = count - index - 1;
			System.arraycopy(m_listeners, index + 1, newListeners, index, remainder);
		}

		// Set the subscriptions object atomically.
		m_listeners = newListeners;
	}

	/**
	 * Removes all listeners.
	 */
	public final synchronized void clear() {
		m_listeners = new EventListener[0];
	}

	public ApplicationEvent createEvent(Object source, int id, Object definition) {
		ApplicationEvent event = null;
		try {
			// Construct the notification method arguments lazily.
				// Build the event object's constructor's arglist.
				Object[] ctorArgs = new Object[_eventCtorArgCount];
				ctorArgs[0] = source;
				ctorArgs[1] = _eventIds[id];
				ctorArgs[2] = _eventBroker;
				if (_eventCtorArgCount == 4) {
					ctorArgs[3] = definition;
				}

				// Invoke the event object's constructor.
				event = (ApplicationEvent) _eventCtor.newInstance(ctorArgs);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new CanyonRuntimeException(e);
		}
		return event;
	}

	public final void fireEvent(ApplicationEvent event) {
		// Use a local reference to the event registration set, because this
		// can change asynchronously.
		EventListener listeners[] = m_listeners;
		Object[] args = null;
		for (int i = 0, n = listeners.length; i < n; i++) {
			try {
				EventListener listener = listeners[i];
				if (args == null) {
					args = new Object[] { event };
				}
        Method notifMethod = getNotificationMethod(event.getEventType());
			  notifMethod.invoke(listener, args);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new CanyonRuntimeException(e);
			}
		}
	}

	/**
	 * Fires a notification method in all listeners subscribed to that event.
	 * 
	 * @param source
	 *          The entity that is the source of the event.
	 * @param id
	 *          The <code>WorkflowEvent</code> ID.
	 * @param definition
	 *          The definition of the source entity. Can be <code>null</code>.
	 */
	protected final void fire(Object source, int id, Object definition) {
		if (log.isDebugEnabled()) {
			log.debug("fire(" + source + ", " + id + ", " + definition + ')');
		}

		ApplicationEvent event = createEvent(source,id,definition);
		fireEvent(event);
	}

	/**
	 * Description of the Method
	 * 
	 * @param listener
	 *          Description of the Parameter
	 * @return Description of the Return Value
	 */
	private int findIndex(EventListener listener) {
		int index = -1;
		for (int i = 0; i < m_listeners.length; i++) {
			if (m_listeners[i] == listener) {
				index = i;
				break;
			}
		}
		return index;
	}
}
