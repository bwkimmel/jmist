/**
 *
 */
package ca.eandb.jmist.framework.caster;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.RayCaster;
import ca.eandb.jmist.framework.SceneObject;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad
 *
 */
public final class GeometryRayCaster implements RayCaster {

	private final SceneObject sceneObject;

	/**
	 * @param sceneObject
	 */
	public GeometryRayCaster(SceneObject sceneObject) {
		this.sceneObject = sceneObject;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RayCaster#castRay(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public Intersection castRay(Ray3 ray) {
		return sceneObject.intersect(ray);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.math.Interval)
	 */
	@Override
	public boolean visibility(Ray3 ray, Interval I) {
		return sceneObject.visibility(ray, I);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public boolean visibility(Ray3 ray) {
		return sceneObject.visibility(ray);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Point3, ca.eandb.jmist.math.Point3)
	 */
	@Override
	public boolean visibility(Point3 p, Point3 q) {
		return sceneObject.visibility(p, q);
	}

}
