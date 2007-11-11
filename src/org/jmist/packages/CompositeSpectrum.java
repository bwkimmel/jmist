/**
 *
 */
package org.jmist.packages;

import java.util.ArrayList;
import java.util.List;

import org.jmist.framework.AbstractSpectrum;
import org.jmist.framework.Spectrum;

/**
 * A <code>Spectrum</code> that is made up of other spectra.
 * @author bkimmel
 */
public abstract class CompositeSpectrum extends AbstractSpectrum {

	/**
	 * Adds a <code>Spectrum</code> to this <code>CompositeSpectrum</code>.
	 * @param child The child <code>Spectrum</code> to add.
	 * @return A reference to this <code>CompositeSpectrum</code> so that calls
	 * 		to this method may be chained.
	 */
	public CompositeSpectrum addChild(Spectrum child) {
		this.children.add(child);
		return this;
	}

	/**
	 * Gets the list of child spectra.
	 * @return The <code>List</code> of child spectra.
	 */
	protected final List<Spectrum> children() {
		return this.children;
	}

	/** The component spectra. */
	private final List<Spectrum> children = new ArrayList<Spectrum>();

}
