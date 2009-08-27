/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.color.Color;

/**
 * @author Brad
 *
 */
public abstract class AbstractScatteringNode extends InternalPathNode {

	private final Color cumulativeWeight;

	/**
	 * @param parent
	 */
	public AbstractScatteringNode(PathNode parent) {
		super(parent);

		PathNode grandParent = parent.getParent();
		if (grandParent == null) {
			throw new IllegalArgumentException("parent.getParent() == null");
		}
		this.cumulativeWeight = grandParent.getCumulativeWeight().times(
				parent.getBSDF()).divide(parent.getForwardPDF());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCumulativeWeight()
	 */
	public final Color getCumulativeWeight() {
		return cumulativeWeight;
	}

}
