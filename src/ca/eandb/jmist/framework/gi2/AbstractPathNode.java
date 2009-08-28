/**
 *
 */
package ca.eandb.jmist.framework.gi2;


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
