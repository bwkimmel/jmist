/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Complex;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A dielectric <code>Material</code> that refracts but does not absorb
 * light.
 * @author Brad Kimmel
 */
public class DielectricMaterial extends AbstractMaterial {

	/** Serialization version ID. */
	private static final long serialVersionUID = -9036003391744538613L;

	/**
	 * Creates a new <code>DielectricMaterial</code>.
	 * @param refractiveIndex The refractive index <code>Spectrum</code> of
	 * 		this dielectric material.
	 * @param disperse A value indicating if this material should be
	 * 		dispersive.
	 */
	public DielectricMaterial(Spectrum refractiveIndex, boolean disperse) {
		this.refractiveIndex = refractiveIndex;
		this.disperse = disperse;
	}

	/**
	 * Creates a new <code>DielectricMaterial</code>.
	 * @param refractiveIndex The refractive index <code>Spectrum</code> of
	 * 		this dielectric material.
	 */
	public DielectricMaterial(Spectrum refractiveIndex) {
		this(refractiveIndex, true);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
		return lambda.getColorModel().getBlack(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
		return refractiveIndex.sample(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.math.Ray3, double, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color transmittance(Ray3 ray, double distance, WavelengthPacket lambda) {
		return lambda.getColorModel().getWhite(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	@Override
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint, WavelengthPacket lambda, Random rng) {

		ColorModel	cm			= lambda.getColorModel();
		Point3		p			= x.getPosition();
		Medium		medium		= x.getAmbientMedium();
		Color		n1			= medium.refractiveIndex(p, lambda);
		Color		k1			= medium.extinctionIndex(p, lambda);
		Color		n2			= refractiveIndex.sample(lambda);
		Vector3		normal		= x.getShadingNormal();
		boolean		fromSide	= x.getNormal().dot(v) < 0.0;
		Color		R			= MaterialUtil.reflectance(v, n1, k1, n2, null, normal);
		Color		T			= cm.getWhite(lambda).minus(R);
		double		r			= ColorUtil.getMeanChannelValue(R);

		if (RandomUtil.bernoulli(r, rng)) {
			Vector3		out		= Optics.reflect(v, normal);
			boolean		toSide	= x.getNormal().dot(out) >= 0.0;

			if (fromSide == toSide) {
				return ScatteredRay.specular(new Ray3(p, out), R.divide(r), 1.0);
			}
		} else {

		if (false && disperse) {
//			for (int i = 0, channels = cm.getNumChannels(); i < channels; i++) {
//				Complex		eta1	= new Complex(n1.getValue(i), k1.getValue(i));
//				Complex		eta2	= new Complex(n2.getValue(i));
//				Vector3		out		= Optics.refract(v, eta1, eta2, normal);
//				boolean		toSide	= x.getNormal().dot(out) >= 0.0;
//
//				if (fromSide != toSide) {
//					recorder.add(ScatteredRay.transmitSpecular(new Ray3(p, out), T.disperse(i), 1.0));
//				}
//			}
		} else { // !disperse
			double		n1avg	= ColorUtil.getMeanChannelValue(n1);
			double		k1avg	= ColorUtil.getMeanChannelValue(k1);
			double		n2avg	= ColorUtil.getMeanChannelValue(n2);
			Complex		eta1	= new Complex(n1avg, k1avg);
			Complex		eta2	= new Complex(n2avg);
			Vector3		out		= Optics.refract(v, eta1, eta2, normal);
			boolean		toSide	= x.getNormal().dot(out) >= 0.0;

			if (fromSide != toSide) {
				return ScatteredRay.transmitSpecular(new Ray3(p, out), T.divide(1 - r), 1.0);
			}
		}
		}

		return null;

	}

	/** The refractive index <code>Color</code> of this dielectric. */
	private final Spectrum refractiveIndex;

	/** A value indicating if this material is dispersive. */
	private final boolean disperse;

}
