/**
 *
 */
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Lens</code> that projects the scene onto a cylindrical virtual
 * screen.
 * @author Brad Kimmel
 */
public final class PanoramicLens implements Lens {

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

	/** The default horizontal field of view (in radians). */
	public static final double DEFAULT_HORIZONTAL_FIELD_OF_VIEW = Math.PI;

	/** The default vertical field of view (in radians). */
	public static final double DEFAULT_VERTICAL_FIELD_OF_VIEW = Math.PI / 2.0;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.lens.TransformableLens#rayAt(ca.eandb.jmist.math.Point2)
	 */
	@Override
	public Ray3 rayAt(Point2 p) {

		double theta = (p.x() - 0.5) * hfov;

		return new Ray3(
				Point3.ORIGIN,
				new Vector3(
						Math.sin(theta),
						(0.5 - p.y()) * height,
						-Math.cos(theta)
				).unit()
		);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#project(ca.eandb.jmist.math.Point3)
	 */
	@Override
	public Projection project(Point3 p) {
		double theta = Math.atan2(p.x(), -p.z());
		final double x = 0.5 + theta / hfov;
		if (!MathUtil.inRangeCC(x, 0.0, 1.0)) {
			return null;
		}
		double d = Math.sqrt(p.x() * p.x() + p.z() * p.z());
		if (d < MathUtil.EPSILON) {
			return null;
		}
		final double y =  0.5 - (p.y() / (d * height));
		if (!MathUtil.inRangeCC(y, 0.0, 1.0)) {
			return null;
		}
		return new Projection() {
			public Point2 pointOnImagePlane() {
				return new Point2(x, y);
			}

			public Point3 pointOnLens() {
				return Point3.ORIGIN;
			}
		};
	}

	/** Horizontal field of view (in radians). */
	private final double hfov;

	/** Height of the virtual image plane. */
	private final double height;

}
