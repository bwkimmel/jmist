/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.math.Point2;

/**
 * @author Brad
 *
 */
public interface BidiPathStrategy {

	PathNode traceLightPath(Light light, PathInfo pathInfo, Random rnd);

	PathNode traceEyePath(Lens lens, Point2 p, PathInfo pathInfo, Random rnd);

	double getWeight(PathNode lightNode, PathNode eyeNode);

}
