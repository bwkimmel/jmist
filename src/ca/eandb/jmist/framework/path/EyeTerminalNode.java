/**
 *
 */
package ca.eandb.jmist.framework.path;


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
	 * @see ca.eandb.jmist.framework.path.PathNode#isOnLightPath()
	 */
	public final boolean isOnLightPath() {
		return false;
	}

}
