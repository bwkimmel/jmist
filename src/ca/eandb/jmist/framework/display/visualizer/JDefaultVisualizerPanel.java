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
public final class JDefaultVisualizerPanel extends JColorVisualizerPanel {

	/** Serialization version ID. */
	private static final long serialVersionUID = 3092504098057458633L;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#visualize(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public RGB visualize(Color color) {
		return ColorVisualizer.DEFAULT.visualize(color);
	}

}
