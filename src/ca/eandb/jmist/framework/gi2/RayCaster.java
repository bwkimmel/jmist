/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.VisibilityFunction3;

/**
 * @author Brad
 *
 */
public interface RayCaster extends VisibilityFunction3 {

	ScatteringNode castRay(ScatteredRay sr, PathNode parent);

}
