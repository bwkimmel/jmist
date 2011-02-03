/**
 * 
 */
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * @author Brad
 *
 */
public final class PartialSphereGeometry extends PrimitiveGeometry {

	/** Serialization version ID. */
	private static final long serialVersionUID = -2569776608695016193L;
	
	private final double radius;
	private final double zmin;
	private final double zmax;
	private final double thetamax;
	
	public PartialSphereGeometry(double radius, double zmin, double zmax, double thetamax) {
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
		// TODO Auto-generated method stub

	}

}
