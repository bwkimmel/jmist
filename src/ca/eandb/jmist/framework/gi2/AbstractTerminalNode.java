/**
 *
 */
package ca.eandb.jmist.framework.gi2;

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

}
