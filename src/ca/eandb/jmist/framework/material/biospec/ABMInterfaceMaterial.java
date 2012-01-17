/**
 *
 */
package ca.eandb.jmist.framework.material.biospec;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRay.Type;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.material.OpaqueMaterial;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>SurfaceScatterer</code> that represents an interface between two
 * layers in the ABM-U or ABM-B implementation.
 * 
 * @see ABMSurfaceScatterer
 * @author Brad Kimmel
 */
public final class ABMInterfaceMaterial extends OpaqueMaterial {

	/** Serialization version ID. */
	private static final long serialVersionUID = 6042414836517567158L;

	/** The refractive index of the medium above the interface. */
	private final double riBelow;

	/** The refractive index of the medium below the interface. */
	private final double riAbove;

	/**
	 * The perturbation exponent used for a ray reflected from the top side of
	 * the interface.
	 */
	private final double n11;

	/**
	 * The perturbation exponent used for a ray transmitted from the top side to
	 * the bottom side of the interface.
	 */
	private final double n12;

	/**
	 * The perturbation exponent used for a ray transmitted from the bottom side
	 * to the top side of the interface.
	 */
	private final double n21;

	/**
	 * The perturbation exponent used for a ray reflected from the bottom side
	 * of the interface.
	 */
	private final double n22;

	/**
	 * Creates a new <code>ABMInterfaceSurfaceScatterer</code>.
	 * 
	 * @param riBelow
	 *            The refractive index of the medium above the interface.
	 * @param riAbove
	 *            The refractive index of the medium below the interface.
	 * @param n11
	 *            The perturbation exponent used for a ray reflected from the
	 *            top side of the interface.
	 * @param n12
	 *            The perturbation exponent used for a ray transmitted from the
	 *            top side to the bottom side of the interface.
	 * @param n21
	 *            The perturbation exponent used for a ray transmitted from the
	 *            bottom side to the top side of the interface.
	 * @param n22
	 *            The perturbation exponent used for a ray reflected from the
	 *            bottom side of the interface.
	 */
	public ABMInterfaceMaterial(double riBelow, double riAbove, double n11,
			double n12, double n21, double n22) {
		this.riBelow = riBelow;
		this.riAbove = riAbove;
		this.n11 = n11;
		this.n12 = n12;
		this.n21 = n21;
		this.n22 = n22;

		// try {
		// FileOutputStream file = new
		// FileOutputStream("/Users/brad/interface.csv", true);
		// PrintStream out = new PrintStream(new
		// CompositeOutputStream().addChild(System.out).addChild(file));
		//
		// Vector3 N = Vector3.K;
		// for (int angle = 0; angle < 90; angle++) {
		// double rad = Math.toRadians(angle);
		// Vector3 v = new Vector3(Math.sin(rad), 0.0, -Math.cos(rad));
		// for (int lambda = 400; lambda <= 700; lambda += 5) {
		// double n1 = riAbove.evaluate(1e-9 * (double) lambda);
		// double n2 = riBelow.evaluate(1e-9 * (double) lambda);
		// double R = Optics.reflectance(v, n1, n2, N);
		// if (lambda > 400) {
		// out.print(',');
		// }
		// out.print(R);
		// }
		// out.println();
		// }
		// for (int angle = 0; angle < 90; angle++) {
		// double rad = Math.toRadians(angle);
		// Vector3 v = new Vector3(Math.sin(rad), 0.0, Math.cos(rad));
		// for (int lambda = 400; lambda <= 700; lambda += 5) {
		// double n1 = riAbove.evaluate(1e-9 * (double) lambda);
		// double n2 = riBelow.evaluate(1e-9 * (double) lambda);
		// double R = Optics.reflectance(v, n1, n2, N);
		// if (lambda > 400) {
		// out.print(',');
		// }
		// out.print(R);
		// }
		// out.println();
		// }
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist
	 * .framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean,
	 * ca.eandb.jmist.framework.color.WavelengthPacket,
	 * ca.eandb.jmist.framework.Random)
	 */
	public Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			double lambda, Random rnd) {

		double n1 = riAbove;
		double n2 = riBelow;
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
				SphericalCoordinates perturb = new SphericalCoordinates(
						Math.acos(Math.pow(1.0 - rnd.next(),
								1.0 / (specularity + 1.0))), 2.0 * Math.PI
								* rnd.next());
				w = perturb.toCartesian(basis);
			} while ((w.dot(N) > 0.0) != toSide);
		}

		return w;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist
	 * .framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean,
	 * ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
	 */
	@Override
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, double ru, double rv, double rj) {

		double n1 = riAbove;
		double n2 = riBelow;
		Vector3 N = x.getNormal();
		double R = Optics.reflectance(v, n1, n2, N);

		boolean fromSide = (v.dot(N) < 0.0);
		boolean toSide;
		Vector3 w;
		double specularity;

		if (RandomUtil.bernoulli(R, rj)) {
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
				SphericalCoordinates perturb = new SphericalCoordinates(
						Math.acos(Math.pow(1.0 - ru, 1.0 / (specularity + 1.0))),
						2.0 * Math.PI * rv);
				w = perturb.toCartesian(basis);
			} while ((w.dot(N) > 0.0) != toSide);
		}

		return new ScatteredRay(new Ray3(x.getPosition(), w),
				lambda.getColorModel().getWhite(lambda),
				Double.isInfinite(specularity) ? Type.SPECULAR : Type.DIFFUSE,
				1.0, fromSide != toSide);
	}

}
