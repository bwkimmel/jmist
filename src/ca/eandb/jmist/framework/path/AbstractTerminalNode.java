/**
 *
 */
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Vector3;

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
	 * @see ca.eandb.jmist.framework.path.PathNode#getCumulativeWeight()
	 */
	public final Color getCumulativeWeight() {
		return getWhite();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getDepth()
	 */
	public final int getDepth() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getParent()
	 */
	public final PathNode getParent() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getGeometricFactor()
	 */
	public final double getGeometricFactor() {
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getReversePDF(ca.eandb.jmist.math.Vector3)
	 */
	public final double getReversePDF(Vector3 v) {
		return 0.0;
	}

}
