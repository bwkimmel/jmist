/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.painter.UniformPainter;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class IsotropicWardMaterial extends OpaqueMaterial {

	/** Serialization version ID. */
	private static final long serialVersionUID = 58334131261636901L;

	private final Painter diffuse;

	private final Painter specular;

	private final double alpha;

	/**
	 *
	 * @param diffuse
	 * @param specular
	 * @param alpha
	 */
	public IsotropicWardMaterial(Painter diffuse, Painter specular, double alpha) {
		this.diffuse = diffuse;
		this.specular = specular;
		this.alpha = alpha;
	}

	/**
	 *
	 * @param diffuse
	 * @param specular
	 * @param alpha
	 */
	public IsotropicWardMaterial(Spectrum diffuse, Spectrum specular, double alpha) {
		this(new UniformPainter(diffuse), new UniformPainter(specular), alpha);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#bsdf(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color bsdf(SurfacePoint x, Vector3 in, Vector3 out,
			WavelengthPacket lambda) {

		Vector3 N = x.getNormal();
		if (-N.dot(in) <= 0.0 || N.dot(out) <= 0.0) {
			return lambda.getColorModel().getBlack(lambda);
		}

		in = in.unit();
		out = out.unit();

		Color d = diffuse.getColor(x, lambda);
		Color s = specular.getColor(x, lambda);

		Vector3 n = x.getShadingNormal();

		Color D = d.divide(Math.PI);

		double ci = -n.dot(in);
		double co = n.dot(out);
		Vector3 h = out.minus(in).unit();

		double ch = n.dot(h);
		double ch2 = ch * ch;
		double sh2 = 1.0 - ch2;
		double th2 = sh2 / ch2;
		double alpha2 = alpha * alpha;

		double k = (1.0 / Math.sqrt(co * ci))
				* Math.exp(-th2 / alpha2) / (4.0 * Math.PI * alpha2);
		Color S = s.times(k);

		return D.plus(S);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#getScatteringPDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out,
			boolean adjoint, WavelengthPacket lambda) {

		Vector3 N = x.getNormal();
		if (-N.dot(in) <= 0.0 || N.dot(out) <= 0.0) {
			return 0.0;
		}

		in = in.unit();
		out = out.unit();

		double d = ColorUtil.getMeanChannelValue(diffuse.getColor(x, lambda));
		double s = ColorUtil.getMeanChannelValue(specular.getColor(x, lambda));
		double total = d + s;

		Vector3 n = x.getShadingNormal();

		double D = d / Math.PI;

		double ci = -n.dot(in);
		double co = n.dot(out);
		Vector3 h = out.minus(in).unit();

		double ch = n.dot(h);
		double ch2 = ch * ch;
		double sh2 = 1.0 - ch2;
		double th2 = sh2 / ch2;
		double alpha2 = alpha * alpha;

		double k = (1.0 / Math.sqrt(co * ci))
				* Math.exp(-th2 / alpha2) / (4.0 * Math.PI * alpha2);
		double S = s * k;

		if (D < 0 || S < 0) {
			throw new RuntimeException("crap");
		}

		return (D + S) / total;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	@Override
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, Random rng) {

		Vector3 n = x.getNormal();
		if (n.dot(v) > 0.0) {
			return null;
		}

		Color d = diffuse.getColor(x, lambda);
		Color s = specular.getColor(x, lambda);
		double davg = ColorUtil.getMeanChannelValue(d);
		double savg = ColorUtil.getMeanChannelValue(s);
		double total = davg + savg;

		Basis3 basis = x.getShadingBasis();
		Vector3 out;

		if (RandomUtil.bernoulli(davg / total, rng)) { // diffuse

			do {
				out = RandomUtil.diffuse(rng).toCartesian(basis);
			} while (n.dot(out) <= 0.0);

		} else { // specular

			do {
				double r1 = rng.next();
				double r2 = rng.next();
				SphericalCoordinates sc = new SphericalCoordinates(
						Math.atan(alpha * Math.sqrt(-Math.log(1.0 - r1))),
						2.0 * Math.PI * r2);

				Vector3 h = sc.toCartesian(basis);
				out = Optics.reflect(v, h);
			} while (n.dot(out) <= 0.0);

		}

		Ray3 ray = new Ray3(x.getPosition(), out);
		double pdf = getScatteringPDF(x, v, out, true, lambda);
		Color value = bsdf(x, v, out, lambda).divide(pdf);

		return ScatteredRay.diffuse(ray, value, pdf);

	}

}
