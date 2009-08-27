/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRay.Type;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad
 *
 */
public abstract class InternalPathNode extends AbstractPathNode implements
		PathNode {

	private final PathNode parent;

	private PathNode child;

	private Color bsdf;

	private double forwardPDF;

	private double backwardPDF;

	private double geometricFactor;

	private boolean specular;

	private final int depth;

	private final boolean onLightPath;

	protected InternalPathNode(PathNode parent) {
		super(parent.getPathInfo());
		this.parent = parent;
		this.onLightPath = parent.isOnLightPath();
		this.depth = parent.getDepth() + 1;
		this.geometricFactor = Double.NaN; // uninitialized
	}

	protected final PathNode trace(ScatteredRay sr) {
		PathInfo path = getPathInfo();
		RayCaster caster = path.getRayCaster();
		Ray3 ray = sr.getRay();
		this.specular = (sr.getType() == Type.SPECULAR);
//		this.backwardPDF = ???;
//		this.forwardPDF = ???;
//		this.bsdf = ???;
		this.child = caster.castRay(ray, this);
		return child;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getBSDF()
	 */
	public final Color getBSDF() {
		return bsdf;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getBackwardPDF()
	 */
	public final double getBackwardPDF() {
		return backwardPDF;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getChild()
	 */
	public final PathNode getChild() {
		return child;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getDepth()
	 */
	public final int getDepth() {
		return depth;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getForwardPDF()
	 */
	public final double getForwardPDF() {
		return forwardPDF;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getGeometricFactor()
	 */
	public final double getGeometricFactor() {
		if (Double.isNaN(geometricFactor)) {
			assert(parent != null);
			geometricFactor = PathUtil.getGeometricFactor(parent, this);
		}
		return geometricFactor;
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
