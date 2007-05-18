/**
 *
 */
package org.jmist.toolkit;

/**
 * A sphere in two dimensional space (the set of points at most a
 * constant distance from a fixed point).
 * @author brad
 */
public final class Sphere {

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
	 * Gets the radius of the sphere.
	 * @return The radius of the sphere.
	 */
	public double getRadius() {
		return radius;
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
	 * Computes the volume of the sphere.
	 * @return The volume of the sphere.
	 */
	public double getVolume() {
		return (4.0 / 3.0) * Math.PI * radius * radius * radius;
	}

	/**
	 * Computes the diameter of the sphere.
	 * @return The diameter of the sphere.
	 */
	public double getDiameter() {
		return 2.0 * radius;
	}

	/**
	 * Computes the surface area of the sphere.
	 * @return The surface area of the sphere.
	 */
	public double getSurfaceArea() {
		return 4.0 * Math.PI * radius * radius;
	}

	/**
	 * Expands this sphere outwards by the specified amount.
	 * @param amount The amount to expand the sphere by.
	 * @return The expanded sphere.
	 */
	public Sphere expand(double amount) {
		return getInstance(center, radius + amount);
	}

	/**
	 * Expands this sphere outwards to encompass the specified point.
	 * Guarantees that {@code this.contains(p)} after this method is called.
	 * @param p The point to include in this sphere.
	 * @return The expanded sphere.
	 */
	public Sphere expandTo(Point3 p) {
		if (isEmpty()) {
			return new Sphere(p, 0.0);
		} else {
			double newRadius = center.distanceTo(p);

			if (newRadius < radius) {
				return this;
			} else {
				return new Sphere(center, newRadius);
			}
		}
	}

	/**
	 * Default constructor.
	 */
	private Sphere() {
		center = Point3.ORIGIN;
		radius = Double.NaN;
	}

	/**
	 * Gets an instance of a sphere.
	 * @param center The center of the sphere.
	 * @param radius The radius of the sphere.
	 * @return A new sphere if 0 <= radius < infinity,
	 *         Sphere.UNIVERSE if radius == infinity,
	 *         Sphere.EMPTY if radius < 0.
	 */
	private static final Sphere getInstance(Point3 center, double radius) {
		if (radius < 0.0) {
			return Sphere.EMPTY;
		} else if (Double.isInfinite(radius)) {
			return Sphere.UNIVERSE;
		} else {
			return new Sphere(center, radius);
		}
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
	public static final Sphere EMPTY = new Sphere();

	/** The center of the sphere. */
	private final Point3 center;

	/** The radius of the sphere. */
	private final double radius;

}
