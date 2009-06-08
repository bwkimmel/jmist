/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.ScatterRecorder;
import ca.eandb.jmist.framework.ScatterResult;
import ca.eandb.jmist.framework.Spectrum;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.math.Complex;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Tuple;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * A dielectric <code>Material</code> that refracts but does not absorb
 * light.
 * @author Brad Kimmel
 */
public class DielectricMaterial extends AbstractMaterial {

	/**
	 * Creates a new <code>DielectricMaterial</code>.
	 * @param refractiveIndex The refractive index <code>Color</code> of
	 * 		this dielectric material.
	 */
	public DielectricMaterial(Color refractiveIndex) {
		this.refractiveIndex = refractiveIndex;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.toolkit.Point3)
	 */
	public Color extinctionIndex(Point3 p) {
		return ColorModel.getInstance().getBlack();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.toolkit.Point3)
	 */
	public Color refractiveIndex(Point3 p) {
		return this.refractiveIndex;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.math.Ray3, double)
	 */
	public Color transmittance(Ray3 ray, double distance) {
		return ColorModel.getInstance().getWhite();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.framework.ScatterRecorder)
	 */
	@Override
	public void scatter(Intersection x, ScatterRecorder recorder) {

		ColorModel	cm			= ColorModel.getInstance();
		Point3		p			= x.location();
		Color		n1			= x.ambientMedium().refractiveIndex(p);
		Color		k1			= x.ambientMedium().extinctionIndex(p);
		Vector3		in			= x.incident();
		Vector3		normal		= x.microfacetNormal();
		boolean		fromSide	= x.normal().dot(in) < 0.0;
		Color		R			= Optics.reflectance(in, normal, n1, k1, refractiveIndex, null);
		Color		T			= cm.getWhite().minus(R);

		{
			Vector3		out		= Optics.reflect(in, normal);
			boolean		toSide	= x.normal().dot(out) >= 0.0;

			if (fromSide == toSide) {
				// FIXME: should weight reflected and refracted ray according to
				// some measure of R and T, rather than equally.
				recorder.record(ScatterResult.specular(new Ray3(p, out), R));
			}
		}


		{
			Color		imp		= T; // TODO: make this importance * T, where importance is a property of the Intersection
			int			channel	= -1;

			double		total	= ColorUtil.getTotalChannelValue(imp);
			double		rnd		= Math.random() * total;
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
			Complex		eta2	= new Complex(refractiveIndex.getValue(channel));
			Vector3		out		= Optics.refract(in, eta1, eta2, normal);
			boolean		toSide	= x.normal().dot(out) >= 0.0;

			T					= T.disperse(channel);

			if (fromSide != toSide) {
				recorder.record(ScatterResult.transmitSpecular(new Ray3(p, out), T));
			}
		}

	}

	/** The refractive index <code>Color</code> of this dielectric. */
	private final Color refractiveIndex;

}
