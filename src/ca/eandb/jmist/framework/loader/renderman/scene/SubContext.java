/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman.scene;

import ca.eandb.jmist.framework.loader.renderman.RenderManContext;

/**
 * @author Brad
 *
 */
public interface SubContext extends RenderManContext {

	public static enum Type {
		ATTRIBUTE,
		SOLID,
		WORLD,
		FRAME,
		MOTION,
		OBJECT,
		TRANSFORM
	};
	
	Type getContextType();
	
}
