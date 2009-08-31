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
public final class SurfaceLightNode extends LightTerminalNode {

	private final SurfacePoint surf;

	/**
	 * @param pathInfo
	 */
	public SurfaceLightNode(PathInfo pathInfo, SurfacePoint surf) {
		super(pathInfo);
		this.surf = surf;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getCosine(ca.eandb.jmist.math.Vector3)
	 */
	public double getCosine(Vector3 v) {
		Vector3 n = surf.getShadingNormal();
		return v.unit().dot(n);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPDF()
	 */
	public double getPDF() {
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPosition()
	 */
	public HPoint3 getPosition() {
		return surf.getPosition();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isSpecular()
	 */
	public boolean isSpecular() {
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#sample(ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay sample(Random rnd) {
		PathInfo path = getPathInfo();
		WavelengthPacket lambda = path.getWavelengthPacket();
		Material material = surf.getMaterial();
		return material.emit(surf, lambda, rnd);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#scatter(ca.eandb.jmist.math.Vector3)
	 */
	public Color scatter(Vector3 v) {
		PathInfo path = getPathInfo();
		WavelengthPacket lambda = path.getWavelengthPacket();
		Material material = surf.getMaterial();
		return material.emission(surf, v, lambda);
	}

}
