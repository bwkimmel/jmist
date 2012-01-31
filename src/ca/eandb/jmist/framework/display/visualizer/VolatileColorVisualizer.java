/**
 * 
 */
package ca.eandb.jmist.framework.display.visualizer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.Color;

/**
 * A <code>ColorVisualizer</code> that may change its visualization function
 * asynchronously.
 * @author Brad Kimmel
 */
public abstract class VolatileColorVisualizer implements ColorVisualizer {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 5999173448142814524L;
	
	/**
	 * The <code>List</code> of <code>ChangeListener</code>s to be notified
	 * when the visualization function changes.
	 */
	private final List<ChangeListener> listeners = new ArrayList<ChangeListener>(); 

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#addChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
	public final void addChangeListener(ChangeListener l) {
		listeners.add(l);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#removeChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
	public final void removeChangeListener(ChangeListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies all registered <code>ChangeListener</code>s that the
	 * visualization function has changed.
	 */
	protected final void fireStateChanged() {
		for (ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#analyze(java.lang.Iterable)
	 */
	@Override
	public boolean analyze(Iterable<Color> samples) {
		return false;
	}

}
