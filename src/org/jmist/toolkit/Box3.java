/**
 *
 */
package org.jmist.toolkit;

import org.jmist.util.MathUtil;

/**
 * An axis-aligned three dimensional box.
 * This class is immutable.
 * @author brad
 */
public final class Box3 {

	/**
	 * Initializes the extents of the box along the x, y, and z axes.
	 * @param spanX The extent of the box along the x-axis.
	 * @param spanY The extent of the box along the y-axis.
	 * @param spanZ The extent of the box along the z-axis.
	 */
	public Box3(Interval spanX, Interval spanY, Interval spanZ) {
		if (spanX.isEmpty() || spanY.isEmpty() || spanZ.isEmpty()) {
			minimumX = minimumY = minimumZ = Double.NaN;
			maximumX = maximumY = maximumZ = Double.NaN;
		} else {
			minimumX = spanX.getMinimum();
			maximumX = spanX.getMaximum();
			minimumY = spanY.getMinimum();
			maximumY = spanY.getMaximum();
			minimumZ = spanZ.getMinimum();
			maximumZ = spanZ.getMaximum();
		}
	}

	/**
	 * Initializes the box between two points.
	 * @param p One corner of the box.
	 * @param q The corner of the box opposite p.
	 */
	public Box3(Point3 p, Point3 q) {
		minimumX = Math.min(p.x(), q.x());
		maximumX = Math.max(p.x(), q.x());
		minimumY = Math.min(p.y(), q.y());
		maximumY = Math.max(p.y(), q.y());
		minimumZ = Math.min(p.z(), q.z());
		maximumZ = Math.max(p.z(), q.z());
	}

	/**
	 * Initializes the box from its extents.
	 * @param minimumX The lower bound of the box along the x-axis.
	 * @param minimumY The lower bound of the box along the y-axis.
	 * @param minimumZ The lower bound of the box along the z-axis.
	 * @param maximumX The upper bound of the box along the x-axis.
	 * @param maximumY The upper bound of the box along the y-axis.
	 * @param maximumZ The upper bound of the box along the z-axis.
	 */
	public Box3(double minimumX, double minimumY, double minimumZ, double maximumX, double maximumY, double maximumZ) {
		assert(minimumX <= maximumX && minimumY <= maximumY && minimumZ <= maximumZ);

		this.minimumX = minimumX;
		this.minimumY = minimumY;
		this.minimumZ = minimumZ;
		this.maximumX = maximumX;
		this.maximumY = maximumY;
		this.maximumZ = maximumZ;
	}

	/**
	 * Gets the lower bound of this box along the x-axis.
	 * @return The lower bound of this box along the x-axis.
	 */
	public double getMinimumX() {
		return minimumX;
	}

	/**
	 * Gets the lower bound of this box along the y-axis.
	 * @return The lower bound of this box along the y-axis.
	 */
	public double getMinimumY() {
		return minimumY;
	}

	/**
	 * Gets the lower bound of this box along the z-axis.
	 * @return The lower bound of this box along the z-axis.
	 */
	public double getMinimumZ() {
		return minimumZ;
	}

	/**
	 * Gets the upper bound of this box along the x-axis.
	 * @return The upper bound of this box along the x-axis.
	 */
	public double getMaximumX() {
		return maximumX;
	}

	/**
	 * Gets the upper bound of this box along the y-axis.
	 * @return The upper bound of this box along the y-axis.
	 */
	public double getMaximumY() {
		return maximumY;
	}

	/**
	 * Gets the upper bound of this box along the z-axis.
	 * @return The upper bound of this box along the z-axis.
	 */
	public double getMaximumZ() {
		return maximumZ;
	}

	/**
	 * Gets the extent of this box along the x-axis.
	 * @return An interval representing the extent of this box along the x-axis.
	 */
	public Interval getSpanX() {
		return isEmpty() ? Interval.EMPTY : new Interval(minimumX, maximumX);
	}

	/**
	 * Gets the extent of this box along the y-axis.
	 * @return An interval representing the extent of this box along the y-axis.
	 */
	public Interval getSpanY() {
		return isEmpty() ? Interval.EMPTY : new Interval(minimumY, maximumY);
	}

	/**
	 * Gets the extent of this box along the z-axis.
	 * @return An interval representing the extent of this box along the z-axis.
	 */
	public Interval getSpanZ() {
		return isEmpty() ? Interval.EMPTY : new Interval(minimumZ, maximumZ);
	}

	/**
	 * Gets the length of the box along the x-axis.
	 * @return The length of the box along the x-axis.
	 */
	public double getLengthX() {
		return isEmpty() ? Double.NaN : maximumX - minimumX;
	}

