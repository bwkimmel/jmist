/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Spectrum;

/**
 * @author Brad
 *
 */
public interface Painter {

	Spectrum getColor(SurfacePoint p);

}
