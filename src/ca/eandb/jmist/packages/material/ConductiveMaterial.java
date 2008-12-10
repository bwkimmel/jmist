/**
 *
 */
package ca.eandb.jmist.packages.material;

import ca.eandb.jmist.framework.AbstractMaterial;
import ca.eandb.jmist.framework.AbstractSpectrum;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.ScatterRecorder;
import ca.eandb.jmist.framework.ScatterResult;
import ca.eandb.jmist.framework.Spectrum;
import ca.eandb.jmist.toolkit.Complex;
import ca.eandb.jmist.toolkit.Optics;
import ca.eandb.jmist.toolkit.Point3;
import ca.eandb.jmist.toolkit.Ray3;
import ca.eandb.jmist.toolkit.Tuple;
import ca.eandb.jmist.toolkit.Vector3;
import ca.eandb.jmist.util.ArrayUtil;
import ca.eandb.jmist.util.MathUtil;

/**
 * A conductive <code>Material</code> with a complex refractive index.
 * @author Brad Kimmel
 */
public final class ConductiveMaterial extends AbstractMaterial {

	/**
	 * Creates a new <code>ConductiveMaterial</code>.
	 * @param n The real part of the refractive index <code>Spectrum</code>.
	 * @param k The imaginary part of the refractive index
	 * 		<code>Spectrum</code>.
	 */
	public ConductiveMaterial(Spectrum n, Spectrum k) {
		this.n = n;
		this.k = k;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.toolkit.Point3)
	 */
	public Spectrum extinctionIndex(Point3 p) {
		return this.k;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.toolkit.Point3)
	 */
	public Spectrum refractiveIndex(Point3 p) {
		return this.n;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.toolkit.Ray3, double)
	 */
	public Spectrum transmittance(Ray3 ray, final double distance) {

		return new AbstractSpectrum() {

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.AbstractSpectrum#sample(double)
			 */
			@Override
			public double sample(double wavelength) {
				return Math.exp(-4.0 * Math.PI * k.sample(wavelength) * distance / wavelength);
			}

		};

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractMaterial#scatter(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.toolkit.Tuple, ca.eandb.jmist.framework.ScatterRecorder)
	 */
	@Override
	public void scatter(Intersection x, Tuple wavelengths,
			ScatterRecorder recorder) {

		Point3		p			= x.location();
		Complex[]	n1			= Complex.array(
										x.ambientMedium().refractiveIndex(p).sample(wavelengths, null),
										x.ambientMedium().extinctionIndex(p).sample(wavelengths, null)
								);
		Complex[]	n2			= Complex.array(n.sample(wavelengths, null), k.sample(wavelengths, null));
		double[]	R			= new double[n2.length];
		double[]	T			= new double[n2.length];
		Vector3		in			= x.incident();
		Vector3		normal		= x.microfacetNormal();
		boolean		fromSide	= x.normal().dot(in) < 0.0;

		for (int i = 0; i < R.length; i++) {
			R[i] = Optics.reflectance(in, n1[i], n2[i], normal);
		}

		{
			Vector3		out		= Optics.reflect(in, normal);
			boolean		toSide	= x.normal().dot(out) >= 0.0;

			if (fromSide == toSide) {
				recorder.record(ScatterResult.specular(new Ray3(p, out), wavelengths, R));
			}
		}

		MathUtil.subtract(ArrayUtil.setAll(T, 1.0), R);

		if (areEqual(n1) && areEqual(n2)) {

			Vector3		out		= Optics.refract(in, n1[0], n2[0], normal);
			boolean		toSide	= x.normal().dot(out) >= 0.0;

			if (fromSide != toSide) {
				recorder.record(ScatterResult.specular(new Ray3(p, out), wavelengths, T));
			}

		} else {

			for (int i = 0; i < T.length; i++) {

				Vector3	out		= Optics.refract(in, n1[i], n2[i], normal);
				boolean	toSide	= x.normal().dot(out) >= 0.0;

				if (fromSide != toSide) {
					recorder.record(ScatterResult.disperse(new Ray3(p, out), i, wavelengths.at(i), T[i], 1.0));
				}

			}

		}

	}

	/**
	 * Determines if all values in an array of <code>Complex</code> values are
	 * equal (to within {@link MathUtil#EPSILON}).
	 * @param z An array of <code>Complex</code> values to compare.
	 * @return A value indicating if the values in <code>z</code> are all equal
	 * 		to one another to within {@link MathUtil#EPSILON}.
	 * @see MathUtil#EPSILON
	 */
	private final boolean areEqual(Complex[] z) {

		Complex mean = Complex.ZERO;

		for (int i = 0; i < z.length; i++) {
			mean = mean.plus(z[i]);
		}

		mean = mean.divide((double) z.length);

		for (int i = 0; i < z.length; i++) {
			if (MathUtil.equal(z[i].re(), mean.re())
					&& MathUtil.equal(z[i].im(), mean.im())) {
				return false;
			}
		}

		return true;

	}

	/** The real part of the refractive index. */
	private final Spectrum n;

	/** The imaginary part of the refractive index. */
	private final Spectrum k;

}
