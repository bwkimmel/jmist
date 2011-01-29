/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.SceneGraphObject;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.math.Point3;

import com.sun.j3d.utils.geometry.Cone;

/**
 * @author Brad
 *
 */
class ConeVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		// TODO implement this when ConeGeometry is implemented
		Cone geom = (Cone) obj;
		double r = geom.getRadius();
		double h = geom.getHeight();
		Point3 base = new Point3(0, 0.5 * h, 0);
//		return new ConeGeometry(base, r, h);
		
		return null;
	}

}
