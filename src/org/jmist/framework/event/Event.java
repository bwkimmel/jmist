/**
 *
 */
package org.jmist.framework.event;

import java.util.Set;
import java.util.LinkedHashSet;

/**
 * Represents an event that can occur and maintains a list of subscribers
 * to be notified when the event is raised.
 * @author bkimmel
 */
public class Event<T> implements IEvent<T> {

	/* (non-Javadoc)
	 * @see org.jmist.framework.event.IEvent#subscribe(org.jmist.framework.event.IEventHandler)
	 */
	public void subscribe(IEventHandler<? super T> handler) {
		observers.add(handler);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.event.IEvent#unsubscribe(org.jmist.framework.event.IEventHandler)
	 */
	public void unsubscribe(IEventHandler<? super T> handler) {
		observers.remove(handler);
	}

	/**
	 * Notifies observers that this event has occurred.
	 * @param sender The object that is the subject of this event.
	 * @param args Additional information pertaining to this event.
	 */
	public void raise(Object sender, T args) {
		for (IEventHandler<? super T> observer : observers) {
			observer.notify(sender, args);
		}
	}

	/**
	 * The collection of observers to be notified when the
	 * event is raised.
	 */
	private Set<IEventHandler<? super T>> observers = new LinkedHashSet<IEventHandler<? super T>>();

}
