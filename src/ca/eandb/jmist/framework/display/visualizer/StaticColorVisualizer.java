/**
 * 
 */
package ca.eandb.jmist.framework.display.visualizer;

import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.Color;

/**
 * A <code>ColorVisualizer</code> that does not change its visualization
 * function.
 * @author Brad Kimmel
 */
public abstract class StaticColorVisualizer implements ColorVisualizer {

	/** Serialization version ID. */
	private static final long serialVersionUID = -3346813094887603261L;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#addChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
	public final void addChangeListener(ChangeListener l) {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#removeChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
	public final void removeChangeListener(ChangeListener l) {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#analyze(java.lang.Iterable)
	 */
	@Override
	public final boolean analyze(Iterable<Color> samples) {
		return false;
	}

}
