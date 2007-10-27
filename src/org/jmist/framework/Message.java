/**
 *
 */
package org.jmist.framework;

/**
 * A message that was received or to be sent from/to a
 * remote location.
 * @author bkimmel
 */
public interface Message {

	/**
	 * Gets the tag associated with this message.
	 * @return The tag associated with this message.
	 */
	int tag();

}
