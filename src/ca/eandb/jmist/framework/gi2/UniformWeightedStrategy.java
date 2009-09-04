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
public final class UniformWeightedStrategy implements BidiPathStrategy {

	private final int maxLightDepth;

	private final int maxEyeDepth;

	/**
	 * @param maxLightDepth
	 * @param maxEyeDepth
	 */
	public UniformWeightedStrategy(int maxLightDepth, int maxEyeDepth) {
		this.maxLightDepth = maxLightDepth;
		this.maxEyeDepth = maxEyeDepth;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.BidiPathStrategy#getWeight(ca.eandb.jmist.framework.gi2.PathNode, ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public double getWeight(PathNode lightNode, PathNode eyeNode) {
		int s = lightNode != null ? lightNode.getDepth() + 1 : 0;
		int t = eyeNode != null ? eyeNode.getDepth() + 1 : 0;
//		double[] weights = new double[s + t];

		// FIXME account for maximum eye and light depth
		int k = s + t + 1;
		while (eyeNode != null) {
			if (eyeNode.isSpecular()) {
				k--;
			}
			eyeNode = eyeNode.getParent();
		}

		while (lightNode != null) {
			if (lightNode.isSpecular()) {
				k--;
			}
			lightNode = lightNode.getParent();
		}

		return 1.0 / (double) k;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.BidiPathStrategy#traceEyePath(ca.eandb.jmist.framework.Lens, ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.gi2.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public PathNode traceEyePath(Lens lens, Point2 p, PathInfo pathInfo,
			Random rnd) {
		if (maxEyeDepth > 0) {
			PathNode head = lens.sample(p, pathInfo, rnd);
			return PathUtil.expand(head, maxEyeDepth - 1, rnd);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.BidiPathStrategy#traceLightPath(ca.eandb.jmist.framework.Light, ca.eandb.jmist.framework.gi2.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public PathNode traceLightPath(Light light, PathInfo pathInfo, Random rnd) {
		if (maxLightDepth > 0) {
			PathNode head = light.sample(pathInfo, rnd);
			return PathUtil.expand(head, maxLightDepth - 1, rnd);
		}
		return null;
	}

}
