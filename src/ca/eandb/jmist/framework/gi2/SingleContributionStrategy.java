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
public final class SingleContributionStrategy implements
		BidiPathStrategy {

	private final int lightDepth;

	private final int eyeDepth;

	public SingleContributionStrategy(int lightDepth, int eyeDepth) {
		this.lightDepth = lightDepth;
		this.eyeDepth = eyeDepth;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.BidiPathStrategy#getWeight(ca.eandb.jmist.framework.gi2.PathNode, ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public double getWeight(PathNode lightNode, PathNode eyeNode) {
		int s = lightNode != null ? lightNode.getDepth() + 1 : 0;
		int t = eyeNode != null ? eyeNode.getDepth() + 1 : 0;
		return (s == lightDepth && t == eyeDepth) ? 1.0 : 0.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.BidiPathStrategy#traceEyePath(ca.eandb.jmist.framework.Lens, ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.gi2.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public PathNode traceEyePath(Lens lens, Point2 p, PathInfo pathInfo,
			Random rnd) {
		if (eyeDepth > 0) {
			PathNode head = lens.sample(p, pathInfo, rnd);
			return PathUtil.expand(head, eyeDepth - 1, rnd);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.BidiPathStrategy#traceLightPath(ca.eandb.jmist.framework.Light, ca.eandb.jmist.framework.gi2.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public PathNode traceLightPath(Light light, PathInfo pathInfo, Random rnd) {
		if (lightDepth > 0) {
			PathNode head = light.sample(pathInfo, rnd);
			return PathUtil.expand(head, lightDepth - 1, rnd);
		}
		return null;
	}

}
