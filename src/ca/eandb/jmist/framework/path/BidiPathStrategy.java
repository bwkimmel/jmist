/**
 *
 */
package ca.eandb.jmist.framework.path;

import java.io.Serializable;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.math.Point2;

/**
 * @author Brad
 *
 */
public interface BidiPathStrategy extends Serializable {

	PathNode traceLightPath(Light light, PathInfo pathInfo, Random rnd);

	PathNode traceEyePath(Lens lens, Point2 p, PathInfo pathInfo, Random rnd);

	double getWeight(PathNode lightNode, PathNode eyeNode);

}
