/**
 * 
 */
package ca.eandb.jmist.framework.display.visualizer;

import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.Color;

/**
 * @author brad
 *
 */
public abstract class JColorVisualizerPanel extends JPanel implements
		ColorVisualizer {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 4463169883690282424L;
	
	/**
	 * The <code>List</code> of <code>ChangeListener</code>s to be notified
	 * when the visualization function changes.
	 */
	private final List<ChangeListener> listeners = new ArrayList<ChangeListener>(); 

	/**
	 * 
	 */
	public JColorVisualizerPanel() {
		super();
	}

	/**
	 * @param isDoubleBuffered
	 */
	public JColorVisualizerPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	/**
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public JColorVisualizerPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	/**
	 * @param layout
	 */
	public JColorVisualizerPanel(LayoutManager layout) {
		super(layout);
	}

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
