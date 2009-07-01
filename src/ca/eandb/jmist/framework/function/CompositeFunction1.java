/**
 *
 */
package ca.eandb.jmist.framework.function;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Function1;

/**
 * A <code>Function1</code> that is made up of other spectra.
 * @author Brad Kimmel
 */
public abstract class CompositeFunction1 implements Function1 {

	/**
	 * Adds a <code>Function1</code> to this <code>CompositeFunction1</code>.
	 * @param child The child <code>Function1</code> to add.
	 * @return A reference to this <code>CompositeFunction1</code> so that calls
	 * 		to this method may be chained.
	 */
	public CompositeFunction1 addChild(Function1 child) {
		this.children.add(child);
		return this;
	}

	/**
	 * Gets the list of child spectra.
	 * @return The <code>List</code> of child spectra.
	 */
	protected final List<Function1> children() {
		return this.children;
	}

	/** The component spectra. */
	private final List<Function1> children = new ArrayList<Function1>();

}
