/**
 *
 */
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * A spherical <code>SceneElement</code>.
 * @author Brad Kimmel
 */
public final class SphereGeometry extends PrimitiveGeometry {

	/**
	 * Creates a new <code>SphereGeometry</code>.
	 * @param sphere The <code>Sphere</code> describing to be rendered.
	 * @param material The <code>Material</code> to apply to the sphere.
	 */
	public SphereGeometry(Sphere sphere) {
		this.sphere = sphere;
	}

	/**
	 * Creates a new <code>SphereGeometry</code>.
	 * @param center The <code>Point3</code> at the center of the sphere.
	 * @param radius The radius of the sphere.
	 */
	public SphereGeometry(Point3 center, double radius) {
		this.sphere = new Sphere(center, radius);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {

		Interval I = this.sphere.intersect(ray);

		if (!I.isEmpty()) {
			recorder.record(super.newIntersection(ray, I.minimum(), true));
			recorder.record(super.newIntersection(ray, I.maximum(), false));
		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractGeometry#getBasis(ca.eandb.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Basis3 getBasis(GeometryIntersection x) {
		return Basis3.fromW(x.getNormal(), Basis3.Orientation.RIGHT_HANDED);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractGeometry#getNormal(ca.eandb.jmist.framework.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Vector3 getNormal(GeometryIntersection x) {
		return this.sphere.normalAt(x.getPosition());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getTextureCoordinates(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Point2 getTextureCoordinates(GeometryIntersection x) {
		Vector3					n = x.getNormal();
		SphericalCoordinates	sc = SphericalCoordinates.fromCartesian(new Vector3(n.x(), -n.z(), n.y()));

		return new Point2(
				(Math.PI + sc.azimuthal()) / (2.0 * Math.PI),
				sc.polar() / Math.PI
		);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#isClosed()
	 */
	public boolean isClosed() {
		return true;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	public Box3 boundingBox() {
		return this.sphere.boundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		return this.sphere;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersects(ca.eandb.jmist.math.Box3)
	 */
	@Override
	public boolean intersects(Box3 box) {

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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public void generateRandomSurfacePoint(ShadingContext context) {
		Random random = context.getRandom();
		Point3 p = sphere.center().plus(RandomUtil.uniformOnSphere(sphere.radius(), random).toCartesian());
		Intersection x = newSurfacePoint(p);
		x.prepareShadingContext(context);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getSurfaceArea()
	 */
	@Override
	public double getSurfaceArea() {
		return sphere.surfaceArea();
	}

	/** The <code>Sphere</code> describing this <code>SceneElement</code>. */
	private final Sphere sphere;

}
