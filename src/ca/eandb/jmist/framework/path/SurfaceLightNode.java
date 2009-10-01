/**
 *
 */
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
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
	public SurfaceLightNode(PathInfo pathInfo, SurfacePoint surf, double ru, double rv, double rj) {
		super(pathInfo, ru, rv, rj);
		this.surf = surf;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getCosine(ca.eandb.jmist.math.Vector3)
	 */
	public double getCosine(Vector3 v) {
		Vector3 n = surf.getShadingNormal();
		return v.unit().dot(n);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getPDF()
	 */
	public double getPDF() {
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getPosition()
	 */
	public HPoint3 getPosition() {
		return surf.getPosition();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#isSpecular()
	 */
	public boolean isSpecular() {
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#sample(double, double, double)
	 */
	public ScatteredRay sample(double ru, double rv, double rj) {
		PathInfo path = getPathInfo();
		WavelengthPacket lambda = path.getWavelengthPacket();
		Material material = surf.getMaterial();
		return material.emit(surf, lambda, ru, rv, rj);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#scatter(ca.eandb.jmist.math.Vector3)
	 */
	public Color scatter(Vector3 v) {
		PathInfo path = getPathInfo();
		WavelengthPacket lambda = path.getWavelengthPacket();
		Material material = surf.getMaterial();
		return material.emission(surf, v, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getPDF(ca.eandb.jmist.math.Vector3)
	 */
	public double getPDF(Vector3 v) {
		PathInfo path = getPathInfo();
		WavelengthPacket lambda = path.getWavelengthPacket();
		Material material = surf.getMaterial();
		return material.getEmissionPDF(surf, v, lambda);
	}

	@Override
	public PathNode reverse(PathNode newParent, PathNode grandChild) {
		if (newParent != null) {
			PathNode child = (grandChild != null) ? grandChild.getParent() : null;
			if (grandChild != null) {
				if (!PathUtil.isSameNode(child, newParent)) {
					throw new IllegalArgumentException(
							"newParent and grandChild.getParent() are different.");
				} else if (!PathUtil.isSameNode(child.getParent(), this)) {
					throw new IllegalArgumentException(
							"grandChild is not a grandchild of this node.");
				} else if (!PathUtil.isSameNode(newParent.getParent(),
						grandChild)) {
					throw new IllegalArgumentException(
							"grandChild and newParent.getParent() are different.");
				}
			}
			
			Vector3 v = PathUtil.getDirection(newParent, this);
			Point3 origin = newParent.isAtInfinity()
					? surf.getPosition().minus(v)
					: newParent.getPosition().toPoint3();
			Ray3 ray = new Ray3(origin, v);			
			ScatteredRay sr;
			
			if (grandChild != null) {
				double rpdf = grandChild.getReversePDF();
				double pdf = grandChild.getPDF();
				Color color = grandChild.getCumulativeWeight()
						.divide(child.getCumulativeWeight())
						.times(pdf / rpdf);
				sr = grandChild.isSpecular()
						? ScatteredRay.specular(ray, color, rpdf)
						: ScatteredRay.diffuse(ray, color, rpdf);
			} else { // grandChild == null
				double pdf = newParent.getPDF(v);
				Color color = newParent.scatter(v).divide(pdf);
				sr = ScatteredRay.diffuse(ray, color, pdf);
			}

			return new SurfaceNode(newParent, sr, surf, getRU(), getRV(), getRJ());
		} else { // newParent == null
			if (grandChild != null) {
				throw new IllegalArgumentException(
						"newParent == null && grandChild != null");
			}
			return null;
		}
	}

}
