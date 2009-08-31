/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.painter.UniformPainter;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * An ideally reflective <code>Material</code>.
 * @author Brad Kimmel
 */
public final class MirrorMaterial extends OpaqueMaterial {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 3451068808276962270L;

	/**
	 * Creates a new <code>MirrorMaterial</code>.
	 * @param reflectance The reflectance <code>Painter</code> of this mirror.
	 */
	public MirrorMaterial(Painter reflectance) {
		this.reflectance = reflectance;
	}

	/**
	 * Creates a new <code>MirrorMaterial</code>.
	 * @param reflectance The reflectance <code>Spectrum</code> of this mirror.
	 */
	public MirrorMaterial(Spectrum reflectance) {
		this(new UniformPainter(reflectance));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	@Override
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint, WavelengthPacket lambda, Random rng) {

		Vector3 out = Optics.reflect(v, x.getShadingNormal());

		return ScatteredRay.specular(new Ray3(x.getPosition(), out),
				reflectance.getColor(x, lambda), 1.0);

	}

	/**
	 * The reflectance <code>Painter</code> of this
	 * <code>MirrorMaterial</code>.
	 */
	private final Painter reflectance;

}
