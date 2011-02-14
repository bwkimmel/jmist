/**
 * 
 */
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Polynomial;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * Represents a partial torus primitive with its apex at the origin and
 * extending along the positive z-axis.
 * 
 * <p>This geometric primitive implements Pixar's
 * <a href="https://renderman.pixar.com/products/rispec/index.htm">RenderMan 
 * Interface Specification</a> for the <code>RiTorus</code> primitive,
 * with the exception that limit angles must be specified in radians,
 * rather than in degrees.</p>
 * 
 * <p>The torus is defined by the following equations:</p>
 * 
 * <ul>
 *   <li>&theta; = u &sdot; &theta;<sub>max</sub></li>
 *   <li>&phi; = &phi;<sub>min</sub> + v &sdot; (&phi;<sub>max</sub> - &phi;<sub>min</sub>)</li>
 *   <li>r = minorradius &sdot; cos(&phi;)</li>
 *   <li>z = minorradius &sdot; sin(&phi;)</li>
 *   <li>x = (majorradius + r) &sdot; cos(&theta;)</li>
 *   <li>z = (majorradius + r) &sdot; sin(&theta;)</li>
 * </ul>
 *  
 * @author Brad Kimmel
 */
public final class PartialTorusGeometry extends PrimitiveGeometry {

	/** Serialization version ID. */
	private static final long serialVersionUID = 1350060514791197971L;
	
	/** The major radius of the torus. */
	private final double majorradius;
	
	/** The minor radius of the torus. */
	private final double minorradius;
	
	/** The starting angle to sweep the torus about the circular axis. */
	private final double phimin;
	
	/** The ending angle to sweep the torus about the circular axis. */
	private final double phimax;
	
	/** The angle to sweep the torus about the z-axis. */
	private final double thetamax;
	
	/**
	 * Creates a new <code>PartialTorusGeometry</code>.
	 * @param majorradius The major radius of the torus.
	 * @param minorradius The minor radius of the torus.
	 * @param phimin The starting angle to sweep the torus about the circular
	 * 		axis.
	 * @param phimax The ending angle to sweep the torus about the circular
	 * 		axis.
	 * @param thetamax The angle to sweep the torus about the z-axis.
	 */
	public PartialTorusGeometry(double majorradius, double minorradius, double phimin, double phimax, double thetamax) {
		this.majorradius = majorradius;
		this.minorradius = minorradius;
		this.phimin = phimin;
		this.phimax = phimax;
		this.thetamax = thetamax;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		return new Sphere(Point3.ORIGIN, majorradius + minorradius);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
		for (int i = 0; i < 4; i++) {
			double theta = ((double) i) * (Math.PI / 2.0);
			if (theta >= thetamax) {
				break;
			}
			addPoints(theta, builder);
		}
		addPoints(thetamax, builder);
		return builder.getBoundingBox();
	}
	
	private void addPoints(double theta, BoundingBoxBuilder3 builder) {
		int imin = (int) Math.ceil(phimin / (Math.PI / 2.0));
		int imax = (int) Math.ceil(phimax / (Math.PI / 2.0));
		
		builder.add(getPoint(theta, phimin));
		builder.add(getPoint(theta, phimax));
		
		for (int i = imin; i < imax; i++) {
			double phi = ((double) i) * (Math.PI / 2.0);
			builder.add(getPoint(theta, phi));
		}
	}
	
	private Point3 getPoint(double theta, double phi) {
		double r = minorradius * Math.cos(phi);
		return new Point3(
				(majorradius + r) * Math.cos(theta),
				(majorradius + r) * Math.sin(theta),
				minorradius * Math.sin(phi));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		
		Vector3		orig			= ray.origin().vectorFromOrigin();
		Vector3		dir				= ray.direction();
		double		sqRadius1		= majorradius * majorradius;
		double		sqRadius2		= minorradius * minorradius;
		double		s2NormOfDir		= dir.squaredLength();
		double		s2NormOfOrig	= orig.squaredLength();
		double		dirDotOrig		= dir.dot(orig);
		double		K				= s2NormOfOrig - (sqRadius1 + sqRadius2);

		Polynomial	f = new Polynomial(
							K * K - 4.0 * sqRadius1 * (sqRadius2 - orig.z() * orig.z()),
							4.0 * dirDotOrig * (s2NormOfOrig - (sqRadius1 + sqRadius2)) + 8.0 * sqRadius1 * dir.z() * orig.z(),
							2.0 * s2NormOfDir * (s2NormOfOrig - (sqRadius1 + sqRadius2)) + 4.0 * ((dirDotOrig * dirDotOrig) + sqRadius1 * dir.z() * dir.z()),
							4.0 * dirDotOrig * s2NormOfDir,
							s2NormOfDir * s2NormOfDir
					);
	
		double[] t = f.roots();
		
		if (t.length % 2 == 0) {
			Interval I = recorder.interval();
			for (int i = 0; i < t.length; i++) {
				if (I.contains(t[i])) {
					Point3 p = ray.pointAt(t[i]);
					double theta = Math.atan2(p.y(), p.x());
					if (theta < 0.0) { theta += 2.0 * Math.PI; }
					if (theta <= thetamax) {
						double hyp = Math.hypot(p.x(), p.y());
						double r = hyp - majorradius;
						double phi = Math.atan2(p.z(), r);
						phi -= 2.0 * Math.PI * Math.floor((phi - phimin) / (2.0 * Math.PI));
						if (phi <= phimax) {
							Intersection x = super.newIntersection(ray, t[i], i % 2 == 0)
								.setLocation(p)
								.setUV(new Point2(theta / thetamax, (phi - phimin) / (phimax - phimin)))
								.setTolerance(MathUtil.EPSILON);
							
							recorder.record(x);
						}
					}
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getBasis(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Basis3 getBasis(GeometryIntersection x) {
		Point3 p = x.getPosition();
		return Basis3.fromWU(
			getNormal(x),
			new Vector3(-p.y(), p.x(), 0.0));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getNormal(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Vector3 getNormal(GeometryIntersection x) {
		Point3	p = x.getPosition();
		Vector3	rel = new Vector3(p.x(), p.y(), 0.0);

		double	length = rel.length();

		if (length > 0.0) {
			rel = rel.times(majorradius / length);
			return p.unitVectorFrom(Point3.ORIGIN.plus(rel));
		} else {
			return Vector3.J;
		}
	}

}
