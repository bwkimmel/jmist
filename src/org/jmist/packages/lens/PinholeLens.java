/**
 *
 */
package org.jmist.packages.lens;

import org.jmist.toolkit.Point2;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Vector3;

/**
 * A camera that captures light at a single point.
 * This is equivalent to the limit as the aperature
 * width and shutter speed approach zero for a
 * normal camera.  A pinhole camera has an infinite
 * depth of field (i.e., no depth of field effects
 * are observed).
 * @author bkimmel
 */
public final class PinholeLens extends TransformableLens {

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
	 * @see org.jmist.packages.TransformableLens#viewRayAt(org.jmist.toolkit.Point2)
	 */
	@Override
	protected Ray3 viewRayAt(Point2 p) {

		return new Ray3(
			Point3.ORIGIN,
			Vector3.unit(
				width * (p.x() - 0.5),
				height * (0.5 - p.y()),
				-1.0
			)
		);

	}

	/** The width of the virtual image plane. */
	private final double width;

	/** The height of the virtual image plane. */
	private final double height;

}
