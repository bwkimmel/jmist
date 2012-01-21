/**
 *
 */
package ca.eandb.jmist.framework.painter;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Painter;

/**
 * A <code>Painter</code> that is made up of other painters.
 * @author Brad Kimmel
 */
public abstract class CompositePainter implements Painter {

	/** Serialization version ID. */
	private static final long serialVersionUID = -141503160433974383L;

	/** The component painters. */
	private final List<Painter> children = new ArrayList<Painter>();

	/**
	 * Adds a <code>Painter</code> to this <code>CompositePainter</code>.
	 * @param child The child <code>Painter</code> to add.
	 * @return A reference to this <code>CompositePainter</code> so that calls
	 * 		to this method may be chained.
	 */
	public CompositePainter addChild(Painter child) {
		this.children.add(child);
		return this;
	}

	/**
	 * Gets the list of child painters.
	 * @return The <code>List</code> of child painters.
	 */
	protected final List<Painter> children() {
		return this.children;
	}

}
