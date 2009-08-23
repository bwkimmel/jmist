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
 * A circular fisheye lens (http://en.wikipedia.org/wiki/Fisheye_lens).
 * @author Brad Kimmel
 */
public final class FisheyeLens extends SingularApertureLens {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 4393119937901820155L;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#rayAt(ca.eandb.jmist.math.Point2)
	 */
	public Ray3 rayAt(Point2 p) {

		double	nx = 2.0 * (p.x() - 0.5);
		double	ny = 2.0 * (0.5 - p.y());
		double	d2 = nx * nx + ny * ny;

		if (d2 > 1.0)
			return null;

		return new Ray3(
				Point3.ORIGIN,
				new Vector3(nx, ny,	-Math.sqrt(1.0 - d2))
		);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#project(ca.eandb.jmist.math.Vector3)
	 */
	public Projection project(final Vector3 v) {
		if (-v.z() < MathUtil.EPSILON) {
			return null;
		}
		return new Projection() {
			public Point2 pointOnImagePlane() {
				double d = v.length();
				return new Point2((v.x() / d + 1.0) / 2.0, (1.0 - v.y() / d) / 2.0);
			}

			public Point3 pointOnLens() {
				return Point3.ORIGIN;
			}

			public double importance() {
				return 1.0; // FIXME Light tracing will not work until this is corrected.
			}
		};
	}

}
