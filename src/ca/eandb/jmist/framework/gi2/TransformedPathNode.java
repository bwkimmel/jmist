/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public abstract class TransformedPathNode extends AbstractPathNode {

	protected final PathNode inner;

	protected final AffineMatrix3 localToWorld;

	protected final AffineMatrix3 worldToLocal;

	/**
	 * @param pathInfo
	 */
	public TransformedPathNode(PathNode inner, AffineMatrix3 localToWorld, AffineMatrix3 worldToLocal) {
		super(inner.getPathInfo());
		this.inner = inner;
		this.localToWorld = localToWorld;
		this.worldToLocal = worldToLocal;
	}

	public TransformedPathNode(PathNode inner, AffineMatrix3 worldToLocal) {
		this(inner, worldToLocal, worldToLocal.inverse());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCosine(ca.eandb.jmist.math.Vector3)
	 */
	public double getCosine(Vector3 v) {
		v = worldToLocal.times(v);
		return inner.getCosine(v);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCumulativeWeight()
	 */
	public Color getCumulativeWeight() {
		return inner.getCumulativeWeight();
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
		return inner.getPDF();
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
		return localToWorld.times(inner.getPosition());
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
		ScatteredRay sr = inner.sample(rnd);
		return sr != null ? sr.transform(localToWorld) : null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#scatter(ca.eandb.jmist.math.Vector3)
	 */
	public Color scatter(Vector3 v) {
		v = worldToLocal.times(v);
		return inner.scatter(v);
	}

}
