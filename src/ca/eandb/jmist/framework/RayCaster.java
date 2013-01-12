/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.math.Ray3;

/**
 * Computes ray-geometry intersections with an underlying scene.
 * @author Brad Kimmel
 */
public interface RayCaster extends VisibilityFunction3, Serializable {

	/**
	 * Computes the nearest ray-geometry intersections along the specified ray.
	 * @param ray The <code>Ray3</code> to test.
	 * @return The nearest <code>Intersection</code> along the ray.
	 */
	Intersection castRay(Ray3 ray);

}
