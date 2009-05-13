/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Circle;
import ca.eandb.jmist.toolkit.*;

/**
 * @author Brad Kimmel
 *
 */
public interface Bounded2 {

	/**
	 * Gets a circle that bounds this object.
	 * @return A bounding circle.
	 */
	Circle boundingCircle();

	/**
	 * Gets a box that bounds this object.
	 * @return A bounding box.
	 */
	Box2 boundingBox();

}
