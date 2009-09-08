/**
 *
 */
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.math.Point2;

/**
 * @author Brad
 *
 */
public final class PurePathTracingStrategy implements BidiPathStrategy {

	/** Serialization version ID. */
	private static final long serialVersionUID = 1493406845275560911L;

	private final int maxDepth;

	public PurePathTracingStrategy(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.BidiPathStrategy#getWeight(ca.eandb.jmist.framework.path.PathNode, ca.eandb.jmist.framework.path.PathNode)
	 */
	public double getWeight(PathNode lightNode, PathNode eyeNode) {
		return lightNode == null ? 1.0 : 0.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.BidiPathStrategy#traceEyePath(ca.eandb.jmist.framework.Lens, ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.path.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public PathNode traceEyePath(Lens lens, Point2 p, PathInfo pathInfo,
			Random rnd) {
		PathNode head = lens.sample(p, pathInfo, rnd);
		return PathUtil.expand(head, maxDepth - 1, rnd);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.BidiPathStrategy#traceLightPath(ca.eandb.jmist.framework.Light, ca.eandb.jmist.framework.path.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public PathNode traceLightPath(Light light, PathInfo pathInfo, Random rnd) {
		return null;
	}

}
