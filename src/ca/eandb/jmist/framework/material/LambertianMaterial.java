/**
 *
 */
package ca.eandb.jmist.framework.material;

import java.io.Serializable;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRayRecorder;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.painter.UniformPainter;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Material</code> that reflects light equally in all directions in
 * the upper hemisphere.
 * @author Brad Kimmel
 */
public final class LambertianMaterial extends OpaqueMaterial implements
		Serializable {

	/**
	 * Creates a new <code>LambertianMaterial</code> that does not emit light.
	 * @param reflectance The reflectance <code>Painter</code>.
	 */
	public LambertianMaterial(Painter reflectance) {
		this(reflectance, null);
	}

	/**
	 * Creates a new <code>LambertianMaterial</code> that emits light.
	 * @param reflectance The reflectance <code>Painter</code>.
	 * @param emittance The emission <code>Painter</code>.
	 */
	public LambertianMaterial(Painter reflectance, Painter emittance) {
		this.reflectance = reflectance;
		this.emittance = emittance;
	}

	/**
	 * Creates a new <code>LambertianMaterial</code> that does not emit light.
	 * @param reflectance The reflectance <code>Spectrum</code>.
	 */
	public LambertianMaterial(Spectrum reflectance) {
		this(reflectance != null ? new UniformPainter(reflectance) : null);
	}

	/**
	 * Creates a new <code>LambertianMaterial</code> that emits light.
	 * @param reflectance The reflectance <code>Spectrum</code>.
	 * @param emittance The emission <code>Spectrum</code>.
	 */
	public LambertianMaterial(Spectrum reflectance, Spectrum emittance) {
		this(reflectance != null ? new UniformPainter(reflectance) : null, emittance != null ? new UniformPainter(emittance) : null);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractMaterial#isEmissive()
	 */
	@Override
	public boolean isEmissive() {
		return (this.emittance != null);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#emission(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda) {
		if (this.emittance != null && x.getNormal().dot(out) > 0.0) {
			return emittance.getColor(x, lambda);
		} else {
			return lambda.getColorModel().getBlack(lambda);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#emit(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random, ca.eandb.jmist.framework.ScatteredRayRecorder)
	 */
	@Override
	public void emit(SurfacePoint x, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder) {

		if (this.emittance != null) {

			SphericalCoordinates out = RandomUtil.diffuse(rng);
			Ray3 ray = new Ray3(x.getPosition(), out.toCartesian(x.getShadingBasis()));

			if (x.getNormal().dot(ray.direction()) > 0.0) {
				recorder.add(ScatteredRay.diffuse(ray, emittance.getColor(x, lambda)));
			}

		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random, ca.eandb.jmist.framework.ScatteredRayRecorder)
	 */
	@Override
	public void scatter(SurfacePoint x, Vector3 v, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder) {

		if (this.reflectance != null) {

			SphericalCoordinates out = RandomUtil.diffuse(rng);
			Ray3 ray = new Ray3(x.getPosition(), out.toCartesian(x.getShadingBasis()));

			if (ray.direction().dot(x.getNormal()) > 0.0) {
				recorder.add(ScatteredRay.diffuse(ray, reflectance.getColor(x, lambda)));
			}

		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scattering(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color scattering(SurfacePoint x, Vector3 in, Vector3 out, WavelengthPacket lambda) {

		Vector3 n = x.getNormal();
		boolean fromFront = (n.dot(in) < 0.0);
		boolean toFront = (n.dot(out) > 0.0);

		if (this.reflectance != null && toFront == fromFront) {
			return reflectance.getColor(x, lambda);
		} else {
			return lambda.getColorModel().getBlack(lambda);
		}

	}

	/** The reflectance <code>Painter</code> of this <code>Material</code>. */
	private final Painter reflectance;

	/** The emittance <code>Painter</code> of this <code>Material</code>. */
	private final Painter emittance;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 485410070543495668L;

}
