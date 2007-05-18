/**
 *
 */
package org.jmist.toolkit;

/**
 * The difference between two points in three dimensional space.
 * This class is immutable.
 * @author brad
 */
public final class Vector3 {

	/**
	 * Initializes the components for the vector.
	 * @param x The length of the vector along the x axis.
	 * @param y The length of the vector along the y axis.
	 * @param z The length of the vector along the z axis.
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Gets the distance from the origin along the x-axis.
	 * Equivalent to {@code this.dot(Vector3.I);}
	 * @return The distance from the origin along the x-axis.
	 * @see getX, I, dot
	 */
	public double x() {
		return x;
	}

	/**
	 * Gets the distance from the origin along the y-axis.
	 * Equivalent to {@code this.dot(Vector3.J);}
	 * @return The distance from the origin along the y-axis.
	 * @see getY, J, dot
	 */
	public double y() {
		return y;
	}

	/**
	 * Gets the distance from the origin along the z-axis.
	 * Equivalent to {@code this.dot(Vector3.K);}
	 * @return The distance from the origin along the z-axis.
	 * @see getZ, K, dot
	 */
	public double z() {
		return z;
	}

	/**
	 * Gets the distance from the origin along the x-axis.
	 * Equivalent to {@code this.dot(Vector3.I);}
	 * @return The distance from the origin along the x-axis.
	 * @see x, I, dot
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the distance from the origin along the y-axis.
	 * Equivalent to {@code this.dot(Vector3.J);}
	 * @return The distance from the origin along the y-axis.
	 * @see y, J, dot
	 */
	public double getY() {
		return y;
	}

	/**
	 * Gets the distance from the origin along the z-axis.
	 * Equivalent to {@code this.dot(Vector3.K);}
	 * @return The distance from the origin along the z-axis.
	 * @see z, K, dot
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Computes the magnitude of the vector.
	 * @return The magnitude of the vector.
	 */
	public double length() {
		return Math.sqrt(dot(this));
	}

	/**
	 * Returns the opposite of this vector.
	 * @return The opposite of this vector.
	 */
	public Vector3 opposite() {
		return new Vector3(-x, -y, -z);
	}

	/**
	 * Computes the sum of two vectors.
	 * @param v The vector to add to this vector.
	 * @return The sum of this vector and v.
	 */
	public Vector3 plus(Vector3 v) {
		return new Vector3(x + v.x, y + v.y, z + v.z);
	}

	/**
	 * Computes the difference between two vectors.
	 * @param v The vector to subtract from this vector.
	 * @return The difference between this vector and v.
	 */
	public Vector3 minus(Vector3 v) {
		return new Vector3(x - v.x, y - v.y, z - v.z);
	}

	/**
	 * Computes this vector scaled by a constant factor.
	 * @param c The factor to scale this vector by.
	 * @return This vector scaled by c.
	 */
	public Vector3 times(double c) {
		return new Vector3(c * x, c * y, c * z);
	}

	/**
	 * Computes the dot product between two vectors.
	 * @param v The vector to compute the dot product of with this vector.
	 * @return The dot product of this vector and v.
	 */
	public double dot(Vector3 v) {
		return (x * v.x) + (y * v.y) + (z * v.z);
	}

	/**
	 * Computes the cross product of two vectors.
	 * @param v The vector to compute the cross product of with this vector.
	 * @return The cross product of this vector and v.
	 */
	public Vector3 cross(Vector3 v) {
		return new Vector3(
					(y * v.z) - (z * v.y),
					(z * v.x) - (x * v.z),
					(x * v.y) - (y * v.x)
				);
	}

	/**
	 * Computes the unit vector in the same direction as this vector.
	 * @return The unit vector in the same direction as this vector.
	 */
	public Vector3 unit() {
		return this.times(1.0 / length());
	}

	/**
	 * The zero vector (represents the vector between two identical points).
	 */
	public static final Vector3 ZERO = new Vector3(0.0, 0.0, 0.0);

	/**
	 * The unit vector along the x-axis.
	 */
	public static final Vector3 I = new Vector3(1.0, 0.0, 0.0);

	/**
	 * The unit vector along the y-axis.
	 */
	public static final Vector3 J = new Vector3(0.0, 1.0, 0.0);

	/**
	 * The unit vector along the z-axis.
	 */
	public static final Vector3 K = new Vector3(0.0, 0.0, 1.0);

	/**
	 * The lengths of the vector along each axis.
	 */
	private final double x, y, z;

}
