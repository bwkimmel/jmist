/**
 *
 */
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.Scene;
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
	 * @see ca.eandb.jmist.framework.path.ScatteringNode#getSourcePDF()
	 */
	public double getSourcePDF() {
		PathInfo path = getPathInfo();
		Scene scene = path.getScene();
		Light light = scene.getLight();
		return light.getSamplePDF(surf, path);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.ScatteringNode#getSourcePDF(ca.eandb.jmist.math.Vector3)
	 */
	public double getSourcePDF(Vector3 v) {
		PathInfo path = getPathInfo();
		Material material = surf.getMaterial();
		WavelengthPacket lambda = path.getWavelengthPacket();
		return material.getEmissionPDF(surf, v, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.ScatteringNode#getSourceRadiance()
	 */
	public Color getSourceRadiance() {
		PathInfo path = getPathInfo();
		Material material = surf.getMaterial();
		WavelengthPacket lambda = path.getWavelengthPacket();
		Vector3 out = PathUtil.getDirection(this, getParent());
		return material.emission(surf, out, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.ScatteringNode#isOnLightSource()
	 */
	public boolean isOnLightSource() {
		return surf.getMaterial().isEmissive();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#sample(ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay sample(Random rnd) {
		PathInfo path = getPathInfo();
		WavelengthPacket lambda = path.getWavelengthPacket();
		Vector3 v = PathUtil.getDirection(getParent(), this);
		Material material = surf.getMaterial();
		return material.scatter(surf, v, isOnEyePath(), lambda, rnd);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getCosine(ca.eandb.jmist.math.Vector3)
	 */
	public double getCosine(Vector3 v) {
		Vector3 n = surf.getShadingNormal();
		return v.unit().dot(n);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getPosition()
	 */
	public HPoint3 getPosition() {
		return surf.getPosition();
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#reverse(ca.eandb.jmist.framework.path.PathNode, ca.eandb.jmist.framework.path.PathNode)
	 */
	public PathNode reverse(PathNode newParent, PathNode grandChild) {
		if (newParent != null) {
			PathNode child = (grandChild != null) ? grandChild.getParent() : null;
			if (grandChild != null) {
				if (!PathUtil.isSameNode(child, newParent)) {
					throw new IllegalArgumentException(
							"newParent and grandChild.getParent() are different.");
				} else if (child.getParent() != this) {
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

			return new SurfaceNode(newParent, sr, surf);
		} else { // newParent == null
			if (grandChild != null) {
				throw new IllegalArgumentException(
						"newParent == null && grandChild != null");
			}
			
			PathInfo pi = getPathInfo();
			Scene scene = pi.getScene();
			Light light = scene.getLight();
			double pdf = light.getSamplePDF(surf, pi);
			
			return ScaledLightNode.create(pdf, new SurfaceLightNode(pi, surf));
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#scatter(ca.eandb.jmist.math.Vector3)
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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getPDF(ca.eandb.jmist.math.Vector3)
	 */
	public double getPDF(Vector3 out) {
		PathInfo path = getPathInfo();
		PathNode parent = getParent();
		Material material = surf.getMaterial();
		WavelengthPacket lambda = path.getWavelengthPacket();
		boolean adjoint = isOnEyePath();
		Vector3 in = PathUtil.getDirection(parent, this);
		return material.getScatteringPDF(surf, in, out, adjoint, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getReversePDF(ca.eandb.jmist.math.Vector3)
	 */
	public double getReversePDF(Vector3 in) {
		PathInfo path = getPathInfo();
		PathNode parent = getParent();
		Material material = surf.getMaterial();
		WavelengthPacket lambda = path.getWavelengthPacket();
		boolean adjoint = isOnLightPath();
		Vector3 out = PathUtil.getDirection(this, parent);
		return material.getScatteringPDF(surf, in, out, adjoint, lambda);
	}

//	@Override
//	public PathNode reverse(PathNode newParent, PathNode grandChild) {		
//		if (newParent != null) {
//			if (grandChild != null) {
//				if (grandChild.getParent() == null
//						|| grandChild.getParent().getParent() != this) {
//					throw new IllegalArgumentException(
//							"grandChild.getParent().getParent() != this");
//				}
//				if (!PathUtil.isSameNode(newParent, grandChild.getParent())) {
//					throw new IllegalArgumentException(
//							"grandChild.getParent() and newParent are different nodes.");
//				}
//			}
//			ScatteredRay sr;
//			if (grandChild.isSpecular()) {
//				Color color = grandChild.getCumulativeWeight().divide(grandChild.getParent().getCumulativeWeight());
//				double pdf = grandChild.getReversePDF();
//				color = color.times(grandChild.getPDF() / pdf);
//				Point3 pos = newParent.getPosition().toPoint3();
//				Vector3 v = PathUtil.getDirection(newParent, this);
//				Ray3 ray = new Ray3(pos, v);
//				sr = ScatteredRay.specular(ray, color, pdf);
//			}
//		} else { // newParent == null
//			if (grandChild != null) {
//				throw new IllegalArgumentException("newParent == null && grandChild != null");
//			}
//			
//		}
//		// TODO Auto-generated method stub
//		return null;
//	}

}
