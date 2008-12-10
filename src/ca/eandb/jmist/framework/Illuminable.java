/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.Vector3;

/**
 * An object capable of being illuminated by a <code>Light</code>.
 * @author Brad Kimmel
 */
public interface Illuminable {

	/**
	 * Illuminates this target.
	 * @param from The direction in which the light is travelling.
	 * @param radiance The radiance <code>Spectrum</code> of the light (in
	 * 		W.m^-3.sr^-1).
	 */
	void illuminate(Vector3 from, Spectrum radiance);

}
