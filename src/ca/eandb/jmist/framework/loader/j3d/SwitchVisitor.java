/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import java.util.BitSet;

import javax.media.j3d.Node;
import javax.media.j3d.SceneGraphObject;
import javax.media.j3d.Switch;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.scene.MergeSceneElement;

/**
 * @author Brad
 *
 */
class SwitchVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		
		Switch group = (Switch) obj;
		BitSet mask = group.getChildMask();
		MergeSceneElement elem = new MergeSceneElement();
		
		for (int i = 0, n = group.numChildren(); i < n; i++) {
			if (mask.get(i)) {
				Node child = group.getChild(i);
				elem.addChild(VisitorFactory.createSceneElement(child));
			}
		}
		
		return elem;
		
	}

}
