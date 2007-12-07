/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Box3;

/**
 * Represents a surface with which one may test for an intersection with a
 * <code>Box3</code>.
 * @author bkimmel
 */
public interface PartialBoundable3 {

	/**
	 * Determines if this surface may potentially intersect with the specified
	 * <code>Box3</code>.  This method may return false positives.
	 * @param box The <code>Box3</code> to test for an intersection with.
	 * @return A value indicating if this surface could intersect with the
	 * 		specified <code>Box3</code>.
	 */
	boolean surfaceMayIntersect(Box3 box);

}
