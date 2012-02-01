/**
 * 
 */
package ca.eandb.jmist.framework.display.visualizer;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.math.Interval;

/**
 * A <code>ColorVisualizer</code> that extracts a single channel from the
 * provided <code>Color</code>.
 * 
 * @author Brad Kimmel
 */
public final class LinearChannelVisualizer extends StaticColorVisualizer {

	/** Serialization version ID. */
	private static final long serialVersionUID = 7864894025681399665L;

	private final int channel;
	
	private final Interval range;
	
	public LinearChannelVisualizer(int channel, Interval range) {
		if (range.isInfinite()) {
			throw new IllegalArgumentException("range is infinite");
		} else if (range.isEmpty()) {
			throw new IllegalArgumentException("range is empty");
		} else if (channel < 0) {
			throw new IllegalArgumentException("channel < 0");
		}
		
		this.channel = channel;
		this.range = range;
	}
	
	public LinearChannelVisualizer(int channel, double minimum, double maximum) {
		this(channel, new Interval(minimum, maximum));
	}
	
	public LinearChannelVisualizer(int channel, double maximum) {
		this(channel, 0.0, maximum);
	}
	
	public LinearChannelVisualizer(int channel) {
		this(channel, 0.0, 1.0);
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#apply(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public RGB visualize(Color color) {
		double value = (color.getValue(channel) - range.minimum()) / range.length();
		return new RGB(value, value, value);
	}

}
