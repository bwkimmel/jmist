/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.SceneGraphObject;

import ca.eandb.jmist.framework.SceneElement;

/**
 * The <code>Visitor</code> used to process J3D <code>SceneGraphObject</code>s
 * without a specialized visitor.  This class is a singleton.
 * 
 * @author Brad Kimmel
 */
final class DefaultVisitor implements Visitor {
	
	/** The single <code>DefaultVisitor</code> instance. */
	public static final DefaultVisitor INSTANCE = new DefaultVisitor();
	
	/** Default constructor. */
	private DefaultVisitor() {}

	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		return null;
	}

}
