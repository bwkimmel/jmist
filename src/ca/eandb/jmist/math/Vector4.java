/**
 *
 */
package ca.eandb.jmist.math;


/**
 * The difference between two points in four dimensional space.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class Vector4 extends Tuple4 {

	/** Serialization version ID. */
	private static final long serialVersionUID = -2237191308578197533L;

	/**
	 * Initializes the components for the vector.
	 * @param x The length of the vector along the x axis.
	 * @param y The length of the vector along the y axis.
	 * @param z The length of the vector along the z axis.
	 * @param w The length of the vector along the w axis.
	 */
	public Vector4(double x, double y, double z, double w) {
		super(x, y, z, w);
	}

	/**
	 * Creates a homogenized 3D point.
	 * @param v The <code>Vector3</code> from which to create the homogenized
	 * 		point.
	 */
	public Vector4(Vector3 v) {
		this(v.x(), v.y(), v.z(), 0.0);
	}

	/**
	 * Creates a homogenized 3D point.
	 * @param v The <code>Point3</code> from which to create the homogenized
	 * 		point.
	 */
	public Vector4(Point3 p) {
		this(p.x(), p.y(), p.z(), 1.0);
	}

	/**
	 * Gets the distance from the origin along the x-axis.
	 * Equivalent to {@code this.dot(Vector3.I);}
	 * @return The distance from the origin along the x-axis.
	 * @see {@link #I}, {@link #dot(Vector4)}.
	 */
	public double x() {
		return x;
	}

	/**
	 * Gets the distance from the origin along the y-axis.
	 * Equivalent to {@code this.dot(Vector3.J);}
	 * @return The distance from the origin along the y-axis.
	 * @see {@link #J}, {@link #dot(Vector4)}.
	 */
	public double y() {
		return y;
	}

	/**
	 * Gets the distance from the origin along the z-axis.
	 * Equivalent to {@code this.dot(Vector3.K);}
	 * @return The distance from the origin along the z-axis.
	 * @see {@link #K}, {@link #dot(Vector4)}.
	 */
	public double z() {
		return z;
	}

	/**
	 * Gets the distance from the origin along the w-axis.
	 * Equivalent to {@code this.dot(Vector3.L);}
	 * @return The distance from the origin along the w-axis.
	 * @see {@link #L}, {@link #dot(Vector4)}.
	 */
	public double w() {
		return w;
	}

	/**
	 * Computes the magnitude of the vector.
	 * @return The magnitude of the vector.
	 */
	public double length() {
		return Math.sqrt(dot(this));
	}

	/**
	 * Computes the square of the magnitude of this vector.
	 * @return The square of the magnitude of this vector.
	 */
	public double squaredLength() {
		return dot(this);
	}

	/**
	 * Returns the opposite of this vector.
	 * @return The opposite of this vector.
	 */
	public Vector4 opposite() {
		return new Vector4(-x, -y, -z, -w);
	}

	/**
	 * Computes the sum of two vectors.
	 * @param v The vector to add to this vector.
	 * @return The sum of this vector and v.
	 */
	public Vector4 plus(Vector4 v) {
		return new Vector4(x + v.x, y + v.y, z + v.z, w + v.w);
	}

	/**
	 * Computes the difference between two vectors.
	 * @param v The vector to subtract from this vector.
	 * @return The difference between this vector and v.
	 */
	public Vector4 minus(Vector4 v) {
		return new Vector4(x - v.x, y - v.y, z - v.z, w - v.w);
	}

	/**
	 * Computes this vector scaled by a constant factor.
	 * @param c The factor to scale this vector by.
	 * @return This vector scaled by c.
	 */
	public Vector4 times(double c) {
		return new Vector4(c * x, c * y, c * z, c * w);
	}

	/**
	 * Computes this vector scaled by the reciprocal of
	 * a constant factor.
	 * Equivalent to {@code this.times(1.0 / c).}
	 * @param c The factor to divide this vector by.
	 * @return The vector scaled by 1.0 / c.
	 * @see {@link #times(double)}.
	 */
	public Vector4 divide(double c) {
		return new Vector4(x / c, y / c, z / c, w / c);
	}

	/**
	 * Computes the dot product between two vectors.
	 * @param v The vector to compute the dot product of with this vector.
	 * @return The dot product of this vector and v.
	 */
	public double dot(Vector4 v) {
		return (x * v.x) + (y * v.y) + (z * v.z) + (w * v.w);
	}

	/**
	 * Computes the cross product of three vectors.
	 * @param a The first vector from which to compute the cross product.
	 * @param b The second vector from which to compute the cross product.
	 * @param c The third vector from which to compute the cross product.
	 * @return The cross product of the three vectors.
	 */
	public static Vector4 cross(Vector4 a, Vector4 b, Vector4 c) {
		return new Vector4(
				 a.y*(b.z*c.w - c.z*b.w) - a.z*(b.y*c.w - c.y*b.w) + a.w*(b.y*c.z - c.y*b.z),
				-a.x*(b.z*c.w - c.z*b.w) + a.z*(b.x*c.w - c.x*b.w) - a.w*(b.x*c.z - c.x*b.z),
				 a.x*(b.y*c.w - c.y*b.w) - a.y*(b.x*c.w - c.x*b.w) + a.w*(b.x*c.y - c.x*b.y),
				-a.x*(b.y*c.z - c.y*b.z) + a.y*(b.x*c.z - c.x*b.z) - a.z*(b.x*c.y - c.x*b.y));
	}

	/**
	 * Computes the unit vector in the same direction as this vector.
	 * @return The unit vector in the same direction as this vector.
	 */
	public Vector4 unit() {
		return this.times(1.0 / length());
	}

	/**
	 * Returns a new unit vector in the same direction as
	 * the vector with the specified components.
	 * Equivalent to {@code new Vector4(x, y, z, w).unit()}.
	 * @param x The magnitude of the vector along the x-axis.
	 * @param y The magnitude of the vector along the y-axis.
	 * @param z The magnitude of the vector along the z-axis.
	 * @param w The magnitude of the vector along the w-axis.
	 * @return A unit vector in the same direction as the
	 * 		vector with the indicated components.
	 * @see {@link #unit()}.
	 */
	public static Vector4 unit(double x, double y, double z, double w) {
		double r = Math.sqrt(x * x + y * y + z * z + w * w);
		return new Vector4(x / r, y / r, z / r, w / r);
	}

	/**
	 * The zero vector (represents the vector between two identical points).
	 */
	public static final Vector4 ZERO = new Vector4(0.0, 0.0, 0.0, 0.0);

	/**
	 * The unit vector along the x-axis.
	 */
	public static final Vector4 I = new Vector4(1.0, 0.0, 0.0, 0.0);

	/**
	 * The unit vector along the y-axis.
	 */
	public static final Vector4 J = new Vector4(0.0, 1.0, 0.0, 0.0);

	/**
	 * The unit vector along the z-axis.
	 */
	public static final Vector4 K = new Vector4(0.0, 0.0, 1.0, 0.0);

	/**
	 * The unit vector along the w-axis.
	 */
	public static final Vector4 L = new Vector4(0.0, 0.0, 0.0, 1.0);

	/**
	 * The unit vector along the negative x-axis.
	 */
	public static final Vector4 NEGATIVE_I = new Vector4(-1.0, 0.0, 0.0, 0.0);

	/**
	 * The unit vector along the negative y-axis.
	 */
	public static final Vector4 NEGATIVE_J = new Vector4(0.0, -1.0, 0.0, 0.0);

	/**
	 * The unit vector along the negative z-axis.
	 */
	public static final Vector4 NEGATIVE_K = new Vector4(0.0, 0.0, -1.0, 0.0);

	/**
	 * The unit vector along the negative w-axis.
	 */
	public static final Vector4 NEGATIVE_L = new Vector4(0.0, 0.0, 0.0, -1.0);

}
