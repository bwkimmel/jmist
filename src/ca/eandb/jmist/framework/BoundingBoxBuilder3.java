/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;
import java.util.Collection;

import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Sphere;

/**
 * Builds a box that contains the elements added to it.
 * @author Brad Kimmel
 */
public class BoundingBoxBuilder3 implements Serializable {

	/**
	 * Default constructor.
	 * Initializes the bounding box builder to return an empty box.
	 */
	public BoundingBoxBuilder3() {
		this.reset();
	}

	/**
	 * Gets the smallest box that bounds everything that has been
	 * added since the last reset.
	 * @return The current bounding box.
	 * @see #reset()
	 */
	public Box3 getBoundingBox() {
		if (this.isEmpty()) {
			return Box3.EMPTY;
		}

		// FIXME epsilon checks should not be done at this level.
		return new Box3(minimumX - MathUtil.EPSILON, minimumY - MathUtil.EPSILON, minimumZ - MathUtil.EPSILON, maximumX + MathUtil.EPSILON, maximumY + MathUtil.EPSILON, maximumZ + MathUtil.EPSILON);
	}

	/**
	 * Resets the bounding box builder to return the empty box.
	 */
	public void reset() {
		minimumX = Double.NaN;
	}

	/**
	 * Extends the bounding box to encompass the given collection of points.
	 * @param points The collection of points to extend the bounding box to.
	 */
	public void addAll(Collection<Point3> points) {
		if (!points.isEmpty()) {
			if (isEmpty()) {
				minimumX = minimumY = minimumZ = Double.POSITIVE_INFINITY;
				maximumX = maximumY = maximumZ = Double.NEGATIVE_INFINITY;
			}
			for (Point3 p : points) {
				if (p.x() < minimumX) {	minimumX = p.x(); }
				if (p.x() > maximumX) {	maximumX = p.x(); }
				if (p.y() < minimumY) {	minimumY = p.y(); }
				if (p.y() > maximumY) { maximumY = p.y(); }
				if (p.z() < minimumZ) {	minimumZ = p.z(); }
				if (p.z() > maximumZ) {	maximumZ = p.z(); }
			}
		}
	}

	/**
	 * Extends the bounding box to encompass the given point.
	 * @param p The point to extend the bounding box to.
	 */
	public void add(Point3 p) {
		if (this.isEmpty()) {
			minimumX = maximumX = p.x();
			minimumY = maximumY = p.y();
			minimumZ = maximumZ = p.z();
		} else {
			minimumX = Math.min(p.x(), minimumX);
			minimumY = Math.min(p.y(), minimumY);
			minimumZ = Math.min(p.z(), minimumZ);
			maximumX = Math.max(p.x(), maximumX);
			maximumY = Math.max(p.y(), maximumY);
			maximumZ = Math.max(p.z(), maximumZ);
		}
	}

	/**
	 * Extends the bounding box to encompass the given sphere.
	 * @param sphere The sphere to include in the bounding box.
	 */
	public void add(Sphere sphere) {
		if (!sphere.isEmpty()) {
			double r = sphere.radius();
			Point3 c = sphere.center();

			if (this.isEmpty()) {
				minimumX = c.x() - r;
				minimumY = c.y() - r;
				minimumZ = c.z() - r;
				maximumX = c.x() + r;
				maximumY = c.y() + r;
				maximumZ = c.z() + r;
			} else {
				minimumX = Math.min(c.x() - r, minimumX);
				minimumY = Math.min(c.y() - r, minimumY);
				minimumZ = Math.min(c.z() - r, minimumZ);
				maximumX = Math.max(c.x() + r, maximumX);
				maximumY = Math.max(c.y() + r, maximumY);
				maximumZ = Math.max(c.z() + r, maximumZ);
			}
		}
	}

	/**
	 * Extends the bounding box to encompass the given box.
	 * @param box The box to include in the bounding box.
	 */
	public void add(Box3 box) {
		if (!box.isEmpty()) {
			if (this.isEmpty()) {
				minimumX = box.minimumX();
				minimumY = box.minimumY();
				minimumZ = box.minimumZ();
				maximumX = box.maximumX();
				maximumY = box.maximumY();
				maximumZ = box.maximumZ();
			} else {
				minimumX = Math.min(box.minimumX(), minimumX);
				minimumY = Math.min(box.minimumY(), minimumY);
				minimumZ = Math.min(box.minimumZ(), minimumZ);
				maximumX = Math.max(box.maximumX(), maximumX);
				maximumY = Math.max(box.maximumY(), maximumY);
				maximumZ = Math.max(box.maximumZ(), maximumZ);
			}
		}
	}

	/**
	 * Indicates whether the bounding box is currently empty.
	 * Equivalent to this.getBoundingBox().isEmpty().
	 * @return A value indicating whether the bounding box is
	 * 		currently empty.
	 * @see #getBoundingBox(), {@link Box3#isEmpty()}
	 */
	public boolean isEmpty() {
		return Double.isNaN(minimumX);
	}

	private double minimumX, minimumY, minimumZ;
	private double maximumX, maximumY, maximumZ;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 351994557507294139L;

}
