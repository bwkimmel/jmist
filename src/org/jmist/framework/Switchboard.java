/**
 *
 */
package org.jmist.framework;

/**
 * Manages remote connections.
 * @author bkimmel
 */
public interface Switchboard extends Dialer {

	/**
	 * Wait for an incoming connection or message.
	 * @return The new connection or the connection on which an incoming
	 * 		message was received.
	 */
	Communicator listen();

}
