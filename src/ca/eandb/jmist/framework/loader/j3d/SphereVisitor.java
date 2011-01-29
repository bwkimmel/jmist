/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.SceneGraphObject;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.primitive.SphereGeometry;
import ca.eandb.jmist.math.Point3;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * @author Brad
 *
 */
class SphereVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		Sphere geom = (Sphere) obj;
		return new SphereGeometry(Point3.ORIGIN, geom.getRadius());
	}

}
