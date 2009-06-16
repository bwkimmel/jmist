/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad Kimmel
 *
 */
public interface RayCaster extends VisibilityFunction3 {

	Intersection castRay(Ray3 ray);

}
