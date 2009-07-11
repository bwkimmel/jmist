/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author Brad
 *
 */
public interface Painter extends Serializable {

	Color getColor(SurfacePoint p, WavelengthPacket lambda);

	public static final Painter BLACK = new Painter() {
		private static final long serialVersionUID = -8689283716208944079L;
		public Color getColor(SurfacePoint p, WavelengthPacket lambda) {
			return lambda.getColorModel().getBlack(lambda);
		}
	};

}
