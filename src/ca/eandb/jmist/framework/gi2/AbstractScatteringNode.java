/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRay.Type;
import ca.eandb.jmist.framework.color.Color;

/**
 * @author Brad
 *
 */
public abstract class AbstractScatteringNode extends AbstractPathNode implements
		ScatteringNode {

	private final PathNode parent;

	private final int depth;

	private final Color cumulativeWeight;

	private final boolean specular;

	private final double pdf;

	private final double geometricFactor;

	private final boolean onLightPath;

	/**
	 * @param pathInfo
	 */
	public AbstractScatteringNode(PathNode parent, ScatteredRay sr) {
		super(parent.getPathInfo());
		this.parent = parent;
		this.depth = parent.getDepth() + 1;
		this.cumulativeWeight = parent.getCumulativeWeight().times(sr.getColor());
		this.specular = (sr.getType() == Type.SPECULAR);
		this.pdf = 1.0; // FIXME
		this.geometricFactor = PathUtil.getGeometricFactor(parent, this);
		this.onLightPath = parent.isOnLightPath();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCumulativeWeight()
	 */
	public final Color getCumulativeWeight() {
		return cumulativeWeight;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getDepth()
	 */
	public final int getDepth() {
		return depth;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getGeometricFactor()
	 */
	public final double getGeometricFactor() {
		return geometricFactor;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPDF()
	 */
	public final double getPDF() {
		return pdf;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getParent()
	 */
	public final PathNode getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isOnLightPath()
	 */
	public final boolean isOnLightPath() {
		return onLightPath;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isSpecular()
	 */
	public final boolean isSpecular() {
		return specular;
	}

}
