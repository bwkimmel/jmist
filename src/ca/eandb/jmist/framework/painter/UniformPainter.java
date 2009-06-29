/**
 *
 */
package ca.eandb.jmist.framework.painter;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Spectrum;

/**
 * @author Brad
 *
 */
public final class UniformPainter implements Painter {

	private final Spectrum color;

	public UniformPainter(Spectrum color) {
		this.color = color;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Painter#getColor(ca.eandb.jmist.framework.SurfacePoint)
	 */
	@Override
	public Spectrum getColor(SurfacePoint p) {
		return color;
	}

}
