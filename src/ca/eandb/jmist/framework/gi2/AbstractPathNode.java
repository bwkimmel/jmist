/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.color.Color;

/**
 * @author Brad
 *
 */
public abstract class AbstractPathNode implements PathNode {

	private final PathInfo pathInfo;

	protected AbstractPathNode(PathInfo pathInfo) {
		this.pathInfo = pathInfo;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getBackwardBSDF(ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public Color getBackwardBSDF(PathNode from) {
		return getBSDF(from, getChild());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getForwardBSDF(ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public final Color getForwardBSDF(PathNode to) {
		return getBSDF(getParent(), to);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isAtInfinity()
	 */
	public final boolean isAtInfinity() {
		return getPosition().isVector();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isOnEyePath()
	 */
	public final boolean isOnEyePath() {
		return !isOnLightPath();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPathInfo()
	 */
	public final PathInfo getPathInfo() {
		return pathInfo;
	}

}
