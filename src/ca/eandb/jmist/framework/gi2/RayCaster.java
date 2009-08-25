/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad
 *
 */
public interface RayCaster extends VisibilityFunction3 {

	ScatteringNode castRay(Ray3 ray, PathNode parent, Color weight, double pdf,
			boolean isSpecular);

}
