/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.color.Color;

/**
 * @author Brad
 *
 */
public abstract class AbstractTerminalNode extends AbstractPathNode {

	/**
	 * @param pathInfo
	 */
	public AbstractTerminalNode(PathInfo pathInfo) {
		super(pathInfo);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCumulativeWeight()
	 */
	public final Color getCumulativeWeight() {
		return getWhite();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getDepth()
	 */
	public final int getDepth() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getParent()
	 */
	public final PathNode getParent() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getGeometricFactor()
	 */
	public final double getGeometricFactor() {
		return 1.0;
	}

}
