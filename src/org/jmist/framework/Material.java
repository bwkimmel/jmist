/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Tuple;
import org.jmist.toolkit.Vector3;

/**
 * @author bkimmel
 *
 */
public interface Material extends Medium {

	Spectrum scattering(Intersection x, Vector3 out);
	Spectrum emission(SurfacePoint x, Vector3 out);

	ScatterResult scatter(Intersection x, Tuple wavelengths);
	ScatterResult emit(SurfacePoint x, Tuple wavelengths);

}
