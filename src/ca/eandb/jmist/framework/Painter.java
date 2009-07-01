/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author Brad
 *
 */
public interface Painter {

	Color getColor(SurfacePoint p, WavelengthPacket lambda);

}
