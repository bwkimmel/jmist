/**
 *
 */
package ca.eandb.jmist.framework.caster;

import ca.eandb.jmist.framework.Geometry;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.RayCaster;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad
 *
 */
public final class GeometryRayCaster implements RayCaster {

	private final Geometry geometry;

	/**
	 * @param geometry
	 */
	public GeometryRayCaster(Geometry geometry) {
		this.geometry = geometry;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RayCaster#castRay(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.math.Interval)
	 */
	@Override
	public Intersection castRay(Ray3 ray, Interval I) {
		ray = new Ray3(ray.pointAt(I.minimum()), ray.direction());
		Intersection x = NearestIntersectionRecorder.computeNearestIntersection(ray, geometry);
		return x != null && x.distance() < I.length() ? x : null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.math.Interval)
	 */
	@Override
	public boolean visibility(Ray3 ray, Interval I) {
		return geometry.visibility(ray, I);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Point3, ca.eandb.jmist.math.Point3)
	 */
	@Override
	public boolean visibility(Point3 p, Point3 q) {
		return geometry.visibility(p, q);
	}

}
