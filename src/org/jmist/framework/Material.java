/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Tuple;
import org.jmist.toolkit.Vector3;

/**
 * @author bkimmel
 *
 */
public interface Material extends Medium {

	Spectrum scattering(Intersection x, Vector3 out);
	Spectrum emission(SurfacePoint x, Vector3 out);

	Ray3 scatter(Intersection x, Tuple wavelengths, double[] radiance);
	Ray3 emit(SurfacePoint x, Tuple wavelengths, double[] radiance);

}
