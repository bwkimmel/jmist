/**
 *
 */
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A camera that captures light at a single point.
 * This is equivalent to the limit as the aperature
 * width and shutter speed approach zero for a
 * normal camera.  A pinhole camera has an infinite
 * depth of field (i.e., no depth of field effects
 * are observed).
 * @author Brad Kimmel
 */
public final class PinholeLens extends SingularApertureLens {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 727072559000881003L;

	/**
	 * Initializes the pinhole camera from the specified dimensions of the
	 * virtual image plane.  The virtual image plane is one meter from the
	 * origin along the negative z-axis.
	 * @param width The width of the virtual image plane (in meters).
	 * @param height The height of the virtual image plane (in meters).
	 */
	public PinholeLens(double width, double height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Initializes the pinhole camera from the specified
	 * field of view and aspect ratio.
	 * @param horizontalFieldOfView The field of view in the horizontal
	 * 		direction (in radians).  This value must be in
	 * 		(0, PI).
	 * @param aspectRatio The ratio between the width and
	 * 		height of the image.  This value must be positive.
	 */
	public static PinholeLens fromHfovAndAspect(double horizontalFieldOfView, double aspectRatio) {

		// Compute the width and height of the virtual
		// image plane from the provided field of view
		// and aspect ratio.  The image plane is assumed
		// to be one unit away from the origin.
		double width = 2.0 * Math.tan(0.5 * horizontalFieldOfView);
		double height = width / aspectRatio;
		return new PinholeLens(width, height);

	}

	/**
	 * Initializes the pinhole camera from the specified
	 * field of view and aspect ratio.
	 * @param verticalFieldOfView The field of view in the vertical
	 * 		direction (in radians).  This value must be in
	 * 		(0, PI).
	 * @param aspectRatio The ratio between the width and
	 * 		height of the image.  This value must be positive.
	 */
	public static PinholeLens fromVfovAndAspect(double verticalFieldOfView, double aspectRatio) {

		// Compute the width and height of the virtual
		// image plane from the provided field of view
		// and aspect ratio.  The image plane is assumed
		// to be one unit away from the origin.
		double height = 2.0 * Math.tan(0.5 * verticalFieldOfView);
		double width = height * aspectRatio;
		return new PinholeLens(width, height);

	}

	/**
	 * Initializes the pinhole camera from the specified
	 * field of view in the horizontal and vertical directions
	 * @param horizontalFieldOfView The field of view in the horizontal
	 * 		direction (in radians).  This value must be in
	 * 		(0, PI).
	 * @param verticalFieldOfView The field of view in the vertical
	 * 		direction (in radians).  This value must be in
	 * 		(0, PI).
	 */
	public static PinholeLens fromFieldOfView(double horizontalFieldOfView,
			double verticalFieldOfView) {

		// Compute the width and height of the virtual
		// image plane from the provided field of view
		// and aspect ratio.  The image plane is assumed
		// to be one unit away from the origin.
		double width = 2.0 * Math.tan(0.5 * horizontalFieldOfView);
		double height = 2.0 * Math.tan(0.5 * verticalFieldOfView);
		return new PinholeLens(width, height);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#rayAt(ca.eandb.jmist.math.Point2)
	 */
	public Ray3 rayAt(Point2 p) {

		return new Ray3(
			Point3.ORIGIN,
			Vector3.unit(
				width * (p.x() - 0.5),
				height * (0.5 - p.y()),
				-1.0
			)
		);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#project(ca.eandb.jmist.math.Vector3)
	 */
	public Projection project(final Vector3 v) {
		if (-v.z() < MathUtil.EPSILON) {
			return null;
		}
		final Point2 ndc = new Point2(
				0.5 - v.x() / (width * v.z()),
				0.5 + v.y() / (height * v.z()));

		if (Box2.UNIT.contains(ndc)) {
			return new Projection() {
				public Point2 pointOnImagePlane() {
					return ndc;
				}

				public Point3 pointOnLens() {
					return Point3.ORIGIN;
				}

				public double importance() {
					double dot = -v.unit().z();
					return 1.0 / (dot * dot * dot * v.squaredLength());
				}
			};
		} else {
			return null;
		}
	}

	/** The width of the virtual image plane. */
	private final double width;

	/** The height of the virtual image plane. */
	private final double height;

}
