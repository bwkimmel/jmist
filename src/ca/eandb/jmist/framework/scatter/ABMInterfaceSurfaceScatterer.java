/**
 *
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.function.ConstantFunction1;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class ABMInterfaceSurfaceScatterer implements SurfaceScatterer {

	private final Function1 riBelow;

	private final Function1 riAbove;

	private final double n11;

	private final double n12;

	private final double n21;

	private final double n22;

	/**
	 * @param riBelow
	 * @param riAbove
	 * @param n11
	 * @param n12
	 * @param n21
	 * @param n22
	 */
	public ABMInterfaceSurfaceScatterer(Function1 riBelow, Function1 riAbove, double n11, double n12, double n21, double n22) {
		this.riBelow = riBelow;
		this.riAbove = riAbove;
		this.n11 = n11;
		this.n12 = n12;
		this.n21 = n21;
		this.n22 = n22;
	}

	public ABMInterfaceSurfaceScatterer(double riBelow, double riAbove, double n11, double n12, double n21, double n22) {
		this(new ConstantFunction1(riBelow), new ConstantFunction1(riAbove), n11, n12, n21, n22);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			double lambda, Random rnd) {

		double n1 = riAbove.evaluate(lambda);
		double n2 = riBelow.evaluate(lambda);
		Vector3 N = x.getNormal();
		double R = Optics.reflectance(v, n1, n2, N);

		boolean fromSide = (v.dot(N) < 0.0);
		boolean toSide;
		Vector3 w;
		double specularity;

		if (RandomUtil.bernoulli(R, rnd)) {
			toSide = fromSide;
			specularity = fromSide ? n11 : n22;
			w = Optics.reflect(v, N);
		} else {
			toSide = !fromSide;
			specularity = fromSide ? n12 : n21;
			w = Optics.refract(v, n1, n2, N);
		}

		if (!Double.isInfinite(specularity)) {
			Basis3 basis = Basis3.fromW(w);
			do {
				// The azimuthal angle is the cosine of what it would
				// normally be for a phong perturbation.
				SphericalCoordinates perturb = new SphericalCoordinates(
						Math.pow(1.0 - rnd.next(), 1.0 / (specularity + 1.0)),
						2.0 * Math.PI * rnd.next());
				w = perturb.toCartesian(basis);
			} while ((w.dot(N) > 0.0) != toSide);
		}

		return w;
	}

}
