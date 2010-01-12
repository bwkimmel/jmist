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
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Lens</code> that simulates an orthogonal projection of a mirrored
 * sphere.
 * @author Brad Kimmel
 */
public final class TestLens extends AbstractLens {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -4366154660419656383L;

	private static final Basis3 BASIS = Basis3.fromUV(Vector3.K,
			Vector3.NEGATIVE_I, Basis3.Orientation.LEFT_HANDED);

	public EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv, double rj) {
		return new Node(p, pathInfo, ru, rv, rj);
	}

	private final class Node extends EyeTerminalNode {

		private final Point2 pointOnImagePlane;

		public Node(Point2 pointOnImagePlane, PathInfo pathInfo, double ru, double rv, double rj) {
			super(pathInfo, ru, rv, rj);
			this.pointOnImagePlane = pointOnImagePlane;
		}

		public Point2 project(HPoint3 q) {
			Vector3 v = q.isPoint() ? q.toPoint3().unitVectorFromOrigin()
					                : q.toVector3().unit();
			SphericalCoordinates sc = SphericalCoordinates.fromCartesian(v,
					BASIS);
			double x = sc.azimuthal() / (2.0 * Math.PI);
			double y = 0.5 * (1.0 - Math.cos(sc.polar()));
			if (x < 0.0) {
				x += 1.0;
			}
			return new Point2(x, y);
		}

		public double getCosine(Vector3 v) {
			return 1.0;
		}

		public double getPDF() {
			return 1.0;
		}

		public double getPDF(Vector3 v) {
			return 1.0 / (4.0 * Math.PI);
		}

		public HPoint3 getPosition() {
			return Point3.ORIGIN;
		}

		public boolean isSpecular() {
			return true;
		}

		public ScatteredRay sample(double ru, double rv, double rj) {
			Point2 p = pointOnImagePlane;
			SphericalCoordinates v = new SphericalCoordinates(
					Math.acos(1.0 - 2.0 * p.y()),
					2.0 * Math.PI * p.x());
			Ray3 ray = new Ray3(Point3.ORIGIN, v.toCartesian(BASIS));
			Color color = getWhite();
			double pdf = 1.0 / (4.0 * Math.PI);

			return ScatteredRay.diffuse(ray, color, pdf);
		}

		public Color scatter(Vector3 v) {
			return getGray(getPDF(v));
		}

	}

}
