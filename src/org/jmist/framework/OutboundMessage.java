/**
 *
 */
package org.jmist.framework;

import java.io.OutputStream;

/**
 * Represents a message to be sent to a remote location.
 * @author bkimmel
 */
public interface OutboundMessage extends Message {

	/**
	 * The stream to write the message to.
	 * @return The <code>OutputStream</code> to write the message to.
	 */
	OutputStream contents();

	/**
	 * Sets the tag associated with this message.
	 * @param value The tag associated with this message.
	 */
	void tag(int value);

}
