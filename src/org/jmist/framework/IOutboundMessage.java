/**
 *
 */
package org.jmist.framework;

import java.io.OutputStream;

/**
 * Represents a message to be sent to a remote location.
 * @author bkimmel
 */
public interface IOutboundMessage extends IMessage {

	/**
	 * The stream to write the message to.
	 * @return The <code>OutputStream</code> to write the message to.
	 */
	OutputStream contents();

}
