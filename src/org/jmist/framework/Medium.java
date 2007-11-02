/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;

/**
 * @author bkimmel
 *
 */
public interface Medium {

	Spectrum transmittance(Ray3 ray, double distance);
	Spectrum refractiveIndex(Point3 p);
	Spectrum extinctionIndex(Point3 p);

}
