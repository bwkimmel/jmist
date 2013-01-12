/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.path.EyeNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Point2;

/**
 * Generates the ray to cast corresponding to given points on the
 * image plane.
 * @author Brad Kimmel
 */
public interface Lens extends Serializable {

	/**
	 * Gets a ray indicating from which point and direction the camera is
	 * sensitive to incoming light at the specified point on its image plane.
	 * This will correspond to the direction to cast a ray in order to shade
	 * the specified point on the image plane.
	 * @param p The point on the image plane in normalized device coordinates
	 * 		(must fall within {@code Box2.UNIT}).
	 * @return The ray to cast for ray shading.
	 * @see {@link Box2#UNIT}
	 */
	ScatteredRay rayAt(Point2 p, WavelengthPacket lambda, Random rnd);

	/**
	 * Creates the terminal <code>EyeNode</code> for use by path-integration
	 * based rendering algorithms.
	 * @param p The point on the image plane in normalized device coordinates
	 * 		(must fall within {@code Box2.UNIT}).
	 * @param pathInfo The <code>PathInfo</code> describing the context in
	 * 		which the path is being generated.
	 * @param ru The first random variable (must be in [0, 1]).
	 * @param rv The second random variable (must be in [0, 1]).
	 * @param rj The third random variable (must be in [0, 1]).
	 * @return A new <code>EyeNode</code>.
	 */
	EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv, double rj);

	/** A dummy <code>Lens</code> that does not render anything. */
	public static final Lens NULL = new Lens() {
		private static final long serialVersionUID = 2076070894932926479L;
		public ScatteredRay rayAt(Point2 p, WavelengthPacket lambda, Random rnd) {
			return null;
		}
		public EyeNode sample(Point2 p, PathInfo pathInfo, double ru,
				double rv, double rj) {
			return null;
		}
	};

//
//	/**
//	 * Projects a point in three-dimensional space onto the image plane.
//	 * @param p The <code>Point3</code> to project onto the image plane.
//	 * @return The <code>Projection</code> representing the projection of
//	 * 		<code>p</code> onto the image plane, or <code>null</code> if
//	 * 		<code>p</code> does not project onto the image plane.
//	 */
//	Projection project(Point3 p);
//
//	/**
//	 * Projects a point at infinite distance in three-dimensional space onto
//	 * the image plane.
//	 * @param v The <code>Vector3</code> indicating the direction of the point
//	 * 		at an infinite distance.  This is assumed to be a unit vector.  If
//	 * 		it is not, the results are undefined.
//	 * @return The <code>Projection</code> representing the projection of a
//	 * 		point at an infinite distance in the direction of <code>v</code>
//	 * 		onto the image plane, or <code>null</code> if <code>v</code> does
//	 * 		not project onto the image plane.
//	 */
//	Projection project(Vector3 v);
//
//	/**
//	 * Projects a homogenized point in three-dimensional space onto the image
//	 * plane.  This is equivalent to {@link #project(Point3)} or to
//	 * {@link #project(Vector3)} depending on whether the homogenized point is
//	 * a point or a vector.
//	 * @param p The <code>HPoint3</code> representing the homogenized point to
//	 * 		project.
//	 * @return The <code>Projection</code> representing the projection of
//	 * 		<code>p</code> onto the image plane, or <code>null</code> if
//	 * 		<code>p</code> does not project onto the image plane.
//	 */
//	Projection project(HPoint3 p);
//
//	/**
//	 * A representation of the projection of a point in three dimensional space
//	 * onto the image plane represented by a <code>Lens</code>.
//	 * @author Brad Kimmel
//	 */
//	public static interface Projection {
//
//		/**
//		 * Returns the <code>Point2</code> representing the normalized point on
//		 * the image plane.
//		 * @return The <code>Point2</code> representing the normalized point on
//		 * 		the image plane.
//		 */
//		Point2 pointOnImagePlane();
//
//		/**
//		 * Returns the <code>Point3</code> representing the physical point on
//		 * the <code>Lens</code>.
//		 * @return The <code>Point3</code> representing the physical point on
//		 * 		the <code>Lens</code>.
//		 */
//		Point3 pointOnLens();
//
//		/**
//		 * Returns the importance associated with this projection.  This is the
//		 * value that should be used to attenuate contribution rays during
//		 * light tracing.
//		 * @return The importance associated with this projection.
//		 */
//		double importance();
//
//	}

}
