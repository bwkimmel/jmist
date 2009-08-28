/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class PathUtil {

	public static double getGeometricFactor(PathNode a, PathNode b) {
		boolean aAtInf = a.isAtInfinity();
		boolean bAtInf = b.isAtInfinity();
		if (aAtInf && bAtInf) {
			return 0.0;
		} else if (bAtInf) {
			return a.getCosine(b);
		} else if (aAtInf) {
			return b.getCosine(a);
		} else {
			return a.getCosine(b) * b.getCosine(a)
					/ a.getPosition().toPoint3().squaredDistanceTo(
							b.getPosition().toPoint3());
		}
	}

	public static boolean visibility(PathNode a, PathNode b) {
		Ray3 ray = Ray3.create(a.getPosition(), b.getPosition());
		if (ray != null) {
			PathInfo path = a.getPathInfo();
			VisibilityFunction3 vf = path.getRayCaster();
			return vf.visibility(ray);
		} else { // ray == null
			return false;
		}
	}

	public static Color join(PathNode a, PathNode b) {
		Color etol = a.scatterTo(b);
		Color ltoe = b.scatterTo(a);
		Color c = etol.times(ltoe);

		if (ColorUtil.getTotalChannelValue(c) > 0.0
				&& PathUtil.visibility(a, b)) {

			c = c.times(PathUtil.getGeometricFactor(a, b));

			c = c.times(a.getCumulativeWeight()).times(
					b.getCumulativeWeight());

			return c;

		} else { // No mutual scattering or nodes not mutually visible
			return null;
		}
	}

	public static Vector3 getDirection(PathNode from, PathNode to) {
		boolean fromInfinity = from.isAtInfinity();
		boolean toInfinity = to.isAtInfinity();
		if (fromInfinity && toInfinity) {
			return null;
		} else if (fromInfinity) {
			return from.getPosition().toVector3().unit().opposite();
		} else if (toInfinity) {
			return to.getPosition().toVector3().unit();
		} else {
			return from.getPosition().toPoint3().unitVectorTo(
					to.getPosition().toPoint3());
		}
	}

	private PathUtil() {}

}
