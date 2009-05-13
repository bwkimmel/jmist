/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Point2;

/**
 * @author Brad Kimmel
 *
 */
public interface Mask2 {

	/**
	 * Evaluates the mask at the specified point.
	 * @param p The point at which to evaluate the mask.
	 * @return The opacity (dimensionless) at the
	 * 		specified point.  The return value shall
	 * 		fall between 0 and 1 inclusive.
	 */
	double opacity(Point2 p);

}
