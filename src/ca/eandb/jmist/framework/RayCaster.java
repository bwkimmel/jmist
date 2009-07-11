/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad Kimmel
 *
 */
public interface RayCaster extends VisibilityFunction3, Serializable {

	Intersection castRay(Ray3 ray);

}