	/**
	 * Gets the length of the box along the y-axis.
	 * @return The length of the box along the y-axis.
	 */
	public double getLengthY() {
		return isEmpty() ? Double.NaN : maximumY - minimumY;
	}

	/**
	 * Gets the length of the box along the z-axis.
	 * @return The length of the box along the z-axis.
	 */
	public double getLengthZ() {
		return isEmpty() ? Double.NaN : maximumZ - minimumZ;
	}

	/**
	 * Determines if this box is empty.
	 * @return A value indicating wither this box is empty.
	 */
	public boolean isEmpty() {
		return Double.isNaN(minimumX);
	}

	/**
	 * Returns the point at the center of this box.
	 * @return The point at the center of this box.
	 */
	public Point3 getCenter() {
		return new Point3((minimumX + maximumX) / 2.0, (minimumY + maximumY) / 2.0, (minimumZ + maximumZ) / 2.0);
	}

	/**
	 * Computes the length of the diagonal of this box.
	 * @return The length of the diagonal of this box.
	 */
	public double getDiagonal() {
		return Math.sqrt(
				(maximumX - minimumX) * (maximumX - minimumX) +
				(maximumY - minimumY) * (maximumY - minimumY) +
				(maximumZ - minimumZ) * (maximumZ - minimumZ)
		);
	}

	/**
	 * Determines if this box contains the specified point.
	 * @param p The point to check for containment of.
	 * @return A value indicating whether p is inside this box.
	 */
	public boolean contains(Point3 p) {
		return (minimumX <= p.x() && p.x() <= maximumX) && (minimumY <= p.y() && p.y() <= maximumY) && (minimumZ <= p.z() && p.z() <= maximumZ);
	}

	/**
	 * Computes the volume of the box.
	 * @return The volume of the box.
	 */
	public double getVolume() {
		return (maximumX - minimumX) * (maximumY - minimumY) * (maximumZ - minimumZ);
	}

	/**
	 * Computes the surface area of the box.
	 * @return The surface area of the box.
	 */
	public double getSurfaceArea() {
		return 2.0 * (((maximumX - minimumX) * (maximumY - minimumY)) +
		              ((maximumY - minimumY) * (maximumZ - minimumZ)) +
		              ((maximumZ - minimumZ) * (maximumX - minimumX)));
	}

	/**
	 * Computes the intersection of this box with another.
	 * @param other The box to intersect with this box.
	 * @return The intersection of this box with the other box.
	 */
	public Box3 intersect(Box3 other) {
		return getInstance(
				Math.max(minimumX, other.minimumX),
				Math.max(minimumY, other.minimumY),
				Math.max(minimumZ, other.minimumZ),
				Math.min(maximumX, other.maximumX),
				Math.min(maximumY, other.maximumY),
				Math.min(maximumZ, other.maximumZ)
		);
	}

	/**
	 * Extends the box to contain the specified point.
	 * Guarantees that {@code this.contains(p)} after this method is called.
	 * @param p The point to extend the box to.
	 * @return The extended box.
	 * @see contains
	 */
	public Box3 extendTo(Point3 p) {
		if (isEmpty()) {
			return new Box3(p.x(), p.y(), p.z(), p.x(), p.y(), p.z());
		} else {
			return new Box3(
					Math.min(minimumX, p.x()),
					Math.min(minimumY, p.y()),
					Math.min(minimumZ, p.z()),
					Math.max(maximumX, p.x()),
					Math.max(maximumY, p.y()),
					Math.max(maximumZ, p.z())
			);
		}
	}

	/**
	 * Expands the box by the specified amount in all directions.
	 * @param amount The amount to expand the box by.
	 * @return The expanded box.
	 */
	public Box3 expand(double amount) {
		return getInstance(
				minimumX - amount,
				minimumY - amount,
				minimumZ - amount,
				maximumX + amount,
				maximumY + amount,
				minimumZ + amount
		);
	}

