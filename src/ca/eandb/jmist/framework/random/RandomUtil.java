/**
 *
 */
package ca.eandb.jmist.framework.random;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.PolarCoordinates;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector2;
import ca.eandb.jmist.math.Vector3;



/**
 * @author Brad Kimmel
 *
 */
public final class RandomUtil {

	public static SphericalCoordinates diffuse(Random random) {
		return diffuse(random.next(), random.next());
	}

	private static SphericalCoordinates diffuse(double xi1, double xi2) {

		assert(0.0 <= xi1 && xi1 <= 1.0);
		assert(0.0 <= xi2 && xi2 <= 1.0);

		return new SphericalCoordinates(
				Math.acos(Math.sqrt(1.0 - xi1)),
				2.0 * Math.PI * xi2
		);

	}

	public static SphericalCoordinates uniformOnUpperHemisphere(Random random) {
		return uniformOnUpperHemisphere(1.0, random.next(), random.next());
	}

	public static SphericalCoordinates uniformOnUpperHemisphere(double radius, Random random) {
		return uniformOnUpperHemisphere(radius, random.next(), random.next());
	}

	private static SphericalCoordinates uniformOnUpperHemisphere(double radius, double xi1, double xi2) {

		// TODO implement this directly so it's more efficient.
		SphericalCoordinates result = uniformOnSphere(radius, xi1, xi2);

		if (result.polar() > (Math.PI / 2.0))
			result = new SphericalCoordinates(Math.PI - result.polar(), result.azimuthal(), radius);

		return result;

	}

	public static SphericalCoordinates uniformOnSphere(Random random) {
		return uniformOnSphere(1.0, random.next(), random.next());
	}

	public static SphericalCoordinates uniformOnSphere(double radius, Random random) {
		return uniformOnSphere(radius, random.next(), random.next());
	}

	private static SphericalCoordinates uniformOnSphere(double radius, double xi1, double xi2) {

		assert(0.0 <= xi1 && xi1 <= 1.0);
		assert(0.0 <= xi2 && xi2 <= 1.0);

		return new SphericalCoordinates(
				Math.acos(2.0 * xi1 - 1.0),
				2.0 * Math.PI * xi2,
				radius
		);

	}

	public static PolarCoordinates uniformOnDisc(Random random) {
		return uniformOnDisc(1.0, random.next(), random.next());
	}

	public static PolarCoordinates uniformOnDisc(double radius, Random random) {
		return uniformOnDisc(radius, random.next(), random.next());
	}

	private static PolarCoordinates uniformOnDisc(double radius, double xi1, double xi2) {

		assert(0.0 <= xi1 && xi1 <= 1.0);
		assert(0.0 <= xi2 && xi2 <= 1.0);

		return new PolarCoordinates(
				2.0 * Math.PI * xi1,
				radius * Math.sqrt(xi2)
		);

	}

	public static Point3 uniformOnTriangle(Point3 a, Point3 b, Point3 c, Random random) {
		return uniformOnTriangle(a, b, c, random.next(), random.next());
	}

	private static Point3 uniformOnTriangle(Point3 a, Point3 b, Point3 c, double alpha, double beta) {
		if (alpha + beta > 1.0) {
			alpha = 1.0 - alpha;
			beta = 1.0 - beta;
		}
		Vector3 ab = a.vectorTo(b).times(alpha);
		Vector3 ac = a.vectorTo(c).times(beta);
		return a.plus(ab).plus(ac);
	}

	public static Point2 uniformOnTriangle(Point2 a, Point2 b, Point2 c, Random random) {
		return uniformOnTriangle(a, b, c, random.next(), random.next());
	}

	private static Point2 uniformOnTriangle(Point2 a, Point2 b, Point2 c, double alpha, double beta) {
		if (alpha + beta > 1.0) {
			alpha = 1.0 - alpha;
			beta = 1.0 - beta;
		}
		Vector2 ab = a.vectorTo(b).times(alpha);
		Vector2 ac = a.vectorTo(c).times(beta);
		return a.plus(ab).plus(ac);
	}

	public static boolean bernoulli(double probability, Random random) {
		return bernoulli(probability, random.next());
	}

	private static boolean bernoulli(double probability, double seed) {
		return seed < probability;
	}

	public static boolean coin(Random random) {
		return coin(random.next());
	}

	private static boolean coin(double seed) {
		return seed < 0.5;
	}

	public static int categorical(double[] weights, Random random) {

		double	x		= random.next() / MathUtil.sum(weights);
		double	mark	= 0.0;

		for (int i = 0; i < weights.length; i++) {
			if (x < (mark += weights[i])) {
				return i;
			}
		}

		return weights.length - 1;

	}

	public static int discrete(int minimum, int maximum, Random random) {
		return discrete(minimum, maximum, random.next());
	}

	private static int discrete(int minimum, int maximum, double seed) {
		return minimum + (int) Math.floor(seed * (double) (maximum - minimum + 1));
	}

	public static double canonical(Random random) {
		return random.next();
	}

	public static Point2 canonical2(Random random) {
		return new Point2(random.next(), random.next());
	}

	public static Point3 canonical3(Random random) {
		return new Point3(random.next(), random.next(), random.next());
	}

	private static double uniform(double minimum, double maximum, double seed) {
		return minimum + seed * (maximum - minimum);
	}

	public static double uniform(double minimum, double maximum, Random random) {
		return uniform(minimum, maximum, random.next());
	}

	public static double uniform(Interval I, Random random) {
		return uniform(I, random.next());
	}

	private static double uniform(Interval I, double seed) {
		return uniform(I.minimum(), I.maximum(), seed);
	}

	public static Point2 uniform(Box2 box, Random random) {
		return new Point2(
				uniform(box.minimumX(), box.maximumX(), random),
				uniform(box.minimumY(), box.maximumY(), random));
	}

	public static Point3 uniform(Box3 box, Random random) {
		return new Point3(
				uniform(box.minimumX(), box.maximumX(), random.next()),
				uniform(box.minimumY(), box.maximumY(), random.next()),
				uniform(box.minimumZ(), box.maximumZ(), random.next()));
	}

}
