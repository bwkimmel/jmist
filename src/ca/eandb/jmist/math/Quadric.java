/**
 *
 */
package ca.eandb.jmist.math;

/**
 * @author Brad
 *
 */
public final class Quadric {

	private final Matrix4 Q;

	private Quadric(Matrix4 Q) {
		this.Q = Q;
	}

	public Matrix4 getMatrixRepresentation() {
		return Q;
	}

	public boolean contains(Point3 p) {
		return characterize(p) < 0.0;
	}

	public boolean intersects(Ray3 ray) {
		double[] x = intersect(ray);
		for (int i = 0; i < x.length; i++) {
			if (x[i] > 0.0) {
				return true;
			}
		}
		return false;
	}

	public double[] intersect(Ray3 ray) {
		Vector4 x0 = ray.origin().toVector4();
		Vector4 x1 = ray.direction().toVector4();

		Polynomial f = new Polynomial(
				Q.dot(x0, x0),
				Q.dot(x1, x0) * 2.0,
				Q.dot(x1, x1));

		return f.roots();
	}

	public double characterize(Vector4 v) {
		return Q.dot(v, v);
	}

	public double characterize(Point3 p) {
		return characterize(p.toVector4());
	}

	public Vector4 gradient(Vector4 v) {
		return Q.times(v).times(2.0);
	}

	public Vector3 gradient(Point3 p) {
		Vector4 dv = gradient(p.toVector4());
		return new Vector3(dv.x(), dv.y(), dv.z());
	}

	public Vector3 normalAt(Point3 p) {
		Vector4 hdv = Q.times(p.toVector4());
		return Vector3.unit(hdv.x(), hdv.y(), hdv.z());
	}

	private static Matrix4 getMatrixForSphere(double radius) {
		double r2i = 1.0 / (radius * radius);
		return new Matrix4(
				r2i,  0.0,  0.0,  0.0,
				0.0,  r2i,  0.0,  0.0,
				0.0,  0.0,  r2i,  0.0,
				0.0,  0.0,  0.0, -1.0);
	}

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

	private static Matrix4 getMatrixForEllipticParaboloid(double a, double b) {
		double a2i = 1.0 / (a * a);
		double b2i = 1.0 / (b * b);
		return new Matrix4(
				a2i,  0.0,  0.0,  0.0,
				0.0,  b2i,  0.0,  0.0,
				0.0,  0.0,  0.0, -0.5,
				0.0,  0.0, -0.5,  0.0);
	}

	private static Matrix4 getMatrixForHyperbolicParaboloid(double a, double b) {
		double a2i = 1.0 / (a * a);
		double b2i = 1.0 / (b * b);
		return new Matrix4(
				a2i,  0.0,  0.0,  0.0,
				0.0, -b2i,  0.0,  0.0,
				0.0,  0.0,  0.0, -0.5,
				0.0,  0.0, -0.5,  0.0);
	}

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

	private static Matrix4 getMatrixForEllipticCylinder(double a, double b) {
		double a2i = 1.0 / (a * a);
		double b2i = 1.0 / (b * b);
		return new Matrix4(
				a2i,  0.0,  0.0,  0.0,
				0.0,  b2i,  0.0,  0.0,
				0.0,  0.0,  0.0,  0.0,
				0.0,  0.0,  0.0, -1.0);
	}

	private static Matrix4 getMatrixForHyperbolicCylinder(double a, double b) {
		double a2i = 1.0 / (a * a);
		double b2i = 1.0 / (b * b);
		return new Matrix4(
				a2i,  0.0,  0.0,  0.0,
				0.0, -b2i,  0.0,  0.0,
				0.0,  0.0,  0.0,  0.0,
				0.0,  0.0,  0.0, -1.0);
	}

	private static Matrix4 getMatrixForParabolicCylinder(double a) {
		return new Matrix4(
				1.0,  0.0,  0.0,  0.0,
				0.0,  0.0,  0.0,  a  ,
				0.0,  0.0,  0.0,  0.0,
				0.0,  a  ,  0.0,  0.0);
	}

	private static Matrix4 getMatrixForParallelPlanes(double a) {
		return new Matrix4(
				1.0,  0.0,  0.0,  0.0,
				0.0,  0.0,  0.0,  0.0,
				0.0,  0.0,  0.0,  0.0,
				0.0,  0.0,  0.0, -a*a);
	}

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

	public static Quadric createGeneralQuadric(Matrix4 Q) {
		return new Quadric(Q.hermitian());
	}

	public static Quadric createSphere(double radius) {
		return new Quadric(getMatrixForSphere(radius));
	}

	public static Quadric createEllipsoid(double a, double b, double c) {
		return new Quadric(getMatrixForEllipsoid(a, b, c));
	}

	public static Quadric createEllipticParaboloid(double a, double b) {
		return new Quadric(getMatrixForEllipticParaboloid(a, b));
	}

	public static Quadric createHyperbolicParaboloid(double a, double b) {
		return new Quadric(getMatrixForHyperbolicParaboloid(a, b));
	}

	public static Quadric createHyperboloidOfOneSheet(double a, double b, double c) {
		return new Quadric(getMatrixForHyperboloidOfOneSheet(a, b, c));
	}

	public static Quadric createHyperboloidOfTwoSheets(double a, double b, double c) {
		return new Quadric(getMatrixForHyperboloidOfTwoSheets(a, b, c));
	}

	public static Quadric createEllipticCone(double a, double b, double c) {
		return new Quadric(getMatrixForEllipticCone(a, b, c));
	}

	public static Quadric createEllipticCylinder(double a, double b) {
		return new Quadric(getMatrixForEllipticCylinder(a, b));
	}

	public static Quadric createHyperbolicCylinder(double a, double b) {
		return new Quadric(getMatrixForHyperbolicCylinder(a, b));
	}

	public static Quadric createParabolicCylinder(double a) {
		return new Quadric(getMatrixForParabolicCylinder(a));
	}

	public static Quadric createParallelPlanes(double a) {
		return new Quadric(getMatrixForParallelPlanes(a));
	}

	public static Quadric createSphere(double radius, Point3 center) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForSphere(radius), center, Basis3.STANDARD));
	}

	public static Quadric createEllipsoid(double a, double b, double c, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForEllipsoid(a, b, c), center, basis));
	}

	public static Quadric createEllipticParaboloid(double a, double b, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForEllipticParaboloid(a, b), center, basis));
	}

	public static Quadric createHyperbolicParaboloid(double a, double b, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForHyperbolicParaboloid(a, b), center, basis));
	}

	public static Quadric createHyperboloidOfOneSheet(double a, double b, double c, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForHyperboloidOfOneSheet(a, b, c), center, basis));
	}

	public static Quadric createHyperboloidOfTwoSheets(double a, double b, double c, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForHyperboloidOfTwoSheets(a, b, c), center, basis));
	}

	public static Quadric createEllipticCone(double a, double b, double c, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForEllipticCone(a, b, c), center, basis));
	}

	public static Quadric createEllipticCylinder(double a, double b, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForEllipticCylinder(a, b), center, basis));
	}

	public static Quadric createHyperbolicCylinder(double a, double b, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForHyperbolicCylinder(a, b), center, basis));
	}

	public static Quadric createParabolicCylinder(double a, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForParabolicCylinder(a), center, basis));
	}

	public static Quadric createParallelPlanes(double a, Point3 center, Basis3 basis) {
		return new Quadric(getMatrixForTransformedQuadric(getMatrixForParallelPlanes(a), center, basis));
	}

}
