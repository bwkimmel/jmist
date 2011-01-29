/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.SceneGraphObject;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.primitive.BoxGeometry;
import ca.eandb.jmist.math.Box3;

import com.sun.j3d.utils.geometry.Box;

/**
 * @author Brad
 *
 */
class BoxVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		Box geom = (Box) obj;
		double hx = geom.getXdimension();
		double hy = geom.getYdimension();
		double hz = geom.getZdimension();
		Box3 box = new Box3(-hx, -hy, -hz, hx, hy, hz);
		return new BoxGeometry(box);
	}

}
