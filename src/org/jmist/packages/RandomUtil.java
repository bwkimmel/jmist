/**
 *
 */
package org.jmist.packages;

import org.jmist.toolkit.SphericalCoordinates;

/**
 * @author bkimmel
 *
 */
public final class RandomUtil {

	public static SphericalCoordinates diffuse() {
		return diffuse(random.nextDouble(), random.nextDouble());
	}

	public static SphericalCoordinates diffuse(double xi1, double xi2) {

		assert(0.0 <= xi1 && xi1 <= 1.0);
		assert(0.0 <= xi2 && xi2 <= 1.0);

		return new SphericalCoordinates(
				Math.acos(Math.sqrt(1.0 - xi1)),
				2.0 * Math.PI * xi2
		);

	}

	public static SphericalCoordinates uniformOnUpperHemisphere() {
		return uniformOnUpperHemisphere(1.0, random.nextDouble(), random.nextDouble());
	}

	public static SphericalCoordinates uniformOnUpperHemisphere(double radius) {
		return uniformOnUpperHemisphere(radius, random.nextDouble(), random.nextDouble());
	}

	public static SphericalCoordinates uniformOnUpperHemisphere(double radius, double xi1, double xi2) {

		// TODO implement this directly so it's more efficient.
		SphericalCoordinates result = uniformOnSphere(radius, xi1, xi2);

		if (result.polar() > (Math.PI / 2.0))
			result = new SphericalCoordinates(Math.PI - result.polar(), result.azimuthal(), radius);

		return result;

	}

	public static SphericalCoordinates uniformOnSphere() {
		return uniformOnSphere(1.0, random.nextDouble(), random.nextDouble());
	}

	public static SphericalCoordinates uniformOnSphere(double radius) {
		return uniformOnSphere(radius, random.nextDouble(), random.nextDouble());
	}

	public static SphericalCoordinates uniformOnSphere(double radius, double xi1, double xi2) {

		assert(0.0 <= xi1 && xi1 <= 1.0);
		assert(0.0 <= xi2 && xi2 <= 1.0);

		return new SphericalCoordinates(
				Math.acos(2.0 * xi1 - 1.0),
				2.0 * Math.PI * xi2,
				radius
		);

	}

	private static final java.util.Random random = new java.util.Random();

}
