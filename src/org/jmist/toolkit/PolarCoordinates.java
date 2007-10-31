/**
 *
 */
package org.jmist.toolkit;

/**
 * Two dimensional vector represented using polar coordinates.  This class is
 * immutable.
 * @author bkimmel
 */
public final class PolarCoordinates {

	/**
	 * Creates a new unit <code>PolarCoordinates</code>.
	 * @param angle The angle (in radians) between the vector and the positive
	 * 		x-axis.
	 */
	public PolarCoordinates(double angle) {
		this.angle = angle;
		this.radius = 1.0;
	}

	/**
	 * Creates a new <code>PolarCoordinates</code>.
	 * @param angle The angle (in radians) between the vector and the positive
	 * 		x-axis.
	 * @param radius The length of the vector.
	 */
	public PolarCoordinates(double angle, double radius) {
		this.angle = angle;
		this.radius = radius;
	}

	/**
	 * The angle (in radians) between the vector and the positive x-axis.
	 * @return The angle (in radians) between the vector and the positive
	 * 		x-axis.
	 */
	public double angle() {
		return this.angle;
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
	public PolarCoordinates unit() {
		return new PolarCoordinates(this.angle, 1.0);
	}

	/**
	 * Scales this vector by a constant factor.
	 * @param factor The factor to scale this vector by.
	 * @return The vector in the same direction as this vector scaled by the
	 * 		specified factor.
	 */
	public PolarCoordinates times(double factor) {
		return new PolarCoordinates(this.angle, this.radius * factor);
	}

	/**
	 * Returns the vector of the same length as this vector in the opposite
	 * direction.
	 * @return The vector of the same length as this vector in the opposite
	 * direction.
	 */
	public PolarCoordinates opposite() {
		return new PolarCoordinates(this.angle, -this.radius);
	}

	/**
	 * Converts this vector to cartesian coordinates.
	 * @return The <code>Vector2</code> representing the same vector as this
	 * 		vector.
	 */
	public Vector2 toCartesian() {
		return new Vector2(
				this.radius * Math.cos(this.angle),
				this.radius * Math.sin(this.angle)
		);
	}

	/**
	 * Converts a vector from cartesian coordinates to
	 * <code>PolarCoordinates</code>.
	 * @param v The <code>Vector2</code> to convert to
	 * 		<code>PolarCoordinates</code>.
	 * @return The vector in <code>PolarCoordinates</code> representing the
	 * 		same vector as <code>v</code>.
	 */
	public static PolarCoordinates fromCartesian(Vector2 v) {
		return new PolarCoordinates(
				Math.atan2(v.y(), v.x()),
				v.length()
		);
	}

	/** The angle (in radians) between the vector and the positive x-axis. */
	private final double angle;

	/** The length of this vector. */
	private final double radius;

}
