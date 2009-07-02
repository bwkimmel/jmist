/**
 *
 */
package ca.eandb.jmist.math;



/**
 * @author Brad Kimmel
 *
 */
public final class RandomUtil {

	public static SphericalCoordinates diffuse() {
		return diffuse(canonical(), canonical());
	}

	public static SphericalCoordinates diffuse(double xi1, double xi2) {

		assert(0.0 <= xi1 && xi1 <= 1.0);
		assert(0.0 <= xi2 && xi2 <= 1.0);

		return new SphericalCoordinates(
				Math.acos(Math.sqrt(1.0 - xi1)),
				2.0 * Math.PI * xi2
		);

	}

	public static SphericalCoordinates uniformOnUpperHemisphere() {
		return uniformOnUpperHemisphere(1.0, canonical(), canonical());
	}

	public static SphericalCoordinates uniformOnUpperHemisphere(double radius) {
		return uniformOnUpperHemisphere(radius, canonical(), canonical());
	}

	public static SphericalCoordinates uniformOnUpperHemisphere(double radius, double xi1, double xi2) {

		// TODO implement this directly so it's more efficient.
		SphericalCoordinates result = uniformOnSphere(radius, xi1, xi2);

		if (result.polar() > (Math.PI / 2.0))
			result = new SphericalCoordinates(Math.PI - result.polar(), result.azimuthal(), radius);

		return result;

	}

	public static SphericalCoordinates uniformOnSphere() {
		return uniformOnSphere(1.0, canonical(), canonical());
	}

	public static SphericalCoordinates uniformOnSphere(double radius) {
		return uniformOnSphere(radius, canonical(), canonical());
	}

	public static SphericalCoordinates uniformOnSphere(double radius, double xi1, double xi2) {

		assert(0.0 <= xi1 && xi1 <= 1.0);
		assert(0.0 <= xi2 && xi2 <= 1.0);

		return new SphericalCoordinates(
				Math.acos(2.0 * xi1 - 1.0),
				2.0 * Math.PI * xi2,
				radius
		);

	}

	public static PolarCoordinates uniformOnDisc() {
		return uniformOnDisc(1.0, canonical(), canonical());
	}

	public static PolarCoordinates uniformOnDisc(double radius) {
		return uniformOnDisc(radius, canonical(), canonical());
	}

	public static PolarCoordinates uniformOnDisc(double radius, double xi1, double xi2) {

		assert(0.0 <= xi1 && xi1 <= 1.0);
		assert(0.0 <= xi2 && xi2 <= 1.0);

		return new PolarCoordinates(
				2.0 * Math.PI * xi1,
				radius * Math.sqrt(xi2)
		);

	}

	public static Point3 uniformOnTriangle(Point3 a, Point3 b, Point3 c) {
		return uniformOnTriangle(a, b, c, canonical(), canonical());
	}

	public static Point3 uniformOnTriangle(Point3 a, Point3 b, Point3 c, double alpha, double beta) {
		if (alpha + beta > 1.0) {
			alpha = 1.0 - alpha;
			beta = 1.0 - beta;
		}
		Vector3 ab = a.vectorTo(b).times(alpha);
		Vector3 ac = a.vectorTo(c).times(beta);
		return a.plus(ab).plus(ac);
	}

	public static Point2 uniformOnTriangle(Point2 a, Point2 b, Point2 c) {
		return uniformOnTriangle(a, b, c, canonical(), canonical());
	}

	public static Point2 uniformOnTriangle(Point2 a, Point2 b, Point2 c, double alpha, double beta) {
		if (alpha + beta > 1.0) {
			alpha = 1.0 - alpha;
			beta = 1.0 - beta;
		}
		Vector2 ab = a.vectorTo(b).times(alpha);
		Vector2 ac = a.vectorTo(c).times(beta);
		return a.plus(ab).plus(ac);
	}

	public static boolean bernoulli(double probability) {
		return canonical() < probability;
	}

	public static boolean coin() {
		return bernoulli(0.5);
	}

	public static int categorical(double[] weights) {

		double	x		= canonical() / MathUtil.sum(weights);
		double	mark	= 0.0;

		for (int i = 0; i < weights.length; i++) {
			if (x < (mark += weights[i])) {
				return i;
			}
		}

		return weights.length - 1;

	}

	public static int discrete(int minimum, int maximum) {
		return minimum + random.nextInt(maximum - minimum + 1);
	}

	public static int discrete(int minimum, int maximum, double seed) {
		return minimum + (int) Math.floor(canonical() * (double) (maximum - minimum + 1));
	}

	public static double canonical() {
		return random.nextDouble();
	}

	public static Point2 canonical2() {
		return new Point2(canonical(), canonical());
	}

	public static Point3 canonical3() {
		return new Point3(canonical(), canonical(), canonical());
	}

	public static double uniform(double minimum, double maximum) {
		return minimum + canonical() * (maximum - minimum);
	}

	public static double uniform(Interval I) {
		return uniform(I.minimum(), I.maximum());
	}

	public static Point2 uniform(Box2 box) {
		return new Point2(
				uniform(box.minimumX(), box.maximumX()),
				uniform(box.minimumY(), box.maximumY()));
	}

	public static Point3 uniform(Box3 box) {
		return new Point3(
				uniform(box.minimumX(), box.maximumX()),
				uniform(box.minimumY(), box.maximumY()),
				uniform(box.minimumZ(), box.maximumZ()));
	}

	private static final java.util.Random random = new java.util.Random();

}
