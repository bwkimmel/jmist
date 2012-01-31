/**
 * 
 */
package ca.eandb.jmist.framework.display.visualizer;

import javax.swing.event.ChangeListener;

/**
 * A <code>ColorVisualizer</code> that never changes its visualization function
 * asynchronously (that is, one that does not fire state-change events).  A
 * <code>ColorVisualizer</code> of this type may only change its visualization
 * function when {@link ColorVisualizer#analyze(Iterable)} is called.

 * @see ColorVisualizer#analyze(Iterable)
 * @author Brad Kimmel
 */
public abstract class NonVolatileColorVisualizer implements ColorVisualizer {

	/** Serialization version ID. */
	private static final long serialVersionUID = 6545220601074256718L;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#addChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
	public void addChangeListener(ChangeListener l) {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#removeChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
	public void removeChangeListener(ChangeListener l) {
		/* nothing to do. */
	}

}
