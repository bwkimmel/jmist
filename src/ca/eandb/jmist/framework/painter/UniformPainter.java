/**
 *
 */
package ca.eandb.jmist.framework.painter;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;

/**
 * @author Brad
 *
 */
public final class UniformPainter implements Painter {

	private final Color color;

	public UniformPainter(Color color) {
		this.color = color;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Painter#getColor(ca.eandb.jmist.framework.SurfacePoint)
	 */
	@Override
	public Color getColor(SurfacePoint p) {
		return color;
	}

}
