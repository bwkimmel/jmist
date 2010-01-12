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
public final class UniformWeightedStrategy implements BidiPathStrategy {

	/** Serialization version ID. */
	private static final long serialVersionUID = 214808820256943457L;

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
	 * @see ca.eandb.jmist.framework.path.BidiPathStrategy#getWeight(ca.eandb.jmist.framework.path.PathNode, ca.eandb.jmist.framework.path.PathNode)
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
	 * @see ca.eandb.jmist.framework.path.BidiPathStrategy#traceEyePath(ca.eandb.jmist.framework.Lens, ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.path.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public PathNode traceEyePath(Lens lens, Point2 p, PathInfo pathInfo,
			Random rnd) {
		if (maxEyeDepth > 0) {
			PathNode head = lens.sample(p, pathInfo, rnd.next(), rnd.next(), rnd.next());
			return PathUtil.expand(head, maxEyeDepth - 1, rnd);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.BidiPathStrategy#traceLightPath(ca.eandb.jmist.framework.Light, ca.eandb.jmist.framework.path.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public PathNode traceLightPath(Light light, PathInfo pathInfo, Random rnd) {
		if (maxLightDepth > 0) {
			PathNode head = light.sample(pathInfo, rnd.next(), rnd.next(), rnd.next());
			return PathUtil.expand(head, maxLightDepth - 1, rnd);
		}
		return null;
	}

}
