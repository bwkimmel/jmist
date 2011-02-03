/**
 *
 */
package ca.eandb.jmist.math;

import java.util.Arrays;
import java.util.Collection;

/**
 * Static geometric utility methods.
 * @author brad
 */
public final class Geometry3Util {
	
	public static Box3 boundingBox(Point3... points) {
		return boundingBox(Arrays.asList(points));
	}
	
	public static Box3 boundingBox(Collection<Point3> points) {
		if (!points.isEmpty()) {
			double xmin = Double.POSITIVE_INFINITY;
			double ymin = Double.POSITIVE_INFINITY;
			double zmin = Double.POSITIVE_INFINITY;
			double xmax = Double.NEGATIVE_INFINITY;
			double ymax = Double.NEGATIVE_INFINITY;
			double zmax = Double.NEGATIVE_INFINITY;
			
			for (Point3 p : points) {
				if (p.x() < xmin) { xmin = p.x(); }
				if (p.x() > xmax) { xmax = p.x(); }
				if (p.y() < ymin) { ymin = p.y(); }
				if (p.y() > ymax) { ymax = p.y(); }
				if (p.z() < zmin) { zmin = p.z(); }
				if (p.z() > zmax) { zmax = p.z(); }
			}
			
			return new Box3(xmin, ymin, zmin, xmax, ymax, zmax);
		} else {
			return Box3.EMPTY;
		}
	}
	
	public static Point3 centroid(Point3... points) {
		return centroid(Arrays.asList(points));
	}
	
	public static Point3 centroid(Collection<Point3> points) {
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		double n = (double) points.size();
		for (Point3 p : points) {
			x += p.x() / n;
			y += p.y() / n;
			z += p.z() / n;
		}
		return new Point3(x, y, z);
	}
	
	public static Sphere boundingSphere(Point3... points) {
		return boundingSphere(Arrays.asList(points));
	}
	
	public static Sphere boundingSphere(Collection<Point3> points) {
		if (!points.isEmpty()) {
			Point3 c = centroid(points);
			double r = 0.0;
			for (Point3 p : points) {
				double d = c.distanceTo(p);
				if (d > r) {
					r = d;
				}
			}
			return new Sphere(c, r);
		} else {
			return Sphere.EMPTY;
		}
	}

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
	
	public static double rayIntersectTriangle(Ray3 ray, Point3 a, Point3 b, Point3 c) {
		Plane3 plane = Plane3.throughPoints(a, b, c);
		double t = plane.intersect(ray);
		Point3 p = ray.pointAt(t);
		double abp = areaOfTriangle(a, b, p);
		double bcp = areaOfTriangle(b, c, p);
		double cap = areaOfTriangle(c, a, p);
		double abc = areaOfTriangle(a, b, c);
		
		if (abp + bcp > abc || bcp + cap > abc || cap + abp > abc) {
			return Double.NaN;
		} else {
			return t;
		}
	}

	/**
	 * This class cannot be instantiated.
	 */
	private Geometry3Util() {}

}
