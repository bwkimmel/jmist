/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point2;

/**
 * @author Brad
 *
 */
public interface EyeNode extends PathNode {

	ScatteredRay sample(Point2 p, Random rnd);

	ScatteringNode expand(Point2 p, Random rnd);

	Point2 project(HPoint3 x);

}
