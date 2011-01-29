/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.SceneGraphObject;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.primitive.CylinderGeometry;
import ca.eandb.jmist.math.Point3;

import com.sun.j3d.utils.geometry.Cylinder;

/**
 * @author Brad
 *
 */
class CylinderVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		Cylinder geom = (Cylinder) obj;
		double r = geom.getRadius();
		double h = geom.getHeight();
		Point3 base = new Point3(0, 0.5 * h, 0);
		return new CylinderGeometry(base, r, h);
	}

}
