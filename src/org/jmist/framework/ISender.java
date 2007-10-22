/**
 *
 */
package org.jmist.framework;

/**
 * Sends messages to a remote location.
 * @author bkimmel
 */
public interface ISender {

	/**
	 * Creates a new outbound message.
	 * @return The new <code>IOutboundMessage</code>.
	 */
	IOutboundMessage createOutboundMessage();

	/**
	 * Sends a message immediately.
	 * @param message The message to send.
	 */
	void send(IOutboundMessage message);

	/**
	 * Queue a message to be sent.
	 * @param message The message to send.
	 */
	void queue(IOutboundMessage message);

	/**
	 * Clears the queue of any remaining outbound messages
	 * waiting to be sent.
	 */
	void flush();

}
