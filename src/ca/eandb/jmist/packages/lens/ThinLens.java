/**
 *
 */
package ca.eandb.jmist.packages.lens;

import ca.eandb.jmist.toolkit.Point2;
import ca.eandb.jmist.toolkit.Point3;
import ca.eandb.jmist.toolkit.RandomUtil;
import ca.eandb.jmist.toolkit.Ray3;
import ca.eandb.jmist.toolkit.Vector2;
import ca.eandb.jmist.toolkit.Vector3;

/**
 * A thin <code>Lens</code>.
 * @author Brad Kimmel
 */
public final class ThinLens extends TransformableLens {

	/**
	 * Creates a new <code>ThinLens</code>.
	 */
	public ThinLens() {
		this.focalLength		= DEFAULT_FOCAL_LENGTH;
		this.aperature			= DEFAULT_APERATURE;
		this.focusDistance		= DEFAULT_FOCUS_DISTANCE;
		this.fov				= DEFAULT_FIELD_OF_VIEW;
		this.aspect				= DEFAULT_ASPECT_RATIO;
		this.aperatureRadius	= 0.5 * focalLength / aperature;
		this.objPlaneWidth		= 2.0 * focusDistance * Math.tan(fov / 2.0);
		this.objPlaneHeight		= objPlaneWidth / aspect;
	}

	/**
	 * Creates a new <code>ThinLens</code>.
	 * @param focalLength The focal length (in meters).
	 * @param aperature The aperature (f-number).
	 * @param focusDistance The distance to the plane in focus (in meters).
	 * @param fov The field of view (in radians).
	 * @param aspect The aspect ratio.
	 */
	public ThinLens(double focalLength, double aperature, double focusDistance, double fov, double aspect) {
		this.focalLength		= focalLength;
		this.aperature			= aperature;
		this.focusDistance		= focusDistance;
		this.fov				= fov;
		this.aspect				= aspect;
		this.aperatureRadius	= 0.5 * focalLength / aperature;
		this.objPlaneWidth		= 2.0 * focusDistance * Math.tan(fov / 2.0);
		this.objPlaneHeight		= objPlaneWidth / aspect;
	}

	/** The default field of view (in radians). */
	static final double	DEFAULT_FIELD_OF_VIEW		= Math.PI / 2.0;

	/** The default aspect ratio. */
	static final double	DEFAULT_ASPECT_RATIO		= 1.0;

	/** The default focal length (in meters). */
	static final double	DEFAULT_FOCAL_LENGTH		= 0.050;

	/** The default aperature (f-number). */
	static final double	DEFAULT_APERATURE			= 3.6;

	/** The default distance to the plane in focus (in meters). */
	static final double	DEFAULT_FOCUS_DISTANCE		= 1.0;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.packages.TransformableLens#viewRayAt(ca.eandb.jmist.toolkit.Point2)
	 */
	@Override
	protected Ray3 viewRayAt(Point2 p) {

		Vector2		ap				= RandomUtil.uniformOnDisc(aperatureRadius).toCartesian();
		Point3		aperaturePoint	= new Point3(ap.x(), ap.y(), 0.0);
		Point3		focalPoint		= new Point3(
											objPlaneWidth * (p.x() - 0.5),
											objPlaneHeight * (0.5 - p.y()),
											focusDistance
									);

		Vector3		direction		= aperaturePoint.vectorTo(focalPoint).unit();

		return new Ray3(aperaturePoint, direction);

	}

	/** The focal length (in meters). */
	private final double focalLength;

	/** The aperature (f-number). */
	private final double aperature;

	/** The distance to the plane in focus (in meters). */
	private final double focusDistance;

	/** The field of view (in radians). */
	private final double fov;

	/** The aspect ratio. */
	private final double aspect;

	/** The radius of the aperature (in meters). */
	private final double aperatureRadius;

	/** The width of the virtual screen at the focus distance (in meters). */
	private final double objPlaneWidth;

	/** The height of the virtual screen at the focus distance (in meters). */
	private final double objPlaneHeight;

}
