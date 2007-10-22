/**
 *
 */
package org.jmist.framework;

/**
 * Receives messages from a remote source.
 * @author bkimmel
 */
public interface IReceiver {

	/**
	 * Wait for an incoming message.
	 * @param timeout The length of time (in milliseconds) to wait before
	 * 		timing out.
	 * @return The incoming message, or <code>null</code> if no message was
	 * 		received before <code>timeout</code> milliseconds elapsed.
	 */
	IInboundMessage receive(long timeout);

	/**
	 * Wait for an incoming message.
	 * @return The incoming message.
	 */
	IInboundMessage receive();

	/**
	 * Check for an incoming message.
	 * @return The incoming message, or <code>null</code> if no message has
	 * 		been received.
	 */
	IInboundMessage peek();

}
