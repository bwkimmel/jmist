/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Point2;

/**
 * Maps two dimensional space to spectra.
 * @author Brad Kimmel
 */
public interface Texture2 {

	/**
	 * Computes the spectrum at the specified <code>Point2</code> in the
	 * domain.
	 * @param p The <code>Point2</code> in the domain.
	 * @return The <code>Function1</code> at <code>p</code>.
	 */
	Color evaluate(Point2 p);

}
