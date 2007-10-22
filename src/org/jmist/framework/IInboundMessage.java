/**
 *
 */
package org.jmist.framework;

import java.io.InputStream;

/**
 * Represents a message that has arrived from a remote location.
 * @author bkimmel
 */
public interface IInboundMessage extends IMessage {

	/**
	 * Gets a <code>String</code> representing the source of the message.
	 * @return A <code>String</code> representing the source of the message.
	 */
	String from();

	/**
	 * The stream from which to read the contents of the message.
	 * @return The <code>InputStream</code> from which to read the contents of
	 * 		the message.
	 */
	InputStream contents();

}
