/**
 * 
 */
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
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
 * Represents a partial, uncapped, cone primitive with its base in the xy-plane
 * and extending along the positive z-axis.
 * 
 * <p>This geometric primitive implements Pixar's
 * <a href="https://renderman.pixar.com/products/rispec/index.htm">RenderMan 
 * Interface Specification</a> for the <code>RiCone</code> primitive, with the
 * exception that <code>thetamax</code> must be specified in radians,
 * rather than in degrees.</p>
 * 
 * <p>The cone is defined by the following equations:</p>
 * 
 * <ul>
 *   <li>&theta; = u &sdot; &theta;<sub>max</sub></li>
 *   <li>x = radius &sdot; (1 - v) &sdot; cos(&theta;)</li>
 *   <li>y = radius &sdot; (1 - v) &sdot; sin(&theta;)</li>
 *   <li>z = v &sdot; height</li>
 * </ul>
 *  
 * @author Brad Kimmel
 */
public final class PartialConeGeometry extends PrimitiveGeometry {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 1596532405741131626L;
	
	/** The height of the cone along the z-axis. */
	private final double height;
	
	/** The radius of the base of the cone in the xy-plane. */
	private final double radius;
	
	/** The angle through which the cone is swept. */
	private final double thetamax;
	
	/**
	 * Creates a new <code>PartialConeGeometry</code>.
	 * @param height The height of the cone along the z-axis.
	 * @param radius The radius of the base of the cone in the xy-plane.
	 * @param thetamax The angle through which the cone is swept.
	 */
	public PartialConeGeometry(double height, double radius, double thetamax) {
		this.height = height;
		this.radius = radius;
		this.thetamax = thetamax;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		double z = height > radius ? (height * height - radius * radius) / (2.0 * height) : 0.0;
		double r = height > radius ? height - z : radius;
		return new Sphere(new Point3(0, 0, z), r);
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
		return new Box3(xmin, ymin, 0.0, xmax, ymax, height);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		
		Interval I = recorder.interval();
		Point3 org = ray.origin();
		Vector3 dir = ray.direction();
		
		double x0 = org.x() / radius;
		double y0 = org.y() / radius;
		double z0 = org.z() / height - 1.0;
		double x1 = dir.x() / radius;
		double y1 = dir.y() / radius;
		double z1 = dir.z() / height;
		
		Polynomial f = new Polynomial(
				x0 * x0 + y0 * y0 - z0 * z0,
				2.0 * (x0 * x1 + y0 * y1 - z0 * z1),
				x1 * x1 + y1 * y1 - z1 * z1);
		
		double[] t = f.roots();
		
		for (int i = 0; i < t.length; i++) {
			if (I.contains(t[i])) {
				Point3 p = ray.pointAt(t[i]);
				double theta = Math.atan2(p.y(), p.x());
				if (theta < 0.0) { theta += 2.0 * Math.PI; }
				if (theta <= thetamax && 0.0 <= p.z() && p.z() <= height) {
					boolean front = (t.length == 2) ? i == 0 : 
						ray.direction().dot(ray.origin().vectorToOrigin()) > 0.0; 
					Intersection x = super.newIntersection(ray, t[i], front)
						.setLocation(p)
						.setUV(new Point2(theta / thetamax, p.z() / height));
					recorder.record(x);
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#getSurfaceArea()
	 */
	@Override
	public double getSurfaceArea() {
		double s = Math.hypot(radius, height);
		return 0.5 * Math.abs(thetamax) * s * Math.abs(radius);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getBasis(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Basis3 getBasis(GeometryIntersection x) {
		return Basis3.fromWV(
			getNormal(x),
			x.getPosition().vectorTo(new Point3(0, height, 0)));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getNormal(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Vector3 getNormal(GeometryIntersection x) {
		Point3 p = x.getPosition();
		double side = Math.hypot(radius, height);
		double c = radius / side;
		double s = height / side;
		double hyp = Math.hypot(p.x(), p.y());
		
		return new Vector3(
				s * p.x() / hyp,
				s * p.y() / hyp,
				c);
	}

}
