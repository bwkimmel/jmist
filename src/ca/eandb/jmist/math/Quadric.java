/**
 *
 */
package ca.eandb.jmist.math;

import java.io.Serializable;

/**
 * Represents a quadric surface, i.e., the solution to
 * <code><b>x<sup>t</sup>Qx</b> = 0</code>, where
 * <code><b>x</b> = (x y z 1)<b><sup>t</sup></b></code> and
 * <code><b>Q</b></code> is the 4x4 symmetric matrix characterizing the
 * surface.
 * @see <a href="http://en.wikipedia.org/wiki/Quadric">Quadric (wikipedia)</a>
 * @see <a href="http://mathworld.wolfram.com/QuadraticSurface.html">Quadratic Surface (MathWorld)</a>
 * @author Brad Kimmel
 */
public final class Quadric implements Serializable {

	/** Serialization version ID. */
	private static final long serialVersionUID = 6807159479632893773L;

	/**
	 * The symmetric <code>Matrix4</code> that characterizes this
	 * <code>Quadric</code> surface
	 */
	private final Matrix4 Q;

	/**
	 * Creates a new <code>Quadric</code>.
	 * @param Q The <code>Matrix4</code> that characterizes the quadric
	 * 		surface.  This matrix must be symmetric.
	 */
	private Quadric(Matrix4 Q) {
		this.Q = Q;
	}

	/**
	 * Gets the symmetric <code>Matrix4</code>, <code><b>Q</b></code>, having
	 * the property that <code><b>x</b> = (x y z 1)<b><sup>t</sup></b></code>
	 * lies on the surface of this <code>Quadric</code> if and only if
	 * <code><b>x<sup>t</sup>Qx</b> = 0</code>.
	 * @return The <code>Matrix4</code> that defines this <code>Quadric</code>.
	 */
	public Matrix4 getMatrixRepresentation() {
		return Q;
	}

	/**
	 * Determines if the specified <code>Point3</code> lies "inside" this
	 * <code>Quadric</code>.  That is, if
	 * <code><b>p<sup>t</sup>Qp</b> &lt; 0</code>, where <code><b>Q</b></code>
	 * is the characteristic <code>Matrix4</code>.
	 * @param p The <code>Point3</code> to test for containment.
	 * @return A value indicating if <code>p</code> is inside this
	 * 		<code>Quadric</code>.
	 * @see #getMatrixRepresentation()
	 */
	public boolean contains(Point3 p) {
		return characterize(p) < 0.0;
	}

