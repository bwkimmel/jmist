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
public class EventSubject<T> implements Event<T> {

	/* (non-Javadoc)
	 * @see org.jmist.framework.event.Event#subscribe(org.jmist.framework.event.EventObserver)
	 */
	public void subscribe(EventObserver<? super T> handler) {
		observers.add(handler);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.event.Event#unsubscribe(org.jmist.framework.event.EventObserver)
	 */
	public void unsubscribe(EventObserver<? super T> handler) {
		observers.remove(handler);
	}

	/**
	 * Notifies observers that this event has occurred.
	 * @param sender The object that is the subject of this event.
	 * @param args Additional information pertaining to this event.
	 */
	public void raise(Object sender, T args) {
		for (EventObserver<? super T> observer : observers) {
			observer.notify(sender, args);
		}
	}

	/**
	 * The collection of observers to be notified when the
	 * event is raised.
	 */
	private Set<EventObserver<? super T>> observers = new LinkedHashSet<EventObserver<? super T>>();

}
