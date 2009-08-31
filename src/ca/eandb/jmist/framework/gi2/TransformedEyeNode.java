/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class TransformedEyeNode extends TransformedPathNode implements
		EyeNode {

	/**
	 * @param inner
	 * @param localToWorld
	 * @param worldToLocal
	 */
	public TransformedEyeNode(EyeNode inner, AffineMatrix3 localToWorld,
			AffineMatrix3 worldToLocal) {
		super(inner, localToWorld, worldToLocal);
	}

	/**
	 * @param inner
	 * @param worldToLocal
	 */
	public TransformedEyeNode(EyeNode inner, AffineMatrix3 worldToLocal) {
		super(inner, worldToLocal);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.EyeNode#expand(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.Random)
	 */
	public ScatteringNode expand(Point2 p, Random rnd) {
		return trace(sample(p, rnd));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.EyeNode#project(ca.eandb.jmist.math.HPoint3)
	 */
	public Point2 project(HPoint3 x) {
		x = worldToLocal.times(x);
		return ((EyeNode) inner).project(x);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.EyeNode#sample(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay sample(Point2 p, Random rnd) {
		ScatteredRay sr = ((EyeNode) inner).sample(p, rnd);
		return sr.transform(localToWorld);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPDF(ca.eandb.jmist.math.Vector3)
	 */
	public double getPDF(Vector3 v) {
		v = worldToLocal.times(v);
		return inner.getPDF(v);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getReversePDF(ca.eandb.jmist.math.Vector3)
	 */
	public double getReversePDF(Vector3 v) {
		return 0.0;
	}

}
