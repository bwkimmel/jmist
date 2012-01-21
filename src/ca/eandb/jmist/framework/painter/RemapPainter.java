/**
 * 
 */
package ca.eandb.jmist.framework.painter;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Interval;

/**
 * @author brad
 *
 */
public final class RemapPainter implements Painter {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -4526940092822564018L;

	private final Painter inner;
	
	private final Interval range;
	
	public RemapPainter(Interval range, Painter inner) {
		this.range = range;
		this.inner = inner;
		
		if (range.isInfinite()) {
			throw new IllegalArgumentException("range may not be infinite");
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Painter#getColor(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color getColor(SurfacePoint p, WavelengthPacket lambda) {
		Color result = inner.getColor(p, lambda);
		Color min = lambda.getColorModel().getGray(range.minimum(), lambda);
		return min.plus(result.times(range.length()));
	}

}
