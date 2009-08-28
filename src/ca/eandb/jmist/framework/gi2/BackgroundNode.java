/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
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
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.ScatteringNode#getSourceRadiance()
	 */
	public Color getSourceRadiance() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#expand()
	 */
	public ScatteringNode expand() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCosine(ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public double getCosine(PathNode node) {
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPosition()
	 */
	public HPoint3 getPosition() {
		return direction;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#scatterTo(ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public Color scatterTo(PathNode node) {
		PathInfo path = getPathInfo();
		ColorModel cm = path.getColorModel();
		WavelengthPacket lambda = path.getWavelengthPacket();
		return cm.getBlack(lambda);
	}

}
