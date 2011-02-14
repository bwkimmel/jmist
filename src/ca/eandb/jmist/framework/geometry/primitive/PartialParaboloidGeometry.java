/**
 * 
 */
package ca.eandb.jmist.framework.geometry.primitive;

import java.io.IOException;
import java.io.ObjectInputStream;

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
 * Represents a partial, uncapped, paraboloid primitive with its apex at the
 * origin and extending along the positive z-axis.
 * 
 * <p>This geometric primitive implements Pixar's
 * <a href="https://renderman.pixar.com/products/rispec/index.htm">RenderMan 
 * Interface Specification</a> for the <code>RiParaboloid</code> primitive,
 * with the exception that <code>thetamax</code> must be specified in radians,
 * rather than in degrees.</p>
 * 
 * <p>The paraboloid is defined by the following equations:</p>
 * 
 * <ul>
 *   <li>&theta; = u &sdot; &theta;<sub>max</sub></li>
 *   <li>z = z<sub>min</sub> + v &sdot; (z<sub>max</sub> - z<sub>min</sub>)</li>
 *   <li>r = r<sub>max</sub> &sdot; sqrt(z / z<sub>max</sub>)</li>
 *   <li>x = r &sdot; cos(&theta;)</li>
 *   <li>y = r &sdot; sin(&theta;)</li>
 * </ul>
 *  
 * @author Brad Kimmel
 */
public final class PartialParaboloidGeometry extends PrimitiveGeometry {

	/** Serialization version ID. */
	private static final long serialVersionUID = 4979858402087249830L;
	
	/** The maximum radius of the paraboloid. */
	private final double rmax;
	
	/** The minimum extent of the paraboloid along the z-axis. */
	private final double zmin;

	/** The maximum extent of the paraboloid along the z-axis. */
	private final double zmax;
	
	/** The angle through which the paraboloid is swept about the z-axis. */
	private final double thetamax;
	
	private transient double scale;
	
	/**
	 * Creates a new <code>PartialParaboloidGeometry</code>.
	 * @param rmax The maximum radius of the paraboloid.
	 * @param zmin The minimum extent of the paraboloid along the z-axis.
	 * @param zmax The maximum extent of the paraboloid along the z-axis.
	 * @param thetamax The angle through which the paraboloid is swept about
	 * 		the z-axis.
	 */
	public PartialParaboloidGeometry(double rmax, double zmin, double zmax, double thetamax) {
		this.rmax = rmax;
		this.zmin = zmin;
		this.zmax = zmax;
		this.thetamax = thetamax;
		computeScale();
	}
	
	/** Computes the scale factor for the paraboloid. */
	private void computeScale() {
		scale = rmax * rmax / zmax;
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		computeScale();
	} 
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		double h = zmax - zmin;
		double r = Math.hypot(0.5 * h, rmax);
		return new Sphere(new Point3(0, 0, 0.5 * (zmin + zmax)), r);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		double xmax = rmax;
		double xmin = (thetamax >= Math.PI) ? -rmax : (rmax * Math.cos(thetamax));
		double ymax = (thetamax >= Math.PI / 2.0) ? rmax : (rmax * Math.sin(thetamax));
		double ymin = (thetamax <= Math.PI)
				? 0.0
				: (thetamax >= 3.0 * Math.PI / 2.0)
						? -rmax
						: (rmax * Math.sin(thetamax));
		return new Box3(xmin, ymin, zmin, xmax, ymax, zmax);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		
		Interval I = recorder.interval();
		Point3 ro = ray.origin();
		Vector3 rd = ray.direction();
		
		double x0 = ro.x();
		double y0 = ro.y();
		double z0 = ro.z() * scale;
		double x1 = rd.x();
		double y1 = rd.y();
		double z1 = rd.z() * scale;
		
		Polynomial f = new Polynomial(
				x0 * x0 + y0 * y0 - z0,
				2.0 * (x0 * x1 + y0 * y1) - z1,
				x1 * x1 + y1 * y1);
		
		double[] t = f.roots();
		
		if (t.length == 2) {
			for (int i = 0; i < 2; i++) {
				if (I.contains(t[i])) {
					Point3 p = ray.pointAt(t[i]);
					if (zmin <= p.z() && p.z() <= zmax) {
						double theta = Math.atan2(p.y(), p.x());
						if (theta < 0.0) { theta += 2.0 * Math.PI; }
						if (theta <= thetamax) {
							Intersection x = super.newIntersection(ray, t[i], i == 0)
								.setLocation(p)
								.setUV(new Point2(
										theta / thetamax,
										(p.z() - zmin) / (zmax - zmin)));
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
		return Basis3.fromWV(
			getNormal(x),
			x.getPosition().vectorFromOrigin());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getNormal(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Vector3 getNormal(GeometryIntersection x) {
		Point3 p = x.getPosition();
		return Vector3.unit(2.0 * p.x() / scale, 2.0 * p.y() / scale, -1.0);
	}

}
