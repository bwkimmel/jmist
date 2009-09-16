/**
 *
 */
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRay.Type;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Vector3;

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

	private final boolean onLightPath;

	private double geometricFactor;
	
	private double reversePDF;

	/**
	 * @param pathInfo
	 */
	public AbstractScatteringNode(PathNode parent, ScatteredRay sr) {
		super(parent.getPathInfo());
		this.parent = parent;
		this.depth = parent.getDepth() + 1;
		this.cumulativeWeight = parent.getCumulativeWeight().times(sr.getColor());
		this.specular = (sr.getType() == Type.SPECULAR);
		this.pdf = sr.getPDF();
		this.geometricFactor = Double.NaN;
		this.reversePDF = Double.NaN;
		this.onLightPath = parent.isOnLightPath();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getCumulativeWeight()
	 */
	public final Color getCumulativeWeight() {
		return cumulativeWeight;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getDepth()
	 */
	public final int getDepth() {
		return depth;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getGeometricFactor()
	 */
	public final double getGeometricFactor() {
		if (Double.isNaN(geometricFactor)) {
			geometricFactor = PathUtil.getGeometricFactor(parent, this);
		}
		return geometricFactor;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getPDF()
	 */
	public final double getPDF() {
		return pdf;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getParent()
	 */
	public final PathNode getParent() {
		return parent;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getReversePDF()
	 */
	public final double getReversePDF() {
		assert(parent != null);
		if (Double.isNaN(reversePDF)) {
			if (specular) {
				// FIXME should not assume symmetry in PDF
				reversePDF = (parent.getParent() != null) ? pdf : 1.0;
			} else {
				Vector3 v = PathUtil.getDirection(this, parent);
				reversePDF = parent.getReversePDF(v);
			}
		}
		return reversePDF;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#isOnLightPath()
	 */
	public final boolean isOnLightPath() {
		return onLightPath;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#isSpecular()
	 */
	public final boolean isSpecular() {
		return specular;
	}

}
