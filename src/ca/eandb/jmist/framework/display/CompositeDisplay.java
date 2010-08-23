/**
 * 
 */
package ca.eandb.jmist.framework.display;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;

/**
 * A <code>Display</code> that forwards methods to each of a collection of
 * child <code>Display</code>s.
 * 
 * @author Brad Kimmel
 */
public final class CompositeDisplay implements Display, Serializable {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 1296043359308113088L;
	
	/** The <code>List</code> of child <code>Display</code>s to draw to. */
	private final List<Display> children = new ArrayList<Display>();
	
	/**
	 * Adds a new child <code>Display</code>.
	 * @param child The <code>Display</code> to add to the list.
	 * @return A reference to this <code>CompositeDisplay</code>.
	 */
	public CompositeDisplay addDisplay(Display child) {
		children.add(child);
		return this;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#fill(int, int, int, int, ca.eandb.jmist.framework.color.Color)
	 */
	public void fill(int x, int y, int w, int h, Color color) {
		for (Display child : children) {
			child.fill(x, y, w, h, color);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#finish()
	 */
	public void finish() {
		for (Display child : children) {
			child.finish();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#initialize(int, int, ca.eandb.jmist.framework.color.ColorModel)
	 */
	public void initialize(int w, int h, ColorModel colorModel) {
		for (Display child : children) {
			child.initialize(w, h, colorModel);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixel(int, int, ca.eandb.jmist.framework.color.Color)
	 */
	public void setPixel(int x, int y, Color pixel) {
		for (Display child : children) {
			child.setPixel(x, y, pixel);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixels(int, int, ca.eandb.jmist.framework.Raster)
	 */
	public void setPixels(int x, int y, Raster pixels) {
		for (Display child : children) {
			child.setPixels(x, y, pixels);
		}
	}

}
