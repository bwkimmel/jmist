/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Vector3;

/**
 * An object capable of being illuminated by a <code>Light</code>.
 * @author Brad Kimmel
 */
public interface Illuminable {

	/**
	 * Illuminates this target.
	 * @param from The direction in which the light is traveling.
	 * @param radiance The radiance <code>Color</code> of the light (in
	 * 		W.m^-2.sr^-1).
	 */
	void illuminate(Vector3 from, Color radiance);

}
