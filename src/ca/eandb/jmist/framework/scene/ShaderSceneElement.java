/**
 * 
 */
package ca.eandb.jmist.framework.scene;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.modifier.ShaderModifier;

/**
 * A decorator <code>SceneElement</code> that applies a <code>Shader</code> to
 * another <code>SceneElement</code>.
 *  
 * @author Brad Kimmel
 */
public final class ShaderSceneElement extends ModifierSceneElement {

	/** Serialization version ID. */
	private static final long serialVersionUID = 568999595548948811L;

	/**
	 * Creates a new <code>ShaderSceneElement</code>.
	 * @param shader The <code>Shader</code> to apply.
	 * @param inner The <code>SceneElement</code> to modify.
	 */
	public ShaderSceneElement(Shader shader, SceneElement inner) {
		super(new ShaderModifier(shader), inner);
	}
	
}
