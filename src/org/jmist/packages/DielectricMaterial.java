/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.AbstractMaterial;
import org.jmist.framework.Intersection;
import org.jmist.framework.ScatterResult;
import org.jmist.framework.Spectrum;
import org.jmist.toolkit.Optics;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Tuple;
import org.jmist.toolkit.Vector3;

/**
 * A dielectric <code>Material</code> that refracts but does not absorb
 * light.
 * @author bkimmel
 */
public class DielectricMaterial extends AbstractMaterial {

	/**
	 * Creates a new <code>DielectricMaterial</code>.
	 * @param refractiveIndex The refractive index <code>Spectrum</code> of
	 * 		this dielectric material.
	 */
	public DielectricMaterial(Spectrum refractiveIndex) {
		this.refractiveIndex = refractiveIndex;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Medium#extinctionIndex(org.jmist.toolkit.Point3)
	 */
	@Override
	public Spectrum extinctionIndex(Point3 p) {
		return Spectrum.ZERO;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Medium#refractiveIndex(org.jmist.toolkit.Point3)
	 */
	@Override
	public Spectrum refractiveIndex(Point3 p) {
		return this.refractiveIndex;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Medium#transmittance(org.jmist.toolkit.Ray3, double)
	 */
	@Override
	public Spectrum transmittance(Ray3 ray, double distance) {
		return Spectrum.ONE;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractMaterial#scatter(org.jmist.framework.Intersection, org.jmist.toolkit.Tuple)
	 */
	@Override
	public ScatterResult scatter(Intersection x, Tuple wavelengths) {

		Point3		p			= x.location();
		double[]	n1			= x.ambientMedium().refractiveIndex(p).sample(wavelengths, null);
		double[]	n2			= this.refractiveIndex.sample(wavelengths, null);
		double[]	R			= new double[n2.length];
		Vector3		in			= x.incident();
		Vector3		normal		= x.microfacetNormal();
		int			key			= rnd.nextInt(n2.length);

		R[key] = Optics.reflectance(in, n1[key], n2[key], normal);

		boolean		reflect		= rnd.nextDouble() < R[key];
		Vector3		out			= reflect
										? Optics.reflect(in, normal)
										: Optics.refract(in, n1[key], n2[key], normal);

		boolean		fromSide	= x.normal().dot(in) < 0.0;
		boolean		toSide		= x.normal().dot(out) >= 0.0;

		if (reflect ^ (fromSide == toSide)) {
			return null;
		}

		if (reflect) {

			for (int i = 0; i < n2.length; i++) {
				if (i != key) {
					R[i] = Optics.reflectance(in, n1[key], n2[key], normal) / R[key];
				}
			}

		} else { /* !reflect */

			for (int i = 1; i < n2.length; i++) {
				if (n1[i] != n1[i - 1] || n2[i] != n2[i - 1]) {
					return ScatterResult.specular(new Ray3(p, out), wavelengths.at(key), 1.0);
				}
			}

			for (int i = 0; i < n2.length; i++) {
				if (i != key) {
					R[i] = (1.0 - Optics.reflectance(in, n1[i], n2[i], normal)) / (1.0 - R[key]);
				}
			}

		}

		R[key] = 1.0;
		return ScatterResult.specular(new Ray3(p, out), wavelengths, R);

	}

	/** The refractive index <code>Spectrum</code> of this dielectric. */
	private final Spectrum refractiveIndex;

	/** A <code>Random</code> number generator. */
	private static final java.util.Random rnd = new java.util.Random();

}
