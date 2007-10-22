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
	 * @return The new connection or the connection on which an incoming
	 * 		message was received.
	 */
	ICommunicator listen();

}
