/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.SceneGraphObject;
import javax.media.j3d.Shape3D;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.scene.MergeSceneElement;

/**
 * @author Brad
 *
 */
class Shape3DVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		Shape3D shape = (Shape3D) obj;
		if (shape.numGeometries() > 1) {
			MergeSceneElement elem = new MergeSceneElement();
			for (int i = 0, n = shape.numGeometries(); i < n; i++) {
				SceneElement child = VisitorFactory.createSceneElement(shape.getGeometry(i));
				elem.addChild(child);
			}
			return elem;
		} else {
			return VisitorFactory.createSceneElement(shape.getGeometry());
		}
	}

}
