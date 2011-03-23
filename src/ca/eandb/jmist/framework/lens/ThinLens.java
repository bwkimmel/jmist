/**
 *
 */
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.path.EyeNode;
import ca.eandb.jmist.framework.path.EyeTerminalNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.HPoint3;
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

	/** The default field of view (in radians). */
	public static final double	DEFAULT_FIELD_OF_VIEW		= Math.PI / 2.0;

	/** The default aspect ratio. */
	public static final double	DEFAULT_ASPECT_RATIO		= 1.0;

	/** The default focal length (in meters). */
	public static final double	DEFAULT_FOCAL_LENGTH		= 0.050;

	/** The default aperture (f-number). */
	public static final double	DEFAULT_APERTURE			= 3.6;

	/** The default distance to the plane in focus (in meters). */
	public static final double	DEFAULT_FOCUS_DISTANCE		= 1.0;

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
//
//	/* (non-Javadoc)
//	 * @see ca.eandb.jmist.framework.Lens#rayAt(ca.eandb.jmist.math.Point2)
//	 */
//	public Ray3 rayAt(Point2 p) {
//
//		Vector2		ap				= RandomUtil.uniformOnDisc(apertureRadius, Random.DEFAULT).toCartesian();
//		Point3		aperturePoint	= new Point3(ap.x(), ap.y(), 0.0);
//		Point3		focalPoint		= new Point3(
//											objPlaneWidth * (p.x() - 0.5),
//											objPlaneHeight * (0.5 - p.y()),
//											-focusDistance
//									);
//
//		Vector3		direction		= aperturePoint.vectorTo(focalPoint).unit();
//
//		return new Ray3(aperturePoint, direction);
//
//	}
//
//	/* (non-Javadoc)
//	 * @see ca.eandb.jmist.framework.Lens#areaOfAperture()
//	 */
//	public double areaOfAperture() {
//		return apertureArea;
//	}
//
//	private Projection project(Vector3 dir, final Point3 aperturePoint) {
//		double			ratio			= -focusDistance / dir.z();
//		double			x				= aperturePoint.x() + ratio * dir.x();
//		double			y				= aperturePoint.y() + ratio * dir.y();
//
//		final double	u				= 0.5 + x / objPlaneWidth;
//		if (!MathUtil.inRangeCC(u, 0.0, 1.0)) {
//			return null;
//		}
//
//		final double	v				= 0.5 - y / objPlaneHeight;
//		if (!MathUtil.inRangeCC(v, 0.0, 1.0)) {
//			return null;
//		}
//
//		return new Projection() {
//			public Point2 pointOnImagePlane() {
//				return new Point2(u, v);
//			}
//
//			public Point3 pointOnLens() {
//				return aperturePoint;
//			}
//
//			public double importance() {
//				return 1.0; // FIXME Light tracing will not work until this is corrected.
//			}
//		};
//
//	}
//
//	private Point3 generateAperturePoint() {
//		Vector2 ap = RandomUtil.uniformOnDisc(apertureRadius, Random.DEFAULT).toCartesian();
//		return new Point3(ap.x(), ap.y(), 0.0);
//	}
//
//	/* (non-Javadoc)
//	 * @see ca.eandb.jmist.framework.Lens#project(ca.eandb.jmist.math.Point3)
//	 */
//	public Projection project(Point3 p) {
//		if (-p.z() < MathUtil.EPSILON) {
//			return null;
//		}
//
//		Point3 aperturePoint = generateAperturePoint();
//		Vector3	dir = aperturePoint.vectorTo(p);
//
//		return project(dir, aperturePoint);
//	}
//
//	/* (non-Javadoc)
//	 * @see ca.eandb.jmist.framework.lens.AbstractLens#project(ca.eandb.jmist.math.Vector3)
//	 */
//	public Projection project(Vector3 v) {
//		if (-v.z() < MathUtil.EPSILON) {
//			return null;
//		}
//
//		Point3 aperturePoint = generateAperturePoint();
//		return project(v, aperturePoint);
//	}

	@Override
	public EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv,
			double rj) {
		return new Node(p, pathInfo, ru, rv, rj);
	}

	/**
	 * An <code>EyeNode</code> generated by a <code>PinholeCamera</code>.
	 */
	private final class Node extends EyeTerminalNode {

		/** Projected point on the image plane. */
		private final Point2 pointOnImagePlane;
		
		private final Ray3 ray;

		public Node(Point2 p, PathInfo pathInfo, double ru, double rv, double rj) {
			super(pathInfo, ru, rv, rj);
			this.pointOnImagePlane = p;
			
			Vector2 uv = RandomUtil.uniformOnDisc(apertureRadius, ru, rv).toCartesian();
			Point3 origin = new Point3(uv.x(), uv.y(), 0.0);
			Point3 focus = new Point3(
					(pointOnImagePlane.x() - 0.5) * objPlaneWidth,
					(0.5 - pointOnImagePlane.y()) * objPlaneHeight,
					-focusDistance);
			
			Vector3 direction = origin.unitVectorTo(focus);
			
			this.ray = new Ray3(origin, direction);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.EyeNode#project(ca.eandb.jmist.math.HPoint3)
		 */
		@Override
		public Point2 project(HPoint3 p) {
			Ray3 pray = new Ray3(ray.origin(), p);
			Vector3 dir = pray.direction();
			if (-dir.z() < MathUtil.EPSILON) {
				return null;
			}
			
			double			ratio			= -focusDistance / dir.z();
			double			x				= ray.origin().x() + ratio * dir.x();
			double			y				= ray.origin().y() + ratio * dir.y();
	
			final double	u				= 0.5 + x / objPlaneWidth;
			if (!MathUtil.inRangeCC(u, 0.0, 1.0)) {
				return null;
			}
	
			final double	v				= 0.5 - y / objPlaneHeight;
			if (!MathUtil.inRangeCC(v, 0.0, 1.0)) {
				return null;
			}
			
			return new Point2(u, v);
		}
		
		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.PathNode#getPDF()
		 */
		@Override
		public double getPDF() {
			return 1.0 / apertureArea;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.PathNode#isSpecular()
		 */
		@Override
		public boolean isSpecular() {
			return false;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.PathNode#getPosition()
		 */
		@Override
		public HPoint3 getPosition() {
			return ray.origin();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.PathNode#sample(double, double, double)
		 */
		@Override
		public ScatteredRay sample(double ru, double rv, double rj) {
			Vector3 v = ray.direction();
			Color color = getWhite();
			double pdf = (focusDistance * focusDistance)
					/ (v.z() * v.z() * v.z() * v.z() * objPlaneWidth * objPlaneHeight);
			return ScatteredRay.diffuse(ray, color, pdf);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.PathNode#scatter(ca.eandb.jmist.math.Vector3)
		 */
		@Override
		public Color scatter(Vector3 v) {
			return getGray(getPDF(v));
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.PathNode#getCosine(ca.eandb.jmist.math.Vector3)
		 */
		@Override
		public double getCosine(Vector3 v) {
			return -v.z() / v.length();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.PathNode#getPDF(ca.eandb.jmist.math.Vector3)
		 */
		@Override
		public double getPDF(Vector3 v) {
			v = v.unit();
			return (focusDistance * focusDistance)
					/ (v.z() * v.z() * v.z() * v.z() * objPlaneWidth * objPlaneHeight);
		}
		
	}
}
