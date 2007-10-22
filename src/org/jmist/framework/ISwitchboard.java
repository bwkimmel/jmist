/**
 *
 */
package org.jmist.framework;

/**
 * Manages remote connections.
 * @author bkimmel
 */
public interface ISwitchboard extends IDialer {

	/**
	 * Wait for an incoming connection or message.
	 * @param timeout The length of time (in milliseconds) to wait.
	 * @return The new connection or the connection on which an incoming
	 * 		message was received, or <code>null</code> if <code>timeout</code>
	 * 		milliseconds elapsed before any activity occurred.
	 */
	ICommunicator listen(long timeout);

	/**
	 * Wait for an incoming connection or message.
	 * @return The new connection or the connection on which an incoming
	 * 		message was received.
	 */
	ICommunicator listen();

}
