/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.HPoint3;

/**
 * @author Brad
 *
 */
public final class LightTerminalNode extends AbstractPathNode implements
		PathNode {

	private LightNode child;

	private Color bsdf;

	public LightTerminalNode(PathInfo pathInfo) {
		super(pathInfo);
	}

	public void setChild(LightNode child) {
		this.child = child;
		this.bsdf = getBSDF(child);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#expand()
	 */
	public PathNode expand() {
		throw new IllegalStateException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getBSDF()
	 */
	public Color getBSDF() {
		return bsdf;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getBSDF(ca.eandb.jmist.framework.gi2.PathNode, ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public Color getBSDF(PathNode from, PathNode to) {
		if (from != null || !(to instanceof LightNode)) {
			return null;
		}
		return getBSDF((LightNode) to);
	}

	private Color getBSDF(LightNode light) {
		return light.getRadiantExitance().divide(
				light.getLightSampler().getTotalEmittedPower());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getBackwardPDF()
	 */
	public double getBackwardPDF() {
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getBackwardPDF(ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public double getBackwardPDF(PathNode from) {
		return from == null ? 1.0 : 0.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getChild()
	 */
	public PathNode getChild() {
		return child;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCosine(ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public double getCosine(PathNode node) {
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCumulativeWeight()
	 */
	public Color getCumulativeWeight() {
		PathInfo pi = getPathInfo();
		ColorModel cm = pi.getColorModel();
		WavelengthPacket lambda = pi.getWavelengthPacket();
		return cm.getWhite(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getDepth()
	 */
	public int getDepth() {
		return -1;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getForwardPDF()
	 */
	public double getForwardPDF() {
		return getForwardPDF(child);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getForwardPDF(ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public double getForwardPDF(PathNode to) {
		return to instanceof LightNode ? getForwardPDF((LightNode) to) : 0.0;
	}

	private double getForwardPDF(LightNode light) {
		return light.getSourcePDF();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getGeometricFactor()
	 */
	public double getGeometricFactor() {
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getParent()
	 */
	public PathNode getParent() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPosition()
	 */
	public HPoint3 getPosition() {
		return HPoint3.ZERO;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isOnLightPath()
	 */
	public boolean isOnLightPath() {
		return true;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isSpecular()
	 */
	public boolean isSpecular() {
		return child.isSourceSingular();
	}

}
