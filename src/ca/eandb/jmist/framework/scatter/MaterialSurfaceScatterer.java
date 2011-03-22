/**
 * 
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.monochrome.MonochromeColorModel;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>SurfaceScatterer</code> that uses a <code>Material</code> for scattering.
 * @author Brad Kimmel
 */
public final class MaterialSurfaceScatterer implements SurfaceScatterer {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -6083464393181438496L;
	
	/** The <code>Material</code> to use for scattering. */
	private final Material material;
	
	/**
	 * Creates a new <code>MaterialSurfaceScatterer</code>.
	 * @param material The <code>Material</code> to use for scattering.
	 */
	public MaterialSurfaceScatterer(Material material) {
		this.material = material;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, double, ca.eandb.jmist.framework.Random)
	 */
	@Override
	public Vector3 scatter(final SurfacePointGeometry x, Vector3 v, boolean adjoint,
			double wavelength, Random rnd) {

		ColorModel cm = new MonochromeColorModel(wavelength);
		Color white = cm.sample(rnd);
		SurfacePoint surf = new SurfacePoint() {
			public Medium getAmbientMedium() {
				return Medium.VACUUM;
			}
			public Material getMaterial() {
				return material;
			}
			public Basis3 getBasis() {
				return x.getBasis();
			}
			public Vector3 getNormal() {
				return x.getNormal();
			}
			public Point3 getPosition() {
				return x.getPosition();
			}
			public int getPrimitiveIndex() {
				return x.getPrimitiveIndex();
			}
			public Basis3 getShadingBasis() {
				return x.getShadingBasis();
			}
			public Vector3 getShadingNormal() {
				return x.getShadingNormal();
			}
			public Vector3 getTangent() {
				return x.getTangent();
			}
			public Point2 getUV() {
				return x.getUV();
			}
		};
		ScatteredRay sr = material.scatter(surf, v, adjoint, white.getWavelengthPacket(), rnd.next(), rnd.next(), rnd.next());
		return sr != null ? sr.getRay().direction() : null;
		
	}

}