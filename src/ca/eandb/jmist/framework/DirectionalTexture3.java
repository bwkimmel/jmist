/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Vector3;

/**
 * Maps directions in three dimensional space to spectra.
 * @author Brad Kimmel
 */
public interface DirectionalTexture3 extends Serializable {

	/**
	 * Computes the color at the specified direction in the domain.
	 * @param v The <code>Vector3</code> in the domain.
	 * @param lambda The <code>WavelengthPacket</code> representing the
	 * 		wavelengths at which to evaluate the texture.
	 * @return The <code>Color</code> at in the direction of <code>v</code>.
	 */
	Color evaluate(Vector3 v, WavelengthPacket lambda);

	/**
	 * Computes the spectrum at the specified direction in the domain.
	 * @param v The <code>Vector3</code> in the domain.
	 * @return The <code>Spectrum</code> at in the direction of <code>v</code>.
	 */
	Spectrum evaluate(Vector3 v);

}
