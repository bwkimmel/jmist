/**
 *
 */
package ca.eandb.jmist.framework.light;

import java.util.Collection;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author Brad Kimmel
 *
 */
public final class SimpleCompositeLight extends CompositeLight {

	/**
	 *
	 */
	public SimpleCompositeLight() {
		super();
	}

	/**
	 * @param children
	 */
	public SimpleCompositeLight(Collection<? extends Light> children) {
		super(children);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Illuminable)
	 */
	public void illuminate(SurfacePoint x, WavelengthPacket lambda, Illuminable target) {
		for (Light light : this.children()) {
			light.illuminate(x, lambda, target);
		}
	}

}
