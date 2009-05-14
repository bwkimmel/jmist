/**
 *
 */
package ca.eandb.jmist.math;

import java.io.Serializable;

/**
 * Three dimensional vector represented using spherical coordinates.  This
 * class is immutable.
 * @author Brad Kimmel
 */
public final class SphericalCoordinates implements Serializable {

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

		return SphericalCoordinates.canonical(this.polar, this.azimuthal, this.radius);

	}

	/**
	 * Canonicalizes an angle (i.e., computes the equivalent angle satisfying
	 * <code>-PI &lt;= theta &lt; PI</code>.
	 * @param theta The angle to compute the canonical form of.
	 * @return The equivalent angle in the canonical range.
	 */
	public static double canonicalize(double theta) {
		return theta - (2.0 * Math.PI) * Math.floor((theta + Math.PI) / (2.0 * Math.PI));
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
	 * Converts this vector to cartesian coordinates relative to the specified
	 * <code>Basis3</code>.
	 * @param basis The <code>Basis3</code> to express this
	 * 		<code>SphericalCoordinates</code> relative to.
	 * @return The <code>Vector3</code> representing the same vector as this
	 * 		vector relative to the specified <code>Basis3</code>.
	 */
	public Vector3 toCartesian(Basis3 basis) {

		double sp = Math.sin(this.polar);
		double sa = Math.sin(this.azimuthal);
		double cp = Math.cos(this.polar);
		double ca = Math.cos(this.azimuthal);

		// FIXME this is grossly inefficient.
		return basis.u().times(this.radius * sp * ca)
				.plus(basis.v().times(this.radius * sp * sa))
				.plus(basis.w().times(this.radius * cp));

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

	/**
	 * Converts a vector from cartesian coordinates to
	 * <code>SphericalCoordinates</code>.
	 * @param v The <code>Vector3</code> to convert to
	 * 		<code>SphericalCoordinates</code>.
	 * @param basis The <code>Basis3</code> in which <code>v</code> is
	 * 		expressed.
	 * @return The vector in <code>SphericalCoordinates</code> representing the
	 * 		same vector as <code>v</code>.
	 */
	public static SphericalCoordinates fromCartesian(Vector3 v, Basis3 basis) {

		double x = basis.u().dot(v);
		double y = basis.v().dot(v);
		double z = basis.w().dot(v);
		double r = v.length();

		return new SphericalCoordinates(
				Math.acos(z / r),
				Math.atan2(y, x),
				r
		);

	}

	/**
	 * Converts this <code>SphericalCoordinates</code> to a compact, two byte
	 * representation of its direction.  The first 8 bits represent the polar
	 * angle, and the second (low order) 8 bits represent the azimuthal angle.
	 * @return The two byte representation of the direction.
	 */
	public short toCompactDirection() {
		int theta = (int) Math.floor(polar * (256.0 / Math.PI));
		if (theta > 255) {
			theta = 255;
		}

		int phi = (int) Math.floor(azimuthal * 256.0 / (2.0 * Math.PI));
		if (phi > 255) {
			phi = 255;
		} else if (phi < 255) {
			phi += 256;
		}

		return (short) ((theta << 8) | phi);
	}

	/**
	 * Creates a <code>SphericalCoordinates</code> corresponding to the
	 * provided two byte direction representation.
	 * @param dir The two byte direction.
	 * @return The corresponding <code>SphericalCoordinates</code>.
	 * @see #toCompactDirection()
	 */
	public static SphericalCoordinates fromCompactDirection(short dir) {
		int phi = dir & 0xff;
		int theta = dir >> 8;
		return new SphericalCoordinates(
				(double) phi * (Math.PI / 256.0),
				(double) theta * (2.0 * Math.PI / 256.0));
	}

	/**
	 * Computes the canonical form of the vector with the given polar and
	 * azimuthal angles and unit radius.  Two equivalent
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
	public static SphericalCoordinates canonical(double polar, double azimuthal) {
		return canonical(polar, azimuthal, 1.0);
	}

	/**
	 * Computes the canonical form of the vector with the given polar and
	 * azimuthal angles and the given radius.  Two equivalent
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
	public static SphericalCoordinates canonical(double polar, double azimuthal, double radius) {

		/* If the radius is negative, compensate by adding PI to the polar
		 * angle.
		 */
		if (radius < 0.0) {
			radius = -radius;
			polar += Math.PI;
		}

		/* Canonicalize the polar angle. */
		polar = canonicalize(polar);

		/* If the polar angle is negative (i.e., between -PI and zero), then
		 * compensate by adding PI to the azimuthal angle.
		 */
		if (polar < 0.0) {
			azimuthal += Math.PI;
			polar = -polar;
		}

		/* Canonicalize the azimuthal angle. */
		azimuthal = canonicalize(azimuthal);

		return new SphericalCoordinates(polar, azimuthal, radius);

	}

	/**
	 * The <code>SphericalCoordinates</code> for the normal direction (i.e.,
	 * the polar and azimuthal angles are both zero, the radius is 1.0).
	 */
	public static final SphericalCoordinates NORMAL = new SphericalCoordinates(0, 0, 1);

	/**
	 * The <code>SphericalCoordinates</code> for the direction opposite to the
	 * normal direction (i.e., the polar angle is PI, the azimuthal angle is
	 * zero, and the radius is 1.0).
	 */
	public static final SphericalCoordinates ANTINORMAL = new SphericalCoordinates(Math.PI, 0, 1);

	/** The angle (in radians) between the vector and the positive z-axis. */
	private final double polar;

	/**
	 * The angle (in radians) between the vector projected onto the xy-plane
	 * and the positive x-axis.
	 */
	private final double azimuthal;

	/** The length of this vector. */
	private final double radius;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -4978227486578315932L;

}
