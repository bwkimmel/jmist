/**
 *
 */
package org.jmist.toolkit;

import org.jmist.util.MathUtil;

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
	 * Determines if this vector is in canonical form.  Two equivalent
	 * <code>SphericalCoordinates</code> (i.e., pointing in the same direction
	 * and having the same radius) will have equal values for both angles and
	 * the radius when converted to canonical form.  For example, the following
	 * spherical coordinates:
	 * <code>
	 * 		<br>
	 * 		<br>(Math.PI / 6, Math.PI / 2, 1.0)
	 * 		<br>(-5 * Math.PI / 6, Math.PI / 2, -1.0)
	 * 		<br><br>
	 * </code>
	 * represent the same vector.  The first of those vectors is in canonical
	 * form.  The second is not.  A <code>SphericalCoordinates</code> vector is
	 * considered to be in canonical form if <code>0 &lt;= polar &lt;= PI</code>,
	 * <code>-PI &lt;= azimuthal &lt; PI</code>, and <code>radius &gt;= 0</code>.
	 * @return A value indicating if this vector is in canonical form.
	 */
	public boolean isCanonical() {
		return (0.0 <= this.polar && this.polar <= Math.PI
				&& -Math.PI <= this.azimuthal && this.azimuthal < Math.PI
				&& this.radius >= 0.0);
	}

	/**
	 * Computes the canonical form of this vector.  Two equivalent
	 * <code>SphericalCoordinates</code> (i.e., pointing in the same direction
	 * and having the same radius) will have equal values for both angles and
	 * the radius when converted to canonical form.  For example, the following
	 * spherical coordinates:
	 * <code>
	 * 		<br>
	 * 		<br>(Math.PI / 6, Math.PI / 2, 1.0)
	 * 		<br>(-5 * Math.PI / 6, Math.PI / 2, -1.0)
	 * 		<br><br>
	 * </code>
	 * represent the same vector.  The first of those vectors is in canonical
	 * form.  The second is not.  A <code>SphericalCoordinates</code> vector is
	 * considered to be in canonical form if <code>0 &lt;= polar &lt;= PI</code>,
	 * <code>-PI &lt;= azimuthal &lt; PI</code>, and <code>radius &gt;= 0</code>.
	 * @return The canonical <code>SphericalCoordinates</code> representation
	 * 		of this vector.
	 */
	public SphericalCoordinates canonical() {

		/* If the vector is already in canonical form, don't create a new
		 * one.
		 */
		if (this.isCanonical()) {
			return this;
		}

		double newPolar = this.polar;
		double newAzimuthal = this.azimuthal;

		/* If the radius is negative, compensate by adding PI to the polar
		 * angle.
		 */
		if (this.radius < 0.0) {
			newPolar += Math.PI;
		}

		/* Canonicalize the polar angle. */
		newPolar = canonicalize(newPolar);

		/* If the polar angle is negative (i.e., between -PI and zero), then
		 * compensate by adding PI to the azimuthal angle.
		 */
		if (newPolar < 0.0) {
			newAzimuthal += Math.PI;
			newPolar = -newPolar;
		}

		return new SphericalCoordinates(newPolar, newAzimuthal, Math.abs(this.radius));

	}

	/**
	 * Canonicalizes an angle (i.e., computes the equivalent angle satisfying
	 * <code>-PI &lt;= theta &lt; PI</code>.
	 * @param theta The angle to compute the canonical form of.
	 * @return The equivalent angle in the canonical range.
	 */
	public static double canonicalize(double theta) {
		return theta - (2.0 * Math.PI) * Math.floor(theta / (2.0 * Math.PI)) - Math.PI;
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