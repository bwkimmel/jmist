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
public final class PartialCylinderGeometry extends PrimitiveGeometry {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 2413814567676291328L;
	
	private final double radius;
	private final double zmin;
	private final double zmax;
	private final double thetamax;
	
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
		// TODO Auto-generated method stub

	}

}
