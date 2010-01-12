/**
 *
 */
package ca.eandb.jmist.framework.job.bidi;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.PathNode;
import ca.eandb.jmist.framework.path.PathUtil;
import ca.eandb.jmist.math.Point2;

/**
 * @author Brad
 *
 */
public final class PathTracingStrategy implements BidiPathStrategy {

	/** Serialization version ID. */
	private static final long serialVersionUID = -2707765386658171598L;

	private final int maxDepth;

	public PathTracingStrategy(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.BidiPathStrategy#getWeight(ca.eandb.jmist.framework.path.PathNode, ca.eandb.jmist.framework.path.PathNode)
	 */
	public double getWeight(PathNode lightNode, PathNode eyeNode) {
		if (eyeNode == null) {
			return 0.0;
		}
		if (lightNode == null) {
			int eyeDepthDiffuse = eyeNode.getDepth();
			while (eyeNode.getDepth() > 0) {
				if (eyeNode.isSpecular()) {
					eyeDepthDiffuse--;
				}
				eyeNode = eyeNode.getParent();
			}
			return eyeDepthDiffuse == 1 ? 1.0 : 0.0;
		}
		return lightNode.getDepth() == 0 && eyeNode.getDepth() > 0 ? 1.0 : 0.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.BidiPathStrategy#traceEyePath(ca.eandb.jmist.framework.Lens, ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.path.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public PathNode traceEyePath(Lens lens, Point2 p, PathInfo pathInfo,
			Random rnd) {
		PathNode head = lens.sample(p, pathInfo, rnd.next(), rnd.next(), rnd.next());
		return PathUtil.expand(head, maxDepth - 1, rnd);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.BidiPathStrategy#traceLightPath(ca.eandb.jmist.framework.Light, ca.eandb.jmist.framework.path.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public PathNode traceLightPath(Light light, PathInfo pathInfo, Random rnd) {
		return light.sample(pathInfo, rnd.next(), rnd.next(), rnd.next());
	}

}