	/**
	 * Determines if the specified ray intersects with this box.
	 * Equivalent to {@code !this.intersects(ray).isEmpty()}.
	 * @param ray The ray to test for an intersection with this
	 * 		box.
	 * @return A value indicating if the specified ray intersects
	 * 		with this box.
	 * @see {@link #intersect(Ray3)}, {@link Interval#isEmpty()}.
	 */
	public boolean intersects(Ray3 ray) {

		// Check for an empty box.
		if (isEmpty()) {
			return false;
		}

		// Check if the ray starts from within the box.
		if (this.contains(ray.getOrigin())) {
			return true;
		}

		assert(ray.getDirection().x() != 0.0 || ray.getDirection().y() != 0.0 || ray.getDirection().z() != 0.0);

		double	t;
		Point3	p;

		// Check for intersection with each of the six sides of the box.
		if (ray.getDirection().x() != 0.0) {
			t = (minimumX - ray.getOrigin().x()) / ray.getDirection().x();
			if (t > 0.0) {
				p = ray.pointAt(t);
				if (minimumY < p.y() && p.y() < maximumY && minimumZ < p.z() && p.z() < maximumZ) {
					return true;
				}
			}

			t = (maximumX - ray.getOrigin().x()) / ray.getDirection().x();
			if (t > 0.0) {
				p = ray.pointAt(t);
				if (minimumY < p.y() && p.y() < maximumY && minimumZ < p.z() && p.z() < maximumZ) {
					return true;
				}
			}
		}

		if (ray.getDirection().y() != 0.0) {
			t = (minimumY - ray.getOrigin().y()) / ray.getDirection().y();
			if (t > 0.0) {
				p = ray.pointAt(t);
				if (minimumX < p.x() && p.x() < maximumX && minimumZ < p.z() && p.z() < maximumZ) {
					return true;
				}
			}

			t = (maximumY - ray.getOrigin().y()) / ray.getDirection().y();
			if (t > 0.0) {
				p = ray.pointAt(t);
				if (minimumX < p.x() && p.x() < maximumX && minimumZ < p.z() && p.z() < maximumZ) {
					return true;
				}
			}
		}

		if (ray.getDirection().z() != 0.0) {
			t = (minimumZ - ray.getOrigin().z()) / ray.getDirection().z();
			if (t > 0.0) {
				p = ray.pointAt(t);
				if (minimumX < p.x() && p.x() < maximumX && minimumY < p.y() && p.y() < maximumY) {
					return true;
				}
			}

			t = (maximumZ - ray.getOrigin().z()) / ray.getDirection().z();
			if (t > 0.0) {
				p = ray.pointAt(t);
				if (minimumX < p.x() && p.x() < maximumX && minimumY < p.y() && p.y() < maximumY) {
					return true;
				}
			}
		}

		// If we didn't find any intersection points, then the
		// ray does not intersect the box.
		return false;

	}

	/**
	 * Computes the intersection of this box with the given
	 * ray.
	 * @param ray The ray to compute the intersection of this
	 * 		box with.
	 * @return The interval in which the ray passes through
	 * 		the box (i.e., this.contains(ray.pointAt(x)) if and
	 * 		only if this.intersect(ray).contains(x)).
	 * @see {@link #contains(Point3)}, {@link Ray3#pointAt(double)},
	 * 		{@link Interval#contains(double)}.
	 */
	public Interval intersect(Ray3 ray) {

		// Check for an empty box.
		if (isEmpty()) {
			return Interval.EMPTY;
		}

		// Check if the ray starts from within the box.
		double[]	t = new double[2];
		int			n = 0;

		if (this.contains(ray.getOrigin())) {
			t[n++] = 0.0;
		}

		assert(ray.getDirection().x() != 0.0 || ray.getDirection().y() != 0.0 || ray.getDirection().z() != 0.0);

		Point3		p;

		// Check for intersection with each of the six sides of the box.
		if (ray.getDirection().x() != 0.0) {
			t[n] = (minimumX - ray.getOrigin().x()) / ray.getDirection().x();
			if (t[n] > 0.0) {
				p = ray.pointAt(t[n]);
				if (minimumY < p.y() && p.y() < maximumY && minimumZ < p.z() && p.z() < maximumZ) {
					if (++n == 2) return Interval.between(t[0], t[1]);
				}
			}

			t[n] = (maximumX - ray.getOrigin().x()) / ray.getDirection().x();
			if (t[n] > 0.0) {
				p = ray.pointAt(t[n]);
				if (minimumY < p.y() && p.y() < maximumY && minimumZ < p.z() && p.z() < maximumZ) {
					if (++n == 2) return Interval.between(t[0], t[1]);
				}
			}
		}

		if (ray.getDirection().y() != 0.0) {
			t[n] = (minimumY - ray.getOrigin().y()) / ray.getDirection().y();
			if (t[n] > 0.0) {
				p = ray.pointAt(t[n]);
				if (minimumX < p.x() && p.x() < maximumX && minimumZ < p.z() && p.z() < maximumZ) {
					if (++n == 2) return Interval.between(t[0], t[1]);
				}
			}

			t[n] = (maximumY - ray.getOrigin().y()) / ray.getDirection().y();
			if (t[n] > 0.0) {
				p = ray.pointAt(t[n]);
				if (minimumX < p.x() && p.x() < maximumX && minimumZ < p.z() && p.z() < maximumZ) {
					if (++n == 2) return Interval.between(t[0], t[1]);
				}
			}
		}

		if (ray.getDirection().z() != 0.0) {
			t[n] = (minimumZ - ray.getOrigin().z()) / ray.getDirection().z();
			if (t[n] > 0.0) {
				p = ray.pointAt(t[n]);
				if (minimumX < p.x() && p.x() < maximumX && minimumY < p.y() && p.y() < maximumY) {
					if (++n == 2) return Interval.between(t[0], t[1]);
				}
			}

			t[n] = (maximumZ - ray.getOrigin().z()) / ray.getDirection().z();
			if (t[n] > 0.0) {
				p = ray.pointAt(t[n]);
				if (minimumX < p.x() && p.x() < maximumX && minimumY < p.y() && p.y() < maximumY) {
					if (++n == 2) return Interval.between(t[0], t[1]);
				}
			}
		}

		// If we didn't find two intersection points, then the
		// ray does not intersect the box.
		return Interval.EMPTY;

	}

