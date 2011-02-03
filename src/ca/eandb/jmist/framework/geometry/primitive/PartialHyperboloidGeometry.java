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
public final class PartialHyperboloidGeometry extends PrimitiveGeometry {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 1895101289621276856L;
	
	private final Point3 point1;
	private final Point3 point2;
	private final double thetamax;
	
	public PartialHyperboloidGeometry(Point3 point1, Point3 point2, double thetamax) {
		this.point1 = point1;
		this.point2 = point2;
		this.thetamax = thetamax;
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
		// TODO Auto-generated method stub

	}

}
