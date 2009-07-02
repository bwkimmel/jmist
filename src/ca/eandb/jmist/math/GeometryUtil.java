/**
 *
 */
package ca.eandb.jmist.math;

/**
 * Static geometric utility methods.
 * @author brad
 */
public final class GeometryUtil {

	public static double areaOfTriangle(Point3 a, Point3 b, Point3 c) {
		Vector3 ab = a.vectorTo(b);
		Vector3 bc = b.vectorTo(c);
		Vector3 ca = c.vectorTo(a);

		double abDOTbc = ab.dot(bc);
		double bcDOTca = bc.dot(ca);
		double caDOTab = ca.dot(ab);

		if (abDOTbc < bcDOTca && abDOTbc < caDOTab) {
			return 0.5 * ab.cross(bc).length();
		} else if (bcDOTca < caDOTab) {
			return 0.5 * bc.cross(ca).length();
		} else {
			return 0.5 * ca.cross(ab).length();
		}
	}

	public static double areaOfTriangle(Point2 a, Point2 b, Point2 c) {
		return Math.abs(0.5 * (
				a.x() * (b.y() - c.y()) +
				b.x() * (c.y() - a.y()) +
				c.x() * (a.y() - b.y())));
	}

	/**
	 * This class cannot be instantiated.
	 */
	private GeometryUtil() {}

}