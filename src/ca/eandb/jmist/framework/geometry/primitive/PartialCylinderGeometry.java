/**
 * 
 */
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Polynomial;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * Represents a partial, uncapped, cylinder primitive with its axis parallel to
 * the z-axis.
 * 
 * <p>This geometric primitive implements Pixar's
 * <a href="https://renderman.pixar.com/products/rispec/index.htm">RenderMan 
 * Interface Specification</a> for the <code>RiCylinder</code> primitive, with
 * the exception that <code>thetamax</code> must be specified in radians,
 * rather than in degrees.</p>
 * 
 * <p>The cylinder is defined by the following equations:</p>
 * 
 * <ul>
 *   <li>&theta; = u &sdot; &theta;<sub>max</sub></li>
 *   <li>x = radius &sdot; cos(&theta;)</li>
 *   <li>y = radius &sdot; sin(&theta;)</li>
 *   <li>z = z<sub>min</sub> + v &sdot; (z<sub>max</sub> - z<sub>min</sub>)</li>
 * </ul>
 *  
 * @author Brad Kimmel
 */
public final class PartialCylinderGeometry extends PrimitiveGeometry {

	/** Serialization version ID. */
	private static final long serialVersionUID = 2413814567676291328L;
	
	/** The radius of the cylinder. */
	private final double radius;
	
	/** The z-coordinate of the base of the cylinder. */
	private final double zmin;
	
	/** The z-coordinate of the top of the cylinder. */
	private final double zmax;
	
	/** The angle through which the cylinder is swept. */
	private final double thetamax;
	
	/**
	 * Creates a new <code>PartialCylinderGeometry</code>.
	 * @param radius The radius of the cylinder.
	 * @param zmin The z-coordinate of the base of the cylinder.
	 * @param zmax The z-coordinate of the top of the cylinder.
	 * @param thetamax The angle through which the cylinder is swept.
	 */
	public PartialCylinderGeometry(double radius, double zmin, double zmax, double thetamax) {
		this.radius = radius;
		this.zmin = zmin;
		this.zmax = zmax;
		this.thetamax = thetamax;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		double h = (zmax - zmin);
		double r = Math.hypot(0.5 * h, radius);
		return new Sphere(new Point3(0, 0, 0.5 * (zmin + zmax)), r);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		double xmax = radius;
		double xmin = (thetamax >= Math.PI) ? -radius : (radius * Math.cos(thetamax));
		double ymax = (thetamax >= Math.PI / 2.0) ? radius : (radius * Math.sin(thetamax));
		double ymin = (thetamax <= Math.PI)
				? 0.0
				: (thetamax >= 3.0 * Math.PI / 2.0)
						? -radius
						: (radius * Math.sin(thetamax));
		return new Box3(xmin, ymin, zmin, xmax, ymax, zmax);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {

		Interval	I		= recorder.interval();
		Vector3		orig	= ray.origin().vectorFromOrigin();
		Vector3		dir		= ray.direction();

		Polynomial	f		= new Polynomial(
								orig.x() * orig.x() + orig.y() * orig.y() - this.radius * this.radius,
								2.0 * (orig.x() * dir.x() + orig.y() * dir.y()),
								dir.x() * dir.x() + dir.y() * dir.y()
							);
		double[]	t		= f.roots();

		if (t.length == 2) {			
			for (int i = 0; i < 2; i++) {
				if (I.contains(t[i])) {
					Point3 p = ray.pointAt(t[i]);
					double theta = Math.atan2(p.y(), p.x());
					if (theta < 0.0) { theta += 2.0 * Math.PI; }
					if (theta <= thetamax && zmin <= p.z() && p.z() <= zmax) {
						Intersection x = super.newIntersection(ray, t[i], i == 0)
							.setLocation(p)
							.setUV(new Point2(theta / thetamax, (p.z() - zmin) / (zmax - zmin)));
						recorder.record(x);
					}
				}
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractGeometry#getBasis(ca.eandb.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Basis3 getBasis(GeometryIntersection x) {
		return Basis3.fromWV(getNormal(x), Vector3.K);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractGeometry#getNormal(ca.eandb.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Vector3 getNormal(GeometryIntersection x) {
		Point3 p = x.getPosition();
		return new Vector3(p.x() / radius, p.y() / radius, 0.0);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public void generateRandomSurfacePoint(ShadingContext context, double ru,
			double rv, double rj) {
	
		double theta = ru * thetamax;
		double c = Math.cos(theta);
		double s = Math.sin(theta);
		double x = radius * c;
		double y = radius * s;
		double z = zmin + rv * (zmax - zmin);
		Point3 p = new Point3(x, y, z);
		Point2 uv = new Point2(ru, rv);
		Vector3 n = new Vector3(c, s, 0);

		context.setPosition(p);
		context.setUV(uv);
		context.setBasis(Basis3.fromWV(n, Vector3.K));

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#getSurfaceArea()
	 */
	@Override
	public double getSurfaceArea() {
		return Math.abs(thetamax) * Math.abs(radius) * (zmax - zmin); 
	}

}
