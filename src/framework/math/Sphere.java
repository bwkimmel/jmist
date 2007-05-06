/**
 *
 */
package framework.math;

/**
 * A sphere in two dimensional space (the set of points at most a
 * constant distance from a fixed point).
 * @author brad
 */
public final class Sphere {

	/**
	 * Default constructor.
	 */
	public Sphere() {
	}

	/**
	 * Initializes the center and radius of the sphere.
	 * @param center The center of the sphere.
	 * @param radius The radius of the sphere (must be non-negative).
	 */
	public Sphere(Point3 center, double radius) {
		assert(radius >= 0.0);

		this.center = center;
		this.radius = radius;
	}

	/**
	 * Gets the center of the sphere.
	 * @return The center of the sphere.
	 */
	public Point3 getCenter() {
		return center;
	}

	/**
	 * Sets the center of the sphere.
	 * @param center The new center of the sphere.
	 */
	public void setCenter(Point3 center) {
		this.center = center;
	}

	/**
	 * Gets the radius of the sphere.
	 * @return The radius of the sphere.
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Sets the radius of the sphere.
	 * @param radius The new radius of the sphere (must be non-negative).
	 */
	public void setRadius(double radius) {
		assert(radius >= 0.0);

		this.radius = radius;
	}

	/**
	 * Determines if this sphere is empty.
	 * @return A value indicating if this sphere is empty.
	 */
	public boolean isEmpty() {
		return Double.isNaN(radius);
	}

	/**
	 * Determines if this sphere contains the specified point.
	 * @param p The point to check for containment of.
	 * @return A value indicating if p is within this sphere.
	 */
	public boolean contains(Point3 p) {
		return center.distanceTo(p) <= radius;
	}

	/**
	 * Expands this sphere outwards by the specified amount.
	 * @param amount The amount to expand the sphere by.
	 */
	public void expand(double amount) {
		radius += amount;

		// If the sphere was contracted by more than its radius,
		// then it will be empty.
		if (radius < 0.0) {
			makeEmpty();
		}
	}

	/**
	 * Expands this sphere outwards to encompass the specified point.
	 * Guarantees that {@code this.contains(p)} after this method is called.
	 * @param p The point to include in this sphere.
	 */
	public void expandTo(Point3 p) {
		if (isEmpty()) {
			center = p;
			radius = 0.0;
		} else {
			radius = Math.max(radius, center.distanceTo(p));
		}
	}

	/**
	 * Makes this sphere empty.
	 */
	private void makeEmpty() {
		center = Point3.ORIGIN;
		radius = Double.NaN;
	}

	/**
	 * Creates an empty sphere.
	 * @return An empty sphere.
	 */
	private static Sphere getEmptySphere() {
		Sphere sphere = new Sphere();
		sphere.makeEmpty();
		return sphere;
	}

	/**
	 * A sphere containing all points.
	 */
	public static final Sphere UNIVERSE = new Sphere(Point3.ORIGIN, Double.POSITIVE_INFINITY);

	/**
	 * The unit sphere (the sphere centered at the origin with a radius of 1.0).
	 */
	public static final Sphere UNIT = new Sphere(Point3.ORIGIN, 1.0);

	/**
	 * An empty sphere.
	 */
	public static final Sphere EMPTY = getEmptySphere();

	/** The center of the sphere. */
	private Point3 center;

	/** The radius of the sphere. */
	private double radius;

}
