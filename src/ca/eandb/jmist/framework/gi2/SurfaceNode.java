/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class SurfaceNode extends AbstractScatteringNode {

	private final SurfacePoint surf;

	/**
	 * @param parent
	 * @param sr
	 * @param surf
	 */
	public SurfaceNode(PathNode parent, ScatteredRay sr, SurfacePoint surf) {
		super(parent, sr);
		this.surf = surf;
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
		Material material = surf.getMaterial();
		WavelengthPacket lambda = path.getWavelengthPacket();
		Vector3 out = PathUtil.getDirection(this, getParent());
		return material.emission(surf, out, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#sample(ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay sample(Random rnd) {
		PathInfo path = getPathInfo();
		WavelengthPacket lambda = path.getWavelengthPacket();
		Vector3 v = PathUtil.getDirection(getParent(), this);
		Material material = surf.getMaterial();
		return material.scatter(surf, v, isOnEyePath(), lambda, rnd);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCosine(ca.eandb.jmist.math.Vector3)
	 */
	public double getCosine(Vector3 v) {
		Vector3 n = surf.getShadingNormal();
		return v.unit().dot(n);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPosition()
	 */
	public HPoint3 getPosition() {
		return surf.getPosition();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#scatter(ca.eandb.jmist.math.Vector3)
	 */
	public Color scatter(Vector3 v) {
		PathInfo path = getPathInfo();
		PathNode parent = getParent();
		Material material = surf.getMaterial();
		WavelengthPacket lambda = path.getWavelengthPacket();
		Vector3 in, out;
		if (isOnLightPath()) {
			in = PathUtil.getDirection(parent, this);
			out = v;
		} else { // isOnEyePath()
			in = v.opposite();
			out = PathUtil.getDirection(this, parent);
		}
		return material.bsdf(surf, in, out, lambda);
	}

}
