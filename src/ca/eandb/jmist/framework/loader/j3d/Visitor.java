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
interface Visitor {

	SceneElement createSceneElement(SceneGraphObject obj);
	
}
