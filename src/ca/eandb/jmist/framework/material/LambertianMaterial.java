/**
 *
 */
package ca.eandb.jmist.framework.material;

import java.io.Serializable;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.ScatterRecorder;
import ca.eandb.jmist.framework.ScatterResult;
import ca.eandb.jmist.framework.Spectrum;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.spectrum.ScaledSpectrum;
import ca.eandb.jmist.math.RandomUtil;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Tuple;
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
	 * @param reflectance The reflectance <code>Spectrum</code>.
	 */
	public LambertianMaterial(Spectrum reflectance) {
		this(reflectance, null);
	}

	/**
	 * Creates a new <code>LambertianMaterial</code> that emits light.
	 * @param reflectance The reflectance <code>Spectrum</code>.
	 * @param emittance The emission <code>Spectrum</code>.
	 */
	public LambertianMaterial(Spectrum reflectance, Spectrum emittance) {
		this.reflectance = reflectance;
		this.emittance = emittance;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractMaterial#isEmissive()
	 */
	@Override
	public boolean isEmissive() {
		return (this.emittance != null);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emission(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.toolkit.Vector3)
	 */
	@Override
	public Spectrum emission(SurfacePoint x, Vector3 out) {

		if (this.emittance == null || x.normal().dot(out) < 0.0) {
			return Spectrum.ZERO;
		}

		double ndotv = x.microfacetNormal().dot(out);
		return ndotv > 0.0 ? new ScaledSpectrum(ndotv, this.emittance) : Spectrum.ZERO;

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractMaterial#emit(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.toolkit.Tuple, ca.eandb.jmist.framework.ScatterRecorder)
	 */
	@Override
	public void emit(SurfacePoint x, Tuple wavelengths, ScatterRecorder recorder) {

		if (this.emittance != null) {

			SphericalCoordinates out = RandomUtil.uniformOnUpperHemisphere();
			Ray3 ray = new Ray3(x.location(), out.toCartesian(x.microfacetBasis()));

			if (x.normal().dot(ray.direction()) > 0.0) {
				double[] radiance = this.emittance.sample(wavelengths, null);
				recorder.record(ScatterResult.diffuse(ray, wavelengths, radiance));
			}

		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractMaterial#scatter(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.toolkit.Tuple, ca.eandb.jmist.framework.ScatterRecorder)
	 */
	@Override
	public void scatter(Intersection x, Tuple wavelengths, ScatterRecorder recorder) {

		if (this.reflectance != null) {

			SphericalCoordinates out = RandomUtil.diffuse();
			Ray3 ray = new Ray3(x.location(), out.toCartesian(x.microfacetBasis()));

			if (ray.direction().dot(x.normal()) > 0.0) {
				double[] weights = this.reflectance.sample(wavelengths, null);
				recorder.record(ScatterResult.diffuse(ray, wavelengths, weights));
			}

		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractMaterial#scattering(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.toolkit.Vector3)
	 */
	@Override
	public Spectrum scattering(Intersection x, Vector3 out) {

		boolean toFront = (x.normal().dot(out) > 0.0);

		if (this.reflectance != null && x.front() == toFront) {
			return this.reflectance;
		} else {
			return Spectrum.ZERO;
		}

	}

	/** The reflectance <code>Spectrum</code> of this <code>Material</code>. */
	private final Spectrum reflectance;

	/** The emittance <code>Spectrum</code> of this <code>Material</code>. */
	private final Spectrum emittance;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 485410070543495668L;

}
