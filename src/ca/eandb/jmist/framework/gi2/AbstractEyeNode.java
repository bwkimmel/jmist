/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.color.Color;

/**
 * @author Brad
 *
 */
public abstract class AbstractEyeNode extends InternalPathNode implements
		EyeNode {

	/**
	 * @param parent
	 */
	public AbstractEyeNode(PathInfo pathInfo) {
		this(new EyeTerminalNode(pathInfo));
	}

	private AbstractEyeNode(EyeTerminalNode parent) {
		super(parent);
		parent.setChild(this);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getBSDF(ca.eandb.jmist.framework.gi2.PathNode, ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public final Color getBSDF(PathNode from, PathNode to) {
		return (from == getParent()) ? getImportance(to).divide(
				getImportanceExitance()) : null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getBackwardPDF(ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public final double getBackwardPDF(PathNode from) {
		return (from == getParent()) ? 1.0 : 0.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCumulativeWeight()
	 */
	public final Color getCumulativeWeight() {
		return getImportanceExitance().divide(getAperturePDF());
	}

}
