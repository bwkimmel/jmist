/**
 * 
 */
package ca.eandb.jmist.framework.geometry.primitive;

import java.io.IOException;
import java.io.ObjectInputStream;

import ca.eandb.jmist.framework.BoundingBoxBuilder3;
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
 * Represents a partial, uncapped, hyperboloid (of one sheet) primitive with
 * its apex at the origin and extending along the positive z-axis.
 * 
 * <p>This geometric primitive implements Pixar's
 * <a href="https://renderman.pixar.com/products/rispec/index.htm">RenderMan 
 * Interface Specification</a> for the <code>RiHyperboloid</code> primitive,
 * with the exception that <code>thetamax</code> must be specified in radians,
 * rather than in degrees.</p>
 * 
 * <p>The hyperboloid is defined by the following equations:</p>
 * 
 * <ul>
 *   <li>&theta; = u &sdot; &theta;<sub>max</sub></li>
 *   <li>x<sub>r</sub> = (1 - v) &sdot; x<sub>1</sub> + v &sdot; x<sub>2</sub></li>
 *   <li>y<sub>r</sub> = (1 - v) &sdot; y<sub>1</sub> + v &sdot; y<sub>2</sub></li>
 *   <li>z<sub>r</sub> = (1 - v) &sdot; z<sub>1</sub> + v &sdot; z<sub>2</sub></li>
 *   <li>x = x<sub>r</sub> &sdot; cos(&theta;) - y<sub>r</sub> &sdot; sin(&theta;)</li>
 *   <li>y = x<sub>r</sub> &sdot; sin(&theta;) + y<sub>r</sub> &sdot; cos(&theta;)</li>
 *   <li>z = z<sub>r</sub></li>
 * </ul>
 * 
 * <p>where <code>point1</code> = (x<sub>1</sub>, y<sub>1</sub>, z<sub>1</sub>)
 *      and <code>point2</code> = (x<sub>2</sub>, y<sub>2</sub>, z<sub>2</sub>).</p>
 *  
 * @author Brad Kimmel
 */
public final class PartialHyperboloidGeometry extends PrimitiveGeometry {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 1895101289621276856L;
	
	/** The first endpoint of the line to be swept about the z-axis. */
	private final Point3 point1;
	
	/** The second endpoint of the line to be swept about the z-axis. */
	private final Point3 point2;
	
	/** The angle through which to sweep the line to form the hyperboloid. */
	private final double thetamax;
	
	/** The radius of the hyperboloid at its narrowest part. */
	private transient double a;
	
	/**
	 * Specifies the slope of the asymtotic cone (in combination with
	 * <code>a</code>).
	 */
	private transient double c;
	
	/**
	 * The z-coordinate of the narrowest part of the hyperboloid.  This is not
	 * necessarily between <code>point1.z()</code> and <code>point2.z()</code>.
	 */
	private transient double zwaist;
	
	/**
	 * Creates a new <code>PartialHyperboloidGeometry</code>.
	 * @param point1 The first endpoint of the line to be swept about the
	 * 		z-axis.
	 * @param point2 The second endpoint of the line to be swept about the
	 * 		z-axis.
	 * @param thetamax The angle through which to sweep the line to form the
	 * 		hyperboloid.
	 */
	public PartialHyperboloidGeometry(Point3 point1, Point3 point2, double thetamax) {
		this.point1 = point1;
		this.point2 = point2;
		this.thetamax = thetamax;
		computeScaleFactors();
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		computeScaleFactors();
	} 
	
