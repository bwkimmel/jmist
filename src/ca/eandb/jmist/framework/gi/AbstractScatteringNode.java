/**
 *
 */
package ca.eandb.jmist.framework.gi;

import ca.eandb.jmist.framework.color.Color;


/**
 * @author Brad
 *
 */
public abstract class AbstractScatteringNode extends AbstractPathNode implements
		ScatteringNode {

	private final PathNode parent;

	private final int depth;

	private final boolean onLightPath;

	/**
	 * @param nodes
	 */
	protected AbstractScatteringNode(Color value, PathNode parent) {
		super(value, parent.getFactory());
		this.parent = parent;
		this.depth = parent.getDepth() + 1;
		this.onLightPath = parent.isOnLightPath();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi.PathNode#getDepth()
	 */
	public final int getDepth() {
		return depth;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi.PathNode#getParent()
	 */
	public final PathNode getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi.PathNode#isOnLightPath()
	 */
	public final boolean isOnLightPath() {
		return onLightPath;
	}

}
