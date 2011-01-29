/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import java.util.Enumeration;

import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.SceneGraphObject;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.scene.MergeSceneElement;

/**
 * @author Brad
 *
 */
class GroupVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		
		Group group = (Group) obj;
		MergeSceneElement elem = new MergeSceneElement();
		
		@SuppressWarnings("rawtypes")
		Enumeration children = group.getAllChildren();
		
		while (children.hasMoreElements()) {
			Node child = (Node) children.nextElement();
			elem.addChild(VisitorFactory.createSceneElement(child));
		}
		
		return elem;
		
	}

}
