/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.ILens;
import org.jmist.toolkit.Point2;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Vector3;

/**
 * A circular fisheye lens (http://en.wikipedia.org/wiki/Fisheye_lens).
 * @author bkimmel
 */
public final class FisheyeLens implements ILens {

	/* (non-Javadoc)
	 * @see org.jmist.framework.ILens#rayAt(org.jmist.toolkit.Point2)
	 */
	public Ray3 rayAt(Point2 p) {

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
