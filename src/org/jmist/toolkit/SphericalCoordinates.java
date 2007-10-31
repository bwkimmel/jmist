/**
 *
 */
package org.jmist.toolkit;

/**
 * Three dimensional vector represented using spherical coordinates.  This
 * class is immutable.
 * @author bkimmel
 */
public final class SphericalCoordinates {

	/**
	 * Creates a new unit <code>SphericalCoordinates</code>.
	 * @param polar The angle (in radians) between the vector and the positive
	 * 		z-axis.
	 * @param azimuthal The angle (in radians) between the vector projected
	 * 		onto the xy-plane and the positive x-axis.
	 */
	public SphericalCoordinates(double polar, double azimuthal) {
		this.polar = polar;
		this.azimuthal = azimuthal;
		this.radius = 1.0;
	}

	/**
	 * Creates a new <code>SphericalCoordinates</code>.
	 * @param polar The angle (in radians) between the vector and the positive
	 * 		z-axis.
	 * @param azimuthal The angle (in radians) between the vector projected
	 * 		onto the xy-plane and the positive x-axis.
	 * @param radius The length of the vector.
	 */
	public SphericalCoordinates(double polar, double azimuthal, double radius) {
		this.polar = polar;
		this.azimuthal = azimuthal;
		this.radius = radius;
	}

	/**
	 * The angle (in radians) between the vector and the positive z-axis.
	 * @return The angle (in radians) between the vector and the positive
	 * 		z-axis.
	 */
	public double polar() {
		return this.polar;
	}

	/**
	 * The angle (in radians) between the vector projected onto the xy-plane
	 * and the positive x-axis.
	 * @return The angle (in radians) between the vector projected onto the
	 * 		xy-plane and the positive x-axis.
	 */
	public double azimuthal() {
		return this.azimuthal;
	}

	/**
	 * The length of the vector.
	 * @return The length of the vector.
	 */
	public double radius() {
		return this.radius;
	}

	/**
	 * Returns the vector of unit length in the same direction as this vector.
	 * @return The vector of unit length in the same direction as this vector.
	 */
	public SphericalCoordinates unit() {
		return new SphericalCoordinates(this.polar, this.azimuthal, 1.0);
	}

	/**
	 * Scales this vector by a constant factor.
	 * @param factor The factor to scale this vector by.
	 * @return The vector in the same direction as this vector scaled by the
	 * 		specified factor.
	 */
	public SphericalCoordinates times(double factor) {
		return new SphericalCoordinates(this.polar, this.azimuthal, this.radius * factor);
	}

	/**
	 * Returns the vector of the same length as this vector in the opposite
	 * direction.
	 * @return The vector of the same length as this vector in the opposite
	 * direction.
	 */
	public SphericalCoordinates opposite() {
		return new SphericalCoordinates(this.polar, this.azimuthal, -this.radius);
	}

	/**
	 * Converts this vector to cartesian coordinates.
	 * @return The <code>Vector3</code> representing the same vector as this
	 * 		vector.
	 */
	public Vector3 toCartesian() {

		double sp = Math.sin(this.polar);
		double sa = Math.sin(this.azimuthal);
		double cp = Math.cos(this.polar);
		double ca = Math.cos(this.azimuthal);

		return new Vector3(
				this.radius * sp * ca,
				this.radius * sp * sa,
				this.radius * cp
		);

	}

	/**
	 * Converts a vector from cartesian coordinates to
	 * <code>SphericalCoordinates</code>.
	 * @param v The <code>Vector3</code> to convert to
	 * 		<code>SphericalCoordinates</code>.
	 * @return The vector in <code>SphericalCoordinates</code> representing the
	 * 		same vector as <code>v</code>.
	 */
	public static SphericalCoordinates fromCartesian(Vector3 v) {

		double r = v.length();

		return new SphericalCoordinates(
				Math.acos(v.z() / r),
				Math.atan2(v.y(), v.x()),
				r
		);

	}

	/** The angle (in radians) between the vector and the positive z-axis. */
	private final double polar;

	/**
	 * The angle (in radians) between the vector projected onto the xy-plane
	 * and the positive x-axis.
	 */
	private final double azimuthal;

	/** The length of this vector. */
	private final double radius;

}
