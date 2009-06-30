/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Vector3;

/**
 * Maps directions in three dimensional space to spectra.
 * @author Brad Kimmel
 */
public interface DirectionalTexture3 {

	/**
	 * Computes the spectrum at the specified direction in the domain.
	 * @param v The <code>Vector3</code> in the domain.
	 * @return The <code>Spectrum</code> at in the direction of <code>v</code>.
	 */
	Color evaluate(Vector3 v, WavelengthPacket lambda);

}
