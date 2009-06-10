/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.ScatterRecorder;
import ca.eandb.jmist.framework.ScatterResult;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
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
	 * Creates a new <code>ConductiveMaterial</code>.
	 * @param n The real part of the refractive index <code>Color</code>.
	 * @param k The imaginary part of the refractive index
	 * 		<code>Color</code>.
	 * @param alpha The absorption coefficient.
	 */
	public ConductiveMaterial(Color n, Color k, Color alpha) {
		this.n = n;
		this.k = k;
		this.alpha = alpha;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.toolkit.Point3)
	 */
	public Color extinctionIndex(Point3 p) {
		return this.k;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.toolkit.Point3)
	 */
	public Color refractiveIndex(Point3 p) {
		return this.n;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.toolkit.Ray3, double)
	 */
	public Color transmittance(Ray3 ray, final double distance) {
		return alpha != null ? alpha.times(-distance).exp() : ColorModel.getInstance().getBlack();
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
		Vector3		normal		= x.shadingNormal();
		boolean		fromSide	= x.normal().dot(in) < 0.0;
		Color		R			= Optics.reflectance(in, normal, n1, k1, n, k);
		Color		T			= cm.getWhite().minus(R);

		{
			Vector3		out		= Optics.reflect(in, normal);
			boolean		toSide	= x.normal().dot(out) >= 0.0;

			if (fromSide == toSide) {
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
			Complex		eta2	= new Complex(n.getValue(channel), k.getValue(channel));
			Vector3		out		= Optics.refract(in, eta1, eta2, normal);
			boolean		toSide	= x.normal().dot(out) >= 0.0;

			T					= T.disperse(channel);

			if (fromSide != toSide) {
				recorder.record(ScatterResult.transmitSpecular(new Ray3(p, out), T));
			}
		}

	}

	/** The real part of the refractive index. */
	private final Color n;

	/** The imaginary part of the refractive index. */
	private final Color k;

	/** The absorption coefficient. */
	private final Color alpha;

}
