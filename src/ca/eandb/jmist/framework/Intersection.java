/**
 *
 */
package ca.eandb.jmist.framework;

/**
 * @author Brad Kimmel
 *
 */
public interface Intersection {

	/**
	 * Gets the distance from the ray origin to the intersection
	 * point.
	 * @return The distance from the ray origin to the intersection.
	 */
	double getDistance();

	/**
	 * Indicates whether the incident ray approaches the interface from the
	 * front (the side toward which the normal points).
	 * @return A value indicating whether the ray approaches the interface
	 * 		from the front.
	 */
	boolean isFront();

	void prepareShadingContext(ShadingContext context);

}
