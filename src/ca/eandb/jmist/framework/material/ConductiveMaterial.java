/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRayRecorder;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Complex;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A conductive <code>Material</code> with a complex refractive index.
 * @author Brad Kimmel
 */
public final class ConductiveMaterial extends AbstractMaterial {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -8516201792111267898L;

	/**
	 * Creates a new <code>ConductiveMaterial</code>.
	 * @param n The real part of the refractive index <code>Spectrum</code>.
	 * @param k The imaginary part of the refractive index
	 * 		<code>Spectrum</code>.
	 * @param alpha The absorption coefficient <code>Spectrum</code>.
	 */
	public ConductiveMaterial(Spectrum n, Spectrum k, Spectrum alpha) {
		this.n = n;
		this.k = k;
		this.alpha = alpha;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
		return k.sample(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
		return n.sample(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.math.Ray3, double, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color transmittance(Ray3 ray, final double distance, WavelengthPacket lambda) {
		return alpha != null ? alpha.sample(lambda).times(-distance).exp() : lambda.getColorModel().getBlack(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random, ca.eandb.jmist.framework.ScatteredRayRecorder)
	 */
	@Override
	public void scatter(SurfacePoint x, Vector3 v, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder) {

		ColorModel	cm			= lambda.getColorModel();
		Point3		p			= x.getPosition();
		Medium		medium		= x.getAmbientMedium();
		Color		n1			= medium.refractiveIndex(p, lambda);
		Color		k1			= medium.extinctionIndex(p, lambda);
		Color		n2			= n.sample(lambda);
		Color		k2			= k.sample(lambda);
		Vector3		normal		= x.getShadingNormal();
		boolean		fromSide	= x.getNormal().dot(v) < 0.0;
		Color		R			= Optics.reflectance(v, normal, n1, k1, n2, k2);
		Color		T			= cm.getWhite(lambda).minus(R);

		{
			Vector3		out		= Optics.reflect(v, normal);
			boolean		toSide	= x.getNormal().dot(out) >= 0.0;

			if (fromSide == toSide) {
				recorder.add(ScatteredRay.specular(new Ray3(p, out), R));
			}
		}

		{
			Color		imp		= T; // TODO: make this importance * T, where importance is a property of the Intersection
			int			channel	= -1;

			double		total	= ColorUtil.getTotalChannelValue(imp);
			double		rnd		= rng.next() * total;
			double		sum		= 0.0;

			for (int i = 0; i < cm.getNumChannels(); i++) {
				double value = imp.getValue(i);
				sum += value;
				if (rnd < sum) {
					T = T.divide(value / total);
					channel = i;
					break;
				}
			}

			if (channel < 0) {
				return;
			}

			Complex		eta1	= new Complex(n1.getValue(channel), k1.getValue(channel));
			Complex		eta2	= new Complex(n2.getValue(channel), k2.getValue(channel));
			Vector3		out		= Optics.refract(v, eta1, eta2, normal);
			boolean		toSide	= x.getNormal().dot(out) >= 0.0;

			T					= T.disperse(channel);

			if (fromSide != toSide) {
				recorder.add(ScatteredRay.transmitSpecular(new Ray3(p, out), T));
			}
		}

	}

	/** The real part of the refractive index. */
	private final Spectrum n;

	/** The imaginary part of the refractive index. */
	private final Spectrum k;

	/** The absorption coefficient. */
	private final Spectrum alpha;

}
