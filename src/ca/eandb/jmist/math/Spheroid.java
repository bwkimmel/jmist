/**
 * 
 */
package ca.eandb.jmist.math;

import java.io.Serializable;

import ca.eandb.util.UnimplementedException;

/**
 * @author brad
 *
 */
public final class Spheroid implements Serializable {

	/** Serialization version ID. */
	private static final long serialVersionUID = -3183436145247636550L;
	
	private final double a;
	private final double c;
	private final Point3 center;
	private final Basis3 basis;
	
	public static final Spheroid UNIVERSE = new Spheroid(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
	
	public static final Spheroid EMPTY = new Spheroid(0.0, 0.0);
	
	public Spheroid(double a, double c, Point3 center, Basis3 basis) {
		if (a < 0.0) {
			throw new IllegalArgumentException("a must be non-negative.");
		}
		if (c < 0.0) {
			throw new IllegalArgumentException("b must be non-negative.");
		}
		this.a = a;
		this.c = c;
		this.center = center;
		this.basis = basis;
	}
	
	public Spheroid(double a, double c, Point3 center, Vector3 axis) {
		this(a, c, center, Basis3.fromW(axis));
	}
	
	public Spheroid(double a, double c) {
		this(a, c, Point3.ORIGIN, Basis3.STANDARD);
	}
	
	public Point3 center() {
		return center;
	}
	
	public Basis3 basis() {
		return basis;
	}
	
	public double a() {
		return a;
	}
	
	public double c() {
		return c;
	}
	
	public Vector3 axis() {
		return basis.w();
	}
	
	public double semimajor() {
		return Math.max(a, c);
	}
	
	public double semiminor() {
		return Math.min(a, c);
	}
	
	public double major() {
		return 2.0 * semimajor();
	}
	
	public double minor() {
		return 2.0 * semiminor();
	}
	
	public double ellipticity() {
		double smaj = semimajor();
		double smin = semiminor();
		return Math.sqrt((smaj * smaj - smin * smin) / (smaj * smaj));
	}
	
	public boolean isEmpty() {
		return a == 0.0 || c == 0.0;
	}
	
	public boolean isProlate() {
		return a < c;
	}
	
	public boolean isOblate() {
		return a > c;
	}
	
	public double volume() {
		return (4.0 / 3.0) * Math.PI * a * a * c; 
	}
	
	public double surfaceArea() {
		double e = ellipticity();
		if (e < MathUtil.EPSILON) { // near sphere shaped
			double r = (a + a + c) / 3.0;
			return 4.0 * Math.PI * r * r;
		} else if (isOblate()) {
			if (1.0 - e < MathUtil.EPSILON) { // near disc shaped
				return 2.0 * Math.PI * a * a;
			} else {
				return 2.0 * Math.PI * a * a + Math.PI * (c * c / e) * Math.log((1.0 + e) / (1.0 - e));
			}
		} else { // prolate
			return 2.0 * Math.PI * a *  a + 2.0 * Math.PI * (a * c / e) * Math.asin(e);
		}
	}
	
	public double characterize(Point3 p) {
		Vector3 r = center.vectorTo(p);
		double x = basis.u().dot(r);
		double y = basis.v().dot(r);
		double z = basis.w().dot(r);
		return (x * x + y * y) / (a * a) + (z * z) / (c * c) - 1.0;
	}
	
	public boolean contains(Point3 p) {
		return characterize(p) < 0.0;
	}
	
	public Vector3 gradient(Point3 p) {
		Vector3 r = center.vectorTo(p);

		double ru = 2.0 * basis.u().dot(r) / (a * a);
		double rv = 2.0 * basis.v().dot(r) / (a * a);
		double rw = 2.0 * basis.w().dot(r) / (c * c);

		return new Vector3(
			ru * basis.u().x() + rv * basis.v().x() + rw * basis.w().x(),
			ru * basis.u().y() + rv * basis.v().y() + rw * basis.w().y(),
			ru * basis.u().z() + rv * basis.v().z() + rw * basis.w().z()
		);
	}
	
	private Ray3 localize(Ray3 ray) {
		Vector3 r = ray.origin().vectorFrom(center);
		Vector3	d = ray.direction();

		double	f = a / c;
		
		Vector3 u = basis.u();
		Vector3 v = basis.v();
		Vector3 w = basis.w();
		double ru = r.dot(u);
		double rv = r.dot(v);
		double rw = r.dot(w);
		double du = d.dot(u);
		double dv = d.dot(v);
		double dw = d.dot(w);
		
		r = basis.toStandard(ru, rv, f * rw);
		d = basis.toStandard(du, dv, f * dw);
		
		return new Ray3(center.plus(r), d);
	}
	
	public boolean intersects(Ray3 ray) {
		
		/* source:	A.S. Glassner, Ed.,
		 *			"An Introduction to Ray Tracing",
		 *			Morgan Kaufmann Publishers, Inc., San Francisco, CA, 2002
		 *			Section 2.2
		 */

		if (isEmpty() || ray == null) return false;

		Ray3	lray = localize(ray);

		Vector3	oc = lray.origin().vectorTo(center);

		double	r2 = a * a;
		double	L2oc = oc.squaredLength();

		if (L2oc < r2) return true;

		double	tca = lray.direction().dot(oc);

		if (tca < 0.0) return false;

		return lray.pointAt(tca).squaredDistanceTo(center) < r2;
	}
	
	public Interval intersect(Ray3 ray) {
		/* source:	A.S. Glassner, Ed.,
		 *			"An Introduction to Ray Tracing",
		 *			Morgan Kaufmann Publishers, Inc., San Francisco, CA, 2002
		 *			Section 2.2
		 */

		if (isEmpty() || ray == null) return Interval.EMPTY;

		Ray3	lray = localize(ray);

		double	dlen = lray.direction().length();
		lray = new Ray3(ray.origin(), ray.direction().divide(dlen));

		double	r2 = a * a;
		Vector3	oc = lray.origin().vectorTo(center);

		double	L2oc = oc.squaredLength();
		boolean	startInside = false;

		if (L2oc < r2)
			startInside = true;

		// distance along ray to point on ray closest to center of sphere (equation (A10))
		double	tca = lray.direction().dot(oc);

		// if ray starts outside the sphere and points away from
		// the center of the sphere, then the ray does not hit
		// the sphere
		if (!startInside && (tca < 0.0))
			return Interval.EMPTY;

		// compute half chord distance squared (equation (A13))
		double	t2hc = r2 - L2oc + (tca * tca);

		if (t2hc < 0.0)
			return Interval.EMPTY;

		double	thc = Math.sqrt(t2hc);

		// compute interval (equation (A14))
		double	tmin = startInside ? 0.0 : (tca - thc) / dlen;
		double	tmax = (tca + thc) / dlen;

		assert((startInside || Math.abs(characterize(ray.pointAt(tmin))) < MathUtil.EPSILON) && Math.abs(characterize(ray.pointAt(tmax))) < MathUtil.EPSILON);

		return new Interval(tmin, tmax);

	}
	
	public boolean contains(Spheroid other) {
		throw new UnimplementedException();
	}
	
	public boolean intersects(Spheroid other) {
		Matrix4 A = getMatrixRepresentation();
		Matrix4 B = other.getMatrixRepresentation();
		Matrix4 AdivB = A.divide(B);
		double[] lambda = AdivB.eigenvalues();
		
		int count = 0;
		for (int i = 0; i < lambda.length; i++) {
			if (lambda[i] < 0.0) {
				count++;
			}
			if (count >= 2) {
				return false;
			}	
		}
		return true;
	}
		
	/**
	 * Gets the <code>Matrix4</code> representation of this
	 * <code>Spheroid</code>.  The <code>Matrix4</code> returned,
	 * <code><b>A</b></code>, will satisfy:
	 * 
	 * <ul>
	 * 		<li>
	 * 			<code><b>x<sup>t</sup>Ax</b> &lt; 0</code> whenever
	 *			<code><b>x</b> = (x y z 1)<sup>t</sup></code> lies inside this
	 *			<code>Spheroid</code>,
	 *		<li>
	 * 			<code><b>x<sup>t</sup>Ax</b> &gt; 0</code> whenever
	 *			<code><b>x</b></code> lies outside this <code>Spheroid</code>,
	 *			and
	 *		<li>
	 * 			<code><b>x<sup>t</sup>Ax</b> = 0</code> whenever
	 *			<code><b>x</b></code> lies on the surface of this
	 *			<code>Spheroid</code>.
	 * </ul>	
	 * @return The <code>Matrix4</code> representation of this
	 * 		<code>Spheroid</code>.
	 */
	public Matrix4 getMatrixRepresentation() {
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
		
		double a2i = 1.0 / (a * a);
		double c2i = 1.0 / (c * c); 
		Matrix4 S = new Matrix4(
				a2i, 0.0, 0.0, 0.0,
				0.0, a2i, 0.0, 0.0,
				0.0, 0.0, c2i, 0.0,
				0.0, 0.0, 0.0, -1.0);
		
		return TtBt.times(S).times(BT);		
	}
	
}
