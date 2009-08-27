/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author Brad
 *
 */
public interface PathInfo {

	ColorModel getColorModel();

	RayCaster getRayCaster();

	WavelengthPacket getWavelengthPacket();

}
