/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * Represents something that can be translated (moved) around
 * in three dimensional space.
 * @author brad
 */
public interface ITranslatable3 {

	/**
	 * Translates the object along the specified vector.
	 * @param v The vector to translate the object by.s
	 */
	public void translate(Vector3 v);

}
