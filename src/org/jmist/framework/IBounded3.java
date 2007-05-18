/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface IBounded3 {

	/**
	 * Gets a sphere that bounds this object.
	 * @return A bounding sphere.
	 */
	Sphere getBoundingSphere();

	/**
	 * Gets a box that bounds this object.
	 * @return A bounding box.
	 */
	Box3 getBoundingBox();

}
