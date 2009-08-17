/**
 *
 */
package ca.eandb.jmist.framework.gi;

import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class PathUtil {

	private PathUtil() {}

	public static double getGeometricFactor(HPoint3 a, HPoint3 b, VisibilityFunction3 vf) {
		Ray3 ray = Ray3.create(a, b);
		if (ray != null && vf.visibility(ray)) {
			return ray.isInfinite() ? 1.0 : 1.0 / (ray.limit() * ray.limit());
		}
		return 0.0;
	}

	private static Color joinPriv(PathNode a, PathNode b) {
		assert(!a.atInfinity());
		Ray3 ray = new Ray3((Point3) a.getPosition(), b.getPosition());
		VisibilityFunction3 vf = a.getScene().getRoot();
		if (vf.visibility(ray)) {
			Vector3 v = ray.direction();
			Color result = a.scatter(v).times(b.scatter(v.opposite()));
			return ray.isInfinite()
					? result
					: result.divide(ray.limit() * ray.limit());
		} else {
			return getBlack(a);
		}
	}

	private static Color getBlack(PathNode node) {
		Color value = node.getValue();
		ColorModel colorModel = value.getColorModel();
		WavelengthPacket lambda = value.getWavelengthPacket();
		return colorModel.getBlack(lambda);
	}

	public static Color join(PathNode a, PathNode b) {
		if (!a.atInfinity()) {
			return joinPriv(a, b);
		} else if (!b.atInfinity()) {
			return joinPriv(b, a);
		} else {
			return getBlack(a);
		}
	}

}
