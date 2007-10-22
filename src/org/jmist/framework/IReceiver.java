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
