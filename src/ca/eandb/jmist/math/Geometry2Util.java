/**
 * 
 */
package ca.eandb.jmist.math;

import java.util.Arrays;
import java.util.Collection;

/**
 * Static geometry utility methods
 * @author Brad Kimmel
 */
public final class Geometry2Util {
	
	public static Box2 boundingBox(Point2... points) {
		return boundingBox(Arrays.asList(points));
	}
	
	public static Box2 boundingBox(Collection<Point2> points) {
		if (!points.isEmpty()) {
			double xmin = Double.POSITIVE_INFINITY;
			double ymin = Double.POSITIVE_INFINITY;
			double xmax = Double.NEGATIVE_INFINITY;
			double ymax = Double.NEGATIVE_INFINITY;
			
			for (Point2 p : points) {
				if (p.x() < xmin) { xmin = p.x(); }
				if (p.x() > xmax) { xmax = p.x(); }
				if (p.y() < ymin) { ymin = p.y(); }
				if (p.y() > ymax) { ymax = p.y(); }
			}
			
			return new Box2(xmin, ymin, xmax, ymax);
		} else {
			return Box2.EMPTY;
		}
	}
		
	public static Point2 centroid(Point2... points) {
		return centroid(Arrays.asList(points));
	}
	
	public static Point2 centroid(Collection<Point2> points) {
		double x = 0.0;
		double y = 0.0;
		double n = (double) points.size();
		for (Point2 p : points) {
			x += p.x() / n;
			y += p.y() / n;
		}
		return new Point2(x, y);		
	}
	
	public static Circle boundingCircle(Point2... points) {
		return boundingCircle(Arrays.asList(points));
	}
	
	public static Circle boundingCircle(Collection<Point2> points) {
		if (!points.isEmpty()) {
			Point2 c = centroid(points);
			double r = 0.0;
			for (Point2 p : points) {
				double d = c.distanceTo(p);
				if (d > r) {
					r = d;
				}
			}
			return new Circle(c, r);
		} else {
			return Circle.EMPTY;
		}		
	}

	public static double areaOfTriangle(Point2 a, Point2 b, Point2 c) {
		return Math.abs(0.5 * (
				a.x() * (b.y() - c.y()) +
				b.x() * (c.y() - a.y()) +
				c.x() * (a.y() - b.y())));
	}
	
	/** This class cannot be instantiated. */
	private Geometry2Util() {}
	
}
