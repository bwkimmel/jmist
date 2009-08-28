/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRays;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class SurfaceNode extends AbstractScatteringNode {

	private final ShadingContext context;

	/**
	 * @param parent
	 * @param sr
	 * @param context
	 */
	public SurfaceNode(PathNode parent, ScatteredRay sr, ShadingContext context) {
		super(parent, sr);
		this.context = context;
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
		PathInfo path = getPathInfo();
		Material material = context.getMaterial();
		WavelengthPacket lambda = path.getWavelengthPacket();
		Vector3 out = PathUtil.getDirection(this, getParent());
		return material.emission(context, out, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#expand()
	 */
	public ScatteringNode expand() {
		ScatteredRays scat = context.getScatteredRays();
		ScatteredRay sr = scat.getRandomScatteredRay(false);
		return trace(sr);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCosine(ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public double getCosine(PathNode node) {
		Vector3 n = context.getShadingNormal();
		Vector3 v = PathUtil.getDirection(this, node);
		return v.dot(n);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPosition()
	 */
	public HPoint3 getPosition() {
		return context.getPosition();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#scatterTo(ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public Color scatterTo(PathNode node) {
		PathInfo path = getPathInfo();
		PathNode parent = getParent();
		Material material = context.getMaterial();
		WavelengthPacket lambda = path.getWavelengthPacket();
		Vector3 in, out;
		if (isOnLightPath()) {
			in = PathUtil.getDirection(parent, this);
			out = PathUtil.getDirection(this, node);
		} else { // isOnEyePath()
			in = PathUtil.getDirection(node, this);
			out = PathUtil.getDirection(this, parent);
		}
		return material.scattering(context, in, out, lambda);
	}

}
