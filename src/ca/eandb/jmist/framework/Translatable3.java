/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.*;

/**
 * Represents something that can be translated (moved) around
 * in three dimensional space.
 * @author brad
 */
public interface Translatable3 {

	/**
	 * Translates the object along the specified vector.
	 * @param v The vector to translate the object by.s
	 */
	void translate(Vector3 v);

}
