/**
 * 
 */
package ca.eandb.jmist.framework.animator;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Animator;

/**
 * An <code>Animator</code> that composes multiple child <code>Animator</code>s
 * and allows them be treated as a single <code>Animator</code>.
 * @author Brad Kimmel
 */
public final class CompositeAnimator implements Animator {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 1091240735157061218L;
	
	/** The <code>List</code> of child <code>Animator</code>s. */
	private final List<Animator> children = new ArrayList<Animator>();
	
	/**
	 * Adds a child <code>Animator</code>. 
	 * @param child The <code>Animator</code> to append.
	 * @return A reference to this <code>CompositeAnimator</code>.
	 */
	public CompositeAnimator addChild(Animator child) {
		children.add(child);
		return this;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Animator#setTime(double)
	 */
	@Override
	public void setTime(double time) {
		for (Animator child : children) {
			child.setTime(time);
		}
	}

}
