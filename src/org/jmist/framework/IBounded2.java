/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface IBounded2 {

	/**
	 * Gets a circle that bounds this object.
	 * @return A bounding circle.
	 */
	Circle getBoundingCircle();

	/**
	 * Gets a box that bounds this object.
	 * @return A bounding box.
	 */
	Box2 getBoundingBox();

}
