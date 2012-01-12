/**
 * 
 */
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Polynomial;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class TaperedCylinderGeometry extends PrimitiveGeometry {

	/** Serialization version ID. */
	private static final long serialVersionUID = -6353821613593755723L;

	/** The radius of the second end of the tapered cylinder */
	private final double radius1;

	/** The radius of the first end of the tapered cylinder */
	private final double radius2;

	/** The height of the first end of the tapered cylinder */
	private final double height1;
	
	/** The height of the second end of the tapered cylinder */
	private final double height2;
	
	/** A value indicating whether the ends of the cylinder are rendered. */
	private final boolean capped;

	/** The surface ID for the body of the tapered cylinder. */
	private static final int TAPERED_CYLINDER_SURFACE_BODY = 0;

	/** The surface ID for the first end of the tapered cylinder. */
	private static final int TAPERED_CYLINDER_SURFACE_END_1 = 1;

	/** The surface ID for the second end of the tapered cylinder. */
	private static final int TAPERED_CYLINDER_SURFACE_END_2 = 2;
	
	public TaperedCylinderGeometry(double height1, double radius1, double height2, double radius2, boolean capped) {
		this.height1 = height1;
		this.radius1 = radius1;
		this.height2 = height2;
		this.radius2 = radius2;
		this.capped = capped;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		Point3 center = new Point3(0.0, 0.5 * (height1 + height2), 0.0);
		double height = Math.abs(height1 - height2);
		double maxR = Math.max(radius1, radius2);
		double radius = Math.sqrt(maxR * maxR + 0.25 * height * height);
		return new Sphere(center, radius);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		double maxR = Math.max(radius1, radius2);
		double minH = Math.min(height1, height2);
		double maxH = Math.max(height1, height2);
		
		return new Box3(-maxR, minH, -maxR, maxR, maxH, maxR);		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		// TODO Auto-generated method stub
		
		double dr = radius2 - radius1;
		double dh = height2 - height1;
		double det = radius1 * height2 - radius2 * height1;
		double dh2 = dh * dh;
		double dr2 = dr * dr;
		
//		if (capped) {
//			double t = Plane3.XZ.intersect(ray);
//						
//			if (recorder.interval().contains(t)) {
//				Point3 p = ray.pointAt(t);
//				if (p.distanceToOrigin() <= radius) {
//					Intersection x = super.newIntersection(ray, t, ray.origin().y() < 0.0, TAPERED_CYLINDER_SURFACE_BASE)
//						.setLocation(p);
//					recorder.record(x);
//				}
//			}
//		}
		
		
		
//		double x0 = org.x() / radius;
//		double y0 = org.y() / height - 1.0;
//		double z0 = org.z() / radius;
//		double x1 = dir.x() / radius;
//		double y1 = dir.y() / height;
//		double z1 = dir.z() / radius;
//		
//		Polynomial f = new Polynomial(
//				x0 * x0 - y0 * y0 + z0 * z0,
//				2.0 * (x0 * x1 - y0 * y1 + z0 * z1),
//				x1 * x1 - y1 * y1 + z1 * z1);

		
		double x0 = ray.origin().x();
		double y0 = ray.origin().y();
		double z0 = ray.origin().z();
		double x1 = ray.direction().x();
		double y1 = ray.direction().y();
		double z1 = ray.direction().z();
		
		Polynomial f = new Polynomial(
				dh2 * (x0 * x0 + z0 * z0) - dr2 * y0 * y0 + 2.0 * dr * det * y0 - det * det,
				2.0 * (dh2 * (x0 * x1 + z0 * z1) - dr2 * y0 * y1 + dr * det * y1),
				dh2 * (x1 * x1 + z1 * z1) - dr2 * y1 * y1);
		
		double[] t = f.roots();
		
		if (t.length == 2) {
			for (int i = 0; i < 2; i++) {
				if (recorder.interval().contains(t[i])) {
					Point3 p = ray.pointAt(t[i]);
					if (MathUtil.inRangeCC(p.y(), 
							Math.min(height1, height2),
							Math.max(height1, height2))) {
						Intersection x = super.newIntersection(ray, t[i], i == 0, TAPERED_CYLINDER_SURFACE_BODY)
							.setLocation(p);
						recorder.record(x);
					}
				}
			}
		}
		
		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#getSurfaceArea()
	 */
	@Override
	public double getSurfaceArea() {
		throw new UnsupportedOperationException();
//		double s = Math.hypot(radius, height);
//		return capped ? Math.PI * radius * radius + Math.PI * s * radius
//				: Math.PI * s * radius;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getBasis(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Basis3 getBasis(GeometryIntersection x) {
		switch (x.getTag()) {
		case TAPERED_CYLINDER_SURFACE_BODY:
			return Basis3.fromWV(
					getNormal(x),
					x.getPosition().vectorTo(new Point3(0, height2, 0)));
					
		case TAPERED_CYLINDER_SURFACE_END_1:
		case TAPERED_CYLINDER_SURFACE_END_2:
			return Basis3.fromW(getNormal(x));
			
		default:
			throw new IllegalArgumentException("Invalid surface ID");
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getNormal(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Vector3 getNormal(GeometryIntersection x) {
		switch (x.getTag()) {
		case TAPERED_CYLINDER_SURFACE_BODY:
			Point3 p = x.getPosition();
			double dh = height2 - height1;
			double dr = radius2 - radius1;
			double side = Math.hypot(dr, dh);
			double c = dr / side;
			double s = dh / side;
			double hyp = Math.hypot(p.x(), p.z());
			
			return new Vector3(
					s * p.x() / hyp,
					c,
					s * p.z() / hyp);
			
		case TAPERED_CYLINDER_SURFACE_END_1:
			return Vector3.NEGATIVE_J;
			
		case TAPERED_CYLINDER_SURFACE_END_2:
			return Vector3.J;
			
		default:
			throw new IllegalArgumentException("Invalid surface ID");
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getTextureCoordinates(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Point2 getTextureCoordinates(GeometryIntersection x) {
		Point3 p = x.getPosition();
		switch (x.getTag()) {
		case TAPERED_CYLINDER_SURFACE_BODY:
			double angle = Math.PI + Math.atan2(p.z(), p.x());			
			return new Point2(angle / ((capped ? 4.0 : 2.0) * Math.PI),
					(p.y() - height1) / (height2 - height1));
			
		case TAPERED_CYLINDER_SURFACE_END_1:
			return new Point2(0.5 + (p.x() + radius1) / (4.0 * radius1),
					(p.z() + radius1) / (4.0 * radius1));
			
		case TAPERED_CYLINDER_SURFACE_END_2:
			return new Point2(0.5 + (p.x() + radius2) / (4.0 * radius2),
					(p.z() + radius2) / (4.0 * radius2));
			
		default:
			throw new IllegalArgumentException("Invalid surface ID");
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#isClosed()
	 */
	public boolean isClosed() {
		return capped;
	}

}
