/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.ScatterRecorder;
import ca.eandb.jmist.framework.ScatterResult;
import ca.eandb.jmist.framework.Spectrum;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Tuple;
import ca.eandb.jmist.math.Vector3;

/**
 * An ideally reflective <code>Material</code>.
 * @author Brad Kimmel
 */
public final class MirrorMaterial extends OpaqueMaterial {

	/**
	 * Creates a new <code>MirrorMaterial</code>.
	 * @param reflectance The reflectance <code>Spectrum</code> of this mirror.
	 */
	public MirrorMaterial(Spectrum reflectance) {
		this.reflectance = reflectance;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractMaterial#scatter(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.toolkit.Tuple, ca.eandb.jmist.framework.ScatterRecorder)
	 */
	@Override
	public void scatter(Intersection x, Tuple wavelengths,
			ScatterRecorder recorder) {

		Vector3 out = Optics.reflect(x.incident(), x.microfacetNormal());
		double[] weights = this.reflectance.sample(wavelengths, null);

		recorder.record(ScatterResult.specular(new Ray3(x.location(), out),
				wavelengths, weights));

	}

	/**
	 * The reflectance <code>Spectrum</code> of this
	 * <code>MirrorMaterial</code>.
	 */
	private final Spectrum reflectance;

}
