/**
 * 
 */
package ca.eandb.jmist.framework.tone.swing;

import java.awt.LayoutManager;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.tone.ToneMapperFactory;

/**
 * @author brad
 *
 */
public abstract class JToneMapperPanel extends JPanel implements
		ToneMapperFactory {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 7574943635232635058L;
	
	private final Set<ChangeListener> changeListeners = new HashSet<ChangeListener>();	

	/**
	 * 
	 */
	public JToneMapperPanel() {
	}

	/**
	 * @param layout
	 */
	public JToneMapperPanel(LayoutManager layout) {
		super(layout);
	}

	/**
	 * @param isDoubleBuffered
	 */
	public JToneMapperPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	/**
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public JToneMapperPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}
	
	public void addChangeListener(ChangeListener l) {
		changeListeners.add(l);
	}
	
	public void removeChangeListener(ChangeListener l) {
		changeListeners.remove(l);
	}
	
	protected void fireStateChanged() {
		ChangeEvent e = new ChangeEvent(this);
		for (ChangeListener l : changeListeners) {
			l.stateChanged(e);
		}
	}

}
