/**
 *
 */
package org.jmist.packages.geometry.primitive;

import org.jmist.framework.Intersection;
import org.jmist.framework.IntersectionRecorder;
import org.jmist.framework.Material;
import org.jmist.framework.SingleMaterialGeometry;
import org.jmist.toolkit.Basis3;
import org.jmist.toolkit.Box2;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Point2;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Sphere;
import org.jmist.toolkit.Vector3;

/**
 * An axis aligned box <code>Geometry</code>.
 * @author bkimmel
 */
public final class BoxGeometry extends SingleMaterialGeometry {

	/**
	 * Creates a new <code>BoxGeometry</code>.
	 * @param box The axis aligned <code>Box3</code> to be rendered.
	 * @param material The <code>Material</code> to apply to the box.
	 */
	public BoxGeometry(Box3 box, Material material) {
		super(material);
		this.box = box;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#intersect(org.jmist.toolkit.Ray3, org.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {

		// Check for an empty box.
		if (!this.box.isEmpty()) {

			assert(ray.direction().x() != 0.0 || ray.direction().y() != 0.0 || ray.direction().z() != 0.0);

			double		t;
			int			n = 0;
			Point3		p;

			// Check for intersection with each of the six sides of the box.
			if (ray.direction().x() != 0.0) {
				t = (box.minimumX() - ray.origin().x()) / ray.direction().x();
				if (t > 0.0) {
					p = ray.pointAt(t);
					if (box.minimumY() < p.y() && p.y() < box.maximumY() && box.minimumZ() < p.z() && p.z() < box.maximumZ()) {
						Intersection x = super.newIntersection(ray, t, ray.direction().x() > 0.0, BOX_SURFACE_MIN_X)
							.setLocation(p);
						recorder.record(x);
						if (++n == 2) return;
					}
				}

				t = (box.maximumX() - ray.origin().x()) / ray.direction().x();
				if (t > 0.0) {
					p = ray.pointAt(t);
					if (box.minimumY() < p.y() && p.y() < box.maximumY() && box.minimumZ() < p.z() && p.z() < box.maximumZ()) {
						Intersection x = super.newIntersection(ray, t, ray.direction().x() < 0.0, BOX_SURFACE_MAX_X)
							.setLocation(p);
						recorder.record(x);
						if (++n == 2) return;
					}
				}
			}

			if (ray.direction().y() != 0.0) {
				t = (box.minimumY() - ray.origin().y()) / ray.direction().y();
				if (t > 0.0) {
					p = ray.pointAt(t);
					if (box.minimumX() < p.x() && p.x() < box.maximumX() && box.minimumZ() < p.z() && p.z() < box.maximumZ()) {
						Intersection x = super.newIntersection(ray, t, ray.direction().y() > 0.0, BOX_SURFACE_MIN_Y)
							.setLocation(p);
						recorder.record(x);
						if (++n == 2) return;
					}
				}

				t = (box.maximumY() - ray.origin().y()) / ray.direction().y();
				if (t > 0.0) {
					p = ray.pointAt(t);
					if (box.minimumX() < p.x() && p.x() < box.maximumX() && box.minimumZ() < p.z() && p.z() < box.maximumZ()) {
						Intersection x = super.newIntersection(ray, t, ray.direction().y() < 0.0, BOX_SURFACE_MAX_Y)
							.setLocation(p);
						recorder.record(x);
						if (++n == 2) return;
					}
				}
			}

			if (ray.direction().z() != 0.0) {
				t = (box.minimumZ() - ray.origin().z()) / ray.direction().z();
				if (t > 0.0) {
					p = ray.pointAt(t);
					if (box.minimumX() < p.x() && p.x() < box.maximumX() && box.minimumY() < p.y() && p.y() < box.maximumY()) {
						Intersection x = super.newIntersection(ray, t, ray.direction().z() > 0.0, BOX_SURFACE_MIN_Z)
							.setLocation(p);
						recorder.record(x);
						if (++n == 2) return;
					}
				}

				t = (box.maximumZ() - ray.origin().z()) / ray.direction().z();
				if (t > 0.0) {
					p = ray.pointAt(t);
					if (box.minimumX() < p.x() && p.x() < box.maximumX() && box.minimumY() < p.y() && p.y() < box.maximumY()) {
						Intersection x = super.newIntersection(ray, t, ray.direction().z() < 0.0, BOX_SURFACE_MAX_Z)
							.setLocation(p);
						recorder.record(x);
						if (++n == 2) return;
					}
				}
			}

		}

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractGeometry#getBasis(org.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Basis3 getBasis(GeometryIntersection x) {
		return Basis3.fromW(x.normal(), Basis3.Orientation.RIGHT_HANDED);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractGeometry#getNormal(org.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Vector3 getNormal(GeometryIntersection x) {
		switch (x.surfaceId())
		{
		case BOX_SURFACE_MAX_X:	return Vector3.I;
		case BOX_SURFACE_MIN_X:	return Vector3.NEGATIVE_I;
		case BOX_SURFACE_MAX_Y:	return Vector3.J;
		case BOX_SURFACE_MIN_Y:	return Vector3.NEGATIVE_J;
		case BOX_SURFACE_MAX_Z:	return Vector3.K;
		case BOX_SURFACE_MIN_Z:	return Vector3.NEGATIVE_K;
		default:				assert(false); return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractGeometry#getTextureCoordinates(org.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Point2 getTextureCoordinates(GeometryIntersection x) {

		Point2	facePoint;
		Point3	p = x.location();

		switch (x.surfaceId())
		{
		case BOX_SURFACE_MAX_X:
			facePoint = new Point2(
					(box.maximumZ() - p.z()) / box.lengthX(),
					(box.maximumY() - p.y()) / box.lengthY()
			);
			break;

		case BOX_SURFACE_MIN_X:
			facePoint = new Point2(
					(p.z() - box.minimumZ()) / box.lengthZ(),
					(box.maximumY() - p.y()) / box.lengthY()
			);
			break;

		case BOX_SURFACE_MAX_Y:
			facePoint = new Point2(
					(p.x() - box.minimumX()) / box.lengthX(),
					(p.z() - box.minimumZ()) / box.lengthZ()
			);
			break;

		case BOX_SURFACE_MIN_Y:
			facePoint = new Point2(
					(p.x() - box.minimumX()) / box.lengthX(),
					(box.maximumZ() - p.z()) / box.lengthZ()
			);
			break;

		case BOX_SURFACE_MAX_Z:
			facePoint = new Point2(
					(p.x() - box.minimumX()) / box.lengthX(),
					(box.maximumY() - p.y()) / box.lengthY()
			);
			break;

		case BOX_SURFACE_MIN_Z:
			facePoint = new Point2(
					(p.x() - box.minimumX()) / box.lengthX(),
					(p.y() - box.minimumY()) / box.lengthY()
			);
			break;

		default:
			throw new IllegalArgumentException("invalid surface id");

		}

		return new Point2(
			FACE_DOMAIN[x.surfaceId() - 1].minimumX() + facePoint.x() * FACE_DOMAIN[x.surfaceId() - 1].lengthX(),
			FACE_DOMAIN[x.surfaceId() - 1].minimumY() + facePoint.y() * FACE_DOMAIN[x.surfaceId() - 1].lengthY()
		);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractGeometry#visibility(org.jmist.toolkit.Ray3, org.jmist.toolkit.Interval)
	 */
	@Override
	public boolean visibility(Ray3 ray, Interval I) {
		return I.intersects(this.box.intersect(ray));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#isClosed()
	 */
	public boolean isClosed() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingBox()
	 */
	public Box3 boundingBox() {
		return this.box;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		return new Sphere(this.box.center(), this.box.diagonal() / 2.0);
	}

	/** The surface id for the side facing toward the positive x-axis. */
	private static final int BOX_SURFACE_MAX_X = 0;

	/** The surface id for the side facing toward the negative x-axis. */
	private static final int BOX_SURFACE_MIN_X = 1;

	/** The surface id for the side facing toward the positive y-axis. */
	private static final int BOX_SURFACE_MAX_Y = 2;

	/** The surface id for the side facing toward the negative y-axis. */
	private static final int BOX_SURFACE_MIN_Y = 3;

	/** The surface id for the side facing toward the positive z-axis. */
	private static final int BOX_SURFACE_MAX_Z = 4;

	/** The surface id for the side facing toward the negative z-axis. */
	private static final int BOX_SURFACE_MIN_Z = 5;

	/**
	 * The <code>Box2</code>s that each face on the cube map to in texture
	 * coordinate space.
	 */
	private static final Box2 FACE_DOMAIN[] = {
			new Box2(2.0 / 3.0, 1.0 / 4.0, 1.0, 1.0 / 2.0),
			new Box2(0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 2.0),
			new Box2(1.0 / 3.0, 0.0, 2.0 / 3.0, 1.0 / 4.0),
			new Box2(1.0 / 3.0, 1.0 / 2.0, 2.0 / 3.0, 3.0 / 4.0),
			new Box2(1.0 / 3.0, 1.0 / 4.0, 2.0 / 3.0, 1.0 / 2.0),
			new Box2(1.0 / 3.0, 3.0 / 4.0, 2.0 / 3.0, 1.0)
	};

	/** The <code>Box3</code> that represents this <code>Geometry</code>. */
	private final Box3 box;

}
