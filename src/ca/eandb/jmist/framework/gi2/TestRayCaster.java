/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad
 *
 */
public final class TestRayCaster implements RayCaster {

	private final Scene scene;

	public TestRayCaster(Scene scene) {
		this.scene = scene;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.RayCaster#castRay(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public ScatteringNode castRay(Ray3 ray, PathNode parent) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3)
	 */
	public boolean visibility(Ray3 ray) {
		// TODO Auto-generated method stub
		return false;
	}

}
