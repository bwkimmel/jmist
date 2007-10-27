/**
 *
 */
package org.jmist.framework;

/**
 * Opens connections with a remote location.
 * @author bkimmel
 */
public interface Dialer {

	/**
	 * Opens a connection with a remote location.
	 * @param to A <code>String</code> identifying the remote location to open
	 * 		a connection to.
	 * @return The communicator to use to send and receive messages to/from the
	 * 		remote location.
	 */
	Communicator dial(String to);

}
