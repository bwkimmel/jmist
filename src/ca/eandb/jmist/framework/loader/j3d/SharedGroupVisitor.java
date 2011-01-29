/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.SceneGraphObject;

import ca.eandb.jmist.framework.SceneElement;

/**
 * @author Brad
 *
 */
class SharedGroupVisitor extends GroupVisitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.GroupVisitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		SceneElement elem = super.createSceneElement(obj);
		obj.setUserData(elem);
		return elem;
	}

}
