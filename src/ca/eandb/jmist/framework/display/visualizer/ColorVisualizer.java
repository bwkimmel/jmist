/**
 * 
 */
package ca.eandb.jmist.framework.display.visualizer;

import java.io.Serializable;

import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.RGB;

/**
 * An object that computes an <code>RGB</code> triplet to visualize some aspect
 * of a provided <code>Color</object>.
 * 
 * @author Brad Kimmel
 */
public interface ColorVisualizer extends Serializable {
	
	/**
	 * Register a listener to be notified when the visualization parameters
	 * change.
	 * @param l The <code>ChangeListener</code> to register.
	 */
	void addChangeListener(ChangeListener l);
	
	/**
	 * Unregister a listener.
	 * @param l The <code>ChangeListener</code> to unregister.
	 */
	void removeChangeListener(ChangeListener l);
	
	/**
	 * Analyzes the provided <code>Color</code> samples for automatically
	 * adjusting the visualization function.
	 * @param samples The <code>Color</code> objects to analyze.
	 * @return A value indicating if the visualization function has changed as
	 * 		a result of the analysis.  If <code>true</code>, then
	 * 		<code>Color</code> objects previously converted to <code>RGB</code>
	 * 		should be re-converted.
	 */
	boolean analyze(Iterable<Color> samples);

	/**
	 * Computes an <code>RGB</code> triplet representing the visualization of
	 * the specified <code>Color</code>.
	 * @param color The <code>Color</code> to visualize.
	 * @return The corresponding <code>RGB</code> triplet.
	 */
	RGB visualize(Color color);
	
	/**
	 * A default <code>ColorVisualizer</code> that invokes
	 * <code>color.toRGB()</code> on the provided <code>Color</code> object.
	 * 
	 * @see ca.eandb.jmist.framework.color.Color#toRGB()
	 */
	public static ColorVisualizer DEFAULT = new ColorVisualizer() {

		/** Serialization version ID. */
		private static final long serialVersionUID = -7672964360487489245L;

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#addChangeListener(javax.swing.event.ChangeListener)
		 */
		@Override
		public void addChangeListener(ChangeListener l) {
			/* nothing to do. */
			
		}

		@Override
		public void removeChangeListener(ChangeListener l) {
			/* nothing to do. */
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#analyze(java.lang.Iterable)
		 */
		@Override
		public boolean analyze(Iterable<Color> samples) {
			return false;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#apply(ca.eandb.jmist.framework.color.Color)
		 */
		@Override
		public RGB visualize(Color color) {
			return color.toRGB();
		}
		
	};
	
}