	private void computeScaleFactors() {
		double x1 = point1.x();
		double y1 = point1.y();
		double z1 = point1.z();
		double x2 = point2.x();
		double y2 = point2.y();
		double z2 = point2.z();
		double dx = x2 - x1;
		double dy = y2 - y1;
		
		double vwaist = -(x1 * dx + y1 * dy) / (dx * dx + dy * dy);		
		double xwaist = (1.0 - vwaist) * x1 + vwaist * x2;
		double ywaist = (1.0 - vwaist) * y1 + vwaist * y2;
		zwaist = (1.0 - vwaist) * z1 + vwaist * z2;
		
		double a2 = xwaist * xwaist + ywaist * ywaist;
		
		double z1a = z1 - zwaist;
		double z2a = z2 - zwaist;
		
		double c2 = (Math.abs(z1a) > Math.abs(z2a))
				? ((z1a * z1a) / (x1 * x1 / a2 + y1 * y1 / a2 - 1.0))
				: ((z2a * z2a) / (x2 * x2 / a2 + y2 * y2 / a2 - 1.0));
		
		a = Math.sqrt(a2);
		c = Math.sqrt(c2); 
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		return new Sphere(Point3.ORIGIN,
				Math.max(point1.distanceToOrigin(), point2.distanceToOrigin()));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
		double radius, theta1, theta2;
		int imin, imax;
		
		radius = Math.hypot(point1.x(), point1.y());
		theta1 = Math.atan2(point1.y(), point1.x());
		theta2 = theta1 + thetamax;
		builder.add(point1);
		builder.add(new Point3(radius * Math.cos(theta2), radius * Math.sin(theta2), point1.z()));
		
		imin = (int) Math.ceil(theta1 / (Math.PI / 2.0));
		imax = (int) Math.ceil(theta2 / (Math.PI / 2.0));
		for (int i = imin; i < imax; i++) {
			double theta = (double) i * (Math.PI / 2.0);
			builder.add(new Point3(radius * Math.cos(theta), radius * Math.sin(theta), point1.z()));
		}

		radius = Math.hypot(point2.x(), point2.y());
		theta1 = Math.atan2(point2.y(), point2.x());
		theta2 = theta1 + thetamax;
		builder.add(point2);
		builder.add(new Point3(radius * Math.cos(theta2), radius * Math.sin(theta2), point2.z()));

		imin = (int) Math.ceil(theta1 / (Math.PI / 2.0));
		imax = (int) Math.ceil(theta2 / (Math.PI / 2.0));
		for (int i = imin; i < imax; i++) {
			double theta = ((double) i) * (Math.PI / 2.0);
			builder.add(new Point3(radius * Math.cos(theta), radius * Math.sin(theta), point1.z()));
		}

		return builder.getBoundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		
		Point3 ro = ray.origin();
		Vector3 rd = ray.direction();
		
		double x0 = ro.x() / a;
		double y0 = ro.y() / a;
		double z0 = (ro.z() - zwaist) / c;
		double x1 = rd.x() / a;
		double y1 = rd.y() / a;
		double z1 = rd.z() / c;
		
		Polynomial f = new Polynomial(
				x0 * x0 + y0 * y0 - z0 * z0 - 1.0,
				2.0 * (x0 * x1 + y0 * y1 - z0 * z1),
				x1 * x1 + y1 * y1 - z1 * z1);
		
		double[] t = f.roots();
		
		if (t.length == 2) {
			Interval I = recorder.interval();
			for (int i = 0; i < 2; i++) {
				if (I.contains(t[i])) {
					double zmin = Math.min(point1.z(), point2.z());
					double zmax = Math.max(point1.z(), point2.z());
					Point3 p = ray.pointAt(t[i]);
					if (zmin <= p.z() && p.z() <= zmax) {
						double theta = Math.atan2(p.y(), p.x());
						double v = (p.z() - point1.z()) / (point2.z() - point1.z());
						double xr = (1.0 - v) * point1.x() + v * point2.x();
						double yr = (1.0 - v) * point1.y() + v * point2.y();
						double thetar = Math.atan2(yr, xr);
						double dtheta = theta - thetar;
						while (dtheta < 0.0) { dtheta += 2.0 * Math.PI; }
						if (dtheta <= thetamax) {
							Intersection x = super.newIntersection(ray, t[i], i == 0)
								.setLocation(p)
								.setUV(new Point2(dtheta / thetamax, v));
							
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
			point2.z() > point1.z() ? Vector3.K : Vector3.NEGATIVE_K);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getNormal(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Vector3 getNormal(GeometryIntersection x) {
		Point3 p = x.getPosition();
		return Vector3.unit(p.x() / (a * a), p.y() / (a * a), -(p.z() - zwaist) / (c * c));
	}

}
