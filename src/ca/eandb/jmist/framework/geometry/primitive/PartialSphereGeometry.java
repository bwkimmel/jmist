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
 * Represents a partial, sphere primitive centred at the origin.
 * 
 * <p>This geometric primitive implements Pixar's
 * <a href="https://renderman.pixar.com/products/rispec/index.htm">RenderMan 
 * Interface Specification</a> for the <code>RiSphere</code> primitive, with
 * the exception that <code>thetamax</code> must be specified in radians,
 * rather than in degrees.</p>
 * 
 * <p>The sphere is defined by the following equations:</p>
 * 
 * <ul>
 *   <li>&phi;<sub>min</sub> = z<sub>min</sub> &gt; -radius ? asin(z<sub>min</sub>/radius) : -PI/2</li>
 *   <li>&phi;<sub>max</sub> = z<sub>max</sub> &lt;  radius ? asin(z<sub>max</sub>/radius) :  PI/2</li>
 *   <li>&phi; = &phi;<sub>min</sub> + v &sdot; (&phi;<sub>max</sub> - &phi;<sub>min</sub>)</li>
 *   <li>&theta; = u &sdot; &theta;<sub>max</sub></li>
 *   <li>x = radius &sdot; cos(&theta;) &sdot; cos(&phi;)</li>
 *   <li>y = radius &sdot; sin(&theta;) &sdot; cos(&phi;)</li>
 *   <li>z = radius &sdot; sin(&phi;)</li>
 * </ul>
 *  
 * @author Brad Kimmel
 */
public final class PartialSphereGeometry extends PrimitiveGeometry {

	/** Serialization version ID. */
	private static final long serialVersionUID = -2569776608695016193L;
	
	/** The radius of the sphere. */
	private final double radius;
	
	/** The minimum extent along the z-axis. */
	private final double zmin;
	
	/** The maximum extent along the z-axis. */
	private final double zmax;
	
	/** The angle to sweep the sphere around the z-axis. */
	private final double thetamax;
	
	/** The minimum polar angle. */
	private transient double phimin;
	
	/** The maximum polar angle. */
	private transient double phimax;
	
	/**
	 * Creates a new <code>PartialSphereGeometry</code>.
	 * @param radius The radius of the sphere.
	 * @param zmin The minimum extent along the z-axis.
	 * @param zmax The maximum extent along the z-axis.
	 * @param thetamax The angle to sweep the sphere around the z-axis.
	 */
	public PartialSphereGeometry(double radius, double zmin, double zmax, double thetamax) {
		this.radius = radius;
		this.zmin = zmin;
		this.zmax = zmax;
		this.thetamax = thetamax;
		computePolarAngleLimits();
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		computePolarAngleLimits();
	} 
	
	/** Computes the polar angle limits from the z-axis extents and the radius. */
	private void computePolarAngleLimits() {
		phimin = zmin > -radius ? Math.asin(zmin / radius) : -Math.PI / 2.0;
		phimax = zmax <  radius ? Math.asin(zmax / radius) :  Math.PI / 2.0;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		return new Sphere(Point3.ORIGIN, radius);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		double xyradius = zmin * zmax <= 0.0 ? 1.0 : Math.cos(Math.min(Math.abs(zmin), Math.abs(zmax)) / radius);
		double xmax = xyradius;
		double xmin = (thetamax >= Math.PI) ? -xyradius : (xyradius * Math.cos(thetamax)); 
		double ymax = (thetamax >= Math.PI / 2.0) ? xyradius : (xyradius * Math.sin(thetamax));
		double ymin = (thetamax <= Math.PI)
				? 0.0 
				: (thetamax >= 3.0 * Math.PI / 2.0) 
						? -xyradius
						: (xyradius * Math.sin(thetamax)); 
		return new Box3(xmin, ymin, zmin, xmax, ymax, zmax); 
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		
		Interval I = recorder.interval();
		Vector3 ro = ray.origin().vectorFromOrigin();
		Vector3 rd = ray.direction();
		
		Polynomial f = new Polynomial(
				ro.squaredLength() - radius * radius,
				2.0 * ro.dot(rd),
				rd.squaredLength());
		
		double[] t = f.roots();
		
		if (t.length == 2) {
			for (int i = 0; i < 2; i++) {
				if (I.contains(t[i])) {
					Point3 p = ray.pointAt(t[i]);
					if (zmin <= p.z() && p.z() <= zmax) {
						double theta = Math.atan2(p.y(), p.x());
						if (theta < 0.0) { theta += 2.0 * Math.PI; }
						if (theta <= thetamax) {
							double phi = Math.asin(p.z() / radius);
							Intersection x = super.newIntersection(ray, t[i], i == 0)
								.setLocation(p)
								.setUV(new Point2(
										theta / thetamax, 
										(phi - phimin) / (phimax - phimin)));
							recorder.record(x);
						}
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
		return x.getPosition().vectorFromOrigin().divide(radius);
	}

}
