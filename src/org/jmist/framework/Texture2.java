/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Point2;

/**
 * Maps two dimensional space to spectra.
 * @author bkimmel
 */
public interface Texture2 {

	/**
	 * Computes the spectrum at the specified <code>Point2</code> in the
	 * domain.
	 * @param p The <code>Point2</code> in the domain.
	 * @return The <code>Spectrum</code> at <code>p</code>.
	 */
	Spectrum evaluate(Point2 p);

}
