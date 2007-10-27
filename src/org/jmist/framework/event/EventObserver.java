/**
 *
 */
package org.jmist.framework.event;

/**
 * Represents an observer to an event that can be notified when
 * the event is raised.
 * @author bkimmel
 */
public interface EventObserver<T> {

	/**
	 * Called to indicate that an event has occurred.
	 * @param sender The subject of this event.
	 * @param args Any additional arguments relating to this event.
	 */
	void notify(Object sender, T args);

}