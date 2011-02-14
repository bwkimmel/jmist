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
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * Represents a partial disc primitive parallel to the xy-plane and centred
 * along the z-axis.
 * 
 * <p>This geometric primitive implements Pixar's
 * <a href="https://renderman.pixar.com/products/rispec/index.htm">RenderMan 
 * Interface Specification</a> for the <code>RiDisk</code> primitive, with the
 * exception that <code>thetamax</code> must be specified in radians,
 * rather than in degrees.</p>
 * 
 * <p>The disc is defined by the following equations:</p>
 * 
 * <ul>
 *   <li>&theta; = u &sdot; &theta;<sub>max</sub></li>
 *   <li>x = radius &sdot; (1 - v) &sdot; cos(&theta;)</li>
 *   <li>y = radius &sdot; (1 - v) &sdot; sin(&theta;)</li>
 *   <li>z = height</li>
 * </ul>
 *  
 * @author Brad Kimmel
 */
public final class PartialDiscGeometry extends PrimitiveGeometry {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 8469997465225459248L;
	
	/** The distance from the origin to the centre of the disk. */
	private final double height;
	
	/** The radius of the disk. */
	private final double radius;
	
	/** The angle through which the disk is swept. */
	private final double thetamax;
	
	/**
	 * Creates a new <code>PartialDiscGeometry</code>.
	 * @param height The distance from the origin to the centre of the disk.
	 * @param radius The radius of the disk.
	 * @param thetamax The angle through which the disk is swept.
	 */
	public PartialDiscGeometry(double height, double radius, double thetamax) {
		this.height = height;
		this.radius = radius;
		this.thetamax = thetamax;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		return new Sphere(new Point3(0, 0, height), radius);
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
		return new Box3(
				xmin, ymin, height - MathUtil.TINY_EPSILON, 
				xmax, ymax, height + MathUtil.TINY_EPSILON);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {

		Interval I = recorder.interval();
		Point3 orig = ray.origin();
		Vector3 dir = ray.direction();
		double t = (height - orig.z()) / dir.z();

		if (I.contains(t)) {
			Point3 p = ray.pointAt(t);
			double d = Math.hypot(p.x(), p.y());
			
			if (d <= radius) {
				double theta = Math.atan2(p.y(), p.x());
				
				if (thetamax >= 0.0) {
					if (theta < 0.0) { theta += 2.0 * Math.PI; }
				} else {
					if (theta > 0.0) { theta -= 2.0 * Math.PI; }
				}
				
				if (Math.abs(theta) <= Math.abs(thetamax)) {
					boolean front = (thetamax >= 0.0) == (orig.z() > height);
					
					Intersection x = super.newIntersection(ray, t, front)
						.setLocation(p)
						.setUV(new Point2(theta / thetamax, 1 - d / Math.abs(radius)));
					
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
		return 0.5 * Math.abs(thetamax) * radius * radius;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractGeometry#getBasis(ca.eandb.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Basis3 getBasis(GeometryIntersection x) {
		Point3 p = x.getPosition();
		return Basis3.fromWV(
				getNormal(x),
				p.vectorTo(new Point3(0, 0, height)));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractGeometry#getNormal(ca.eandb.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Vector3 getNormal(GeometryIntersection x) {
		return thetamax >= 0.0 ? Vector3.K : Vector3.NEGATIVE_K;
	}

}
