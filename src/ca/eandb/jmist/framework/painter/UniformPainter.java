/**
 *
 */
package ca.eandb.jmist.framework.painter;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author Brad
 *
 */
public final class UniformPainter implements Painter {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 2709369299680888222L;

	private final Spectrum value;

	public UniformPainter(Spectrum value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Painter#getColor(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color getColor(SurfacePoint p, WavelengthPacket lambda) {
		return value.sample(lambda);
	}

}
