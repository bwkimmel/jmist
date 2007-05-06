/**
 *
 */
package framework.interfaces;

import framework.core.*;

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
