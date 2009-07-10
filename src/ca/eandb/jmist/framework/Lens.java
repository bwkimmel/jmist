/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;

/**
 * Generates the ray to cast corresponding to given points on the
 * image plane.
 * @author Brad Kimmel
 */
public interface Lens {

	/**
	 * Gets a ray indicating from which point and
	 * direction the camera is sensitive to incoming
	 * light at the specified point on its image plane.
	 * This will correspond to the direction to cast
	 * a ray in order to shade the specified point on
	 * the image plane.
	 * @param p The point on the image plane in
	 * 		normalized device coordinates (must fall
	 * 		within {@code Box2.UNIT}).
	 * @return The ray to cast for ray shading.
	 * @see {@link Box2#UNIT}
	 */
	Ray3 rayAt(Point2 p);

	/**
	 * Projects a point in three-dimensional space onto the image plane.
	 * @param p The <code>Point3</code> to project onto the image plane.
	 * @return The <code>Projection</code> representing the projection of
	 * 		<code>p</code> onto the image plane, or <code>null</code> if
	 * 		<code>p</code> does not project onto the image plane.
	 */
	Projection project(Point3 p);

	/**
	 * A representation of the projection of a point in three dimensional space
	 * onto the image plane represented by a <code>Lens</code>.
	 * @author Brad Kimmel
	 */
	public static interface Projection {

		/**
		 * Returns the <code>Point2</code> representing the normalized point on
		 * the image plane.
		 * @return The <code>Point2</code> representing the normalized point on
		 * 		the image plane.
		 */
		Point2 pointOnImagePlane();

		/**
		 * Returns the <code>Point3</code> representing the physical point on
		 * the <code>Lens</code>.
		 * @return The <code>Point3</code> representing the physical point on
		 * 		the <code>Lens</code>.
		 */
		Point3 pointOnLens();

		/**
		 * Returns the importance associated with this projection.  This is the
		 * value that should be used to attenuate contribution rays during
		 * light tracing.
		 * @return The importance associated with this projection.
		 */
		double importance();

	}

}
