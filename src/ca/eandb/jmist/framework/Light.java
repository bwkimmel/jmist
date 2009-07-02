/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.WavelengthPacket;


/**
 * @author Brad Kimmel
 *
 */
public interface Light {

	void illuminate(SurfacePoint x, WavelengthPacket lambda, Illuminable target);

}
