/**
 * 
 */
package ca.eandb.jmist.framework.geometry.nurbs;

import java.util.List;

import ca.eandb.jmist.math.Point3;

/**
 * @author Brad
 *
 */
final class SplineUtil {
	
	public static void refineBSpline(int order, List<Point3> cpIn, KnotVector tau, KnotVector t, List<Point3> cpOut) {
		int q = t.size() - 1;
		cpOut.clear();
		
		for (int j = 0; j <= q - order; j++) {
			int mu = tau.find(t.get(j));
			cpOut.add(subdivide(cpIn, order, tau, t, order, mu, j));
		}
	}
	
	private static Point3 subdivide(List<Point3> cpIn, int order,
			KnotVector tau, KnotVector t, int rp1, int i, int j) {
		int r = rp1 - 1;
		if (r > 0) {
			Point3 pp1 = Point3.ORIGIN;
			Point3 pp2 = Point3.ORIGIN;
	        double p1 = t.get(j + order - r) - tau.get(i);
	        double p2 = tau.get(i + order - r) - t.get(j + order - r);
	        if (p1 != 0.0) { pp1 = subdivide(cpIn, order, tau, t, r, i, j); }
	        if (p2 != 0.0) { pp2 = subdivide(cpIn, order, tau, t, r, i - 1, j); }
	        
	        return Point3.interpolate(pp1, pp2, p2 / (p1 + p2));
		} else {
			return cpIn.get(i);
		}
	}

	/** Default constructor. */
	private SplineUtil() {}

}