	/**
	 * Determines whether the specified point is near the
	 * boundary of the box.
	 * @param p The point to consider.
	 * @return A value indicating whether the specified point
	 * 		is near the boundary of the box.
	 */
	public boolean nearBoundary(Point3 p) {
		return ((MathUtil.equal(p.x(), minimumX) || MathUtil.equal(p.x(), maximumX)) && (minimumY <= p.y() && p.y() <= maximumY && minimumZ <= p.z() && p.z() <= maximumZ)) ||
			((MathUtil.equal(p.y(), minimumY) || MathUtil.equal(p.y(), maximumY)) && (minimumX <= p.x() && p.x() <= maximumX && minimumZ <= p.z() && p.z() <= maximumZ)) ||
			((MathUtil.equal(p.z(), minimumZ) || MathUtil.equal(p.z(), maximumZ)) && (minimumX <= p.x() && p.x() <= maximumX && minimumY <= p.y() && p.y() <= maximumY));
	}

	/**
	 * Determines whether the specified point is near the
	 * boundary of the box, within a specified tolerance.
	 * @param p The point to consider.
	 * @param epsilon The tolerance.
	 * @return A value indicating whether the specified point
	 * 		is near the boundary of the box.
	 */
	public boolean nearBoundary(Point3 p, double epsilon) {
		return ((MathUtil.equal(p.x(), minimumX, epsilon) || MathUtil.equal(p.x(), maximumX, epsilon)) && (minimumY <= p.y() && p.y() <= maximumY && minimumZ <= p.z() && p.z() <= maximumZ)) ||
			((MathUtil.equal(p.y(), minimumY, epsilon) || MathUtil.equal(p.y(), maximumY, epsilon)) && (minimumX <= p.x() && p.x() <= maximumX && minimumZ <= p.z() && p.z() <= maximumZ)) ||
			((MathUtil.equal(p.z(), minimumZ, epsilon) || MathUtil.equal(p.z(), maximumZ, epsilon)) && (minimumX <= p.x() && p.x() <= maximumX && minimumY <= p.y() && p.y() <= maximumY));
	}

	/**
	 * Computes the normal at the specified point, assuming p
	 * is on the surface of the box.  This method is guaranteed
	 * to return a unit vector.
	 * @param p The point at which to compute the normal.
	 * @return The normal at the specified point.
	 */
	public Vector3 normalAt(Point3 p) {
		double	cx = (minimumX + maximumX) / 2.0;
		double	cy = (minimumY + maximumY) / 2.0;
		double	cz = (minimumZ + maximumZ) / 2.0;

		double	rx = this.getLengthX() / 2.0;
		double	ry = this.getLengthY() / 2.0;
		double	rz = this.getLengthZ() / 2.0;

		double	dx = (p.x() - cx) / rx;
		double	dy = (p.y() - cy) / ry;
		double	dz = (p.z() - cz) / rz;

		if (Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > Math.abs(dz)) {
			return new Vector3(Math.signum(dx), 0.0, 0.0);
		} else if (Math.abs(dy) > Math.abs(dz)) {
			return new Vector3(0.0, Math.signum(dy), 0.0);
		} else { // Math.abs(dz) > Math.abs(dx) && Math.abs(dz) > Math.abs(dy)
			return new Vector3(0.0, 0.0, Math.signum(dz));
		}
	}

	/**
	 * Interpolates between the bounds of this box along the x-axis.
	 * Equivalent to {@code this.getSpanX().interpolate(t)}.
	 * @param t The point at which to interpolate.
	 * @return The interpolated value.
	 * @see {@link #getSpanX()}, {@link Interval#interpolate(double)}.
	 */
	public double interpolateX(double t) {
		return this.minimumX + t * (this.maximumX - this.minimumX);
	}

