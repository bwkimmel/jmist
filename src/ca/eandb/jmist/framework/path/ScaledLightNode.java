/**
 *
 */
package ca.eandb.jmist.framework.path;

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

	private ScaledLightNode(double pdf, LightNode inner, double rj) {
		super(inner.getPathInfo(), inner.getRU(), inner.getRV(), rj);
		this.pdf = pdf;
		this.inner = inner;
	}

	public static LightNode create(double pdf, LightNode node, double rj) {
		if (node instanceof ScaledLightNode) {
			ScaledLightNode n = (ScaledLightNode) node;
			return new ScaledLightNode(pdf * n.pdf, n.inner, rj);
		} else {
			return new ScaledLightNode(pdf, node, rj);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getCosine(ca.eandb.jmist.framework.path.PathNode)
	 */
	public double getCosine(Vector3 v) {
		return inner.getCosine(v);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getCumulativeWeight()
	 */
	public Color getCumulativeWeight() {
		return inner.getCumulativeWeight().divide(pdf);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getDepth()
	 */
	public int getDepth() {
		return inner.getDepth();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getGeometricFactor()
	 */
	public double getGeometricFactor() {
		return inner.getGeometricFactor();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getPDF()
	 */
	public double getPDF() {
		return inner.getPDF() * pdf;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getParent()
	 */
	public PathNode getParent() {
		return inner.getParent();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getPosition()
	 */
	public HPoint3 getPosition() {
		return inner.getPosition();
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getReversePDF()
	 */
	public double getReversePDF() {
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#isAtInfinity()
	 */
	public boolean isAtInfinity() {
		return inner.isAtInfinity();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#isOnLightPath()
	 */
	public boolean isOnLightPath() {
		return inner.isOnLightPath();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#isSpecular()
	 */
	public boolean isSpecular() {
		return inner.isSpecular();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#sample(double, double, double)
	 */
	public ScatteredRay sample(double ru, double rv, double rj) {
		return inner.sample(ru, rv, rj);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#scatter(ca.eandb.jmist.math.Vector3)
	 */
	public Color scatter(Vector3 v) {
		return inner.scatter(v);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getPDF(ca.eandb.jmist.math.Vector3)
	 */
	public double getPDF(Vector3 v) {
		return inner.getPDF(v);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getReversePDF(ca.eandb.jmist.math.Vector3)
	 */
	public double getReversePDF(Vector3 v) {
		return inner.getReversePDF(v);
	}

	@Override
	public PathNode reverse(PathNode newParent, PathNode grandChild) {
		return inner.reverse(newParent, grandChild);
	}

}
