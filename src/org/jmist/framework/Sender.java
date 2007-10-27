/**
 *
 */
package org.jmist.framework;

/**
 * Sends messages to a remote location.
 * @author bkimmel
 */
public interface Sender {

	/**
	 * Creates a new outbound message.
	 * @return The new <code>OutboundMessage</code>.
	 */
	OutboundMessage createOutboundMessage();

	/**
	 * Sends a message immediately.
	 * @param message The message to send.
	 */
	void send(OutboundMessage message);

	/**
	 * Queue a message to be sent.
	 * @param message The message to send.
	 */
	void queue(OutboundMessage message);

	/**
	 * Clears the queue of any remaining outbound messages
	 * waiting to be sent.
	 */
	void flush();

}
