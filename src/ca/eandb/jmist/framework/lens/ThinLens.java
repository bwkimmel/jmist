/**
 *
 */
package ca.eandb.jmist.framework.lens;

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
public final class ThinLens extends AbstractLens {

	/** Serialization version ID. */
	private static final long serialVersionUID = -4932532440872351450L;

	/**
	 * Creates a new <code>ThinLens</code>.
	 */
	public ThinLens() {
		this.focalLength		= DEFAULT_FOCAL_LENGTH;
		this.aperture			= DEFAULT_APERTURE;
		this.focusDistance		= DEFAULT_FOCUS_DISTANCE;
		this.fov				= DEFAULT_FIELD_OF_VIEW;
		this.aspect				= DEFAULT_ASPECT_RATIO;
		this.apertureRadius		= 0.5 * focalLength / aperture;
		this.apertureArea		= Math.PI * apertureRadius * apertureRadius;
		this.objPlaneWidth		= 2.0 * focusDistance * Math.tan(fov / 2.0);
		this.objPlaneHeight		= objPlaneWidth / aspect;
	}

	/**
	 * Creates a new <code>ThinLens</code>.
	 * @param focalLength The focal length (in meters).
	 * @param aperture The aperture (f-number).
	 * @param focusDistance The distance to the plane in focus (in meters).
	 * @param fov The field of view (in radians).
	 * @param aspect The aspect ratio.
	 */
	public ThinLens(double focalLength, double aperture, double focusDistance, double fov, double aspect) {
		this.focalLength		= focalLength;
		this.aperture			= aperture;
		this.focusDistance		= focusDistance;
		this.fov				= fov;
		this.aspect				= aspect;
		this.apertureRadius		= 0.5 * focalLength / aperture;
		this.apertureArea		= Math.PI * apertureRadius * apertureRadius;
		this.objPlaneWidth		= 2.0 * focusDistance * Math.tan(fov / 2.0);
		this.objPlaneHeight		= objPlaneWidth / aspect;
	}

	/** The default field of view (in radians). */
	static final double	DEFAULT_FIELD_OF_VIEW		= Math.PI / 2.0;

	/** The default aspect ratio. */
	static final double	DEFAULT_ASPECT_RATIO		= 1.0;

	/** The default focal length (in meters). */
	static final double	DEFAULT_FOCAL_LENGTH		= 0.050;

	/** The default aperture (f-number). */
	static final double	DEFAULT_APERTURE			= 3.6;

	/** The default distance to the plane in focus (in meters). */
	static final double	DEFAULT_FOCUS_DISTANCE		= 1.0;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#rayAt(ca.eandb.jmist.math.Point2)
	 */
	public Ray3 rayAt(Point2 p) {

		Vector2		ap				= RandomUtil.uniformOnDisc(apertureRadius, Random.DEFAULT).toCartesian();
		Point3		aperturePoint	= new Point3(ap.x(), ap.y(), 0.0);
		Point3		focalPoint		= new Point3(
											objPlaneWidth * (p.x() - 0.5),
											objPlaneHeight * (0.5 - p.y()),
											-focusDistance
									);

		Vector3		direction		= aperturePoint.vectorTo(focalPoint).unit();

		return new Ray3(aperturePoint, direction);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#areaOfAperture()
	 */
	public double areaOfAperture() {
		return apertureArea;
	}

	private Projection project(Vector3 dir, final Point3 aperturePoint) {
		double			ratio			= -focusDistance / dir.z();
		double			x				= aperturePoint.x() + ratio * dir.x();
		double			y				= aperturePoint.y() + ratio * dir.y();

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
				return aperturePoint;
			}

			public double importance() {
				return 1.0; // FIXME Light tracing will not work until this is corrected.
			}
		};

	}

	private Point3 generateAperturePoint() {
		Vector2 ap = RandomUtil.uniformOnDisc(apertureRadius, Random.DEFAULT).toCartesian();
		return new Point3(ap.x(), ap.y(), 0.0);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#project(ca.eandb.jmist.math.Point3)
	 */
	public Projection project(Point3 p) {
		if (-p.z() < MathUtil.EPSILON) {
			return null;
		}

		Point3 aperturePoint = generateAperturePoint();
		Vector3	dir = aperturePoint.vectorTo(p);

		return project(dir, aperturePoint);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.lens.AbstractLens#project(ca.eandb.jmist.math.Vector3)
	 */
	public Projection project(Vector3 v) {
		if (-v.z() < MathUtil.EPSILON) {
			return null;
		}

		Point3 aperturePoint = generateAperturePoint();
		return project(v, aperturePoint);
	}

	/** The focal length (in meters). */
	private final double focalLength;

	/** The aperture (f-number). */
	private final double aperture;

	/** The distance to the plane in focus (in meters). */
	private final double focusDistance;

	/** The field of view (in radians). */
	private final double fov;

	/** The aspect ratio. */
	private final double aspect;

	/** The radius of the aperture (in meters). */
	private final double apertureRadius;

	/** The area of the aperture (in meters squared). */
	private final double apertureArea;

	/** The width of the virtual screen at the focus distance (in meters). */
	private final double objPlaneWidth;

	/** The height of the virtual screen at the focus distance (in meters). */
	private final double objPlaneHeight;

}
