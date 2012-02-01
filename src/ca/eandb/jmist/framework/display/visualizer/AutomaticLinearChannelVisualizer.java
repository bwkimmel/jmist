/**
 * 
 */
package ca.eandb.jmist.framework.display.visualizer;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.RGB;

/**
 * @author brad
 *
 */
public final class AutomaticLinearChannelVisualizer extends
		NonVolatileColorVisualizer {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 6203765621561793103L;

	private final int channel;
	
	private final boolean allowMinimumToFloat;
	
	private ColorVisualizer inner;
	
	public AutomaticLinearChannelVisualizer(int channel, boolean allowMinimumToFloat) {
		this.channel = channel;
		this.allowMinimumToFloat = allowMinimumToFloat;
		this.inner = new LinearChannelVisualizer(channel);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#analyze(java.lang.Iterable)
	 */
	@Override
	public boolean analyze(Iterable<Color> samples) {
		double minimum = Double.POSITIVE_INFINITY;
		double maximum = Double.NEGATIVE_INFINITY;
		for (Color color : samples) {
			double value = color.getValue(channel);
			if (value < minimum) {
				minimum = value;
			}
			if (value > maximum) {
				maximum = value;
			}
		}
		inner = allowMinimumToFloat ?
				new LinearChannelVisualizer(channel, minimum, maximum) :
				new LinearChannelVisualizer(channel, maximum);
		return true;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#visualize(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public RGB visualize(Color color) {
		return inner.visualize(color);
	}

}
