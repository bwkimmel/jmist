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
 * A circular fisheye lens (http://en.wikipedia.org/wiki/Fisheye_lens).
 * @author Brad Kimmel
 */
public final class FisheyeLens implements Lens {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#rayAt(ca.eandb.jmist.math.Point2)
	 */
	@Override
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
	 * @see ca.eandb.jmist.framework.Lens#project(ca.eandb.jmist.math.Point3)
	 */
	@Override
	public Point2 project(Point3 p) {
		if (-p.z() < MathUtil.EPSILON) {
			return null;
		}
		double d = p.distanceTo(Point3.ORIGIN);
		return new Point2((p.x() / d + 1.0) / 2.0, (1.0 - p.y() / d) / 2.0);
	}

}
