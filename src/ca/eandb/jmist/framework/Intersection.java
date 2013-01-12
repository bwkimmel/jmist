/**
 *
 */
package ca.eandb.jmist.framework;

/**
 * A ray-geometry intersection.
 * @author Brad Kimmel
 */
public interface Intersection {

	/**
	 * Gets the distance from the ray origin to the intersection
	 * point.
	 * @return The distance from the ray origin to the intersection.
	 */
	double getDistance();

	/**
	 * Gets the margin of error for the distance computed by the ray
	 * intersection computation.  The actual ray intersection is guaranteed
	 * to be within the interval
	 * <code>(getDistance() - getTolerance(), getDistance() + getTolerance()</code>.
	 * @return The margin of error for the distance computed by the ray
	 * 		intersection computation.
	 */
	double getTolerance();

	/**
	 * Indicates whether the incident ray approaches the interface from the
	 * front (the side toward which the normal points).
	 * @return A value indicating whether the ray approaches the interface
	 * 		from the front.
	 */
	boolean isFront();

	void prepareShadingContext(ShadingContext context);

}
