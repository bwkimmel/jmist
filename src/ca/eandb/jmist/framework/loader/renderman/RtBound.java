/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import ca.eandb.jmist.math.Box3;

/**
 * @author Brad
 *
 */
public final class RtBound {

	private final Box3 box;
	
	public RtBound(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
		this.box = new Box3(xmin, ymin, zmin, xmax, ymax, zmax);
	}
	
	Box3 toBox() {
		return box;
	}
	
}
