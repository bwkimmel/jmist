/**
 * 
 */
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.BoundingBoxBuilder3;
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
public final class PartialTorusGeometry extends PrimitiveGeometry {

	/** Serialization version ID. */
	private static final long serialVersionUID = 1350060514791197971L;
	
	private final double majorradius;
	private final double minorradius;
	private final double phimin;
	private final double phimax;
	private final double thetamax;
	
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
		// TODO Auto-generated method stub

	}

}
