/**
 *
 */
package ca.eandb.jmist.framework.gi2;


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
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isOnLightPath()
	 */
	public final boolean isOnLightPath() {
		return false;
	}

}
