/**
 *
 */
package org.jmist.packages.geometry.primitive;

import org.jmist.framework.IntersectionRecorder;
import org.jmist.framework.Material;
import org.jmist.framework.SingleMaterialGeometry;
import org.jmist.toolkit.Basis3;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Point2;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Sphere;
import org.jmist.toolkit.SphericalCoordinates;
import org.jmist.toolkit.Vector3;

/**
 * A spherical <code>Geometry</code>.
 * @author bkimmel
 */
public final class SphereGeometry extends SingleMaterialGeometry {

	/**
	 * Creates a new <code>SphereGeometry</code>.
	 * @param sphere The <code>Sphere</code> describing to be rendered.
	 * @param material The <code>Material</code> to apply to the sphere.
	 */
	public SphereGeometry(Sphere sphere, Material material) {
		super(material);
		this.sphere = sphere;
	}

	/**
	 * Creates a new <code>SphereGeometry</code>.
	 * @param center The <code>Point3</code> at the center of the sphere.
	 * @param radius The radius of the sphere.
	 * @param material The <code>Material</code> to apply to the sphere.
	 */
	public SphereGeometry(Point3 center, double radius, Material material) {
		super(material);
		this.sphere = new Sphere(center, radius);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#intersect(org.jmist.toolkit.Ray3, org.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {

		Interval I = this.sphere.intersect(ray);

		if (!I.isEmpty()) {
			recorder.record(super.newIntersection(ray, I.minimum(), true));
			recorder.record(super.newIntersection(ray, I.maximum(), false));
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
		return this.sphere.normalAt(x.location());
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractGeometry#getTextureCoordinates(org.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Point2 getTextureCoordinates(GeometryIntersection x) {
		Vector3					n = x.normal();
		SphericalCoordinates	sc = SphericalCoordinates.fromCartesian(new Vector3(n.x(), -n.z(), n.y()));

		return new Point2(
				(Math.PI + sc.azimuthal()) / (2.0 * Math.PI),
				sc.polar() / Math.PI
		);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractGeometry#visibility(org.jmist.toolkit.Ray3, org.jmist.toolkit.Interval)
	 */
	@Override
	public boolean visibility(Ray3 ray, Interval I) {
		return !I.intersects(this.sphere.intersect(ray));
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
		return this.sphere.boundingBox();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		return this.sphere;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractGeometry#surfaceMayIntersect(org.jmist.toolkit.Box3)
	 */
	@Override
	public boolean surfaceMayIntersect(Box3 box) {

		boolean foundCornerInside = false;
		boolean foundCornerOutside = false;

		for (int i = 0; i < 8; i++) {
			if (this.sphere.contains(box.corner(i))) {
				foundCornerInside = true;
			} else {
				foundCornerOutside = true;
			}
			if (foundCornerInside && foundCornerOutside) {
				return true;
			}
		}

		if (box.contains(this.sphere.center().plus(Vector3.I.times(this.sphere.radius())))) {
			return true;
		}

		if (box.contains(this.sphere.center().plus(Vector3.J.times(this.sphere.radius())))) {
			return true;
		}

		if (box.contains(this.sphere.center().plus(Vector3.K.times(this.sphere.radius())))) {
			return true;
		}

		if (box.contains(this.sphere.center().plus(Vector3.NEGATIVE_I.times(this.sphere.radius())))) {
			return true;
		}

		if (box.contains(this.sphere.center().plus(Vector3.NEGATIVE_J.times(this.sphere.radius())))) {
			return true;
		}

		if (box.contains(this.sphere.center().plus(Vector3.NEGATIVE_K.times(this.sphere.radius())))) {
			return true;
		}

		return false;

	}

	/** The <code>Sphere</code> describing this <code>Geometry</code>. */
	private final Sphere sphere;

}
