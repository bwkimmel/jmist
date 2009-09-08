/**
 *
 */
package ca.eandb.jmist.framework.path;

/**
 * @author Brad
 *
 */
public abstract class LightTerminalNode extends AbstractTerminalNode implements
		LightNode {

	/**
	 * @param pathInfo
	 */
	public LightTerminalNode(PathInfo pathInfo) {
		super(pathInfo);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#isOnLightPath()
	 */
	public final boolean isOnLightPath() {
		return true;
	}

}
