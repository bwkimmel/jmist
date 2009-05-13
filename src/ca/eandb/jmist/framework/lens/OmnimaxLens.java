/**
 *
 */
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Lens</code> that simulates an orthogonal projection of a mirrored
 * sphere.
 * @author Brad Kimmel
 */
public final class OmnimaxLens extends TransformableLens {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.packages.TransformableLens#viewRayAt(ca.eandb.jmist.toolkit.Point2)
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
