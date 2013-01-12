/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.math.Point2;

/**
 * A two-dimensional grayscale texture, typically layered in to control the
 * weight of some other property.
 * @author Brad Kimmel
 */
public interface Mask2 extends Serializable {

	/**
	 * Evaluates the mask at the specified point.
	 * @param p The point at which to evaluate the mask.
	 * @return The opacity (dimensionless) at the
	 * 		specified point.  The return value shall
	 * 		fall between 0 and 1 inclusive.
	 */
	double opacity(Point2 p);

}
