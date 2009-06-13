/**
 *
 */
package ca.eandb.jmist.framework.light;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;

/**
 * @author Brad Kimmel
 *
 */
public final class SimpleCompositeLight extends CompositeLight {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.framework.VisibilityFunction3)
	 */
	public Color illuminate(Intersection x, VisibilityFunction3 vf) {
		Color sum = ColorModel.getInstance().getBlack();
		for (Light light : this.children()) {
			sum = sum.plus(light.illuminate(x, vf));
		}
		return sum;
	}

}
