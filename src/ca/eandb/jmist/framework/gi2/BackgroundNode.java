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
public final class BackgroundNode extends AbstractScatteringNode {

	private final Vector3 direction;

	/**
	 * @param parent
	 * @param sr
	 */
	public BackgroundNode(PathNode parent, ScatteredRay sr) {
		super(parent, sr);
		this.direction = sr.getRay().direction();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.ScatteringNode#getSourcePDF()
	 */
	public double getSourcePDF() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.ScatteringNode#getSourcePDF(ca.eandb.jmist.math.Vector3)
	 */
	public double getSourcePDF(Vector3 v) {
		return 0.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.ScatteringNode#getSourceRadiance()
	 */
	public Color getSourceRadiance() {
		return getBlack();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.ScatteringNode#isOnLightSource()
	 */
	public boolean isOnLightSource() {
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#sample(ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay sample(Random rnd) {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCosine(ca.eandb.jmist.math.Vector3)
	 */
	public double getCosine(Vector3 v) {
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPosition()
	 */
	public HPoint3 getPosition() {
		return direction;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#scatter(ca.eandb.jmist.math.Vector3)
	 */
	public Color scatter(Vector3 v) {
		return getBlack();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPDF(ca.eandb.jmist.math.Vector3)
	 */
	public double getPDF(Vector3 v) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getReversePDF(ca.eandb.jmist.math.Vector3)
	 */
	public double getReversePDF(Vector3 v) {
		return 0;
	}

}
