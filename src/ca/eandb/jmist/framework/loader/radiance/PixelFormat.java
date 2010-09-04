/**
 * 
 */
package ca.eandb.jmist.framework.loader.radiance;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.RGB;

/**
 * Represents a pixel format for a <code>RadiancePicture</code>.
 * 
 * @author Brad Kimmel
 */
interface PixelFormat extends Serializable {

	/**
	 * Converts an <code>RGB</code> color to the internal representation.
	 * @param rgb The <code>RGB</code> color to convert.
	 * @return The internal representation of <code>rgb</code>.
	 */
	int toRaw(RGB rgb);
		
	/**
	 * Converts an <code>CIEXYZ</code> color to the internal representation.
	 * @param xyz The <code>CIEXYZ</code> color to convert.
	 * @return The internal representation of <code>xyz</code>.
	 */
	int toRaw(CIEXYZ xyz);
	
	/**
	 * Converts an internal representation of a color to <code>RGB</code>.
	 * @param raw The internal representation of the color.
	 * @return The corresponding <code>RGB</code> color.
	 */
	RGB toRGB(int raw);
	
	/**
	 * Converts an internal representation of a color to <code>CIEXYZ</code>.
	 * @param raw The internal representation of the color.
	 * @return The corresponding <code>CIEXYZ</code> color.
	 */
	CIEXYZ toXYZ(int raw);
	
}
