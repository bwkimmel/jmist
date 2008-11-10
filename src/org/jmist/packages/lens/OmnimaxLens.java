/**
 *
 */
package org.jmist.packages.lens;

import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Optics;
import org.jmist.toolkit.Point2;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Sphere;
import org.jmist.toolkit.Vector3;

/**
 * A <code>Lens</code> that simulates an orthogonal projection of a mirrored
 * sphere.
 * @author bkimmel
 */
public final class OmnimaxLens extends TransformableLens {

	/* (non-Javadoc)
	 * @see org.jmist.packages.TransformableLens#viewRayAt(org.jmist.toolkit.Point2)
	 */
	@Override
	protected Ray3 viewRayAt(Point2 p) {

		double		nx		= 2.0 * (p.x() - 0.5);
		double		ny		= 2.0 * (0.5 - p.y());

		Ray3		init	= new Ray3(new Point3(nx, ny, 1.0), Vector3.K);
		Interval	I		= LENS_SPHERE.intersect(init);

		if (I.isEmpty()) {
			return null;
		}

		Vector3		n		= LENS_SPHERE.center().vectorTo(init.pointAt(I.minimum()));
		Vector3		r		= Optics.reflect(init.direction(), n);

		return new Ray3(Point3.ORIGIN, r);

	}

	/** The <code>Sphere</code> to bounce the orthogonally generated rays from. */
	private static final Sphere LENS_SPHERE = new Sphere(new Point3(0, 0, 2), 1);

}
