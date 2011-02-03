/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import ca.eandb.jmist.math.Point3;

/**
 * @author Brad
 *
 */
public final class RtPoint {
	
	private final RtPoint ORIGIN = new RtPoint(Point3.ORIGIN); 
	
	private final Point3 point;
	
	RtPoint(Point3 point) {
		this.point = point;
	}
	
	public RtPoint(double x, double y, double z) {
		this(new Point3(x, y, z));
	}
	
	public double x() {
		return point.x();
	}
	
	public double y() {
		return point.y();
	}
	
	public double z() {
		return point.z();
	}
	
	Point3 toPoint() {
		return point;
	}

}
