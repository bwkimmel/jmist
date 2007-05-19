/**
 *
 */
package org.jmist.framework;

/**
 * Something that can be scaled by a specified factor.
 * @author brad
 */
public interface IScalable {

	/**
	 * Scales the object by a constant factor.
	 * @param c The factor by which to scale the object.
	 */
	void scale(double c);

}
