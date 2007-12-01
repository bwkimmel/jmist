/**
 *
 */
package org.jmist.packages.geometry.primitive;

import org.jmist.framework.IntersectionRecorder;
import org.jmist.framework.Material;
import org.jmist.framework.SingleMaterialGeometry;
import org.jmist.toolkit.Basis3;
import org.jmist.toolkit.BoundingBoxBuilder3;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Plane3;
import org.jmist.toolkit.Point2;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Sphere;
import org.jmist.toolkit.Vector3;

/**
 * A circular plane <code>Geometry</code>.
 * @author bkimmel
 */
public final class DiscGeometry extends SingleMaterialGeometry {

	/**
	 * Creates a new <code>DiscGeometry</code>.
	 * @param center The <code>Point3</code> at the center of the disc.
	 * @param normal The <code>Vector3</code> that is perpendicular to the
	 * 		disc.
	 * @param radius The radius of the disc (in meters).
	 * @param twoSided A value indicating whether the disc is two sided.
	 * @param material The <code>Material</code> to apply to the disc.
	 */
	public DiscGeometry(Point3 center, Vector3 normal, double radius, boolean twoSided, Material material) {
		super(material);
		this.plane = new Plane3(center, normal);
		this.boundingSphere = new Sphere(center, radius);
		this.twoSided = twoSided;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#intersect(org.jmist.toolkit.Ray3, org.jmist.framework.IntersectionRecorder)
	 */
	@Override
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
	 * @see org.jmist.framework.AbstractGeometry#getBasis(org.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Basis3 getBasis(GeometryIntersection x) {
		switch (x.surfaceId()) {
		case DISC_SURFACE_TOP:		return Basis3.fromW(this.plane.normal(), Basis3.Orientation.RIGHT_HANDED);
		case DISC_SURFACE_BOTTOM:	return Basis3.fromW(this.plane.normal().opposite(), Basis3.Orientation.RIGHT_HANDED);
		default:					assert(false); return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractGeometry#getNormal(org.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Vector3 getNormal(GeometryIntersection x) {
		switch (x.surfaceId()) {
		case DISC_SURFACE_TOP:		return this.plane.normal();
		case DISC_SURFACE_BOTTOM:	return this.plane.normal().opposite();
		default:					assert(false); return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractGeometry#getTextureCoordinates(org.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Point2 getTextureCoordinates(GeometryIntersection x) {

		Basis3 basis = x.basis();
		Vector3 r = x.location().vectorFrom(this.boundingSphere.center());

		return new Point2(
				r.dot(basis.u()) / this.boundingSphere.radius(),
				r.dot(basis.v()) / this.boundingSphere.radius()
		);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#isClosed()
	 */
	@Override
	public boolean isClosed() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
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
	 * @see org.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		return this.boundingSphere;
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
