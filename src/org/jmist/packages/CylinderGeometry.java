/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Geometry;
import org.jmist.framework.IntersectionRecorder;
import org.jmist.framework.WeightedSurfacePoint;
import org.jmist.framework.base.AbstractGeometry;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Polynomial;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Sphere;
import org.jmist.toolkit.Vector3;
import org.jmist.util.MathUtil;

/**
 * @author bkimmel
 *
 */
public final class CylinderGeometry extends AbstractGeometry implements Geometry {

	/**
	 * Initializes the dimensions of this cylinder.
	 * @param base		the center of the base of the cylinder
	 * @param radius	the radius of the cylinder
	 * @param height	the height of the cylinder
	 */
	public CylinderGeometry(Point3 base, double radius, double height) {
		this.base = base;
		this.radius = radius;
		this.height = height;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#generateRandomSurfacePoint()
	 */
	public WeightedSurfacePoint generateRandomSurfacePoint() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#intersect(org.jmist.toolkit.Ray3, org.jmist.toolkit.Interval, org.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, Interval I, IntersectionRecorder recorder) {

		Point3	p;
		double	t;

		// first check for intersection of ray with the caps on the ends of the cylinder

		// check bottom cap
		t = (this.base.y() - ray.origin().y()) / ray.direction().y();
		if (I.contains(t))
		{
			p = ray.pointAt(t);

			if (this.base.squaredDistanceTo(p) < this.radius * this.radius)
			{
//				GeometryIntersection	x(this, ray, t, true, ray.direction().y() > 0.0, CYLINDER_SURFACE_BASE);
//				x.setPoint(p);
//				recorder.record(x);
			}
		}

		// check top cap
		t = (this.base.y() + this.height - ray.origin().y()) / ray.direction().y();
		if (I.contains(t))
		{
			p = ray.pointAt(t);

			double r = (p.x() - this.base.x()) * (p.x() - this.base.x()) + (p.z() - this.base.z()) * (p.z() - this.base.z());

			if (r < this.radius * this.radius)
			{
//				GeometryIntersection	x(this, ray, t, true, ray.direction().y() < 0.0, CYLINDER_SURFACE_TOP);
//				x.setPoint(p);
//				recorder.record(x);
			}
		}

		// now check for intersection of ray with the body
		Vector3		orig	= this.base.vectorTo(ray.origin());
		Vector3		dir		= ray.direction();

		Polynomial	f		= new Polynomial(
								dir.x() * dir.x() + dir.z() * dir.z(),
								2.0 * (orig.x() * dir.x() + orig.z() * dir.z()),
								orig.x() * orig.x() + orig.z() * orig.z() - this.radius * this.radius
							);
		double[]	x		= f.roots();

		if (x.length == 2)
		{
			// for each solution, make sure the point lies between the base and the apex
			p = ray.pointAt(x[0]);
			if (MathUtil.inRangeOO(p.y(), this.base.y(), this.base.y() + this.height))
			{
//				GeometryIntersection	isect(this, ray, x[0], true, x[0] < x[1], CYLINDER_SURFACE_BODY);
//				isect.setPoint(p);
//				recorder.record(isect);
			}

			p = ray.pointAt(x[1]);
			if (MathUtil.inRangeOO(p.y(), this.base.y(), this.base.y() + this.height))
			{
//				GeometryIntersection	isect(this, ray, x[1], true, x[0] > x[1], CYLINDER_SURFACE_BODY);
//				isect.setPoint(p);
//				recorder.record(isect);
			}
		}


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

		return new Box3(
			this.base.x() - this.radius,
			this.base.y(),
			this.base.z() - this.radius,
			this.base.x() + this.radius,
			this.base.y() + this.height,
			this.base.z() + this.radius
		);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {

		double	h = this.height / 2.0;
		double	r = Math.sqrt(this.radius * this.radius + h * h);
		Point3	c = new Point3(this.base.x(), this.base.y() + h, this.base.z());

		return new Sphere(c, r);

	}

	/** The point at the base of the cylinder */
	private final Point3 base;

	/** The radius of the cylinder */
	private final double radius;

	/** The height of the cylinder */
	private final double height;

}
