/**
 *
 */
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.path.EyeNode;
import ca.eandb.jmist.framework.path.EyeTerminalNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Lens</code> that projects the scene onto a cylindrical virtual
 * screen.
 * @author Brad Kimmel
 */
public final class PanoramicLens extends AbstractLens {

	/** Serialization version ID. */
	private static final long serialVersionUID = 9079096932171202826L;

	/** The default horizontal field of view (in radians). */
	public static final double DEFAULT_HORIZONTAL_FIELD_OF_VIEW = Math.PI;

	/** The default vertical field of view (in radians). */
	public static final double DEFAULT_VERTICAL_FIELD_OF_VIEW = Math.PI / 2.0;

	/** Horizontal field of view (in radians). */
	private final double hfov;

	/** Height of the virtual image plane. */
	private final double height;

	/**
	 * Creates a new <code>PanoramicLens</code>.
	 */
	public PanoramicLens() {
		this(DEFAULT_HORIZONTAL_FIELD_OF_VIEW, DEFAULT_VERTICAL_FIELD_OF_VIEW);
	}

	/**
	 * Creates a new <code>PanoramicLens</code>.
	 * @param hfov The horizontal field of view (in radians).
	 */
	public PanoramicLens(double hfov) {
		this(hfov, DEFAULT_VERTICAL_FIELD_OF_VIEW);
	}

	/**
	 * Creates a new <code>PanoramicLens</code>.
	 * @param hfov The horizontal field of view (in radians).
	 * @param vfov The vertical field of view (in radians).
	 */
	public PanoramicLens(double hfov, double vfov) {
		this.hfov = hfov;
		this.height = 2.0 * Math.tan(vfov / 2.0);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#sample(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.path.PathInfo, double, double, double)
	 */
	@Override
	public EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv,
			double rj) {
		return new Node(p, pathInfo, ru, rv, rj);
	}

	/**
	 * An <code>EyeNode</code> generated by a <code>PanoramicLens</code>.
	 */
	private final class Node extends EyeTerminalNode {

		/** Projected point on the image plane. */
		private final Point2 pointOnImagePlane;

		/**
		 * Creates a <code>Node</code>.
		 * @param pointOnImagePlane The <code>Point2</code> on the image plane.
		 * @param pathInfo The <code>PathInfo</code> describing the context for
		 * 		this node.
		 */
		public Node(Point2 pointOnImagePlane, PathInfo pathInfo, double ru, double rv, double rj) {
			super(pathInfo, ru, rv, rj);
			this.pointOnImagePlane = pointOnImagePlane;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.EyeNode#sample(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.Random)
		 */
		public ScatteredRay sample(double ru, double rv, double rj) {			
			Point2 p = pointOnImagePlane;
			double theta = (p.x() - 0.5) * hfov;
			Vector3 v = new Vector3(
					Math.sin(theta),
					(0.5 - p.y()) * height,
					-Math.cos(theta));
			double r = v.length();			
			Ray3 ray = new Ray3(Point3.ORIGIN, v.divide(r));
			Color color = getWhite();
			double pdf = (r * r * r * r) / (hfov * height);
			return ScatteredRay.diffuse(ray, color, pdf);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.PathNode#scatterTo(ca.eandb.jmist.framework.path.PathNode)
		 */
		public Color scatter(Vector3 v) {
			return getGray(getPDF(v));
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.EyeNode#project(ca.eandb.jmist.math.HPoint3)
		 */
		public Point2 project(HPoint3 x) {
			Ray3 ray = new Ray3(Point3.ORIGIN, x);
			Vector3 v = ray.direction();
			double theta = Math.atan2(v.x(), -v.z());
			if (Math.abs(theta) > 0.5 * hfov) {
				return null;
			}
			double h = v.y() / Math.hypot(v.x(), v.z());
			if (Math.abs(h) > 0.5 * height) {
				return null;
			}
			return new Point2(
					0.5 + theta / hfov,
					0.5 - h / height);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.PathNode#getCosine(ca.eandb.jmist.math.Vector3)
		 */
		public double getCosine(Vector3 v) {
			return Math.hypot(v.x(), v.z()) / v.length();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.PathNode#getPosition()
		 */
		public HPoint3 getPosition() {
			return Point3.ORIGIN;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.PathNode#getPDF()
		 */
		public double getPDF() {
			return 1.0;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.PathNode#isSpecular()
		 */
		public boolean isSpecular() {
			return true;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.path.PathNode#getPDF(ca.eandb.jmist.math.Vector3)
		 */
		public double getPDF(Vector3 v) {
			double theta = Math.atan2(v.x(), -v.z());
			if (Math.abs(theta) > 0.5 * hfov) {
				return 0.0;
			}
			double h = v.y() / Math.hypot(v.x(), v.z());
			if (Math.abs(h) > 0.5 * height) {
				return 0.0;
			}
			double u = getCosine(v);
			return 1.0 / (u * u * u * u * hfov * height);
		}

	}

}
