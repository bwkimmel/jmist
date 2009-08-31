/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class ScaledLightNode extends AbstractPathNode implements
		LightNode {

	private final LightNode inner;

	private final double pdf;

	private ScaledLightNode(double pdf, LightNode inner) {
		super(inner.getPathInfo());
		this.pdf = pdf;
		this.inner = inner;
	}

	public static LightNode create(double pdf, LightNode node) {
		if (node instanceof ScaledLightNode) {
			ScaledLightNode n = (ScaledLightNode) node;
			return new ScaledLightNode(pdf * n.pdf, n.inner);
		} else {
			return new ScaledLightNode(pdf, node);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCosine(ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public double getCosine(Vector3 v) {
		return inner.getCosine(v);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCumulativeWeight()
	 */
	public Color getCumulativeWeight() {
		return inner.getCumulativeWeight().divide(pdf);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getDepth()
	 */
	public int getDepth() {
		return inner.getDepth();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getGeometricFactor()
	 */
	public double getGeometricFactor() {
		return inner.getGeometricFactor();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPDF()
	 */
	public double getPDF() {
		return inner.getPDF() * pdf;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getParent()
	 */
	public PathNode getParent() {
		return inner.getParent();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPosition()
	 */
	public HPoint3 getPosition() {
		return inner.getPosition();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isAtInfinity()
	 */
	public boolean isAtInfinity() {
		return inner.isAtInfinity();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isOnLightPath()
	 */
	public boolean isOnLightPath() {
		return inner.isOnLightPath();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isSpecular()
	 */
	public boolean isSpecular() {
		return inner.isSpecular();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#sample(ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay sample(Random rnd) {
		return inner.sample(rnd);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#scatter(ca.eandb.jmist.math.Vector3)
	 */
	public Color scatter(Vector3 v) {
		return inner.scatter(v);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPDF(ca.eandb.jmist.math.Vector3)
	 */
	public double getPDF(Vector3 v) {
		return inner.getPDF(v);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getReversePDF(ca.eandb.jmist.math.Vector3)
	 */
	public double getReversePDF(Vector3 v) {
		return inner.getReversePDF(v);
	}

}
