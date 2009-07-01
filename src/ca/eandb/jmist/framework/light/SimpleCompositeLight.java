/**
 *
 */
package ca.eandb.jmist.framework.light;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author Brad Kimmel
 *
 */
public final class SimpleCompositeLight extends CompositeLight {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Illuminable)
	 */
	public void illuminate(SurfacePoint x, WavelengthPacket lambda, Illuminable target) {
		for (Light light : this.children()) {
			light.illuminate(x, lambda, target);
		}
	}

}
