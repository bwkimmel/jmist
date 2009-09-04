/**
 *
 */
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.gi2.EyeNode;
import ca.eandb.jmist.framework.gi2.EyeTerminalNode;
import ca.eandb.jmist.framework.gi2.PathInfo;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A circular fisheye lens (http://en.wikipedia.org/wiki/Fisheye_lens).
 * @author Brad Kimmel
 */
public final class FisheyeLens extends AbstractLens {

	/** Serialization version ID. */
	private static final long serialVersionUID = 4393119937901820155L;

	public EyeNode sample(Point2 p, PathInfo pathInfo, Random rnd) {
		return new Node(p, pathInfo);
	}

	private final class Node extends EyeTerminalNode {

		private final Point2 pointOnImagePlane;

		public Node(Point2 pointOnImagePlane, PathInfo pathInfo) {
			super(pathInfo);
			this.pointOnImagePlane = pointOnImagePlane;
		}

		public Point2 project(HPoint3 x) {
			Vector3 v = x.isPoint() ? x.toPoint3().vectorFromOrigin()
			                        : x.toVector3();
			double d = v.length();

			if (-v.z() / d < MathUtil.EPSILON) {
				return null;
			}
			return new Point2(
					(v.x() / d + 1.0) / 2.0,
					(1.0 - v.y() / d) / 2.0);
		}

		public double getCosine(Vector3 v) {
			return -v.z() / v.length();
		}

		public double getPDF() {
			return 1.0;
		}

		public double getPDF(Vector3 v) {
			return 0.25;
		}

		public HPoint3 getPosition() {
			return Point3.ORIGIN;
		}

		public boolean isSpecular() {
			return true;
		}

		public ScatteredRay sample(Random rnd) {
			Point2	p = pointOnImagePlane;
			double	nx = 2.0 * (p.x() - 0.5);
			double	ny = 2.0 * (0.5 - p.y());
			double	d2 = nx * nx + ny * ny;

			if (d2 > 1.0)
				return null;

			Ray3 ray = new Ray3(
					Point3.ORIGIN,
					new Vector3(nx, ny,	-Math.sqrt(1.0 - d2)));
			Color color = getWhite();
			double pdf = 0.25;

			return ScatteredRay.diffuse(ray, color, pdf);
		}

		public Color scatter(Vector3 v) {
			if (getCosine(v) < MathUtil.EPSILON) {
				return null;
			}
			return getGray(getPDF(v));
		}

	}

}
