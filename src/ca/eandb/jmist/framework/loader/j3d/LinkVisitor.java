/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.Link;
import javax.media.j3d.SceneGraphObject;
import javax.media.j3d.SharedGroup;

import ca.eandb.jmist.framework.SceneElement;

/**
 * @author Brad
 *
 */
class LinkVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		Link link = (Link) obj;
		SharedGroup group = link.getSharedGroup();
		SceneElement elem = (SceneElement) group.getUserData();
		
		if (elem == null) { // has not been processed yet
			elem = VisitorFactory.createSceneElement(group);
		}
		
		return elem;
	}

}
