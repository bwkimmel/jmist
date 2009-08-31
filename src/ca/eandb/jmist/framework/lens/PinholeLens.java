/**
 *
 */
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.gi2.EyeNode;
import ca.eandb.jmist.framework.gi2.EyeTerminalNode;
import ca.eandb.jmist.framework.gi2.PathInfo;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A camera that captures light at a single point.  This is equivalent to the
 * limit as the aperature width and shutter speed approach zero for a normal
 * camera.  A pinhole camera has an infinite depth of field (i.e., no depth of
 * field effects are observed).
 * @author Brad Kimmel
 */
public final class PinholeLens extends AbstractLens {

	/** Serialization version ID. */
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

		double hw = 0.5 * width;
		double hh = 0.5 * height;
		double rw = Math.sqrt(hw * hw + 1.0);
		double rh = Math.sqrt(hh * hh + 1.0);
		double a = hw * Math.atan(hh / rw) / rw;
		double b = hh * Math.atan(hw / rh) / rh;
		this.normalizationFactor = 2.0 * (a + b);
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
	 * @see ca.eandb.jmist.framework.lens.SingularApertureLens#rayAt(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public EyeNode sample(PathInfo pathInfo, Random rnd) {
		return new Node(pathInfo);
	}

	private final class Node extends EyeTerminalNode {

		public Node(PathInfo pathInfo) {
			super(pathInfo);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.gi2.EyeNode#sample(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.Random)
		 */
		public ScatteredRay sample(Point2 p, Random rnd) {
			Ray3 ray = new Ray3(
					Point3.ORIGIN,
					Vector3.unit(
							width * (p.x() - 0.5),
							height * (0.5 - p.y()),
							-1.0));
			Color color = getWhite();
			double z = p.x() * p.x() + p.y() * p.y() + 1.0;
			double w = 1.0 / (z * z);
			double pdf = w / normalizationFactor;
			return ScatteredRay.diffuse(ray, color, pdf);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.gi2.PathNode#scatterTo(ca.eandb.jmist.framework.gi2.PathNode)
		 */
		public Color scatter(Vector3 v) {
			Point2 p = project(v);
			if (p != null) {
				double z = p.x() * p.x() + p.y() * p.y() + 1.0;
				double w = 1.0 / (z * z);
				return getGray(w / normalizationFactor);
			} else {
				return getBlack();
			}
		}

		public Point2 project(HPoint3 x) {
			Ray3 ray = new Ray3(Point3.ORIGIN, x);
			Vector3 v = ray.direction();
			if (-v.z() < MathUtil.EPSILON) {
				return null;
			}
			Point2 p = new Point2(
					0.5 - v.x() / (width * v.z()),
					0.5 + v.y() / (height * v.z()));
			return Box2.UNIT.contains(p) ? p : null;
		}

		public double getCosine(Vector3 v) {
			return -v.z() / v.length();
		}

		public HPoint3 getPosition() {
			return Point3.ORIGIN;
		}

		public double getPDF() {
			return 1.0;
		}

		public boolean isSpecular() {
			return true;
		}

	}

	/** The width of the virtual image plane. */
	private final double width;

	/** The height of the virtual image plane. */
	private final double height;

	private final double normalizationFactor;

}
