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
	 * Determines whether the specified ray intersects with
	 * this sphere.  Equivalent to {@code !this.intersect(ray).isEmpty()}.
	 * @param ray The ray to test for an intersection with.
	 * @return A value indicating whether the ray intersects
	 * 		this sphere.
	 * @see {@link #intersect(Ray3)}, {@link Interval#isEmpty()}.
	 */
	public boolean intersects(Ray3 ray) {
		
		//
		// Algorithm from:
		//
		// A.S. Glassner, Ed.,
		// "An Introduction to Ray Tracing",
		// Morgan Kaufmann Publishers, Inc., San Francisco, CA, 2002
		// Section 2.2
		//

		if (isEmpty()) {
			return false;
		}
		
		double		r2 = radius * radius;
		Vector3		oc = ray.getOrigin().vectorTo(center);
		double		L2oc = oc.dot(oc);
		
		if (L2oc < r2) return true;
		
		double		tca = oc.dot(ray.getDirection());
		
		if (tca < 0.0) return false;
		
		return ray.pointAt(tca).squaredDistanceTo(center) < r2;
		
	}

	/**
	 * Computes the intersection of this sphere with the given
	 * ray.
	 * @param ray The ray to compute the intersection of this
	 * 		sphere with.
	 * @return The interval in which the ray passes through
	 * 		the sphere (i.e., this.contains(ray.pointAt(x)) if and
	 * 		only if this.intersect(ray).contains(x)).
	 * @see {@link #contains(Point3)}, {@link Ray3#pointAt(double)},
	 * 		{@link Interval#contains(double)}.
	 */
	public Interval intersect(Ray3 ray) {
		
		//
		// Algorithm from:
		//
		// A.S. Glassner, Ed.,
		// "An Introduction to Ray Tracing",
		// Morgan Kaufmann Publishers, Inc., San Francisco, CA, 2002
		// Section 2.2
		//

		// Check for an empty box.
		if (isEmpty()) {
			return Interval.EMPTY;
		}

		// Check if the ray starts from within the box.
		double		r2 = radius * radius;
		Vector3		oc = ray.getOrigin().vectorTo(center);
		double		L2oc = oc.dot(oc);
		boolean		startInside = (L2oc < r2);

		// distance along ray to point on ray closest to center of sphere (equation (A10)).
		double		tca = oc.dot(ray.getDirection());
		
		// if the ray starts outside the sphere and points away from the center of the
		// sphwere, then the ray does not hit the sphere.
		if (!startInside && tca < 0.0) {
			return Interval.EMPTY;
		}
		
		// compute half chord distance squared (equation (A13)).
		double		t2hc = r2 - L2oc + (tca * tca);
		
		if (t2hc < 0.0) {
			return Interval.EMPTY;
		}
		
		double		thc = Math.sqrt(t2hc);
		
		// compute interval (equation (A14)).
		return new Interval(startInside ? 0.0 : (tca - thc), tca + thc);

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
