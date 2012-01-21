/**
 * 
 */
package ca.eandb.jmist.framework.painter;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author brad
 *
 */
public final class SumPainter extends CompositePainter {

	/** Serialization version ID. */
	private static final long serialVersionUID = 2409465440939244394L;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Painter#getColor(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color getColor(SurfacePoint p, WavelengthPacket lambda) {
		Color sum = null;
		for (Painter child : children()) {
			sum = ColorUtil.add(sum, child.getColor(p, lambda));
		}
		return sum;
	}

}
