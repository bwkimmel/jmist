/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman.scene;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.ConstructiveSolidGeometry;
import ca.eandb.jmist.framework.loader.renderman.RtToken;

/**
 * @author Brad
 *
 */
public final class CSGSolidContext extends SolidContext {

	private final ConstructiveSolidGeometry geometry;
	
	private SolidContext child;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.SolidContext#solidBegin(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void solidBegin(RtToken operation) {
		if (operation == RI_PRIMITIVE) {
			child = new PrimitiveSolidContext();
		} else {
			
		}
		
		// TODO Auto-generated method stub
		super.solidBegin(operation);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.SolidContext#solidEnd()
	 */
	@Override
	public void solidEnd() {
		// TODO Auto-generated method stub
		super.solidEnd();
	}

	@Override
	public SceneElement getSceneElement() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
