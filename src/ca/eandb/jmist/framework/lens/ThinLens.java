/**
 *
 */
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector2;
import ca.eandb.jmist.math.Vector3;

/**
 * A thin <code>Lens</code>.
 * @author Brad Kimmel
 */
public final class ThinLens implements Lens {

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
	 * @see ca.eandb.jmist.framework.Lens#rayAt(ca.eandb.jmist.math.Point2)
	 */
	@Override
	public Ray3 rayAt(Point2 p) {

		Vector2		ap				= RandomUtil.uniformOnDisc(aperatureRadius, Random.DEFAULT).toCartesian();
		Point3		aperaturePoint	= new Point3(ap.x(), ap.y(), 0.0);
		Point3		focalPoint		= new Point3(
											objPlaneWidth * (p.x() - 0.5),
											objPlaneHeight * (0.5 - p.y()),
											-focusDistance
									);

		Vector3		direction		= aperaturePoint.vectorTo(focalPoint).unit();

		return new Ray3(aperaturePoint, direction);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#project(ca.eandb.jmist.math.Point3)
	 */
	@Override
	public Projection project(Point3 p) {
		if (-p.z() < MathUtil.EPSILON) {
			return null;
		}

		Vector2			ap				= RandomUtil.uniformOnDisc(aperatureRadius, Random.DEFAULT).toCartesian();
		final Point3	aperaturePoint	= new Point3(ap.x(), ap.y(), 0.0);
		Vector3			dir				= aperaturePoint.vectorTo(p);
		double			ratio			= -focusDistance / dir.z();
		double			x				= ap.x() + ratio * dir.x();
		double			y				= ap.y() + ratio * dir.y();

		final double	u				= 0.5 + x / objPlaneWidth;
		if (!MathUtil.inRangeCC(u, 0.0, 1.0)) {
			return null;
		}

		final double	v				= 0.5 - y / objPlaneHeight;
		if (!MathUtil.inRangeCC(v, 0.0, 1.0)) {
			return null;
		}

		return new Projection() {
			public Point2 pointOnImagePlane() {
				return new Point2(u, v);
			}

			public Point3 pointOnLens() {
				return aperaturePoint;
			}
		};
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
