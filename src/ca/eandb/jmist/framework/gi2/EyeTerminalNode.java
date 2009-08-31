/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Point2;

/**
 * @author Brad
 *
 */
public abstract class EyeTerminalNode extends AbstractTerminalNode implements
		EyeNode {

	/**
	 * @param pathInfo
	 */
	public EyeTerminalNode(PathInfo pathInfo) {
		super(pathInfo);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isOnLightPath()
	 */
	public final boolean isOnLightPath() {
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#sample(ca.eandb.jmist.framework.Random)
	 */
	public final ScatteredRay sample(Random rnd) {
		Point2 p = RandomUtil.canonical2(rnd);
		return sample(p, rnd);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.EyeNode#expand(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.Random)
	 */
	public final ScatteringNode expand(Point2 p, Random rnd) {
		return trace(sample(p, rnd));
	}

}
