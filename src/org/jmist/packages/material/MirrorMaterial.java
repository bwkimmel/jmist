/**
 *
 */
package org.jmist.packages.material;

import org.jmist.framework.Intersection;
import org.jmist.framework.OpaqueMaterial;
import org.jmist.framework.ScatterRecorder;
import org.jmist.framework.ScatterResult;
import org.jmist.framework.Spectrum;
import org.jmist.toolkit.Optics;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Tuple;
import org.jmist.toolkit.Vector3;

/**
 * An ideally reflective <code>Material</code>.
 * @author bkimmel
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
	 * @see org.jmist.framework.AbstractMaterial#scatter(org.jmist.framework.Intersection, org.jmist.toolkit.Tuple, org.jmist.framework.ScatterRecorder)
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
