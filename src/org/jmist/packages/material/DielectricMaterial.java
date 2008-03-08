/**
 *
 */
package org.jmist.packages.material;

import org.jmist.framework.AbstractMaterial;
import org.jmist.framework.Intersection;
import org.jmist.framework.ScatterRecorder;
import org.jmist.framework.ScatterResult;
import org.jmist.framework.Spectrum;
import org.jmist.toolkit.Optics;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Tuple;
import org.jmist.toolkit.Vector3;
import org.jmist.util.ArrayUtil;
import org.jmist.util.MathUtil;

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
	public Spectrum extinctionIndex(Point3 p) {
		return Spectrum.ZERO;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Medium#refractiveIndex(org.jmist.toolkit.Point3)
	 */
	public Spectrum refractiveIndex(Point3 p) {
		return this.refractiveIndex;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Medium#transmittance(org.jmist.toolkit.Ray3, double)
	 */
	public Spectrum transmittance(Ray3 ray, double distance) {
		return Spectrum.ONE;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractMaterial#scatter(org.jmist.framework.Intersection, org.jmist.toolkit.Tuple, org.jmist.framework.ScatterRecorder)
	 */
	@Override
	public void scatter(Intersection x, Tuple wavelengths, ScatterRecorder recorder) {

		Point3		p			= x.location();
		double[]	n1			= x.ambientMedium().refractiveIndex(p).sample(wavelengths, null);
		double[]	n2			= this.refractiveIndex.sample(wavelengths, null);
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

		if (MathUtil.areEqual(n1) && MathUtil.areEqual(n2)) {

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

//	/* (non-Javadoc)
//	 * @see org.jmist.framework.AbstractMaterial#scatter(org.jmist.framework.Intersection, org.jmist.toolkit.Tuple)
//	 */
//	public Ray3 scatter(Intersection x, Tuple wavelengths, double[] radiance) {
//
//		Point3		p			= x.location();
//		double[]	n1			= x.ambientMedium().refractiveIndex(p).sample(wavelengths, null);
//		double[]	n2			= this.refractiveIndex.sample(wavelengths, null);
//		double[]	R			= new double[n2.length];
//		Vector3		in			= x.incident();
//		Vector3		normal		= x.microfacetNormal();
//		int			key			= RandomUtil.categorical(radiance);
//
//		R[key] = Optics.reflectance(in, n1[key], n2[key], normal);
//
//		boolean		reflect		= Math.random() < R[key];
//		Vector3		out			= reflect
//										? Optics.reflect(in, normal)
//										: Optics.refract(in, n1[key], n2[key], normal);
//
//		boolean		fromSide	= x.normal().dot(in) < 0.0;
//		boolean		toSide		= x.normal().dot(out) >= 0.0;
//
//		if (reflect ^ (fromSide == toSide)) {
//			return null;
//		}
//
//		if (reflect) {
//
//			for (int i = 0; i < n2.length; i++) {
//				if (i != key) {
//					R[i] = Optics.reflectance(in, n1[key], n2[key], normal) / R[key];
//				}
//			}
//
//		} else { /* !reflect */
//
//			for (int i = 1; i < n2.length; i++) {
//				if (n1[i] != n1[i - 1] || n2[i] != n2[i - 1]) {
//					for (int j = 0; j < radiance.length; j++) {
//						if (j != key) {
//							radiance[j] = 0.0;
//						}
//					}
//					return new Ray3(p, out);
//				}
//			}
//
//			for (int i = 0; i < n2.length; i++) {
//				if (i != key) {
//					R[i] = (1.0 - Optics.reflectance(in, n1[i], n2[i], normal)) / (1.0 - R[key]);
//				}
//			}
//
//		}
//
//		R[key] = 1.0;
//		MathUtil.modulate(radiance, R);
//		return new Ray3(p, out);
//
//	}

	/** The refractive index <code>Spectrum</code> of this dielectric. */
	private final Spectrum refractiveIndex;

}