	/**
	 * Determines if the specified <code>Ray3</code> intersects this
	 * <code>Quadric</code>.
	 * @param ray The <code>Ray3</code> to test for intersection.
	 * @return A value indicating whether <code>ray</code> intersects this
	 * 		<code>Quadric</code>.
	 */
	public boolean intersects(Ray3 ray) {
		double[] x = intersect(ray);
		for (int i = 0; i < x.length; i++) {
			if (x[i] > 0.0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines the points along the specified ray that intersect with this
	 * <code>Quadric</code>.
	 * @param ray The <code>Ray3</code> to compute the intersection with.
	 * @return An array containing up to two values, <code>t</code>, satisfying
	 * 		<code>this.characterize(ray.pointAt(t)) == 0</code>.  These values
	 * 		may include negative values.
	 * @see Ray3#pointAt(double)
	 * @see #characterize(Point3)
	 */
	public double[] intersect(Ray3 ray) {
		Vector4 x0 = ray.origin().toVector4();
		Vector4 x1 = ray.direction().toVector4();

		Polynomial f = new Polynomial(
				Q.dot(x0, x0),
				Q.dot(x1, x0) * 2.0,
				Q.dot(x1, x1));

		return f.roots();
	}

	/**
	 * Evaluates the characteristic function
	 * <code>f(v) = v<sup>t</sup><b>Q</b>v</code>, which has the following
	 * properties:
	 *
	 * <ul>
	 *   <li><code>f(v) = 0</code> if <code>v</code> is on the surface,
	 *   <li><code>f(v) &lt; 0</code> if <code>v</code> is inside the surface,
	 *   		and
	 *   <li><code>f(v) &gt; 0</code> if <code>v</code> is outside the surface.
	 * </ul>
	 * @param v The <code>Vector4</code> at which to evaluate the
	 * 		characteristic function.
	 * @return A value characterizing the <code>v</code> with respect to this
	 * 		<code>Quadric</code>.
	 */
	public double characterize(Vector4 v) {
		return Q.dot(v, v);
	}

	/**
	 * Evaluates the characteristic function
	 * <code>f(p) = p<sup>t</sup><b>Q</b>p</code>, where
	 * <code>p = (x y z 1)<sup>t</sup></code>.  The characteristic function has
	 * the following properties:
	 *
	 * <ul>
	 *   <li><code>f(p) = 0</code> if <code>p</code> is on the surface,
	 *   <li><code>f(p) &lt; 0</code> if <code>p</code> is inside the surface,
	 *   		and
	 *   <li><code>f(p) &gt; 0</code> if <code>p</code> is outside the surface.
	 * </ul>
	 *
	 * Equivalent to <code>this.characteristic(p.toVector4())</code>.
	 * @param p The <code>Point3</code> at which to evaluate the
	 * 		characteristic function.
	 * @return A value characterizing the <code>p</code> with respect to this
	 * 		<code>Quadric</code>.
	 * @see #characterize(Vector4)
	 * @see Point3#toVector4()
	 */
	public double characterize(Point3 p) {
		return characterize(p.toVector4());
	}

	/**
	 * Evaluates the gradient of the characteristic function.  If
	 * <code>v</code> lies on the surface of this <code>Quadric</code>, then
	 * the gradient is normal to the surface.
	 * @param v The <code>Vector4</code> at which the gradient is to be
	 * 		evaluated.
	 * @return The <code>Vector4</code> of the derivatives with respect to each
	 * 		component.
	 * @see #characterize(Vector4)
	 */
	public Vector4 gradient(Vector4 v) {
		return Q.times(v).times(2.0);
	}

	/**
	 * Evaluates the gradient of the characteristic function.  If
	 * <code>p</code> lies on the surface of this <code>Quadric</code>, then
	 * the gradient is normal to the surface.
	 * @param p The <code>Point3</code>, <code>p = (x y z 1)<sup>t</sup></code>,
	 * 		at which the gradient is to be evaluated.
	 * @return The <code>Vector3</code> of the derivatives with respect to each
	 * 		component.  The fourth component is ignored.
	 * @see #characterize(Vector4)
	 */
	public Vector3 gradient(Point3 p) {
		Vector4 dv = gradient(p.toVector4());
		return new Vector3(dv.x(), dv.y(), dv.z());
	}

	/**
	 * Determines the normal to this <code>Quadric</code> surface at the
	 * specified point.<br>
	 * <br>
	 * Equivalent to <code>this.gradient(p).unit()</code>.
	 * @param p The <code>Point3</code> on the surface at which to evaluate the
	 * 		normal.
	 * @return A <code>Vector3</code> of unit length that is normal to the
	 * 		surface at <code>p</code>.  The result is undefined if
	 * 		<code>p</code> does not lie on the surface.
	 */
	public Vector3 normalAt(Point3 p) {
		Vector4 hdv = Q.times(p.toVector4());
		return Vector3.unit(hdv.x(), hdv.y(), hdv.z());
	}

	/**
	 * Gets the characteristic matrix for a sphere centered at the origin.
	 * @param radius The radius of the sphere.
	 * @return The <code>Matrix4</code> characterizing the sphere.
	 */
	private static Matrix4 getMatrixForSphere(double radius) {
		double r2i = 1.0 / (radius * radius);
		return new Matrix4(
				r2i,  0.0,  0.0,  0.0,
				0.0,  r2i,  0.0,  0.0,
				0.0,  0.0,  r2i,  0.0,
				0.0,  0.0,  0.0, -1.0);
	}

	/**
	 * Gets the characteristic matrix for an ellipsoid centered at the origin
	 * and axes parallel to the coordinate axes.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @return The <code>Matrix4</code> characterizing the ellipsoid.
	 */
	private static Matrix4 getMatrixForEllipsoid(double a, double b, double c) {
		double a2i = 1.0 / (a * a);
		double b2i = 1.0 / (b * b);
		double c2i = 1.0 / (c * c);
		return new Matrix4(
				a2i,  0.0,  0.0,  0.0,
				0.0,  b2i,  0.0,  0.0,
				0.0,  0.0,  c2i,  0.0,
				0.0,  0.0,  0.0, -1.0);
	}

	/**
	 * Gets the characteristic matrix for an elliptic paraboloid with its base
	 * at the origin and opening around the positive z-axis.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @return The <code>Matrix4</code> characterizing the elliptic paraboloid.
	 */
	private static Matrix4 getMatrixForEllipticParaboloid(double a, double b) {
		double a2i = 1.0 / (a * a);
		double b2i = 1.0 / (b * b);
		return new Matrix4(
				a2i,  0.0,  0.0,  0.0,
				0.0,  b2i,  0.0,  0.0,
				0.0,  0.0,  0.0, -0.5,
				0.0,  0.0, -0.5,  0.0);
	}

	/**
	 * Gets the characteristic matrix for a hyperbolic paraboloid with its
	 * base at the origin, opening upward in the x direction and downward in
	 * the y direction.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @return The <code>Matrix4</code> characterizing the hyperbolic
	 * 		paraboloid.
	 */
	private static Matrix4 getMatrixForHyperbolicParaboloid(double a, double b) {
		double a2i = 1.0 / (a * a);
		double b2i = 1.0 / (b * b);
		return new Matrix4(
				a2i,  0.0,  0.0,  0.0,
				0.0, -b2i,  0.0,  0.0,
				0.0,  0.0,  0.0, -0.5,
				0.0,  0.0, -0.5,  0.0);
	}

	/**
	 * Gets the characteristic matrix for a hyperboloid of one sheet centered
	 * at the origin and with its primary axis parallel to the z-axis.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @return The <code>Matrix4</code> characterizing the hyperboloid of one
	 * 		sheet.
	 */
	private static Matrix4 getMatrixForHyperboloidOfOneSheet(double a, double b, double c) {
		double a2i = 1.0 / (a * a);
		double b2i = 1.0 / (b * b);
		double c2i = 1.0 / (c * c);
		return new Matrix4(
				a2i,  0.0,  0.0,  0.0,
				0.0,  b2i,  0.0,  0.0,
				0.0,  0.0, -c2i,  0.0,
				0.0,  0.0,  0.0, -1.0);
	}

	/**
	 * Gets the characteristic matrix for a hyperboloid of two sheets centered
	 * at the origin and with its primary axis parallel to the z-axis.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @return The <code>Matrix4</code> characterizing the hyperboloid of two
	 * 		sheet.
	 */
	private static Matrix4 getMatrixForHyperboloidOfTwoSheets(double a, double b, double c) {
		double a2i = 1.0 / (a * a);
		double b2i = 1.0 / (b * b);
		double c2i = 1.0 / (c * c);
		return new Matrix4(
				a2i,  0.0,  0.0,  0.0,
				0.0,  b2i,  0.0,  0.0,
				0.0,  0.0, -c2i,  0.0,
				0.0,  0.0,  0.0,  1.0);
	}

	/**
	 * Gets the characteristic matrix for an elliptic cone centered at the
	 * origin with its primary axis parallel to the z-axis.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @return The <code>Matrix4</code> characterizing the elliptic cone.
	 */
	private static Matrix4 getMatrixForEllipticCone(double a, double b, double c) {
		double a2i = 1.0 / (a * a);
		double b2i = 1.0 / (b * b);
		double c2i = 1.0 / (c * c);
		return new Matrix4(
				a2i,  0.0,  0.0,  0.0,
				0.0,  b2i,  0.0,  0.0,
				0.0,  0.0, -c2i,  0.0,
				0.0,  0.0,  0.0,  0.0);
	}

	/**
	 * Gets the characteristic matrix for an elliptic cylinder centered at the
	 * origin with its primary axis parallel to the z-axis.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @return The <code>Matrix4</code> characterizing the elliptic cylinder.
	 */
	private static Matrix4 getMatrixForEllipticCylinder(double a, double b) {
		double a2i = 1.0 / (a * a);
		double b2i = 1.0 / (b * b);
		return new Matrix4(
				a2i,  0.0,  0.0,  0.0,
				0.0,  b2i,  0.0,  0.0,
				0.0,  0.0,  0.0,  0.0,
				0.0,  0.0,  0.0, -1.0);
	}

	/**
	 * Gets the characteristic matrix for an hyperbolic cylinder centered at
	 * the origin with its primary axis parallel to the z-axis.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @return The <code>Matrix4</code> characterizing the hyperbolic cylinder.
	 */
	private static Matrix4 getMatrixForHyperbolicCylinder(double a, double b) {
		double a2i = 1.0 / (a * a);
		double b2i = 1.0 / (b * b);
		return new Matrix4(
				a2i,  0.0,  0.0,  0.0,
				0.0, -b2i,  0.0,  0.0,
				0.0,  0.0,  0.0,  0.0,
				0.0,  0.0,  0.0, -1.0);
	}

	/**
	 * Gets the characteristic matrix for an parabolic cylinder with its base
	 * at the origin, its primary axis parallel to the z-axis, and opening
	 * toward the positive y-axis.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @return The <code>Matrix4</code> characterizing the parabolic cylinder.
	 */
	private static Matrix4 getMatrixForParabolicCylinder(double a) {
		return new Matrix4(
				1.0,  0.0,  0.0,  0.0,
				0.0,  0.0,  0.0,  a  ,
				0.0,  0.0,  0.0,  0.0,
				0.0,  a  ,  0.0,  0.0);
	}

	/**
	 * Gets the characteristic matrix for pair of parallel planes normal to the
	 * x-axis, <code>x = &#177;a</code>.
	 * @param a The distance between each plane and the origin.
	 * @return The <code>Matrix4</code> characterizing the parallel planes.
	 */
	private static Matrix4 getMatrixForParallelPlanes(double a) {
		return new Matrix4(
				1.0,  0.0,  0.0,  0.0,
				0.0,  0.0,  0.0,  0.0,
				0.0,  0.0,  0.0,  0.0,
				0.0,  0.0,  0.0, -a*a);
	}

	/**
	 * Gets the characteristic matrix for a transformed quadric surface.
	 * @param Q The <code>Matrix4</code> characterizing the quadric to be
	 * 		transformed.
	 * @param center The <code>Point3</code> indicating the center of the
	 * 		transformed quadric.
	 * @param basis The <code>Basis3</code> indicating the directions of the
	 * 		principal axes of the transformed quadric.
	 * @return The <code>Matrix4</code> characterizing the transformed quadric.
	 */
	private static Matrix4 getMatrixForTransformedQuadric(Matrix4 Q, Point3 center, Basis3 basis) {
		double Tx = -center.x();
		double Ty = -center.y();
		double Tz = -center.z();
		Matrix4 T = new Matrix4(
				1.0, 0.0, 0.0, Tx,
				0.0, 1.0, 0.0, Ty,
				0.0, 0.0, 1.0, Tz,
				0.0, 0.0, 0.0, 1.0);

		Vector3 u = basis.u();
		Vector3 v = basis.v();
		Vector3 w = basis.w();
		Matrix4 B = new Matrix4(
				u.x(), u.y(), u.z(), 0.0,
				v.x(), v.y(), v.z(), 0.0,
				w.x(), w.y(), w.z(), 0.0,
				0.0  , 0.0  , 0.0  , 1.0);

		Matrix4	BT = B.times(T);
		Matrix4 TtBt = BT.transposed();

		return TtBt.times(Q).times(BT);
	}

	/**
	 * Creates a general <code>Quadric</code> surface given by
	 * <code><b>x<sup>t</sup>Qx</b> = 0</code>, where
	 * <code><b>x</b> = (x y z 1)<sup>t</sup></code>.
	 * @param Q The <code>Matrix4</code> characterizing the quadric surface to
	 * 		create.
	 * @return The <code>Quadric</code> surface characterized by
	 * 		<code>Q</code>.
	 */
	public static Quadric createGeneralQuadric(Matrix4 Q) {
		return new Quadric(Q.hermitian());
	}

	/**
	 * Creates a <code>Quadric</code> representing a sphere centered at the
	 * origin.
	 * @param radius The radius of the sphere.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createSphere(double radius) {
		return new Quadric(getMatrixForSphere(radius));
	}

	/**
	 * Creates a <code>Quadric</code> representing an ellipsoid centered at the
	 * origin and with its principal axes parallel to the coordinate axes.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createEllipsoid(double a, double b, double c) {
		return new Quadric(getMatrixForEllipsoid(a, b, c));
	}

	/**
	 * Creates a <code>Quadric</code> representing an elliptic paraboloid
	 * centered at the origin and with its principal axes parallel to the
	 * coordinate axes.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createEllipticParaboloid(double a, double b) {
		return new Quadric(getMatrixForEllipticParaboloid(a, b));
	}

	/**
	 * Creates a <code>Quadric</code> representing a hyperbolic paraboloid
	 * centered at the origin and with its principal axes parallel to the
	 * coordinate axes.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createHyperbolicParaboloid(double a, double b) {
		return new Quadric(getMatrixForHyperbolicParaboloid(a, b));
	}

	/**
	 * Creates a <code>Quadric</code> representing a hyperboloic of one sheet
	 * centered at the origin and with its principal axes parallel to the
	 * coordinate axes.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createHyperboloidOfOneSheet(double a, double b, double c) {
		return new Quadric(getMatrixForHyperboloidOfOneSheet(a, b, c));
	}

	/**
	 * Creates a <code>Quadric</code> representing a hyperboloic of two sheets
	 * centered at the origin and with its principal axes parallel to the
	 * coordinate axes.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createHyperboloidOfTwoSheets(double a, double b, double c) {
		return new Quadric(getMatrixForHyperboloidOfTwoSheets(a, b, c));
	}

	/**
	 * Creates a <code>Quadric</code> representing an elliptic cone centered at
	 * the origin and with its principal axes parallel to the coordinate axes.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createEllipticCone(double a, double b, double c) {
		return new Quadric(getMatrixForEllipticCone(a, b, c));
	}

	/**
	 * Creates a <code>Quadric</code> representing a elliptic cylinder centered
	 * at the origin and with its principal axes parallel to the coordinate
	 * axes.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createEllipticCylinder(double a, double b) {
		return new Quadric(getMatrixForEllipticCylinder(a, b));
	}

	/**
	 * Creates a <code>Quadric</code> representing a hyperboloic cylinder
	 * centered at the origin and with its principal axes parallel to the
	 * coordinate axes.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createHyperbolicCylinder(double a, double b) {
		return new Quadric(getMatrixForHyperbolicCylinder(a, b));
	}

	/**
	 * Creates a <code>Quadric</code> representing a parabolic cylinder
	 * centered at the origin and with its principal axes parallel to the
	 * coordinate axes.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createParabolicCylinder(double a) {
		return new Quadric(getMatrixForParabolicCylinder(a));
	}

	/**
	 * Creates a <code>Quadric</code> representing a pair of parallel planes,
	 * <code>x = &#177;a</code>.
	 * @param a The distance between each plane and the origin.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createParallelPlanes(double a) {
		return new Quadric(getMatrixForParallelPlanes(a));
	}

	/**
	 * Creates a <code>Quadric</code> representing a sphere.
	 * @param radius The radius of the sphere.
	 * @param center The <code>Point3</code> indicating the center of the
	 * 		sphere.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createSphere(double radius, Point3 center) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForSphere(radius), center, Basis3.STANDARD));
	}

	/**
	 * Creates a <code>Quadric</code> representing an ellipsoid.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @param center The <code>Point3</code> indicating the center of the
	 * 		ellipsoid.
	 * @param basis The <code>Basis3</code> indicating the directions of the
	 * 		principal axes.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createEllipsoid(double a, double b, double c, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForEllipsoid(a, b, c), center, basis));
	}

	/**
	 * Creates a <code>Quadric</code> representing an elliptic paraboloid.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param center The <code>Point3</code> indicating the center of the
	 * 		elliptic paraboloid
	 * @param basis The <code>Basis3</code> indicating the directions of the
	 * 		principal axes.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createEllipticParaboloid(double a, double b, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForEllipticParaboloid(a, b), center, basis));
	}

	/**
	 * Creates a <code>Quadric</code> representing a hyperbolic paraboloid.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param center The <code>Point3</code> indicating the center of the
	 * 		hyperbolic paraboloid.
	 * @param basis The <code>Basis3</code> indicating the directions of the
	 * 		principal axes.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createHyperbolicParaboloid(double a, double b, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForHyperbolicParaboloid(a, b), center, basis));
	}

	/**
	 * Creates a <code>Quadric</code> representing a hyperboloid of one sheet.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @param center The <code>Point3</code> indicating the center of the
	 * 		hyperboloid of one sheet.
	 * @param basis The <code>Basis3</code> indicating the directions of the
	 * 		principal axes.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createHyperboloidOfOneSheet(double a, double b, double c, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForHyperboloidOfOneSheet(a, b, c), center, basis));
	}

	/**
	 * Creates a <code>Quadric</code> representing a hyperboloid of two sheets.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @param center The <code>Point3</code> indicating the center of the
	 * 		hyperboloid of two sheets.
	 * @param basis The <code>Basis3</code> indicating the directions of the
	 * 		principal axes.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createHyperboloidOfTwoSheets(double a, double b, double c, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForHyperboloidOfTwoSheets(a, b, c), center, basis));
	}

	/**
	 * Creates a <code>Quadric</code> representing an elliptic cone.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @param center The <code>Point3</code> indicating the center of the
	 * 		elliptic cone
	 * @param basis The <code>Basis3</code> indicating the directions of the
	 * 		principal axes.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createEllipticCone(double a, double b, double c, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForEllipticCone(a, b, c), center, basis));
	}

	/**
	 * Creates a <code>Quadric</code> representing an elliptic cylinder.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @param center The <code>Point3</code> indicating the center of the
	 * 		elliptic cylinder.
	 * @param basis The <code>Basis3</code> indicating the directions of the
	 * 		principal axes.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createEllipticCylinder(double a, double b, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForEllipticCylinder(a, b), center, basis));
	}

	/**
	 * Creates a <code>Quadric</code> representing a hyperbolic cylinder.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @param center The <code>Point3</code> indicating the center of the
	 * 		hyperbolic cylinder.
	 * @param basis The <code>Basis3</code> indicating the directions of the
	 * 		principal axes.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createHyperbolicCylinder(double a, double b, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForHyperbolicCylinder(a, b), center, basis));
	}

	/**
	 * Creates a <code>Quadric</code> representing a parabolic cylinder.
	 * @param a The length of the semi-principal axis in the x direction.
	 * @param b The length of the semi-principal axis in the y direction.
	 * @param c The length of the semi-principal axis in the z direction.
	 * @param center The <code>Point3</code> indicating the center of the
	 * 		parabolic cylinder.
	 * @param basis The <code>Basis3</code> indicating the directions of the
	 * 		principal axes.
	 * @return The new <code>Quadric</code>.
	 */
	public static Quadric createParabolicCylinder(double a, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForParabolicCylinder(a), center, basis));
	}

	/**
	 * Creates a <code>Quadric</code> representing a pair of parallel planes.
	 * @param a The distance between each plane and <code>center</code>.
	 * @param center A <code>Point3</code> half-way between the planes
	 * @param basis The <code>Basis3</code> indicating the directions of the
	 * 		principal axes.  The normal to the planes is given by
	 * 		<code>basis.u()</code>.
	 * @return The new <code>Quadric</code>.
	 * @see Basis3#u()
	 */
	public static Quadric createParallelPlanes(double a, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForParallelPlanes(a), center, basis));
	}

}
