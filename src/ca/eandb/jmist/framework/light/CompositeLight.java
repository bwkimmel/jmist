/**
 *
 */
package ca.eandb.jmist.framework.light;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.eandb.jmist.framework.Light;

/**
 * @author Brad Kimmel
 *
 */
public abstract class CompositeLight extends AbstractLight {

	/**
	 * Initializes this <code>CompositeLight</code> with no light sources.
	 */
	protected CompositeLight() {
		/* nothing to do. */
	}

	/**
	 * Initializes this <code>CompositeLight</code> with the specified child
	 * light sources.
	 * @param children
	 */
	protected CompositeLight(Collection<? extends Light> children) {
		this.children.addAll(children);
	}

	/**
	 * Adds a child <code>Light</code> to this <code>CompositeLight</code>.
	 * @param child The child <code>Light</code> to add.
	 * @return A reference to this <code>CompositeLight</code> so that calls to
	 * 		this method may be chained.
	 */
	public CompositeLight addChild(Light child) {
		this.children.add(child);
		return this;
	}

	/**
	 * Gets the list of child lights.
	 * @return The <code>List</code> of child lights.
	 */
	protected final List<Light> children() {
		return this.children;
	}

	/** The child lights. */
	private final List<Light> children = new ArrayList<Light>();

}
