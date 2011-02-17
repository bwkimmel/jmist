/**
 * 
 */
package ca.eandb.jmist.framework.geometry.nurbs;

import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.util.UnimplementedException;

/**
 * @author Brad
 *
 */
public final class NURBS {
	
	
	
	public Box2 domain() {
		throw new UnimplementedException();
	}
	
	public Point3 evaluate(Point2 st) {
		throw new UnimplementedException();
	}
	
	public AffineMatrix3 evaluateFrame(Point2 st) {
		throw new UnimplementedException();		
	}

}
