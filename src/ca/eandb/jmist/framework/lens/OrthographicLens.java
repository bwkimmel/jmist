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
 * A <code>Lens</code> that projects the world on to the image plane
 * ortographically.
 * @author Brad Kimmel
 */
public final class OrthographicLens implements Lens {

	/**
	 * Creates a new <code>OrthographicLens</code>.
	 * @param width The extent of the image plane along the x-axis.
	 * @param height The extent of the image plane along the y-axis.
	 */
	public OrthographicLens(double width, double height) {
		this.width = width;
		this.height = height;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#rayAt(ca.eandb.jmist.math.Point2)
	 */
	@Override
	public Ray3 rayAt(Point2 p) {

		return new Ray3(
				new Point3(
						(p.x() - 0.5) * this.width,
						(0.5 - p.y()) * this.height,
						0.0
				),
				Vector3.NEGATIVE_K
		);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#project(ca.eandb.jmist.math.Point3)
	 */
	@Override
	public Projection project(final Point3 p) {
		if (-p.z() < MathUtil.EPSILON) {
			return null;
		}
		return new Projection() {
			public Point2 pointOnImagePlane() {
				return new Point2((p.x() / width) + 0.5, 0.5 - (p.y() / height));
			}

			public Point3 pointOnLens() {
				return new Point3(p.x(), p.y(), 0.0);
			}

			public double importance() {
				return 1.0; // FIXME Light tracing will not work until this is corrected.
			}
		};
	}

	/** The extent of the image plane along the x-axis. */
	private final double width;

	/** The extent of the image plane along the y-axis. */
	private final double height;

}
