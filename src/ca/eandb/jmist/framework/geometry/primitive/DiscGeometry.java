/**
 *
 */
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.RandomUtil;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector2;
import ca.eandb.jmist.math.Vector3;

/**
 * A circular plane <code>SceneElement</code>.
 * @author Brad Kimmel
 */
public final class DiscGeometry extends PrimitiveGeometry {

	/**
	 * Creates a new <code>DiscGeometry</code>.
	 * @param center The <code>Point3</code> at the center of the disc.
	 * @param normal The <code>Vector3</code> that is perpendicular to the
	 * 		disc.
	 * @param radius The radius of the disc (in meters).
	 * @param twoSided A value indicating whether the disc is two sided.
	 */
	public DiscGeometry(Point3 center, Vector3 normal, double radius, boolean twoSided) {
		this.plane = new Plane3(center, normal);
		this.boundingSphere = new Sphere(center, radius);
		this.twoSided = twoSided;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {

		boolean	fromTop = this.plane.altitude(ray.origin()) > 0.0;

		if (!twoSided && !fromTop)
			return;

		double t = this.plane.intersect(ray);

		if (recorder.interval().contains(t)) {

			Point3 p = ray.pointAt(t);

			if (this.boundingSphere.contains(p)) {

				GeometryIntersection x = super.newIntersection(ray, t, true, fromTop ? DISC_SURFACE_TOP : DISC_SURFACE_BOTTOM)
					.setLocation(p);

				recorder.record(x);

			}

		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractGeometry#getBasis(ca.eandb.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Basis3 getBasis(GeometryIntersection x) {
		switch (x.getTag()) {
		case DISC_SURFACE_TOP:		return Basis3.fromW(this.plane.normal(), Basis3.Orientation.RIGHT_HANDED);
		case DISC_SURFACE_BOTTOM:	return Basis3.fromW(this.plane.normal().opposite(), Basis3.Orientation.RIGHT_HANDED);
		default:					assert(false); return null;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractGeometry#getNormal(ca.eandb.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Vector3 getNormal(GeometryIntersection x) {
		switch (x.getTag()) {
		case DISC_SURFACE_TOP:		return this.plane.normal();
		case DISC_SURFACE_BOTTOM:	return this.plane.normal().opposite();
		default:					assert(false); return null;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractGeometry#getTextureCoordinates(ca.eandb.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Point2 getTextureCoordinates(GeometryIntersection x) {

		Basis3 basis = x.getBasis();
		Vector3 r = x.getPosition().vectorFrom(this.boundingSphere.center());

		return new Point2(
				r.dot(basis.u()) / this.boundingSphere.radius(),
				r.dot(basis.v()) / this.boundingSphere.radius()
		);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#isClosed()
	 */
	public boolean isClosed() {
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	public Box3 boundingBox() {

		BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
		Basis3 basis = Basis3.fromW(this.plane.normal(), Basis3.Orientation.RIGHT_HANDED);
		Point3 center = this.boundingSphere.center();
		Vector3 u = basis.u().times(this.boundingSphere.radius());
		Vector3 v = basis.v().times(this.boundingSphere.radius());

		builder.add(center.plus(u).plus(v));
		builder.add(center.plus(u).minus(v));
		builder.add(center.minus(u).minus(v));
		builder.add(center.minus(u).plus(v));

		return builder.getBoundingBox();

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		return this.boundingSphere;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#generateRandomSurfacePoint()
	 */
	@Override
	public SurfacePoint generateRandomSurfacePoint() {
		Vector2 uv = RandomUtil.uniformOnDisc(boundingSphere.radius()).toCartesian();
		Basis3 basis = Basis3.fromW(this.plane.normal(), Basis3.Orientation.RIGHT_HANDED);

		Point3 p = boundingSphere.center()
				.plus(basis.u().times(uv.x()))
				.plus(basis.v().times(uv.y()));

		int id = (twoSided && RandomUtil.coin())
				? DISC_SURFACE_BOTTOM
				: DISC_SURFACE_TOP;

		return this.newSurfacePoint(p, id);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getSurfaceArea()
	 */
	@Override
	public double getSurfaceArea() {
		double r = boundingSphere.radius();
		return (twoSided ? 2.0 : 1.0) * Math.PI * r * r;
	}

	/**
	 * The surface ID for the top of the disc (the side toward which the normal
	 * points.
	 */
	private static final int DISC_SURFACE_TOP = 0;

	/**
	 * The surface ID for the bottom of the disc (the side away from which the
	 * normal points.
	 */
	private static final int DISC_SURFACE_BOTTOM = 1;

	/**
	 * The <code>Plane3</code> in which this <code>DiscGeometry</code> lies.
	 */
	private final Plane3 plane;

	/** The bounding <code>Sphere</code>. */
	private final Sphere boundingSphere;

	/** A value indicating whether this disc is two sided. */
	private final boolean twoSided;

}
