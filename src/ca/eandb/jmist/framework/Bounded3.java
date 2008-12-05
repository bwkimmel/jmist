/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface Bounded3 {

	/**
	 * Gets a sphere that bounds this object.
	 * @return A bounding sphere.
	 */
	Sphere boundingSphere();

	/**
	 * Gets a box that bounds this object.
	 * @return A bounding box.
	 */
	Box3 boundingBox();

}