	/**
	 * Interpolates between the bounds of this box along the y-axis.
	 * Equivalent to {@code this.getSpanY().interpolate(t)}.
	 * @param t The point at which to interpolate.
	 * @return The interpolated value.
	 * @see {@link #getSpanY()}, {@link Interval#interpolate(double)}.
	 */
	public double interpolateY(double t) {
		return this.minimumY + t * (this.maximumY - this.minimumY);
	}

	/**
	 * Interpolates between the bounds of this box along the z-axis.
	 * Equivalent to {@code this.getSpanZ().interpolate(t)}.
	 * @param t The point at which to interpolate.
	 * @return The interpolated value.
	 * @see {@link #getSpanZ()}, {@link Interval#interpolate(double)}.
	 */
	public double interpolateZ(double t) {
		return this.minimumZ + t * (this.maximumZ - this.minimumZ);
	}

	/**
	 * Interpolates within the bounds of this box.  If (tx, ty, tz) is
	 * in [0, 1]^3, then the interpolated point will fall inside the
	 * box, otherwise the interpolated point will fall outside the
	 * box.  Equivalent to {@code this.interpolate(new Point3(tx, ty, tz))}.
	 * @param tx The point at which to interpolate along the x-axis.
	 * @param ty The point at which to interpolate along the y-axis.
	 * @param tz The point at which to interpolate along the z-axis.
	 * @return The interpolated point.
	 * @see {@link #interpolate(Point3)}.
	 */
	public Point3 interpolate(double tx, double ty, double tz) {
		return new Point3(
				this.interpolateX(tx),
				this.interpolateY(ty),
				this.interpolateZ(tz)
		);
	}

	/**
	 * Interpolates within the bounds of this box.  If {@code p} is
	 * in [0, 1]^3, then the interpolated point will fall inside the
	 * box, otherwise the interpolated point will fall outside the
	 * box.  Equivalent to {@code this.interpolate(p.x(), p.y(), p.z())}.
	 * @param p The point at which to interpolate.
	 * @return The interpolated point.
	 * @see {@link #interpolate(double, double)}.
	 */
	public Point3 interpolate(Point3 p) {
		return this.interpolate(p.x(), p.y(), p.z());
	}

	/**
	 * Default constructor.
	 */
	private Box3() {
		minimumX = minimumY = minimumZ = Double.NaN;
		maximumX = maximumY = maximumZ = Double.NaN;
	}

	/**
	 * Gets an instance of a three dimensional box.
	 * @param minimumX The minimum extent of the box along the x-axis.
	 * @param minimumY The minimum extent of the box along the y-axis.
	 * @param minimumZ The minimum extent of the box along the z-axis.
	 * @param maximumX The maximum extent of the box along the x-axis.
	 * @param maximumY The maximum extent of the box along the y-axis.
	 * @param maximumZ The maximum extent of the box along the z-axis.
	 * @return A new instance of the specified box if minimumX <= maximumX,
	 *         minimumY <= maximumY, and minimumZ <= maximumZ, or
	 *         Box3.EMPTY otherwise.
	 */
	private static final Box3 getInstance(double minimumX, double minimumY, double minimumZ, double maximumX, double maximumY, double maximumZ) {
		if (minimumX > maximumX || minimumY > maximumY || minimumZ > maximumZ) {
			return Box3.EMPTY;
		} else {
			return new Box3(minimumX, minimumY, minimumZ, maximumX, maximumY, maximumZ);
		}
	}

	/**
	 * The box containing all of two dimensional space.
	 * {@code Box3.UNIVERSE.contains(p)} will be true for all p.
	 */
	public static final Box3 UNIVERSE = new Box3(Interval.UNIVERSE, Interval.UNIVERSE, Interval.UNIVERSE);

	/**
	 * The unit box: [0, 1]^3.
	 */
	public static final Box3 UNIT = new Box3(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

	/**
	 * The empty box.
	 * {@code Box3.EMPTY.contains(p)} will be false for all p.
	 */
	public static final Box3 EMPTY = new Box3();

	/** The lower bound along the x-axis. */
	private double minimumX;

	/** The upper bound along the x-axis. */
	private double maximumX;

	/** The lower bound along the y-axis. */
	private double minimumY;

	/** The upper bound along the y-axis. */
	private double maximumY;

	/** The lower bound along the z-axis. */
	private double minimumZ;

	/** The upper bound along the z-axis. */
	private double maximumZ;

}
