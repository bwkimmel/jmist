/**
 *
 */
package org.jmist.framework.event;

/**
 * Represents an event to which observers may subscribe to receive
 * notifications when the event is raised.
 * @author bkimmel
 */
public interface Event<T> {

	/**
	 * Registers an observer to be notified when this event is raised.
	 * @param handler The observer to receive notifications.
	 */
	void subscribe(EventObserver<? super T> handler);

	/**
	 * Removes an observer from the notification list, so that it will
	 * no longer receive notifications when this event is raised.
	 * @param handler The observer to remove from the notification list.
	 */
	void unsubscribe(EventObserver<? super T> handler);

}
