/**
 *
 */
package ca.eandb.jmist.packages.lens;

import ca.eandb.jmist.toolkit.Point2;
import ca.eandb.jmist.toolkit.Point3;
import ca.eandb.jmist.toolkit.Ray3;
import ca.eandb.jmist.toolkit.Vector3;

/**
 * A circular fisheye lens (http://en.wikipedia.org/wiki/Fisheye_lens).
 * @author Brad Kimmel
 */
public final class FisheyeLens extends TransformableLens {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.packages.TransformableLens#viewRayAt(ca.eandb.jmist.toolkit.Point2)
	 */
	@Override
	protected Ray3 viewRayAt(Point2 p) {

		double	nx = 2.0 * (p.x() - 0.5);
		double	ny = 2.0 * (0.5 - p.y());
		double	d = Math.sqrt(nx * nx + ny * ny);
		double	theta = Math.atan2(ny, nx);

		if (d > 1.0)
			return null;

		return new Ray3(
				Point3.ORIGIN,
				new Vector3(
						d * Math.cos(theta),
						d * Math.sin(theta),
						-Math.sqrt(1.0 - d * d)
				)
		);

	}

}
