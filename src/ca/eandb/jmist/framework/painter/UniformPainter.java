/**
 *
 */
package ca.eandb.jmist.framework.painter;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.SurfacePointGeometry;
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
	 * @see ca.eandb.jmist.framework.Painter#getColor(ca.eandb.jmist.framework.SurfacePointGeometry)
	 */
	@Override
	public Color getColor(SurfacePointGeometry p) {
		return color;
	}

}
