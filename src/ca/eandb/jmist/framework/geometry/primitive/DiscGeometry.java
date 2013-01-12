/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector2;
import ca.eandb.jmist.math.Vector3;

/**
 * A circular plane <code>SceneElement</code>.
 * @author Brad Kimmel
 */
public final class DiscGeometry extends PrimitiveGeometry {

	/** Serialization version ID. */
	private static final long serialVersionUID = 7682212284130537132L;

	/**
	 * Creates a new <code>DiscGeometry</code>.
	 * @param center The <code>Point3</code> at the center of the disc.
	 * @param normal The <code>Vector3</code> that is perpendicular to the
	 * 		disc.
	 * @param radius The radius of the disc (in meters).
	 * @param twoSided A value indicating whether the disc is two sided.
	 */
	public DiscGeometry(Point3 center, Vector3 normal, double radius, boolean twoSided) {
		this.plane = Plane3.throughPoint(center, normal);
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

		Basis3 basis = Basis3.fromW(this.plane.normal(), Basis3.Orientation.RIGHT_HANDED);
		Vector3 u = basis.u();
		Vector3 v = basis.v();

		double r = this.boundingSphere.radius();
		double ri = r * Math.hypot(u.x(), v.x());
		double rj = r * Math.hypot(u.y(), v.y());
		double rk = r * Math.hypot(u.z(), v.z());

		Point3 c = this.boundingSphere.center();

		return new Box3(
				c.x() - ri,
				c.y() - rj,
				c.z() - rk,
				c.x() + ri,
				c.y() + rj,
				c.z() + rk
				);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		return this.boundingSphere;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public void generateRandomSurfacePoint(ShadingContext context, double ru, double rv, double rj) {
		Vector2 uv = RandomUtil.uniformOnDisc(boundingSphere.radius(), ru, rv).toCartesian();
		Basis3 basis = Basis3.fromW(this.plane.normal(), Basis3.Orientation.RIGHT_HANDED);

		Point3 p = boundingSphere.center()
				.plus(basis.u().times(uv.x()))
				.plus(basis.v().times(uv.y()));

		int id = (twoSided && RandomUtil.coin(rj))
				? DISC_SURFACE_BOTTOM
				: DISC_SURFACE_TOP;

		Intersection x = newSurfacePoint(p, id);
		x.prepareShadingContext(context);
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
