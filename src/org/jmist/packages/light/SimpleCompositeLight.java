/**
 * 
 */
package org.jmist.packages.light;

import org.jmist.framework.Illuminable;
import org.jmist.framework.Light;
import org.jmist.framework.SurfacePoint;
import org.jmist.framework.VisibilityFunction3;

/**
 * @author bkimmel
 *
 */
public final class SimpleCompositeLight extends CompositeLight {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Light#illuminate(org.jmist.framework.SurfacePoint, org.jmist.framework.VisibilityFunction3, org.jmist.framework.Illuminable)
	 */
	public void illuminate(SurfacePoint x, VisibilityFunction3 vf,
			Illuminable target) {
		
		for (Light light : this.children()) {
			light.illuminate(x, vf, target);
		}
		
	}

}
