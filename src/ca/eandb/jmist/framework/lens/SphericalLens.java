/**
 *
 */
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Lens</code> that projects the scene onto a spherical virtual screen.
 * @author Brad Kimmel
 */
public final class SphericalLens extends SingularApertureLens {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 156342820309540922L;

	/**
	 * Creates a new <code>SphericalLens</code>.
	 */
	public SphericalLens() {
		this.hfov = DEFAULT_HORIZONTAL_FIELD_OF_VIEW;
		this.vfov = DEFAULT_VERTICAL_FIELD_OF_VIEW;
	}

	/**
	 * Creates a new <code>SphericalLens</code>.
	 * @param hfov The horizontal field of view (in radians).
	 * @param vfov The vertical field of view (in radians).
	 */
	public SphericalLens(double hfov, double vfov) {
		this.hfov = hfov;
		this.vfov = vfov;
	}

	/** The default horizontal field of view (in radians). */
	public static final double DEFAULT_HORIZONTAL_FIELD_OF_VIEW = 2.0 * Math.PI;

	/** The default vertical field of view (in radians). */
	public static final double DEFAULT_VERTICAL_FIELD_OF_VIEW = Math.PI;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.packages.TransformableLens#viewRayAt(ca.eandb.jmist.toolkit.Point2)
	 */
	public Ray3 rayAt(Point2 p) {

	    double		nx = (0.5 - p.x()) * hfov;
	    double		ny = (0.5 - p.y()) * vfov;

	    double		sx = Math.sin(nx);
	    double		sy = Math.sin(ny);
	    double		cx = Math.cos(nx);
	    double		cy = Math.cos(ny);

	    return new Ray3(
	    		Point3.ORIGIN,
	    		new Vector3(-sx * cy, sy, -cx * cy));

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#project(ca.eandb.jmist.math.Vector3)
	 */
	public Projection project(Vector3 v) {
		v = v.unit();

		final double y = 0.5 - Math.asin(v.y()) / vfov;
		if (!MathUtil.inRangeCC(y, 0.0, 1.0)) {
			return null;
		}

		final double x = 0.5 - Math.atan2(-v.x(), -v.z()) / hfov;
		if (!MathUtil.inRangeCC(x, 0.0, 1.0)) {
			return null;
		}

		return new Projection() {
			public Point2 pointOnImagePlane() {
				return new Point2(x, y);
			}

			public Point3 pointOnLens() {
				return Point3.ORIGIN;
			}

			public double importance() {
				return 1.0; // FIXME Light tracing will not work until this is corrected.
			}
		};
	}

	/** Horizontal field of view (in radians). */
	private final double hfov;

	/** Vertical field of view (in radians). */
	private final double vfov;

}
