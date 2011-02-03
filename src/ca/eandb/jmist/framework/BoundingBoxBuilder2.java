/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;
import java.util.Collection;

import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Circle;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;

/**
 * Builds a box that contains the elements added to it.
 * @author Brad Kimmel
 */
public class BoundingBoxBuilder2 implements Serializable {

	/**
	 * Default constructor.
	 * Initializes the bounding box builder to return an empty box.
	 */
	public BoundingBoxBuilder2() {
		this.reset();
	}

	/**
	 * Gets the smallest box that bounds everything that has been
	 * added since the last reset.
	 * @return The current bounding box.
	 * @see #reset()
	 */
	public Box2 getBoundingBox() {
		if (this.isEmpty()) {
			return Box2.EMPTY;
		}

		return new Box2(minimumX, minimumY, maximumX, maximumY);
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
				minimumX = minimumY = Double.POSITIVE_INFINITY;
				maximumX = maximumY = Double.NEGATIVE_INFINITY;
			}
			for (Point3 p : points) {
				if (p.x() < minimumX) {	minimumX = p.x(); }
				if (p.x() > maximumX) {	maximumX = p.x(); }
				if (p.y() < minimumY) {	minimumY = p.y(); }
				if (p.y() > maximumY) { maximumY = p.y(); }
			}
		}
	}

	/**
	 * Extends the bounding box to encompass the given point.
	 * @param p The point to extend the bounding box to.
	 */
	public void add(Point2 p) {
		if (this.isEmpty()) {
			minimumX = maximumX = p.x();
			minimumY = maximumY = p.y();
		} else {
			minimumX = Math.min(p.x(), minimumX);
			minimumY = Math.min(p.y(), minimumY);
			maximumX = Math.max(p.x(), maximumX);
			maximumY = Math.max(p.y(), maximumY);
		}
	}

	/**
	 * Extends the bounding box to encompass the given sphere.
	 * @param circle The circle to include in the bounding box.
	 */
	public void add(Circle circle) {
		if (!circle.isEmpty()) {
			double r = circle.radius();
			Point2 c = circle.center();

			if (this.isEmpty()) {
				minimumX = c.x() - r;
				minimumY = c.y() - r;
				maximumX = c.x() + r;
				maximumY = c.y() + r;
			} else {
				minimumX = Math.min(c.x() - r, minimumX);
				minimumY = Math.min(c.y() - r, minimumY);
				maximumX = Math.max(c.x() + r, maximumX);
				maximumY = Math.max(c.y() + r, maximumY);
			}
		}
	}

	/**
	 * Extends the bounding box to encompass the given box.
	 * @param box The box to include in the bounding box.
	 */
	public void add(Box2 box) {
		if (!box.isEmpty()) {
			if (this.isEmpty()) {
				minimumX = box.minimumX();
				minimumY = box.minimumY();
				maximumX = box.maximumX();
				maximumY = box.maximumY();
			} else {
				minimumX = Math.min(box.minimumX(), minimumX);
				minimumY = Math.min(box.minimumY(), minimumY);
				maximumX = Math.max(box.maximumX(), maximumX);
				maximumY = Math.max(box.maximumY(), maximumY);
			}
		}
	}

	/**
	 * Indicates whether the bounding box is currently empty.
	 * Equivalent to this.getBoundingBox().isEmpty().
	 * @return A value indicating whether the bounding box is
	 * 		currently empty.
	 * @see #getBoundingBox(), {@link Box2#isEmpty()}
	 */
	public boolean isEmpty() {
		return Double.isNaN(minimumX);
	}

	private double minimumX, minimumY;
	private double maximumX, maximumY;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 351994557507294139L;

}
