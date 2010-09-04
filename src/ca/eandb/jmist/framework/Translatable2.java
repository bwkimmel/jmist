/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Vector2;

/**
 * Represents something that can be translated (moved) around
 * in two dimensional space.
 * @author Brad Kimmel
 */
public interface Translatable2 {

	/**
	 * Translates the object along the specified vector.
	 * @param v The vector to translate the object by.
	 */
	void translate(Vector2 v);

}
