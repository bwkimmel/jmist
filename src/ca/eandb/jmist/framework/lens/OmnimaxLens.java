/**
 *
 */
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.path.EyeNode;
import ca.eandb.jmist.framework.path.EyeTerminalNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.math.HPoint3;
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
public final class OmnimaxLens extends AbstractLens {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -4366154660419656383L;

	/** The <code>Sphere</code> to bounce the orthogonally generated rays from. */
	private static final Sphere LENS_SPHERE = new Sphere(new Point3(0, 0, 2), 1);

	public EyeNode sample(Point2 p, PathInfo pathInfo, Random rnd) {
		return new Node(p, pathInfo);
	}

	private final class Node extends EyeTerminalNode {

		private final Point2 pointOnImagePlane;

		public Node(Point2 pointOnImagePlane, PathInfo pathInfo) {
			super(pathInfo);
			this.pointOnImagePlane = pointOnImagePlane;
		}

		public Point2 project(HPoint3 q) {
			Vector3 v = q.isPoint() ? q.toPoint3().unitVectorFromOrigin()
					                : q.toVector3().unit();
			Vector3 half = new Vector3(0.5 * v.x(), 0.5 * v.y(),
					0.5 * (v.z() - 1.0)).unit();
			double x = 0.5 * (half.x() + 1.0);
			double y = 0.5 * (1.0 - half.y());
			return new Point2(x, y);
		}

		public double getCosine(Vector3 v) {
			return 1.0;
		}

		public double getPDF() {
			return 1.0;
		}

		public double getPDF(Vector3 v) {
			return 1.0 / 16.0;
		}

		public HPoint3 getPosition() {
			return Point3.ORIGIN;
		}

		public boolean isSpecular() {
			return true;
		}

		public ScatteredRay sample(Random rnd) {
			Point2		p		= pointOnImagePlane;
			double		nx		= 2.0 * (p.x() - 0.5);
			double		ny		= 2.0 * (0.5 - p.y());

			Ray3		init	= new Ray3(new Point3(nx, ny, 1.0), Vector3.K);
			Interval	I		= LENS_SPHERE.intersect(init);

			if (I.isEmpty()) {
				return null;
			}

			Vector3		n		= LENS_SPHERE.center().vectorTo(init.pointAt(I.minimum()));
			Vector3		r		= Optics.reflect(init.direction(), n);

			Ray3		ray		= new Ray3(Point3.ORIGIN, r);
			Color		color	= getWhite();
			double		pdf		= 1.0 / 16.0;

			return ScatteredRay.diffuse(ray, color, pdf);
		}

		public Color scatter(Vector3 v) {
			return getGray(getPDF(v));
		}

	}

}
